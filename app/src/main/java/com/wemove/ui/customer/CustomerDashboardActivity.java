package com.wemove.ui.customer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.wemove.R;
import com.wemove.databinding.ActivityCustomerDashboardBinding;
import com.wemove.viewmodel.CustomerViewModel;
import com.wemove.viewmodel.MoveRequestFormViewModel;

public class CustomerDashboardActivity extends AppCompatActivity {

    private ActivityCustomerDashboardBinding binding;
    private CustomerViewModel customerViewModel;
    private NavController navController ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityCustomerDashboardBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.customer_fragments_view_container);
        navController = navHostFragment.getNavController();

        NavigationUI.setupActionBarWithNavController(this,navHostFragment.getNavController());

        BottomNavigationView bnv = binding.customerBottomNavigationView;
        bnv.setBackgroundColor(ContextCompat.getColor(this,R.color.blue));
        NavigationUI.setupWithNavController(bnv,navController);

        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(this,R.color.blue)));

        customerViewModel = new ViewModelProvider(this).get(CustomerViewModel.class);
        setLoggedInUser();


    }

    @Override
    public boolean onSupportNavigateUp() {
        return navController.navigateUp() ||  super.onSupportNavigateUp();
    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(false);
    }

    private void setLoggedInUser() {
        SharedPreferences sharedPreferences = this.getSharedPreferences("wemove", Context.MODE_PRIVATE);
        String email = sharedPreferences.getString("email", null);
        String password = sharedPreferences.getString("password", null);
        String userDetails = sharedPreferences.getString("userDetails", null);


        customerViewModel.setUser(email,password,userDetails);
    }
}