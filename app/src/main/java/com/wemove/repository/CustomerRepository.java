package com.wemove.repository;

import com.wemove.model.MoveRequest;
import com.wemove.model.MoveRequestDto;
import com.wemove.model.Review;
import com.wemove.network.RetrofitClientInstance;
import com.wemove.service.ICustomerService;

import java.util.List;

import retrofit2.Call;

public class CustomerRepository {


    private ICustomerService customerService;

    public CustomerRepository(String username, String password){
        this.customerService = RetrofitClientInstance.createService(ICustomerService.class,username,password);
    }

    public Call<List<MoveRequestDto>> getAllCustomerMoveRequest(String email){
        return customerService.getAllCustomerMoveRequest(email);
    }

    public Call<Boolean> createMoveRequest(MoveRequest moveRequest){
        return customerService.createMoveRequest(moveRequest);
    }
    public Call<Review> addReview(Review review){
        return customerService.addReview(review);
    }

    public Call<Boolean> acceptPriceQuote(String moverEmail, int moveRequestId){
        return customerService.acceptPriceQuote( moverEmail,  moveRequestId);
    }

    public Call<Boolean> declinePriceQuote(String moverEmail, int moveRequestId) {
        return customerService.declinePriceQuote( moverEmail,  moveRequestId);
    }
}
