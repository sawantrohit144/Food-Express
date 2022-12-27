package com.yummy.foodonmytrain;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.yummy.foodonmytrain.DrawerLocker;

import static com.yummy.foodonmytrain.ProfileFragment.PARAM2;
import static com.yummy.foodonmytrain.ProfileFragment.newInstance;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, DrawerLocker {

    Toolbar toolbar;
    DrawerLayout drawer;
    ActionBarDrawerToggle toggle;
    NavigationView mNavigationView;
    View mNavigationProfileDetails;
    TextView mTxtNavProfileName, mTxtNavProfilePhone, mTxtNavProfileUser ;
    ImageButton mBtnNavEditProfile;
    private static final String PARAM1 = "Edit";
    Personal_details personalDetails;
    DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        databaseHelper= new DatabaseHelper(this);
        if(SharedPreferencesManager.get(SharedPreferencesManager.IS_USER_LOGIN, false)) {

            if(SharedPreferencesManager.get(SharedPreferencesManager.IS_PROFILE_FILLED, false )) {
                personalDetails = databaseHelper.getMemberDetails();

                /*String usertype=personalDetails.getUserType();

                if(usertype.equalsIgnoreCase("1")) {
                    setContentView(R.layout.activity_main_seller);
                }else {
                    setContentView(R.layout.activity_main);
                }*/
            }
        }
        setContentView(R.layout.activity_main);

        initNavigationView();
       /* mBtnNavEditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isConnected()) {
                    getSupportFragmentManager()
                            .beginTransaction().replace(R.id.mainFragContent, newInstance(PARAM2))
                            .addToBackStack("Profile Editing")
                            .commit();
                    changeDrawerState();
                }
                else{
                    nointernetcaller();
                }
            }
        });*/
    }

    private void initNavigationView() {

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawer = findViewById(R.id.drawer_layout);

        mNavigationView = findViewById(R.id.nav_view);
        mNavigationProfileDetails = mNavigationView.inflateHeaderView(R.layout.nav_profile_details);
        mNavigationView.setNavigationItemSelectedListener(this);

        mTxtNavProfileName  = mNavigationView.findViewById(R.id.nav_name);
        mTxtNavProfilePhone = mNavigationView.findViewById(R.id.nav_phone);
        mTxtNavProfileUser  = mNavigationView.findViewById(R.id.nav_user_type);
        mBtnNavEditProfile  = mNavigationView.findViewById(R.id.bt_edt_nav_user_details);

        toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close){

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                    setNavigationValues();
            }
        };


        drawer.setStatusBarBackground(R.color.colorPrimary);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        refreshData();

    }

    public void refreshData(){
        if(SharedPreferencesManager.get(SharedPreferencesManager.IS_USER_LOGIN, false)) {
            if(SharedPreferencesManager.get(SharedPreferencesManager.IS_PROFILE_FILLED, false )) {
                personalDetails = databaseHelper.getMemberDetails();
                setNavigationValues();

                String usertype=personalDetails.getUserType();

                if(usertype.equalsIgnoreCase("1")) {
                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.mainFragContent, SellerMainMenuFragment.newInstance(personalDetails.getTrainNo()))
                            .commit();
                }else {
                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.mainFragContent,SellerMainMenuFragment.newInstance(null))
                            .commit();
                }
            }else{
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.mainFragContent, ProfileFragment.newInstance(ProfileFragment.PARAM1))
                        .commit();
            }

        }else{
            startActivity(new Intent(MainActivity.this, PhoneAuthActivity.class));
            finish();
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

    private void nointernetcaller() {

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle("No internet");

        alertDialogBuilder
                .setMessage("Please connect to internet to update you profile details")
                .setCancelable(false)
                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();

        switch (id) {

            case R.id.nav_home:

                if(personalDetails!=null) {
                    String usertype = personalDetails.getUserType();

                    if (usertype.equalsIgnoreCase("1")) {
                        getSupportFragmentManager()
                                .beginTransaction()
                                .replace(R.id.mainFragContent, SellerMainMenuFragment.newInstance(personalDetails.getTrainNo()))
                                .commit();
                    } else {
                        getSupportFragmentManager()
                                .beginTransaction()
                                .replace(R.id.mainFragContent, SellerMainMenuFragment.newInstance(null))
                                .commit();
                    }
                }

                break;

            case R.id.nav_my_order:

                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.mainFragContent, MyOrderFragment.newInstance(null,"1"))
                        .addToBackStack("FromNavigationDrawer")
                        .commit();

                break;

            case R.id.nav_last_order:
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.mainFragContent, MyOrderFragment.newInstance(personalDetails.getTrainNo(),"2"))
                        .addToBackStack("FromNavigationDrawer")
                        .commit();

                break;

            case R.id.nav_new_order:
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.mainFragContent, new CustomerMainMenuFragment())
                        .addToBackStack("FromNavigationDrawer")
                        .commit();
                break;

            case R.id.nav_feedback:
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.mainFragContent, FeedbackFragment.newInstance())
                        .addToBackStack("FromNavigationDrawer")
                        .commit();
                break;

            case R.id.nav_terms:
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.mainFragContent, TNCFragment.newInstance())
                        .addToBackStack("FromNavigationDrawer")
                        .commit();
                break;

            case R.id.nav_contact:
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.mainFragContent, ContactUsFragment.newInstance())
                        .addToBackStack("FromNavigationDrawer")
                        .commit();
                break;

            case R.id.nav_logout:
                SharedPreferencesManager.clearPreference();
                databaseHelper.deleteDB(getApplicationContext());
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(MainActivity.this, PhoneAuthActivity.class));
                finish();
                break;

        }


        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);

        return true;
    }

    @Override
    public void onBackPressed() {
        changeDrawerState();
    }

    public void changeDrawerState(){
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }
        else {
            super.onBackPressed();
        }
    }

    @Override
    public void setDrawerEnabled(boolean enabled,String name) {
        int lockMode = enabled ? DrawerLayout.LOCK_MODE_UNLOCKED :
                DrawerLayout.LOCK_MODE_LOCKED_CLOSED;
        drawer.setDrawerLockMode(lockMode);
        toggle.setDrawerIndicatorEnabled(enabled);
        toolbar.setTitle(name);

        switch (name){
            case "Home":
                mNavigationView.getMenu().findItem(R.id.nav_home).setChecked(true);
                break;
            case "My Order":
                mNavigationView.getMenu().findItem(R.id.nav_my_order).setChecked(true);
                break;
            case "Past Order":
                mNavigationView.getMenu().findItem(R.id.nav_last_order).setChecked(true);
                break;
            case "New Order":
                mNavigationView.getMenu().findItem(R.id.nav_new_order).setChecked(true);
                break;
            case "Terms and Conditions":
                mNavigationView.getMenu().findItem(R.id.nav_terms).setChecked(true);
                break;
            case "Feedback":
                mNavigationView.getMenu().findItem(R.id.nav_feedback).setChecked(true);
                break;
            case "Contact Us":
                mNavigationView.getMenu().findItem(R.id.nav_contact).setChecked(true);
                break;

        }

        if (enabled) {
            toolbar.setVisibility(View.VISIBLE);
            setNavigationValues();
        } else
            toolbar.setVisibility(View.GONE);
    }

    private void setNavigationValues() {

        if(personalDetails != null) {
            String tempFullName = personalDetails.getFirstname() +" "+ personalDetails.getLastname();
            mTxtNavProfileName.setText(tempFullName);
            mTxtNavProfilePhone.setText(personalDetails.getPersonalno());

            if(personalDetails.getUserType().length() !=0) {
                String tempGender = personalDetails.getUserType();
                switch (tempGender) {
                    case "1":
                        mNavigationView.getMenu().findItem(R.id.nav_my_order).setVisible(false);
                        mNavigationView.getMenu().findItem(R.id.nav_new_order).setVisible(false);
                        mTxtNavProfileUser.setText(R.string.user_seller);
                        break;
                    case "2":
                        mNavigationView.getMenu().findItem(R.id.nav_last_order).setVisible(false);
                        mTxtNavProfileUser.setText(R.string.user_customer);
                        break;
                }
            } else
                mTxtNavProfileUser.setVisibility(View.GONE);

            mBtnNavEditProfile.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    DrawerLayout drawer = findViewById(R.id.drawer_layout);
                    drawer.closeDrawer(GravityCompat.START);
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.mainFragContent,ProfileFragment.newInstance(ProfileFragment.PARAM2))
                            .addToBackStack(null)
                            .commit();
                }
            });

        }
    }
}
