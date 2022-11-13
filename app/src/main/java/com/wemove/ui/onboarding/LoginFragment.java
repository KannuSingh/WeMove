package com.wemove.ui.onboarding;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.gson.Gson;
import com.wemove.R;
import com.wemove.databinding.FragmentLoginBinding;
import com.wemove.model.UserDetails;
import com.wemove.model.UserType;
import com.wemove.viewmodel.LoginViewModel;

import java.util.Map;


public class LoginFragment extends Fragment {
    private FragmentLoginBinding binding;
    private LoginViewModel  loginViewModel;

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor sharedEditor;


    public LoginFragment() {
        // Required empty public constructor

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loginViewModel  = new ViewModelProvider(this).get(LoginViewModel.class);

        final Observer<Boolean> loginObserver = loginFlag -> {
                // Update the UI, in this case, a TextView.

                if (Boolean.TRUE.equals(loginFlag)) {
                    Toast.makeText(getContext(),"Login Successfully",Toast.LENGTH_SHORT).show();

                }else if(Boolean.FALSE.equals(loginFlag)) {
                    setErrorTextField(true);
                    Toast.makeText(getContext(),"Login Failed",Toast.LENGTH_SHORT).show();
                }
                Log.i("LoginFragment","Navigate to Dashboard");
            };


        final Observer<UserDetails> loginUserObserver = new Observer<UserDetails>() {
            @Override
            public void onChanged(@Nullable final UserDetails userDetails) {
                // Update the UI, in this case, a TextView.

                if (userDetails!=null && userDetails.getUserType().equals(UserType.CUSTOMER)) {
                    String email = binding.etEmail.getText().toString();
                    String password = binding.etPassword.getText().toString();
                    setLoggedInUserDetails(userDetails,email,password);
                    Navigation.findNavController(getView()).navigate(R.id.action_loginFragment_to_customerDashboardActivity);

                }else if(userDetails!=null && userDetails.getUserType().equals(UserType.MOVER)) {
                    Navigation.findNavController(getView()).navigate(R.id.action_loginFragment_to_moverDashboardActivity);

                }
                Log.i("LoginFragment","Invalid User Credentials");
            }
        };

        loginViewModel.isLoginFlag().observe(this.getActivity(),loginObserver);

        loginViewModel.getLoginUserDetails().observe(this.getActivity(),loginUserObserver);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_login, container, false);
        Log.d("LoginFragment", "LoginFragment created/re-created!");
        //  NavHostFragment navHostFragment = (NavHostFragment) requireActivity().getSupportFragmentManager().findFragmentById(R.id.view_onboarding_container);
        // NavController navController = navHostFragment.getNavController();




        return binding.getRoot();


    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        //super.onViewCreated(view, savedInstanceState);

        //On login Button Clicked checking users credentials in firebase database.
        binding.buttonLogin.setOnClickListener(v -> onLogin());

        binding.buttonRegister.setOnClickListener(v -> Navigation.findNavController(v).navigate(R.id.action_loginFragment_to_registrationFragment));
        binding.buttonForgotPassword.setOnClickListener(v -> Navigation.findNavController(v).navigate(R.id.action_loginFragment_to_forgotPasswordFragment));

    }

    private void onLogin(){
        String email = binding.etEmail.getText().toString();
        String password = binding.etPassword.getText().toString();
        loginViewModel.login(email,password);

    }

    private void setErrorTextField(boolean error) {
        if (error) {
            binding.tflEmail.setErrorEnabled(true);
            binding.tflEmail.setError("Invalid");
            binding.tflPassword.setErrorEnabled(true);
            binding.tflPassword.setError("Invalid");

        } else {
            binding.tflEmail.setErrorEnabled(false);
            binding.tflEmail.setError(null);
            binding.tflPassword.setErrorEnabled(false);
            binding.tflPassword.setError(null);
        }
    }

    private void setLoggedInUserDetails(UserDetails userDetails,String email, String password) {
        sharedPreferences = getContext().getSharedPreferences("wemove", Context.MODE_PRIVATE);
        sharedEditor = sharedPreferences.edit();
        Gson gson = new Gson();
        String userDetailsString = gson.toJson(userDetails);
        sharedEditor.putString("email",email);
        sharedEditor.putString("password",password);
        sharedEditor.putString("userDetails", userDetailsString);
        sharedEditor.commit();
        sharedEditor.apply();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.d("LoginFragment", "LoginFragment destroyed!");
    }
}