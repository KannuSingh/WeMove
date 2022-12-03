package com.wemove.repository;

import com.wemove.model.MRStatusItem;
import com.wemove.model.MoveRequest;
import com.wemove.model.MoveRequestDto;
import com.wemove.model.PriceQuote;
import com.wemove.model.Review;
import com.wemove.network.RetrofitClientInstance;
import com.wemove.service.ICustomerService;
import com.wemove.service.IMoverService;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Query;

public class MoverRepository {

    private IMoverService moverService;

    public MoverRepository(String username, String password){
        this.moverService = RetrofitClientInstance.createService(IMoverService.class,username,password);
    }

    public Call<List<MoveRequestDto>> getAllMoveRequestDeliveries(){
        return moverService.getAllMoveRequestDeliveries();
    }
    public Call<List<MoveRequestDto>> getMyMoveRequestDeliveries(String moverEmail){
        return moverService.getMyMoveRequestDeliveries(moverEmail);
    }


    public Call<Boolean> submitPriceQuote(PriceQuote priceQuote){
        return moverService.submitPriceQuote(priceQuote);
    }

    public Call<List<Review>> getReviews(String moverEmail){
        return moverService.getReviews(moverEmail);
    }

    public Call<List<MRStatusItem>> getStatusTimeline(int moveRequestId){
        return moverService.getStatusTimeline(moveRequestId);
    }


}
