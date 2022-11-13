package com.wemove.repository;

import com.wemove.model.ResetPasswordDAO;
import com.wemove.model.UserCredentials;
import com.wemove.model.UserDetails;
import com.wemove.network.RetrofitClientInstance;
import com.wemove.service.IUserOnboardingService;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Query;

public class OnboardingRepository {

    private IUserOnboardingService userOnboardingService;

    public OnboardingRepository(){
        this.userOnboardingService = RetrofitClientInstance.getRetrofitInstance().create(IUserOnboardingService.class);
    }

    Call<UserDetails> login(UserCredentials userCredentials){
       return userOnboardingService.login(userCredentials);
    }

    Call<UserDetails> register(UserDetails userDetails){
        return userOnboardingService.register(userDetails);
    }

    Call<String> forgotPassword(String email ){
        return userOnboardingService.forgotPassword(email);
    }

    Call<String> resetPassword(ResetPasswordDAO resetPassword ){
        return userOnboardingService.resetPassword(resetPassword);
    }


}
