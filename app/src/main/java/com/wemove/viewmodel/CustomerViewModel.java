package com.wemove.viewmodel;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.gson.Gson;
import com.wemove.model.InventoryItemGroup;
import com.wemove.model.MoveRequest;
import com.wemove.model.MoveRequestDto;
import com.wemove.model.MoveStatus;
import com.wemove.model.PriceQuote;
import com.wemove.model.QuoteStatus;
import com.wemove.model.UserDetails;
import com.wemove.network.RetrofitClientInstance;
import com.wemove.repository.CustomerRepository;
import com.wemove.service.ICustomerService;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CustomerViewModel extends ViewModel {

    private static final String TAG= "CustomerViewModel";

    private MutableLiveData<UserDetails> _userDetails = new MutableLiveData<>();
    private LiveData<UserDetails> userDetails;

    private MutableLiveData<List<MoveRequestDto>> _activeMoveRequests = new MutableLiveData<>();
    private LiveData<List<MoveRequestDto>> activeMoveRequests;

    private MutableLiveData<List<MoveRequestDto>> _allMoveRequests = new MutableLiveData<>();
    private LiveData<List<MoveRequestDto>> allMoveRequests;

    private MutableLiveData<List<MoveRequestDto>> _completedMoveRequests = new MutableLiveData<>();
    private LiveData<List<MoveRequestDto>> completedMoveRequests;

    private MutableLiveData<MoveRequestDto> _selectedMoveRequest = new MutableLiveData<>();
    private LiveData<MoveRequestDto> selectedMoveRequest;


    private MutableLiveData<PriceQuote> _acceptedPriceQuote = new MutableLiveData<>();
    private LiveData<PriceQuote> acceptedPriceQuote;
    public LiveData<PriceQuote> getAcceptedPriceQuote() {
        return _acceptedPriceQuote;
    }




    private MutableLiveData<String> _email = new MutableLiveData<>();


    private MutableLiveData<String> _password = new MutableLiveData<>();


    public LiveData<UserDetails> getUserDetails() {
        return _userDetails;
    }

    public LiveData<List<MoveRequestDto>> getActiveMoveRequests() {
        return _activeMoveRequests;
    }

    public LiveData<List<MoveRequestDto>> getCompletedMoveRequests() {
        return _completedMoveRequests;
    }

    public LiveData<MoveRequestDto> getSelectedMoveRequest() {
        return _selectedMoveRequest;
    }

    public void setSelectedMoveRequest(MoveRequestDto selectedMoveRequestDto) {
        _selectedMoveRequest.setValue(selectedMoveRequestDto);
        for(PriceQuote priceQuote:selectedMoveRequestDto.getPriceQuotes()){
            if(priceQuote.getQuoteStatus().equals(QuoteStatus.ACCEPTED)){
                Log.i(TAG,priceQuote.getMovers().toString());
                _acceptedPriceQuote.setValue(priceQuote);
            }
        }
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
        CustomerRepository customerRepository = new CustomerRepository(email,password);
        Call<List<MoveRequestDto>> moveRequestList = customerRepository.getAllCustomerMoveRequest(email);

        moveRequestList.enqueue(new Callback<List<MoveRequestDto>>() {
            @Override
            public void onResponse(Call<List<MoveRequestDto>> call, Response<List<MoveRequestDto>> response) {
                if(response.isSuccessful()){
                    Log.i(TAG, "Customer MoveRequests::"+response.body());

                    List<MoveRequestDto> myActiveMoveRequests = new ArrayList<>();
                    List<MoveRequestDto> myCompletedMoveRequests = new ArrayList<>();
                    for(MoveRequestDto moveRequestDto : response.body()){
                        Log.i(TAG, ""+moveRequestDto.getMoveRequest().getMoveRequestStatus());
                        if(moveRequestDto.getMoveRequest().getMoveRequestStatus().equals(MoveStatus.FINISHED)){
                            myCompletedMoveRequests.add(moveRequestDto);
                        }else{
                            myActiveMoveRequests.add(moveRequestDto);
                        }
                    }
                    Log.i(TAG,"myActiveMoveRequests:"+myActiveMoveRequests);
                    Log.i(TAG,"myCompletedMoveRequests:"+myCompletedMoveRequests);
                    _allMoveRequests.setValue(myActiveMoveRequests);
                    _activeMoveRequests.setValue(myActiveMoveRequests);
                    _completedMoveRequests.setValue(myCompletedMoveRequests);

                }else{
                    Log.i(TAG, "Customer MoveRequests::"+response.raw());
                }
            }

            @Override
            public void onFailure(Call<List<MoveRequestDto>> call, Throwable t) {
                Log.i(TAG, "Customer MoveRequests:: Failed");
            }
        });

    }



    @Override
    protected void onCleared() {
        super.onCleared();
        Log.d("CustomerViewModel","CustomerViewModel destroyed!");

    }

    public HashMap<String,List<String>> getInventoryDataOfSelectedMoveRequest(){
        HashMap<String, List<String>> inventoryListDetail = new HashMap<String, List<String>>();
        if(_selectedMoveRequest != null && _selectedMoveRequest.getValue().getMoveRequest().getItemInventory() != null){
            for (InventoryItemGroup inventoryItem: _selectedMoveRequest.getValue().getMoveRequest().getItemInventory()) {
                if(inventoryListDetail.containsKey(inventoryItem.getCategory())){
                    inventoryListDetail.get(inventoryItem.getCategory()).addAll(inventoryItem.getItems());
                }
                else{
                    inventoryListDetail.put(inventoryItem.getCategory(),inventoryItem.getItems());
                }
            }
        }
        return inventoryListDetail;
    }

    public void filterMoveRequestByStatus(String filterStatus) {
        if("Any".equals(filterStatus)){
            _activeMoveRequests.setValue(_allMoveRequests.getValue());
        }else {
            List<MoveRequestDto> filteredMoveRequests = new ArrayList<>();
            for (MoveRequestDto mr : _allMoveRequests.getValue()) {
                if (mr.getMoveRequest().getMoveRequestStatus().toString().equals(filterStatus)) {
                    filteredMoveRequests.add(mr);
                }
            }
            _activeMoveRequests.setValue(filteredMoveRequests);
        }
    }
}
