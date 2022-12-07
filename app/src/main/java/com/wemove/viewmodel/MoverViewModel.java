package com.wemove.viewmodel;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.gson.Gson;
import com.wemove.model.InventoryItemGroup;
import com.wemove.model.MRStatusItem;
import com.wemove.model.MoveRequest;
import com.wemove.model.MoveRequestDto;
import com.wemove.model.MoveStatus;
import com.wemove.model.PriceQuote;
import com.wemove.model.QuoteStatus;
import com.wemove.model.UserDetails;
import com.wemove.network.RetrofitClientInstance;
import com.wemove.repository.MoverRepository;
import com.wemove.service.ICustomerService;
import com.wemove.service.IMoverService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MoverViewModel extends ViewModel {

    private static final String TAG= "MoverViewModel";

    private MutableLiveData<UserDetails> _userDetails = new MutableLiveData<>();
    private LiveData<UserDetails> userDetails;

    private MutableLiveData<List<MoveRequestDto>> _activeMoveRequestsDelivery = new MutableLiveData<>();
    private LiveData<List<MoveRequestDto>> activeMoveRequestsDelivery;

    private MutableLiveData<List<MoveRequestDto>> _allMoveRequestsDelivery = new MutableLiveData<>();
    private LiveData<List<MoveRequestDto>> allMoveRequestsDelivery;

    private MutableLiveData<List<MoveRequestDto>> _myMoveRequestsDelivery = new MutableLiveData<>();
    private LiveData<List<MoveRequestDto>> myMoveRequestsDelivery;


    private MutableLiveData<List<MoveRequestDto>> _myPastMoveRequestsDelivery = new MutableLiveData<>();
    private LiveData<List<MoveRequestDto>> myPastMoveRequestsDelivery;


    private MutableLiveData<List<MoveRequestDto>> _completedMoveRequestsDelivery = new MutableLiveData<>();
    private LiveData<List<MoveRequestDto>> completedMoveRequestsDelivery;

    private MutableLiveData<String> _email = new MutableLiveData<>();


    private MutableLiveData<String> _password = new MutableLiveData<>();

    private MutableLiveData<MoveRequestDto> _selectedMoveRequest = new MutableLiveData<>();

    private LiveData<MoveRequestDto> selectedMoveRequest;

    public LiveData<MoveRequestDto> getSelectedMoveRequest() {
        return _selectedMoveRequest;
    }


    public LiveData<UserDetails> getUserDetails() {
        return _userDetails;
    }

    public LiveData<List<MoveRequestDto>> getActiveMoveRequestsDelivery() {
        return _activeMoveRequestsDelivery;
    }
    public LiveData<List<MoveRequestDto>> getMyMoveRequestsDelivery() {
        return _myMoveRequestsDelivery;
    }
    public LiveData<List<MoveRequestDto>> getMyPastMoveRequestsDelivery() {
        return _myPastMoveRequestsDelivery;
    }


    public LiveData<List<MoveRequestDto>> getAllMoveRequestsDelivery() {
        return _allMoveRequestsDelivery;
    }

    public LiveData<List<MoveRequestDto>> getCompletedMoveRequestsDelivery() {
        return _completedMoveRequestsDelivery;
    }

    private MutableLiveData<Boolean> _priceQuoteSubmitResponseFlag = new MutableLiveData<>();
    private LiveData<Boolean> priceQuoteSubmitResponseFlag ;

    private MutableLiveData<PriceQuote> _userPriceQuote = new MutableLiveData<>();
    private LiveData<PriceQuote> userPriceQuote ;

    private MutableLiveData<List<MRStatusItem>> _mrStatusList = new MutableLiveData<>();
    private LiveData<List<MRStatusItem>> mrStatusList ;

    public LiveData<List<MRStatusItem>> getMRStatusList() {
        this.mrStatusList = _mrStatusList;
        return mrStatusList;
    }

    public LiveData<PriceQuote> getUserPriceQuote() {
        this.userPriceQuote = _userPriceQuote;
        return userPriceQuote;
    }


    public LiveData<Boolean> getPriceQuoteSubmitResponseFlag() {
        this.priceQuoteSubmitResponseFlag = _priceQuoteSubmitResponseFlag;
        return priceQuoteSubmitResponseFlag;
    }



    public MoverViewModel(){
        Log.d(TAG,"MoverViewModel Created : "+this.toString());
        _activeMoveRequestsDelivery.setValue(new ArrayList<>());
        _completedMoveRequestsDelivery.setValue(new ArrayList<>());

    }

    public void updateMoveStatus(MoveStatus moveStatus){
        MoverRepository moverRepository = new MoverRepository(_email.getValue(), _password.getValue());
        Call<Void> request = moverRepository.updateMoveRequestStatus(Integer.parseInt(_selectedMoveRequest.getValue().getMoveRequest().getMoveRequestId()),moveStatus);
        request.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if(response.isSuccessful()){
                    Log.i(TAG,"Successfully change the status to :"+moveStatus.name());
                }
                else{
                    Log.i(TAG,"Failed to change the status to :"+moveStatus.name());

                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.i(TAG,"Failed to change the status to :"+moveStatus.name()+". Some error occurred ");

            }
        });

    }
    public void setUser( String email, String password,String userDetails){
        _email.setValue(email);
        _password.setValue(password);
        Gson gson = new Gson();

        _userDetails.setValue(gson.fromJson(userDetails,UserDetails.class));
        Log.i(TAG,userDetails);
    }

    public void setSelectedMoveRequest(MoveRequestDto selectedMoveRequestDto) {
        Log.i(TAG, "Setting selectedMoveRequestDto");
        _selectedMoveRequest.setValue(selectedMoveRequestDto);
        _priceQuoteSubmitResponseFlag.setValue(null);
        _userPriceQuote.setValue(null);
        for (PriceQuote priceQuote:selectedMoveRequestDto.getPriceQuotes()) {
            if(priceQuote.getMovers().getEmail().equals(_userDetails.getValue().getEmail())){
                _userPriceQuote.setValue(priceQuote);
            }
        }
        MoverRepository moverRepository = new MoverRepository(_email.getValue(), _password.getValue());
        Call<List<MRStatusItem>> statusTimeline = moverRepository.getStatusTimeline(Integer.parseInt(_selectedMoveRequest.getValue().getMoveRequest().getMoveRequestId()));
        statusTimeline.enqueue(new Callback<List<MRStatusItem>>() {
            @Override
            public void onResponse(Call<List<MRStatusItem>> call, Response<List<MRStatusItem>> response) {
                if(response.isSuccessful()){
                    _mrStatusList.setValue(response.body());
                }
                else{
                    _mrStatusList.setValue(new ArrayList<>());
                }
            }

            @Override
            public void onFailure(Call<List<MRStatusItem>> call, Throwable t) {
                _mrStatusList.setValue(new ArrayList<>());
            }
        });

    }



    public void getAllMoveRequestDelivery(String email, String password){
        MoverRepository moverRepository = new MoverRepository(email,password);
        Call<List<MoveRequestDto>> moveRequestList = moverRepository.getAllMoveRequestDeliveries();

        moveRequestList.enqueue(new Callback<List<MoveRequestDto>>() {
            @Override
            public void onResponse(Call<List<MoveRequestDto>> call, Response<List<MoveRequestDto>> response) {
                if(response.isSuccessful()){
                    Log.i(TAG, ""+response.body());
                    _allMoveRequestsDelivery.setValue(response.body());
                    _activeMoveRequestsDelivery.setValue(response.body());
                }else{
                    Log.i(TAG, ""+response.raw());
                }
            }

            @Override
            public void onFailure(Call<List<MoveRequestDto>> call, Throwable t) {
                Log.i(TAG, "getAllMoveRequestDelivery:: Failed");
                Log.e(TAG,t.getMessage());
            }
        });

    }
    public void getMyMoveRequestDelivery(String email, String password){
        MoverRepository moverRepository = new MoverRepository(email,password);
        Call<List<MoveRequestDto>> moveRequestList = moverRepository.getMyMoveRequestDeliveries(email);

        moveRequestList.enqueue(new Callback<List<MoveRequestDto>>() {
            @Override
            public void onResponse(Call<List<MoveRequestDto>> call, Response<List<MoveRequestDto>> response) {
                if(response.isSuccessful()){
                    Log.i(TAG, ""+response.body());
                    List<MoveRequestDto> myMoveRequests = new ArrayList<>();
                    List<MoveRequestDto> myPastMoveRequests = new ArrayList<>();
                    for(MoveRequestDto moveRequestDto : response.body()){
                        Log.i(TAG, ""+moveRequestDto.getMoveRequest().getMoveRequestStatus());
                        if(moveRequestDto.getMoveRequest().getMoveRequestStatus().equals(MoveStatus.FINISHED)){
                            myPastMoveRequests.add(moveRequestDto);
                        }else{
                            myMoveRequests.add(moveRequestDto);
                        }
                    }
                    Log.i(TAG,"myMoveRequests:"+myMoveRequests);
                    Log.i(TAG,"myPastMoveRequests:"+myPastMoveRequests);
                    _myMoveRequestsDelivery.setValue(myMoveRequests);
                    _myPastMoveRequestsDelivery.setValue(myPastMoveRequests);
                    //_activeMoveRequestsDelivery.setValue(response.body());
                }else{
                    Log.i(TAG, ""+response.raw());
                }
            }

            @Override
            public void onFailure(Call<List<MoveRequestDto>> call, Throwable t) {
                Log.i(TAG, "getAllMoveRequestDelivery:: Failed");
            }
        });

    }

    @Override
    protected void onCleared() {
        super.onCleared();
        Log.d(TAG,"ViewModel destroyed!");
    }

    public void submitPriceQuote(double price){
        PriceQuote priceQuote = new PriceQuote();
        priceQuote.setPrice(String.valueOf(price));
        priceQuote.setMovers(_userDetails.getValue());
        priceQuote.setMoveRequestId(Integer.parseInt(_selectedMoveRequest.getValue().getMoveRequest().getMoveRequestId()));
        priceQuote.setQuoteStatus(QuoteStatus.PROPOSED);

        MoverRepository moverRepository = new MoverRepository(_email.getValue(), _password.getValue());
        Call<Boolean> requestFlag = moverRepository.submitPriceQuote(priceQuote);
        Gson gson = new Gson();

        Log.i(TAG,gson.toJson(priceQuote,PriceQuote.class));
        final boolean[] responseFlag = {false};
        requestFlag.enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                if(response.isSuccessful()){
                    Log.i(TAG, ""+response.body());
                    _priceQuoteSubmitResponseFlag.setValue(Boolean.TRUE.equals(response.body()));
                    _userPriceQuote.setValue(priceQuote);

                }else{
                    Log.i(TAG, ""+response.raw());
                    _priceQuoteSubmitResponseFlag.setValue(Boolean.FALSE.equals(response.body()));
                }
            }

            @Override
            public void onFailure(Call<Boolean> call, Throwable t) {
                Log.i(TAG, "submitPriceQuote:: Failed");
                _priceQuoteSubmitResponseFlag.setValue(Boolean.FALSE);
            }
        });

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
        Log.i(TAG,"getInventoryDataOfSelectedMoveRequest : inventory size :"+ inventoryListDetail.entrySet().size());
        return inventoryListDetail;
    }

    public void filterMoveRequestByStatus(String filterStatus) {
        if("Any".equals(filterStatus)){
            _activeMoveRequestsDelivery.setValue(_allMoveRequestsDelivery.getValue());
        }else {
            List<MoveRequestDto> filteredMoveRequests = new ArrayList<>();
            for (MoveRequestDto mr : _allMoveRequestsDelivery.getValue()) {
                if (mr.getMoveRequest().getMoveRequestStatus().toString().equals(filterStatus)) {
                    filteredMoveRequests.add(mr);
                }
            }
            _activeMoveRequestsDelivery.setValue(filteredMoveRequests);
        }
    }

    public boolean verifyQRCodeData(String contents) {
        if(_selectedMoveRequest!= null && _selectedMoveRequest.getValue().getMoveRequest() !=null) {
            MoveRequest moveRequest = _selectedMoveRequest.getValue().getMoveRequest();
            String text = String.format("%s%s%s", moveRequest.getMoveRequestId(), moveRequest.getMoveRequestOwner(), moveRequest.getPickupAddress().getAddress1());
            Log.i(TAG,"Text : "+text);
            Log.i(TAG,"Scanned Contents : "+contents);
            return text.equals(contents);
        }
        return false;
    }
}
