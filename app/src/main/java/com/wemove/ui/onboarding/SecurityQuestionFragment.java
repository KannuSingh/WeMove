package com.wemove.ui.onboarding;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.wemove.R;
import com.wemove.databinding.FragmentForgotPasswordBinding;
import com.wemove.databinding.FragmentSecurityQuestionBinding;
import com.wemove.viewmodel.ForgotPasswordViewModel;


public class SecurityQuestionFragment extends Fragment {
    private String TAG = "SecurityQuestionFragment";
    private FragmentSecurityQuestionBinding binding;
    private ForgotPasswordViewModel forgotPasswordViewModel;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        forgotPasswordViewModel  = new ViewModelProvider(this).get(ForgotPasswordViewModel.class);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_security_question, container, false);
        Log.i(TAG,"Security Question : "+forgotPasswordViewModel.getFpSecurityQuestion().getValue());
        binding.tvSecurityQuestion.setText(forgotPasswordViewModel.getFpSecurityQuestion().getValue());
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        //super.onViewCreated(view, savedInstanceState);

        //On login Button Clicked checking users credentials in firebase database.
        binding.buttonProceed.setOnClickListener(v -> {
            Log.i(TAG,"Proceed Button Clicked");

        });

        binding.buttonCancel.setOnClickListener(v -> Navigation.findNavController(v).navigate(R.id.action_securityQuestionFragment_to_loginFragment));
    }
}