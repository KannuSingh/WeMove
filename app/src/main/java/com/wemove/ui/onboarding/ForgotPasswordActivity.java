package com.wemove.ui.onboarding;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import android.os.Bundle;

import com.wemove.R;
import com.wemove.viewmodel.ForgotPasswordViewModel;

public class ForgotPasswordActivity extends AppCompatActivity {

    private final String TAG = "ForgotPasswordActivity";
    private ForgotPasswordViewModel forgotPasswordViewModel;
    private NavController navController;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.wemove.R.layout.activity_forgot_password);
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.view_forgot_password_container);
        navController = navHostFragment.getNavController();

        forgotPasswordViewModel = new ViewModelProvider(this).get(ForgotPasswordViewModel.class);
        getSupportActionBar().hide();


    }
}