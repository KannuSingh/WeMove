package com.wemove.service;

import com.wemove.model.MoveRequest;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ICustomerService {

    @POST("/createMoveRequest")
    Call<Boolean> createMoveRequest(@Body MoveRequest moveRequest);

    @POST("/getAllMoveRequest")
    Call<List<MoveRequest>> getAllCustomerMoveRequest(@Query("email") String email );


}
