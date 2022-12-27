package com.yummy.foodonmytrain;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.annotation.IdRes;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.tsongkha.spinnerdatepicker.SpinnerDatePickerDialogBuilder;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;
import java.util.UUID;

import static android.content.Context.INPUT_METHOD_SERVICE;

public class ProfileFragment extends Fragment implements View.OnClickListener, View.OnFocusChangeListener, View.OnTouchListener, com.tsongkha.spinnerdatepicker.DatePickerDialog.OnDateSetListener {

    private static final String ARG_PARAM = "typeOfRequest";
    public static final String PARAM1 = "Create";
    public static final String PARAM2 = "Edit";
    private String typeOfRequest;
    private static int validAge = 0;
    private int curr_day, curr_month, curr_year;
    private View tempView;
    private Personal_details personalDetails;
    private DatabaseHelper databaseHelper;
    private FrameLayout mFrameLayoutMemberDetails;
    private TextInputEditText mMemberFirstName,mMemberLastName;
    private TextInputEditText mMemberTrainNo=null;
    private TextInputLayout mTrainLayout;
    private RadioGroup mRadioUserType;
    private RadioButton mUserTypeBtn, mUserSeller, mUserCustomer;
    private int mRadioOptionSelectedValue;
    private MainActivity mMobileAct;
    private ProgressBar mProgressBarEditProfile;
    private LinearLayout mLinearOverlay,mDOBLinearOverlay;
    private Toolbar mManualToolbar;
    private String mMembernoFromFirebase;
    private DatabaseReference mFirebaseDatabaseRef;

    public static ProfileFragment newInstance(String typeOfRequest) {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM, typeOfRequest);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);
        mMobileAct= (MainActivity) getActivity();
        databaseHelper = new DatabaseHelper(getActivity());
        mFirebaseDatabaseRef = FirebaseDatabase.getInstance().getReference();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.lay_member_profile, container, false);

        initializeViews(view);

        ((AppCompatActivity)getActivity()).setSupportActionBar(mManualToolbar);
        mManualToolbar.setTitle("Profile details");

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

        if (firebaseUser != null) {
            mMembernoFromFirebase= firebaseUser.getPhoneNumber();
        }

        if(getArguments() != null) {
            typeOfRequest = getArguments().getString(ARG_PARAM);
            assert typeOfRequest != null;
            switch (typeOfRequest) {
                case PARAM1:
                    ((DrawerLocker)getActivity()).setDrawerEnabled(false,"Profile details");
                    break;

                case PARAM2:
                    ((DrawerLocker)getActivity()).setDrawerEnabled(true,"Profile details");
                    personalDetails = databaseHelper.getMemberDetails();
                    setViewValues();
                    break;
            }
        }

       /* mMemberDOB.setOnClickListener(this);
        mMemberDOB.setOnTouchListener(this);
        mMemberDOB.setOnFocusChangeListener(this);*/

        if(typeOfRequest.equals(PARAM1)) {
            mManualToolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);

            mManualToolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    hideSoftKeyboard();
                    getActivity().getSupportFragmentManager().popBackStack();
                }
            });
        }

        /*mLinearOverlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });*/


        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        return view;
    }

    private void getViewValues() {

//        mLinearOverlay.setVisibility(View.VISIBLE);
        //mProgressBarEditProfile.setVisibility(View.VISIBLE);

        Personal_details personalDetails1 = new Personal_details();
        personalDetails1.setFirstname(mMemberFirstName.getText().toString());
        personalDetails1.setLastname(mMemberLastName.getText().toString());
        personalDetails1.setPersonalno(mMembernoFromFirebase);
        //personalDetails1.setPersonalDOB(mMemberDOB.getText().toString());

        switch (mUserTypeBtn.getText().toString()) {
            case "Seller":
                personalDetails1.setUserType("1");
                personalDetails1.setTrainNo(mMemberTrainNo.getText().toString());
                break;
            case "Customer":
                personalDetails1.setUserType("2");
                personalDetails1.setTrainNo("");
                break;
        }

        if (isConnected(Objects.requireNonNull(getActivity()))) {
            if(SharedPreferencesManager.get(SharedPreferencesManager.IS_PROFILE_FILLED,false)){
                databaseHelper.updateMember(personalDetails1);
            }else {
                databaseHelper.insertMember(personalDetails1);
                SharedPreferencesManager.store(SharedPreferencesManager.IS_PROFILE_FILLED, true);
            }
            mFirebaseDatabaseRef.child("Users")
                    .child(personalDetails1.getPersonalno())
                    .setValue(personalDetails1);
        }else{
            nointernetcaller();
        }
    }

    private void nointernetcaller() {

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
        alertDialogBuilder.setTitle("No internet");

        alertDialogBuilder
                .setMessage("Please connect to internet")
                .setCancelable(false)
                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                        getActivity().finish();
                    }
                });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();

    }

    @Override
    public void onClick(View view) {

        switch(view.getId()) {

           /* case R.id.edt_DOB:
                hideSoftKeyboard();
                break;*/
        }
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {

        if (hasFocus) {
            switch (v.getId()) {

              /*  case R.id.edt_DOB:
                    hideSoftKeyboard();

                    Calendar calendar = Calendar.getInstance();

                    curr_year = calendar.get(Calendar.YEAR);
                    curr_month = calendar.get(Calendar.MONTH);
                    curr_day = calendar.get(Calendar.DAY_OF_MONTH);

                    showDate(curr_year, curr_month, curr_day, R.style.DatePickerSpinner);
                    break;*/
            }
        }
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {

        if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {

            switch (view.getId()) {

                /*case R.id.edt_DOB:
                    hideSoftKeyboard();
                    Calendar calendar = Calendar.getInstance();
                    curr_year = calendar.get(Calendar.YEAR);
                    curr_month = calendar.get(Calendar.MONTH);
                    curr_day = calendar.get(Calendar.DAY_OF_MONTH);
                    showDate(curr_year, curr_month, curr_day, R.style.DatePickerSpinner);
                    break;*/
            }
        }
        return true;
    }

    void showDate(int year, int monthofYear, int dayofMonth, int spinnerTheme) {

        Calendar calendarCurrent =Calendar.getInstance();
        calendarCurrent.setTimeInMillis(System.currentTimeMillis());
        new SpinnerDatePickerDialogBuilder()
                .context(getActivity())
                .callback(this)
                .spinnerTheme(spinnerTheme)
                .defaultDate(calendarCurrent.get(Calendar.YEAR), calendarCurrent.get(Calendar.MONTH), calendarCurrent.get(Calendar.DAY_OF_MONTH))
                .maxDate(calendarCurrent.get(Calendar.YEAR), calendarCurrent.get(Calendar.MONTH), calendarCurrent.get(Calendar.DAY_OF_MONTH))
                .build()
                .show();
    }

    private boolean checkPersonalDetailsErrors() {
        boolean tempState = true;
        if (mMemberFirstName.getText().toString().equals("")) {
            mMemberFirstName.setError("Please enter first name");
            tempState = false;
        }
        if (mMemberLastName.getText().toString().equals("")) {
            mMemberLastName.setError("Please enter last name");
            tempState = false;
        }
        if (mRadioUserType.getCheckedRadioButtonId() == -1) {
            mUserCustomer.setError("Please choose gender");
            tempState = false;
        }
       /* if (mMemberDOB.getText().toString().equals("")) {
            mMemberDOB.setError("Please enter date of birth");
            tempState = false;
        }*/
        return tempState;
    }

    private boolean isConnected(Context context) {
        ConnectivityManager
                cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm != null ? cm.getActiveNetworkInfo() : null;
        return activeNetwork != null
                && activeNetwork.isConnectedOrConnecting();
    }

    @Override
    public void onDateSet(com.tsongkha.spinnerdatepicker.DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        Calendar calendar = Calendar.getInstance();
        Calendar calendarCurrent = Calendar.getInstance();
        calendarCurrent.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, monthOfYear);
        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

        if (calendar.compareTo(calendarCurrent) < 0) {
            Date date = calendar.getTime();
            String datePicked = convertToSimpleFormat(date);
           /* mMemberDOB.setError(null);
            mMemberDOB.setText(datePicked);*/
        } else {

            mDOBLinearOverlay.setVisibility(View.VISIBLE);

            Snackbar snackbar = Snackbar
                    .make(mFrameLayoutMemberDetails, "Please select correct Date of Birth", Snackbar.LENGTH_INDEFINITE)
                    .setActionTextColor(getResources().getColor(R.color.colorPrimaryDark))
                    .setAction("OK", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            mLinearOverlay.setVisibility(View.GONE);
                            Calendar calendarTemp = Calendar.getInstance();

                            int curr_year_Temp = calendarTemp.get(Calendar.YEAR);
                            int curr_month_Temp = calendarTemp.get(Calendar.MONTH);
                            int curr_day_Temp = calendarTemp.get(Calendar.DAY_OF_MONTH);

                            showDate(curr_year_Temp, curr_month_Temp, curr_day_Temp, R.style.DatePickerSpinner);
                        }
                    });

            snackbar.show();
        }
    }

    private String convertToSimpleFormat(Date date) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        return format.format(date);
    }

    private void hideSoftKeyboard() {
        if (getActivity().getCurrentFocus() != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(INPUT_METHOD_SERVICE);
            if (inputMethodManager != null) {
                inputMethodManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
            }
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId()== R.id.menusavebttn){
            hideSoftKeyboard();
            if(checkPersonalDetailsErrors()) {
                getViewValues();
                switch (mUserTypeBtn.getText().toString()) {
                    case "Seller":
                        SharedPreferencesManager.store(SharedPreferencesManager.IS_USER_CUSTOMER,false);
                        if (mMemberTrainNo.getText().toString().equals("")) {
                            mMemberTrainNo.setError("Please enter Train no.");
                        }else {
                            mMobileAct.refreshData();
                            /*getFragmentManager()
                                    .beginTransaction()
                                    .replace(R.id.mainFragContent, SellerMainMenuFragment.newInstance(mMemberTrainNo.getText().toString()))
                                    .commit();*/
                        }
                        break;
                    case "Customer":
                        SharedPreferencesManager.store(SharedPreferencesManager.IS_USER_CUSTOMER,true);
                        mMobileAct.refreshData();
                        /*getFragmentManager()
                                .beginTransaction()
                                .replace(R.id.mainFragContent, SellerMainMenuFragment.newInstance(null))
                                .commit();*/
                        break;
                }
            }
            return true;
        }else
            return super.onOptionsItemSelected(item);
    }

    private void initializeViews(View view) {

        //All Layouts initialization-----
        tempView = view;

        //Toolbar--
        mManualToolbar=view.findViewById(R.id.manual_toolbar);

        //Layout---
        mFrameLayoutMemberDetails=view.findViewById(R.id.frame_member_details);

        //Text input Edit Text----------------
        mMemberFirstName=view.findViewById(R.id.edtFirstName);
        mMemberLastName=view.findViewById(R.id.edtLastName);
        mMemberTrainNo=view.findViewById(R.id.edtTrain_no);
       // mMemberDOB=view.findViewById(R.id.edt_DOB);

        //Radio Buttons-----
        mRadioUserType = view.findViewById(R.id.radioUserType);
        mUserSeller =view.findViewById(R.id.radioSeller);
        mUserCustomer =view.findViewById(R.id.radioCustomer);

        mTrainLayout=view.findViewById(R.id.train_no_layout);

        mRadioUserType.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, @IdRes int i) {
                hideSoftKeyboard();
                mRadioOptionSelectedValue = mRadioUserType.getCheckedRadioButtonId();
                if (mUserSeller.isChecked())
                    mTrainLayout.setVisibility(View.VISIBLE);
                else mTrainLayout.setVisibility(View.INVISIBLE);
                mUserTypeBtn = tempView.findViewById(mRadioOptionSelectedValue);
            }
        });
    }

    private void setViewValues() {

        mMemberFirstName.setText(personalDetails.getFirstname());
        mMemberLastName.setText(personalDetails.getLastname());
        //mMemberDOB.setText(personalDetails.getPersonalDOB());
        mMemberTrainNo.setText(personalDetails.getTrainNo());

        switch (personalDetails.getUserType()) {
            case "1":
                mUserSeller.setChecked(true);
                mRadioOptionSelectedValue = mUserSeller.getId();
                mUserTypeBtn = tempView.findViewById(mRadioOptionSelectedValue);
                break;
            case "2":
                mUserCustomer.setChecked(true);
                mRadioOptionSelectedValue = mUserCustomer.getId();
                mUserTypeBtn = tempView.findViewById(mRadioOptionSelectedValue);
                break;
        }
    }
}
