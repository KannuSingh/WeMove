package com.wemove.viewmodel;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.wemove.model.ResetPasswordDAO;
import com.wemove.model.SecurityQuestionDto;
import com.wemove.repository.OnboardingRepository;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ForgotPasswordViewModel extends ViewModel {
    private static final String TAG= "ForgotPasswordViewModel";

    private MutableLiveData<String> _fpEmail = new MutableLiveData<>();
    private MutableLiveData<String> _fpSecurityQuestion = new MutableLiveData<>();
    private MutableLiveData<Boolean> _fpResetPasswordFlag = new MutableLiveData<>();

    private LiveData<String> fpEmail;
    private LiveData<String> fpSecurityQuestion ;
    private LiveData<String> fpSecurityAnswer ;

    public LiveData<String> getFpEmail() {
        return _fpEmail;
    }

    public LiveData<String> getFpSecurityQuestion() {
        return _fpSecurityQuestion;
    }

    public LiveData<Boolean> getFpResetPasswordFlag() {
        return _fpResetPasswordFlag;
    }



    public ForgotPasswordViewModel(){
        Log.d(TAG,"ForgotPasswordViewModel Created ");
    }



    public void getSecurityQuestion(String email){
        OnboardingRepository onboardingRepository = new OnboardingRepository();
        Call<SecurityQuestionDto> securityQuestion = onboardingRepository.forgotPassword(email);
        Log.i(TAG,"forgotPassword for email:"+email);
        securityQuestion.enqueue(new Callback<SecurityQuestionDto>() {
            @Override
            public void onResponse(Call<SecurityQuestionDto> call, Response<SecurityQuestionDto> response) {
                if(response.isSuccessful()){
                    Log.i(TAG,response.raw().body().toString());
                    Log.i(TAG,"Response for forgot password:"+response.body().getSecurityQuestion());
                    _fpEmail.setValue(response.body().getEmail());

                    _fpSecurityQuestion.setValue(response.body().getSecurityQuestion());

                }
                else{
                    Log.i(TAG,"Forgot password api call unsuccessful :"+response.raw());
                }
            }

            @Override
            public void onFailure(Call<SecurityQuestionDto> call, Throwable t) {

            }
        });
    }

    public void resetPassword(String newPassword, String confirmPassword, String securityAnswer){

        ResetPasswordDAO resetPasswordDAO = new ResetPasswordDAO();
        resetPasswordDAO.setNewPassword(newPassword);
        resetPasswordDAO.setSecurityQuestion(_fpSecurityQuestion.getValue());
        resetPasswordDAO.setSecretAnswer(securityAnswer);
        resetPasswordDAO.setEmail(getFpEmail().getValue());

        OnboardingRepository onboardingRepository = new OnboardingRepository();
        Call<Boolean> resetPassword = onboardingRepository.resetPassword(resetPasswordDAO);
        resetPassword.enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                if(response.isSuccessful()){
                    Log.i(TAG, String.valueOf(response.body()));
                    _fpResetPasswordFlag.setValue(response.body());

                }else {
                    Log.i(TAG, String.valueOf(response.body()));
                    _fpResetPasswordFlag.setValue(false);
                }
            }

            @Override
            public void onFailure(Call<Boolean> call, Throwable t) {
                Log.i(TAG, String.valueOf(t.getMessage()));
                _fpResetPasswordFlag.setValue(false);
            }
        });

    }




}
