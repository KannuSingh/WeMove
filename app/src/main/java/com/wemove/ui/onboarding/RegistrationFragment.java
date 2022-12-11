package com.wemove.ui.onboarding;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.wemove.R;
import com.wemove.databinding.FragmentRegistrationBinding;
import com.wemove.model.Address;
import com.wemove.model.UserDetails;
import com.wemove.model.UserType;
import com.wemove.viewmodel.LoginViewModel;
import com.wemove.viewmodel.RegistrationViewModel;


public class RegistrationFragment extends Fragment {
    private static String TAG = "RegistrationFragment.class";
    private FragmentRegistrationBinding binding;
    private RegistrationViewModel registrationViewModel;

    public RegistrationFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        registrationViewModel  = new ViewModelProvider(this).get(RegistrationViewModel.class);

        final Observer<Boolean> registerObserver = new Observer<Boolean>() {
            @Override
            public void onChanged(@Nullable final Boolean registerFlag) {
                if (Boolean.TRUE.equals(registerFlag)) {
                    Toast.makeText(getContext(),"Registration Successfully",Toast.LENGTH_LONG).show();
                    Navigation.findNavController(getView()).navigate(R.id.action_registrationFragment_to_loginFragment);
                }else if(Boolean.FALSE.equals(registerFlag)) {
                    Toast.makeText(getContext(),"Registration Failed",Toast.LENGTH_LONG).show();
                }
            }
        };
        registrationViewModel.isRegisterFlag().observe(this.getActivity(),registerObserver);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_registration, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        binding.buttonRegister.setOnClickListener(v -> onRegister());

        binding.buttonLogin.setOnClickListener(v -> Navigation.findNavController(v).navigate(R.id.action_registrationFragment_to_loginFragment));

    }

    private void onRegister() {
        Log.i(TAG,"OnRegister");
        UserDetails userDetails = new UserDetails();
        userDetails.setUserType(binding.rgUserType.getCheckedRadioButtonId() == R.id.rb_customer ? UserType.CUSTOMER : UserType.MOVER);
        userDetails.setEmail(binding.etEmail.getText().toString());
        userDetails.setPassword(binding.etPassword.getText().toString());
        userDetails.setMobile(binding.etMobile.getText().toString());
        userDetails.setFirstname(binding.etFirstname.getText().toString());
        userDetails.setLastname(binding.etLastname.getText().toString());
        userDetails.setSecurityAnswer(binding.etSecurityAnswer.getText().toString());
        userDetails.setSecurityQuestion(binding.etSecurityQuestion.getText().toString());

        registrationViewModel.onRegistration(userDetails);
    }
}