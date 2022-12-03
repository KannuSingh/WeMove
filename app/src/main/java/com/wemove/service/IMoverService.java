package com.wemove.service;

import com.wemove.model.MRStatusItem;
import com.wemove.model.MoveRequest;
import com.wemove.model.MoveRequestDto;
import com.wemove.model.MoveStatus;
import com.wemove.model.PriceQuote;
import com.wemove.model.Review;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface IMoverService {

  /*  @POST("/createMoveRequest")
    Call<Boolean> createMoveRequest(@Body MoveRequest moveRequest);*/

    @POST("/getAllMoveRequestDeliveries")
    Call<List<MoveRequestDto>> getAllMoveRequestDeliveries();

    @POST("/getMyMoveRequestDeliveries")
    Call<List<MoveRequestDto>> getMyMoveRequestDeliveries(@Query("moverEmail") String moverEmail);

    @POST("/submitPriceQuote")
    Call<Boolean> submitPriceQuote(@Body PriceQuote priceQuote);

    @GET("/getReviews")
    Call<List<Review>> getReviews(@Query("moverEmail") String moverEmail);

    @GET("/getStatusTimeline")
    public Call<List<MRStatusItem>> getStatusTimeline(@Query("moveRequestId") int moveRequestId);


    @POST("/updateMoveRequestStatus")
    public Call<Void> updateMoveRequestStatus(@Query("moveRequestId") int moveRequestId, @Query("moveStatus") MoveStatus moveStatus);

    }
