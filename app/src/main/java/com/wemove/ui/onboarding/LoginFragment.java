package com.wemove.ui.onboarding;

import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.text.Editable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wemove.R;
import com.wemove.databinding.FragmentLoginBinding;


public class LoginFragment extends Fragment {
    private FragmentLoginBinding binding;


    public LoginFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_login, container, false);

        //  NavHostFragment navHostFragment = (NavHostFragment) requireActivity().getSupportFragmentManager().findFragmentById(R.id.view_onboarding_container);
        // NavController navController = navHostFragment.getNavController();

        //On login Button Clicked checking users credentials in firebase database.
        binding.buttonLogin.setOnClickListener(view -> {
            Editable email = binding.etEmail.getText();
            Editable password = binding.etPassword.getText();
            if (email != null && password != null) {
                // checkLoginCredentials(email.toString(), password.toString());
            }

            //navController.navigate(R.id.action_loginFragment_to_customerActivity);

        });

        binding.buttonRegister.setOnClickListener(view -> Navigation.findNavController(view).navigate(R.id.action_loginFragment_to_registrationFragment));


        return binding.getRoot();


    }
}