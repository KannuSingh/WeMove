package com.wemove.service;

import com.wemove.model.ResetPasswordDAO;
import com.wemove.model.SecurityQuestionDto;
import com.wemove.model.UserCredentials;
import com.wemove.model.UserDetails;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface  IUserOnboardingService{

    @POST("/userLogin")
    Call<UserDetails> login(@Body UserCredentials userCredentials);

    @POST("/register")
    Call<UserDetails> register(@Body UserDetails userDetails);

    @POST("/forgotPassword")
    Call<SecurityQuestionDto> forgotPassword(@Query("email") String email );

    @POST("/forgotPassword/resetPassword")
    Call<Boolean> resetPassword(@Body ResetPasswordDAO resetPassword );


}