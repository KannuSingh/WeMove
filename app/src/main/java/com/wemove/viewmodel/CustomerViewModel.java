package com.wemove.viewmodel;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.gson.Gson;
import com.wemove.model.MoveRequest;
import com.wemove.model.UserDetails;
import com.wemove.network.RetrofitClientInstance;
import com.wemove.service.ICustomerService;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CustomerViewModel extends ViewModel {

    private static final String TAG= "CustomerViewModel";

    private MutableLiveData<UserDetails> _userDetails = new MutableLiveData<>();
    private LiveData<UserDetails> userDetails;

    private MutableLiveData<List<MoveRequest>> _activeMoveRequests = new MutableLiveData<>();
    private LiveData<List<MoveRequest>> activeMoveRequests;

    private MutableLiveData<List<MoveRequest>> _allMoveRequests = new MutableLiveData<>();
    private LiveData<List<MoveRequest>> allMoveRequests;

    private MutableLiveData<List<MoveRequest>> _completedMoveRequests = new MutableLiveData<>();
    private LiveData<List<MoveRequest>> completedMoveRequests;

    private MutableLiveData<String> _email = new MutableLiveData<>();


    private MutableLiveData<String> _password = new MutableLiveData<>();


    public LiveData<UserDetails> getUserDetails() {
        return _userDetails;
    }

    public LiveData<List<MoveRequest>> getActiveMoveRequests() {
        return _activeMoveRequests;
    }

    public LiveData<List<MoveRequest>> getCompletedMoveRequests() {
        return _completedMoveRequests;
    }

    public CustomerViewModel(){
        Log.d(TAG,"CustomerViewModel Created : "+this.toString());
        _activeMoveRequests.setValue(new ArrayList<>());
        _completedMoveRequests.setValue(new ArrayList<>());

    }
    public void setUser( String email, String password,String userDetails){
        _email.setValue(email);
        _password.setValue(password);
        Gson gson = new Gson();
        _userDetails.setValue(gson.fromJson(userDetails,UserDetails.class));
    }

    public void getAllMoveRequest(String email, String password){
        ICustomerService customerService = RetrofitClientInstance.createService(ICustomerService.class,email,password);
        Call<List<MoveRequest>> moveRequestList = customerService.getAllCustomerMoveRequest(email);

        moveRequestList.enqueue(new Callback<List<MoveRequest>>() {
            @Override
            public void onResponse(Call<List<MoveRequest>> call, Response<List<MoveRequest>> response) {
                if(response.isSuccessful()){
                    Log.i(TAG, "Customer MoveRequests::"+response.body());
                    _allMoveRequests.setValue(response.body());
                    _activeMoveRequests.setValue(response.body());
                }else{
                    Log.i(TAG, "Customer MoveRequests::"+response.raw());
                }
            }

            @Override
            public void onFailure(Call<List<MoveRequest>> call, Throwable t) {
                Log.i(TAG, "Customer MoveRequests:: Failed");
            }
        });

    }

    @Override
    protected void onCleared() {
        super.onCleared();
        Log.d("CustomerViewModel","CustomerViewModel destroyed!");

    }

    public void filterMoveRequestByStatus(String filterStatus) {
        if("Any".equals(filterStatus)){
            _activeMoveRequests.setValue(_allMoveRequests.getValue());
        }else {
            List<MoveRequest> filteredMoveRequests = new ArrayList<>();
            for (MoveRequest mr : _allMoveRequests.getValue()) {
                if (mr.getMoveRequestStatus().toString().equals(filterStatus)) {
                    filteredMoveRequests.add(mr);
                }
            }
            _activeMoveRequests.setValue(filteredMoveRequests);
        }
    }
}
