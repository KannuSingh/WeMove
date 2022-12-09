package com.wemove;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import com.wemove.viewmodel.ForgotPasswordViewModel;
import com.wemove.viewmodel.MoverViewModel;

public class MainActivity extends AppCompatActivity {
    private final String TAG = "MainActivity";
    private ForgotPasswordViewModel forgotPasswordViewModel;
    private NavController navController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.view_onboarding_container);
        navController = navHostFragment.getNavController();

        NavigationUI.setupActionBarWithNavController(this, navHostFragment.getNavController());


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (isUserLoggedIn()) {
                    String userType = getLoggedInUserType();
                    if ("CUSTOMER".equals(userType)) {
                        navController.navigate(R.id.action_splashScreenFragment_to_customerDashboardActivity);
                    } else if ("MOVER".equals(userType)) {
                        navController.navigate(R.id.action_splashScreenFragment_to_moverDashboardActivity);
                    }

                    Log.i(TAG, "User_type : " + userType);
                    //Get User details and show appropriate activity
                } else {
                    navController.navigate(R.id.action_splashScreenFragment_to_loginFragment);
                }
            }
        }, 1000);

        forgotPasswordViewModel = new ViewModelProvider(this).get(ForgotPasswordViewModel.class);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(this,R.color.button_primary)));

    }


    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }

    public boolean isUserLoggedIn() {
        SharedPreferences sharedPreferences = getSharedPreferences("wemove", Context.MODE_PRIVATE);
        SharedPreferences.Editor sharedEditor = sharedPreferences.edit();
        boolean isUserLoggedIn = sharedPreferences.getBoolean("isUserLoggedIn", false);
        Log.i(TAG, "User Logged In  : " + isUserLoggedIn);
        return isUserLoggedIn;
    }

    //Checking sharedPreferences to check whether userType of loggedIn user
    public String getLoggedInUserType() {
        SharedPreferences sharedPreferences = getSharedPreferences("wemove", Context.MODE_PRIVATE);
        return sharedPreferences.getString("user_type", null);
    }
}