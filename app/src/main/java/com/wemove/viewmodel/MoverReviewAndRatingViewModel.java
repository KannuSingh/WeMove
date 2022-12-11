package com.wemove.viewmodel;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.wemove.model.PriceQuote;
import com.wemove.model.Review;
import com.wemove.model.UserDetails;
import com.wemove.repository.CustomerRepository;
import com.wemove.repository.MoverRepository;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MoverReviewAndRatingViewModel extends ViewModel {
    private static final String TAG= "M_R_AND_R_ViewModel";

    private MutableLiveData<UserDetails> _viewingUserDetails = new MutableLiveData<>();
    private LiveData<UserDetails> viewingUserDetails;

    private MutableLiveData<UserDetails> _moverUserDetails = new MutableLiveData<>();
    private LiveData<UserDetails> moverUserDetails;

    private MutableLiveData<PriceQuote> _priceQuote = new MutableLiveData<>();
    private LiveData<PriceQuote> priceQuote;

    public LiveData<PriceQuote> getPriceQuote() {
        priceQuote = _priceQuote;
        return priceQuote;
    }



    public LiveData<UserDetails> getMoverUserDetails() {
        moverUserDetails = _moverUserDetails;
        return moverUserDetails;
    }

    private MutableLiveData<Boolean> _approveOrDeclineStatus = new MutableLiveData<>();
    private LiveData<Boolean> approveOrDeclineStatus ;

    private MutableLiveData<Boolean> _reviewPostingStatus = new MutableLiveData<>();
    private LiveData<Boolean> reviewPostingStatus ;

    public LiveData<Boolean> getReviewPostingStatus(){
        reviewPostingStatus = _reviewPostingStatus;
        return reviewPostingStatus;
    }


    public void setPriceQuote(PriceQuote priceQuote) {
        this._priceQuote.setValue(priceQuote);
        _approveOrDeclineStatus = new MutableLiveData<>();
    }
    public LiveData<Boolean> getApproveOrDeclineStatus() {
        this.approveOrDeclineStatus = _approveOrDeclineStatus;
        return approveOrDeclineStatus;
    }

    public void setApproveOrDeclineStatus(Boolean approveOrDeclineStatus) {
        this._approveOrDeclineStatus.setValue( approveOrDeclineStatus);
        this.approveOrDeclineStatus = _approveOrDeclineStatus;
    }

    private MutableLiveData<List<Review>> _moverReviews = new MutableLiveData<>();

    private LiveData<List<Review>> moverReviews ;



    public LiveData<List<Review>> getMoverReviews() {
        moverReviews = _moverReviews;
        return moverReviews;
    }

    public void setViewingUserDetails(UserDetails userDetails) {
        _viewingUserDetails.setValue(userDetails);
    }

    public void setMover(UserDetails movers) {
            _moverUserDetails.setValue(movers);
            getReviewAndRating(movers.getEmail());
    }


    private void getReviewAndRating(String email) {
        MoverRepository moverRepository = new MoverRepository(_viewingUserDetails.getValue().getEmail(),_viewingUserDetails.getValue().getPassword());
        Call<List<Review>> reviews = moverRepository.getReviews(email);

        reviews.enqueue(new Callback<List<Review>>() {
            @Override
            public void onResponse(Call<List<Review>> call, Response<List<Review>> response) {
                if(response.isSuccessful()){
                    _moverReviews.setValue(response.body());
                }
                else{
                    _moverReviews.setValue(new ArrayList<>());
                }
            }

            @Override
            public void onFailure(Call<List<Review>> call, Throwable t) {
                _moverReviews.setValue(new ArrayList<>());
            }
        });

    }


    public float getAvgRating() {
        float ratingSum = 0;
        if(_moverReviews.getValue() != null) {
            for (Review review : _moverReviews.getValue()) {
                ratingSum += review.getRating();
            }
            Log.i(TAG,"Avg ="+ratingSum/_moverReviews.getValue().size());
            return ratingSum/_moverReviews.getValue().size();
        }
        Log.i(TAG,""+ratingSum);
        return ratingSum;
    }

    public void approvePriceQuote(int moveRequestId) {
        CustomerRepository customerRepository = new CustomerRepository(_viewingUserDetails.getValue().getEmail(),_viewingUserDetails.getValue().getPassword());
        Call<Boolean> approveRequestStatus = customerRepository.acceptPriceQuote(_moverUserDetails.getValue().getEmail(),moveRequestId);
        Log.i(TAG,"approvePriceQuote:"+_moverUserDetails.getValue().getEmail()+","+moveRequestId);

        approveRequestStatus.enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                if(response.isSuccessful()){
                    setApproveOrDeclineStatus(response.body());
                }
                setApproveOrDeclineStatus(false);
            }

            @Override
            public void onFailure(Call<Boolean> call, Throwable t) {
                setApproveOrDeclineStatus(false);
            }
        });

    }

    public void declinePriceQuote(int moveRequestId) {
        CustomerRepository customerRepository = new CustomerRepository(_viewingUserDetails.getValue().getEmail(),_viewingUserDetails.getValue().getPassword());
        Call<Boolean> approveRequestStatus = customerRepository.declinePriceQuote(_moverUserDetails.getValue().getEmail(),moveRequestId);
        Log.i(TAG,"declinePriceQuote:"+_moverUserDetails.getValue().getEmail()+","+moveRequestId);
        approveRequestStatus.enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                if(response.isSuccessful()){
                    setApproveOrDeclineStatus(response.body());
                }
                setApproveOrDeclineStatus(false);
            }

            @Override
            public void onFailure(Call<Boolean> call, Throwable t) {
                setApproveOrDeclineStatus(false);
            }
        });
    }

    public void addReview(String reviewString, float rating){
        Review postingReview = new Review();
        postingReview.setReview(reviewString);
        postingReview.setRating(rating);
        postingReview.setMoverEmail(_moverUserDetails.getValue().getEmail());
        postingReview.setCustomerEmail(_viewingUserDetails.getValue().getEmail());
        postingReview.setMoveRequestId(String.valueOf(_priceQuote.getValue().getMoveRequestId()));

        CustomerRepository customerRepository = new CustomerRepository(_viewingUserDetails.getValue().getEmail(),_viewingUserDetails.getValue().getPassword());
        Call<Review> reviewResult = customerRepository.addReview(postingReview);
        reviewResult.enqueue(new Callback<Review>() {
            @Override
            public void onResponse(Call<Review> call, Response<Review> response) {
                if(response.isSuccessful()){
                    if(response.body() != null){
                        _reviewPostingStatus.setValue(true);
                        getReviewAndRating(_moverUserDetails.getValue().getEmail());
                        getAvgRating();

                    }else{
                        _reviewPostingStatus.setValue(false);
                    }
                }else{
                    _reviewPostingStatus.setValue(false);
                }
            }

            @Override
            public void onFailure(Call<Review> call, Throwable t) {
                _reviewPostingStatus.setValue(false);
            }
        });
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        Log.d(TAG,"ReviewAndRatingViewModel destroyed!");

    }
}
