package com.wemove.service;

import com.wemove.model.MoveRequest;
import com.wemove.model.MoveRequestDto;
import com.wemove.model.Review;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ICustomerService {

    @POST("/createMoveRequest")
    Call<Boolean> createMoveRequest(@Body MoveRequest moveRequest);

    @POST("/getAllCustomerMoveRequest")
    Call<List<MoveRequestDto>> getAllCustomerMoveRequest(@Query("email") String email );

    @POST("/acceptPriceQuote")
    Call<Boolean> acceptPriceQuote(@Query("moverEmail") String moverEmail,@Query("moveRequestId") int moveRequestId);

    @POST("/declinePriceQuote")
    Call<Boolean> declinePriceQuote(@Query("moverEmail")String moverEmail, @Query("moveRequestId") int moveRequestId);

    @POST("/addReview")
    Call<Review> addReview(@Body Review review);
}
