package com.wemove.viewmodel;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.wemove.model.UserDetails;
import com.wemove.network.RetrofitClientInstance;
import com.wemove.service.IUserOnboardingService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegistrationViewModel extends ViewModel {
    private static final String TAG = "RegistrationViewModel";

    private MutableLiveData<UserDetails> _userDetails = new MutableLiveData<>();
    private LiveData<UserDetails> userDetails;

    public LiveData<UserDetails> getUserDetails() {
        return _userDetails;
    }

    private MutableLiveData<Boolean>  _registerFlag = new MutableLiveData<Boolean>();
    private LiveData<Boolean> registerFlag;

    public LiveData<Boolean> isRegisterFlag() {
        return _registerFlag;
    }

    public void onRegistration(UserDetails userDetails) {
        Log.i(TAG,"onRegistration");
        IUserOnboardingService onboardingService = RetrofitClientInstance.createService(IUserOnboardingService.class);
        onboardingService.register(userDetails).enqueue(new Callback<UserDetails>() {
            @Override
            public void onResponse(Call<UserDetails> call, Response<UserDetails> response) {
                if(response.isSuccessful()) {
                    if(response.body() != null) {
                        _registerFlag.setValue(true);
                    }else{
                        _registerFlag.setValue(false);
                    }
                    Log.i("RegistrationFragment:", response.body().toString());
                }
            }

            @Override
            public void onFailure(Call<UserDetails> call, Throwable t) {
                Log.i("RegistrationFragment", t.toString());
                _registerFlag.setValue(false);
            }
        });
    }
}
