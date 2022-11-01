package com.wemove.viewmodel;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.wemove.model.UserCredentials;
import com.wemove.network.RetrofitClientInstance;
import com.wemove.service.IUserOnboardingService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginViewModel extends ViewModel {
    private static final String TAG= "LoginViewModel";
    private MutableLiveData<Boolean>  _loginFlag = new MutableLiveData<Boolean>();
    private LiveData<Boolean> loginFlag;


    public LiveData<Boolean> isLoginFlag() {
        return _loginFlag;
    }


    public LoginViewModel(){
        Log.d(TAG,"LoginViewModel Created : "+this.toString());

    }

   public void login(String email,String password){
       IUserOnboardingService onboardingService = RetrofitClientInstance.createService(IUserOnboardingService.class,email,password);
       UserCredentials userCredentials = new UserCredentials();
       userCredentials.setEmail(email);
       userCredentials.setPassword(password);
       onboardingService.login(userCredentials).enqueue(new Callback<Boolean>() {

           @Override
           public void onResponse(Call<Boolean> call, Response<Boolean> response) {
               Log.i("LOGIN Response :", response.body().toString());
               _loginFlag.setValue(true);
           }

           @Override
           public void onFailure(Call<Boolean> call, Throwable t) {
               Log.i("LOGIN Failure :",t.toString() );
               _loginFlag.setValue(false);
           }
       });
        /*if(onboardingService.login(userCredentials);){
            Log.i("LoginViewModel",email);
            return true;
        }*/

    }

    @Override
    protected void onCleared() {
        super.onCleared();
        Log.d("LoginViewModel","LoginViewModel destroyed!");

    }
}
