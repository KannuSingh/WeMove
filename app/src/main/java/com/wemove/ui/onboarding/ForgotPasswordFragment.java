package com.wemove.ui.onboarding;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.wemove.R;
import com.wemove.databinding.FragmentForgotPasswordBinding;
import com.wemove.viewmodel.ForgotPasswordViewModel;
import com.wemove.viewmodel.LoginViewModel;
import com.wemove.viewmodel.RegistrationViewModel;


public class ForgotPasswordFragment extends Fragment {
    private String TAG = "ForgotPasswordFragment";
    private FragmentForgotPasswordBinding binding;
    private ForgotPasswordViewModel forgotPasswordViewModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        forgotPasswordViewModel  = new ViewModelProvider(getActivity()).get(ForgotPasswordViewModel.class);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_forgot_password, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        //super.onViewCreated(view, savedInstanceState);

        //On login Button Clicked checking users credentials in firebase database.
        binding.buttonProceed.setOnClickListener(v -> {
            Log.i(TAG,"Proceed Button Clicked");
            if(binding.etEmail.getText() != null || binding.etEmail.getText().toString()!=""){
                Log.i(TAG,"Calling forgotPassword ");
                forgotPasswordViewModel.getSecurityQuestion(binding.etEmail.getText().toString());
            }
            else{
                Toast.makeText(getContext(),"Please enter valid email address",Toast.LENGTH_SHORT).show();
            }
        });

        final Observer<String> securityQuestionObserver = new Observer<String>() {
            @Override
            public void onChanged(String securityQuestion) {
                Log.i(TAG,"securityQuestionObserver : "+securityQuestion);
                Navigation.findNavController(getView()).navigate(R.id.action_forgotPasswordFragment2_to_securityQuestionFragment2);
            }
        };
        forgotPasswordViewModel.getFpSecurityQuestion().observe(this.getActivity(),securityQuestionObserver);

        binding.buttonCancel.setOnClickListener(v -> Navigation.findNavController(v).navigate(R.id.action_forgotPasswordFragment2_to_mainActivity));
    }


}