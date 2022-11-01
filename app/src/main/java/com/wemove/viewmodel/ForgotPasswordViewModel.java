package com.wemove.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ForgotPasswordViewModel extends ViewModel {
    private static final String TAG= "ForgotPasswordViewModel";
    private MutableLiveData<String> _fpEmail = new MutableLiveData<>();
    private MutableLiveData<String> _fpSecurityQuestion = new MutableLiveData<>();
    private MutableLiveData<String> _fpSecurityAnswer = new MutableLiveData<>();

    private LiveData<String> fpEmail ;
    private LiveData<String> fpSecurityQuestion ;
    private LiveData<String> fpSecurityAnswer ;

    public LiveData<String> getFpEmail() {
        return _fpEmail;
    }

    public LiveData<String> getFpSecurityQuestion() {
        return _fpSecurityQuestion;
    }

    public LiveData<String> getFpSecurityAnswer() {
        return _fpSecurityAnswer;
    }

    public void getSecurityQuestion(String email){

    }




}
