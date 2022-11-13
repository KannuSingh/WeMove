package com.wemove.repository;

import com.wemove.model.MoveRequest;
import com.wemove.network.RetrofitClientInstance;
import com.wemove.service.ICustomerService;

import java.util.List;

import retrofit2.Call;

public class CustomerRepository {


    private ICustomerService customerService;

    public CustomerRepository(String username, String password){
        this.customerService = RetrofitClientInstance.createService(ICustomerService.class,username,password);
    }

    Call<List<MoveRequest>> getAllCustomerMoveRequest(String email){
        return customerService.getAllCustomerMoveRequest(email);
    }

    Call<Boolean> createMoveRequest(MoveRequest moveRequest){
        return customerService.createMoveRequest(moveRequest);
    }

}
