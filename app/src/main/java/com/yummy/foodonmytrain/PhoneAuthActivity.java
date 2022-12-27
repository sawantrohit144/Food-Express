package com.yummy.foodonmytrain;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.alimuzaffar.lib.pin.PinEntryEditText;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hbb20.CountryCodePicker;

import java.util.concurrent.TimeUnit;

public class PhoneAuthActivity extends AppCompatActivity implements View.OnClickListener{

    private static final int STATE_INITIALIZED = 1;
    private static final int STATE_CODE_SENT = 2;
    private static final int STATE_VERIFY_FAILED = 3;
    private static final int STATE_VERIFY_SUCCESS = 4;
    private static final int STATE_SIGNIN_FAILED = 5;
    private static final int STATE_SIGNIN_SUCCESS = 6;
    private static final String KEY_VERIFY_IN_PROGRESS = "key_verify_in_progress";

    private FirebaseAuth mAuth;
    private boolean mVerificationInProgress = false;
    private String mVerificationId;
    private PhoneAuthProvider.ForceResendingToken mResendToken;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    private DatabaseReference mFirebaseDatabaseRef;

    private ConstraintLayout mAuthLayoutSend,mAuthLayoutProgress,mAuthLayoutVerify,mAuthLayoutDone;
    private Button mAuthBtnResend, mAuthBtnSend;
    private TextView mAuthCountDowm,mTxtAuthVerificationMsg;
    private CountryCodePicker mAuthCCP;
    private PinEntryEditText mAuthEdtOTPEntry;
    private EditText mAuthEdtPhoneNo;
    private String mAuthPhoneWithCountryCode;
    private DatabaseHelper databaseHelper;
    private static  final int RESOLVE_HINT=101;
    boolean mPhoneNoValid=false;
    ImageView mPhoneValidityImg;
    boolean validno;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lay_phoneauth);
        databaseHelper = new DatabaseHelper(this);
        FirebaseApp.initializeApp(this);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
        }

        // Restore instance state
        if (savedInstanceState != null) {
            onRestoreInstanceState(savedInstanceState);
        }

        mFirebaseDatabaseRef = FirebaseDatabase.getInstance().getReference();
        // Assign views
        mAuthLayoutSend= findViewById(R.id.auth_layout_send);
        mAuthLayoutProgress= findViewById(R.id.auth_layout_progress);
        mAuthLayoutVerify= findViewById(R.id.auth_layout_verify);
        mAuthLayoutDone= findViewById(R.id.auth_layout_done);
        mAuthBtnResend= findViewById(R.id.auth_btn_resend);
        mAuthCountDowm= findViewById(R.id.auth_txt_count_down);
        mAuthCCP= findViewById(R.id.auth_Country_Code);
        mAuthEdtOTPEntry= findViewById(R.id.auth_txt_otp);
        mAuthEdtPhoneNo= findViewById(R.id.auth_phone_number);
        mAuthCountDowm= findViewById(R.id.auth_txt_count_down);
        mTxtAuthVerificationMsg= findViewById(R.id.auth_txt_verification_msg);
        mAuthBtnSend = findViewById(R.id.auth_btn_start_verification);
        Toolbar mAuthToolbar = findViewById(R.id.auth_toolbar);

        mAuthBtnResend.setVisibility(View.GONE);

        mPhoneValidityImg=findViewById(R.id.authimageView);

        //mAuthEdtPhoneNo.requestFocus();

        // Assign click listeners
        mAuthLayoutDone.setOnClickListener(this);
        mAuthLayoutProgress.setOnClickListener(this);
        mAuthBtnSend.setOnClickListener(this);
        mAuthBtnResend.setOnClickListener(this);

        mAuthCCP.registerCarrierNumberEditText(mAuthEdtPhoneNo);
        mAuthCCP.setPhoneNumberValidityChangeListener(new CountryCodePicker.PhoneNumberValidityChangeListener() {
            @Override
            public void onValidityChanged(boolean isValidNumber) {
                if(isValidNumber) {
                    mPhoneValidityImg.setBackgroundResource(R.drawable.ic_check_green_24dp);
                    mPhoneNoValid=true;
                }else{
                    mPhoneValidityImg.setBackgroundResource(R.drawable.ic_invalid_red_24dp);
                    mPhoneNoValid=false;

                }
            }
        });
        mAuthCCP.detectSIMCountry(true);

        mAuthEdtOTPEntry.setOnPinEnteredListener(new PinEntryEditText.OnPinEnteredListener() {
            @Override
            public void onPinEntered(CharSequence str) {
                mAuthLayoutProgress.setVisibility(View.VISIBLE);
                String code = mAuthEdtOTPEntry.getText().toString();

                if (TextUtils.isEmpty(code)) {
                    mAuthEdtOTPEntry.setError("Cannot be empty.");
                    return;
                }
                verifyPhoneNumberWithCode(mVerificationId, code);
            }
        });


        // [START initialize_auth]
        mAuth = FirebaseAuth.getInstance();
        // [END initialize_auth]

        // Initialize phone auth callbacks
        // [START phone_auth_callbacks]
        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            @Override
            public void onVerificationCompleted(PhoneAuthCredential credential) {
                mVerificationInProgress = false;
                updateUI(STATE_VERIFY_SUCCESS, credential);
                signInWithPhoneAuthCredential(credential);
            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                mVerificationInProgress = false;

                if (e instanceof FirebaseAuthInvalidCredentialsException) {
                    mAuthEdtPhoneNo.setError("Invalid phone number.");
                } else if (e instanceof FirebaseTooManyRequestsException) {
                    Snackbar.make(findViewById(android.R.id.content), "Quota exceeded.",
                            Snackbar.LENGTH_SHORT).show();
                }
                updateUI(STATE_VERIFY_FAILED);
            }

            @Override
            public void onCodeSent(String verificationId,
                                   PhoneAuthProvider.ForceResendingToken token) {
                mVerificationId = verificationId;
                mResendToken = token;
                updateUI(STATE_CODE_SENT);
            }
        };
    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);

        if (mVerificationInProgress && validatePhoneNumber()) {
            startPhoneNumberVerification(mAuthPhoneWithCountryCode);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(KEY_VERIFY_IN_PROGRESS, mVerificationInProgress);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mVerificationInProgress = savedInstanceState.getBoolean(KEY_VERIFY_IN_PROGRESS);
    }


    private void startPhoneNumberVerification(String phoneNumber) {
        PhoneAuthProvider.getInstance(mAuth).verifyPhoneNumber(
                phoneNumber,        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                this,               // Activity (for callback binding)
                mCallbacks);        // OnVerificationStateChangedCallbacks

        mVerificationInProgress = true;
    }

    private void verifyPhoneNumberWithCode(String verificationId, String code) {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);
        signInWithPhoneAuthCredential(credential);
    }

    private void resendVerificationCode(String phoneNumber,
                                        PhoneAuthProvider.ForceResendingToken token) {
        PhoneAuthProvider.getInstance(mAuth).verifyPhoneNumber(
                phoneNumber,        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                this,               // Activity (for callback binding)
                mCallbacks,         // OnVerificationStateChangedCallbacks
                token);             // ForceResendingToken from callbacks
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = task.getResult().getUser();
                            updateUI(STATE_SIGNIN_SUCCESS, user);
                        } else {
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                mAuthEdtOTPEntry.setError("Invalid code.");
                            }
                            updateUI(STATE_SIGNIN_FAILED);
                        }
                    }
                });
    }

    private void updateUI(int uiState) {
        updateUI(uiState, mAuth.getCurrentUser(), null);
    }

    private void updateUI(FirebaseUser user) {
        if (user != null) {
            updateUI(STATE_SIGNIN_SUCCESS, user);
        } else {
            updateUI(STATE_INITIALIZED);
        }
    }

    private void updateUI(int uiState, FirebaseUser user) {
        updateUI(uiState, user, null);
    }

    private void updateUI(int uiState, PhoneAuthCredential cred) {
        updateUI(uiState, null, cred);
    }

    private void updateUI(int uiState, FirebaseUser user, PhoneAuthCredential cred) {

        switch (uiState) {
            case STATE_INITIALIZED:
                mAuthLayoutSend.setVisibility(View.VISIBLE);
                mAuthLayoutProgress.setVisibility(View.GONE);
                mAuthLayoutVerify.setVisibility(View.GONE);
                mAuthLayoutDone.setVisibility(View.GONE);
                break;

            case STATE_CODE_SENT:
                mTxtAuthVerificationMsg.setText("Please type verification code sent \n to "+mAuthCCP.getSelectedCountryCodeWithPlus()+" "+mAuthEdtPhoneNo.getText().toString());
                new CountDownTimer(60000, 1000) {

                    public void onTick(long millisUntilFinished) {
                        mAuthCountDowm.setText("Please wait: 0." + millisUntilFinished / 1000);
                    }

                    public void onFinish() {
                        mAuthCountDowm.setText("Please wait: 0.0");
                        mAuthBtnResend.setVisibility(View.VISIBLE);
                    }
                }.start();
                mAuthLayoutSend.setVisibility(View.GONE);
                mAuthLayoutProgress.setVisibility(View.GONE);
                mAuthLayoutVerify.setVisibility(View.VISIBLE);
                mAuthLayoutDone.setVisibility(View.GONE);
                break;

            case STATE_VERIFY_FAILED:
                mAuthLayoutSend.setVisibility(View.GONE);
                mAuthLayoutProgress.setVisibility(View.GONE);
                mAuthLayoutVerify.setVisibility(View.VISIBLE);
                mAuthLayoutDone.setVisibility(View.GONE);
                break;

            case STATE_VERIFY_SUCCESS:
                mAuthLayoutDone.setVisibility(View.VISIBLE);

                if (cred != null) {
                    if (cred.getSmsCode() != null) {
                        mAuthEdtOTPEntry.setText(cred.getSmsCode());
                    } else {
                        mAuthEdtOTPEntry.setText("");
                    }
                }

                break;

            case STATE_SIGNIN_FAILED:
                break;

            case STATE_SIGNIN_SUCCESS:
                SharedPreferencesManager.store(SharedPreferencesManager.IS_USER_LOGIN,true);
                SharedPreferencesManager.store(SharedPreferencesManager.USER_NUMBER,user.getPhoneNumber() );
                //Toast.makeText(this, "Sending to main", Toast.LENGTH_SHORT).show();

                if(user.getPhoneNumber()!=null){
                mFirebaseDatabaseRef.child("Users").child(user.getPhoneNumber())
                        .addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                Personal_details personal_details = snapshot.getValue(Personal_details.class);
                                if (personal_details != null) {
                                    databaseHelper.insertMember(personal_details);
                                    SharedPreferencesManager.store(SharedPreferencesManager.IS_PROFILE_FILLED, true );
                                    if(personal_details.getUserType().equalsIgnoreCase("2"))
                                        SharedPreferencesManager.store(SharedPreferencesManager.IS_USER_CUSTOMER,true);
                                    else
                                        SharedPreferencesManager.store(SharedPreferencesManager.IS_USER_CUSTOMER,false);
                                }
                                startActivity(new Intent(PhoneAuthActivity.this, MainActivity .class));
                                finish();
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                }
                break;
        }

        if (user == null) {

        } else {
            // Signed in
            mAuthLayoutDone.setVisibility(View.VISIBLE);
            mAuthEdtPhoneNo.setText(null);
            mAuthEdtOTPEntry.setText(null);
        }
    }

    private boolean validatePhoneNumber() {
        String phoneNumber = mAuthEdtPhoneNo.getText().toString();

        if (TextUtils.isEmpty(phoneNumber)) {
            mAuthEdtPhoneNo.setError("Invalid phone number");
            validno=false;
        }else if(!mPhoneNoValid){
            mAuthEdtPhoneNo.setError("Please enter correct phone no.");
            validno = false;
        }else
            validno=true;

        return validno;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.auth_btn_start_verification:

                if(isConnected()) {
                    if (!validatePhoneNumber()) {
                        return;
                    }
                    mAuthPhoneWithCountryCode = mAuthCCP.getSelectedCountryCodeWithPlus() + mAuthEdtPhoneNo.getText().toString();
                    mAuthLayoutProgress.setVisibility(View.VISIBLE);
                    startPhoneNumberVerification(mAuthPhoneWithCountryCode);
                }else {
                    nointernetchecker();
                }
                break;

            case R.id.auth_btn_resend:

                mAuthBtnResend.setVisibility(View.GONE);
                resendVerificationCode(mAuthPhoneWithCountryCode, mResendToken);
                break;
        }
    }

    public boolean isConnected() {
        ConnectivityManager
                cm = (ConnectivityManager) getApplicationContext()
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm != null ? cm.getActiveNetworkInfo() : null;
        return activeNetwork != null
                && activeNetwork.isConnectedOrConnecting();
    }

    private void nointernetchecker() {

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle("No internet");
        alertDialogBuilder
                .setMessage("Please connect to internet for the application to generate OTP")
                .setCancelable(false)
                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }


}

