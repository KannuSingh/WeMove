package com.wemove.service;

import com.wemove.model.ResetPasswordDAO;
import com.wemove.model.UserCredentials;
import com.wemove.model.UserDetails;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface  IUserOnboardingService{

    @POST("/userLogin")
    Call<Boolean> login(@Body UserCredentials userCredentials);

    @POST("/register")
    Call<UserDetails> register(@Body UserDetails userDetails);

    @GET("/forgotPassword")
    Call<String> forgotPassword(@Query("email") String email );

    @POST("/forgotPassword/resetPassword")
    Call<String> resetPassword(@Body ResetPasswordDAO resetPassword );


}