package com.wemove.viewmodel;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.wemove.model.UserCredentials;
import com.wemove.model.UserDetails;
import com.wemove.network.RetrofitClientInstance;
import com.wemove.service.IUserOnboardingService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginViewModel extends ViewModel {
    private static final String TAG= "LoginViewModel";

    private MutableLiveData<UserDetails>  _loginUserDetails = new MutableLiveData<UserDetails>();
    private MutableLiveData<UserDetails>  loginUserDetails;
    public LiveData<UserDetails> getLoginUserDetails(){ return _loginUserDetails;}

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
       onboardingService.login(userCredentials).enqueue(new Callback<UserDetails>() {

           @Override
           public void onResponse(Call<UserDetails> call, Response<UserDetails> response) {
               if(response.isSuccessful()){
                   Log.i("LOGIN Response :", ""+response.body());
                   _loginUserDetails.setValue(response.body());
                   _loginFlag.setValue(true);
                   //_loginUserDetails()
               }
               else{
                   _loginFlag.setValue(false);
               }

              // _loginFlag.setValue(true);
           }

           @Override
           public void onFailure(Call<UserDetails> call, Throwable t) {
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
