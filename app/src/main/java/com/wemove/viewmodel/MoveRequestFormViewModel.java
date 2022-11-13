package com.wemove.viewmodel;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.wemove.model.Address;
import com.wemove.model.InventoryItemGroup;
import com.wemove.model.MoveHelp;
import com.wemove.model.MoveRequest;
import com.wemove.model.MoveStatus;
import com.wemove.network.RetrofitClientInstance;
import com.wemove.service.ICustomerService;
import com.wemove.service.IUserOnboardingService;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MoveRequestFormViewModel extends ViewModel {
    private static final String TAG= "MoveRequestViewModel";

    private MutableLiveData<MoveRequest> _moveRequest = new MutableLiveData<>();
    private LiveData<MoveRequest> moveRequest;

    private MutableLiveData<List<InventoryItemGroup>> _inventory = new MutableLiveData<>();
    private LiveData<List<InventoryItemGroup>> inventory ;


    private MutableLiveData<Boolean>  _createMoveRequestFlag = new MutableLiveData<Boolean>();
    private LiveData<Boolean> createMoveRequestFlag;

    public LiveData<Boolean> isCreateMoveRequestSuccessful(){
        return _createMoveRequestFlag;
    }

    public MoveRequestFormViewModel(){
        Log.d(TAG,"MoveRequestViewModel Created : "+this.toString());
        _moveRequest.setValue(new MoveRequest());
    }


    public LiveData<List<InventoryItemGroup>> getInventory(){ return _inventory;}
    public LiveData<MoveRequest> getMoveRequest(){ return _moveRequest;}

    public void addNewInventoryItem(InventoryItemGroup newInventoryItem){
        List<InventoryItemGroup> inventoryItems = _inventory.getValue();
        if(inventoryItems != null){
            inventoryItems.add(newInventoryItem);
        }else{
            inventoryItems = new ArrayList<>();
            inventoryItems.add(newInventoryItem);
        }
        _inventory.setValue(inventoryItems);
        _moveRequest.getValue().setItemInventory(inventoryItems);
        Log.i(TAG,"Inventory Data changed!");
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        Log.d(TAG,"MoveRequestViewModel destroyed!");

    }

    public void setMoveRequestTitle(String moveRequestTitle){
        _moveRequest.getValue().setMoveTitle(moveRequestTitle);
    }

    public void setMoveRequestDeliveryAddress(String address1, String city, String postalCode){
        Address address = new Address();
        address.setAddress1(address1);
        address.setPostcode(postalCode);
        address.setCity(city);
        address.setCountry("Canada");
        _moveRequest.getValue().setDeliveryAddress(address);
    }

    public void setMoveRequestPickupAddress(String address1, String city, String postalCode){
        Address address = new Address();
        address.setAddress1(address1);
        address.setPostcode(postalCode);
        address.setCity(city);
        address.setCountry("Canada");
        _moveRequest.getValue().setPickupAddress(address);
    }

    public void setPickupFloorLevel(String level){
        _moveRequest.getValue().setPickupFloorLevel(level);
    }
    public void setDeliveryFloorLevel(String level){
        _moveRequest.getValue().setDeliveryFloorLevel(level);
    }

    public void setMoveRequestType(String type){
        _moveRequest.getValue().setMoveType(type);
    }

    public void setMoveRequestMoveDate(String date){
        _moveRequest.getValue().setMoveDate(date);
    }

    public void setMoveRequestMoveHelp(MoveHelp moveHelp){
        _moveRequest.getValue().setMoveHelp(moveHelp);
    }

    public void createMoveRequest(String email, String password) {
        _moveRequest.getValue().setUpdatedOn((new Date()).toString());
        _moveRequest.getValue().setCreatedOn((new Date()).toString());
        _moveRequest.getValue().setMoveRequestStatus(MoveStatus.CREATED);
        ICustomerService customerService = RetrofitClientInstance.createService(ICustomerService.class,email,password);
        _moveRequest.getValue().setMoveRequestOwner(email);
        Call<Boolean> result = customerService.createMoveRequest(_moveRequest.getValue());
        result.enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                if(response.isSuccessful()){
                    Log.i(TAG, "Create MoveRequest::"+response.body());
                    _createMoveRequestFlag.setValue(response.body());

                }
                else{
                    _createMoveRequestFlag.setValue(false);
                }
            }

            @Override
            public void onFailure(Call<Boolean> call, Throwable t) {
                _createMoveRequestFlag.setValue(false);
            }
        });
        Log.i(TAG,"Will make a final network call to save data"+_moveRequest.getValue().toString());

        //make network call to create moveRequest.
    }
}
