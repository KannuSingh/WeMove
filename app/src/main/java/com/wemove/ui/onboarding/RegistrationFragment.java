package com.wemove.ui.onboarding;

import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wemove.R;
import com.wemove.databinding.FragmentRegistrationBinding;


public class RegistrationFragment extends Fragment {
    private static String TAG ="RegistrationFragment.class";
    private FragmentRegistrationBinding binding;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_registration, container, false);
        NavHostFragment navHostFragment = (NavHostFragment) requireActivity().getSupportFragmentManager().findFragmentById(R.id.view_onboarding_container);
        NavController navController = null;
        if (navHostFragment != null) {

            navController = navHostFragment.getNavController();
        }


        //this will check the registration in firebase database
        binding.buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               // handleRegistration();
                //here will the logic for registration in firebase database
            }
        });

        binding.buttonLogin.setOnClickListener(view -> Navigation.findNavController(view).navigate(R.id.action_registrationFragment_to_loginFragment));

        return binding.getRoot();
    }
}