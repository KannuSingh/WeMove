package com.wemove.ui.mover;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.wemove.MainActivity;
import com.wemove.R;
import com.wemove.databinding.ActivityCustomerDashboardBinding;
import com.wemove.databinding.ActivityMoverDashboardBinding;
import com.wemove.viewmodel.CustomerViewModel;
import com.wemove.viewmodel.MoverViewModel;

import java.util.Objects;

public class MoverDashboardActivity extends AppCompatActivity {


    private ActivityMoverDashboardBinding binding;
    public DrawerLayout drawerLayout;
    public ActionBarDrawerToggle actionBarDrawerToggle;
    private MoverViewModel moverViewModel;
    private NavController navController ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMoverDashboardBinding.inflate(getLayoutInflater());


        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.mover_fragments_view_container);
        navController = navHostFragment.getNavController();


        NavigationView nv = binding.moverNavigationView;

        actionBarDrawerToggle = new ActionBarDrawerToggle(this, binding.myDrawerLayout, R.string.nav_open, R.string.nav_close);
        actionBarDrawerToggle.setToolbarNavigationClickListener(view -> {
            actionBarDrawerToggle.setDrawerIndicatorEnabled(true); // Show the burger icon & enable the drawer funcionality
            navController.navigate(R.id.moverDashboardFragment); // Back to default fragment (replace home with your default fragment id in the navGraph)
        });
        binding.myDrawerLayout.addDrawerListener(actionBarDrawerToggle);

        navController.addOnDestinationChangedListener((navController1, navDestination, bundle) -> {
            // Repeat this condition for all the Fragments that you want to show the back button
            if (navDestination.getId() == R.id.findDeliveryFragment
            || navDestination.getId() == R.id.favoriteDeliveryFragment
                    || navDestination.getId() == R.id.MRDetailMoverViewFragment
                    || navDestination.getId() == R.id.pastDeliveryFragment) { // replace `settings_id` with your fragment id in the navGraph that you want to show the back button
                // Disable the functionality of opening the side drawer, when the burger icon is clicked & show the UP button instead
                actionBarDrawerToggle.setDrawerIndicatorEnabled(false);

            }
            else{
                actionBarDrawerToggle.setDrawerIndicatorEnabled(true);
            }
        });

        actionBarDrawerToggle.syncState();

        //BottomNavigationView bnv = binding.moverBottomNavigationView;
        NavigationUI.setupWithNavController(nv,navController);
        //NavigationUI.setupActionBarWithNavController(this,navHostFragment.getNavController());

        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(this,R.color.blue)));
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);


        binding.moverNavigationView.setNavigationItemSelectedListener(item -> {
            int id=item.getItemId();
            //it's possible to do more actions on several items, if there is a large amount of items I prefer switch(){case} instead of if()
            if (id==R.id.moverLogoutFragment){
                Toast.makeText(getApplicationContext(), "Logging Out", Toast.LENGTH_SHORT).show();
                removedLoggedInUserDetails();
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                this.finish();

            }
            //This is for maintaining the behavior of the Navigation view
            NavigationUI.onNavDestinationSelected(item,navController);
            //This is for closing the drawer after acting on it
            binding.myDrawerLayout.closeDrawer(GravityCompat.START);
            return true;
        });

        moverViewModel = new ViewModelProvider(this).get(MoverViewModel.class);
        setLoggedInUser();
        setContentView(binding.getRoot());
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
            Log.i("MoverDashboardActivity",""+item.getTitle());
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        return navController.navigateUp() ||  super.onSupportNavigateUp();
    }

    @Override
    public void onBackPressed() {
        Log.i("MoverDashboardActivity","Back button pressed");
        moveTaskToBack(false);
    }

    private void setLoggedInUser() {
        SharedPreferences sharedPreferences = this.getSharedPreferences("wemove", Context.MODE_PRIVATE);
        String email = sharedPreferences.getString("email", null);
        String password = sharedPreferences.getString("password", null);
        String userDetails = sharedPreferences.getString("userDetails", null);


        moverViewModel.setUser(email,password,userDetails);
    }

    private void removedLoggedInUserDetails() {
        SharedPreferences sharedPreferences = this.getSharedPreferences("wemove", Context.MODE_PRIVATE);
        SharedPreferences.Editor sharedEditor = sharedPreferences.edit();
        sharedEditor.clear();
        sharedEditor.commit();
        sharedEditor.apply();
    }
}