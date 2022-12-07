package com.wemove.ui.common;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.gson.Gson;
import com.wemove.R;
import com.wemove.adapters.QuotationListAdapter;
import com.wemove.adapters.ReviewListAdapter;
import com.wemove.databinding.FragmentMRDetailCustomerViewBinding;
import com.wemove.databinding.FragmentReviewAndRatingBinding;
import com.wemove.model.PriceQuote;
import com.wemove.model.QuoteStatus;
import com.wemove.model.Review;
import com.wemove.model.UserDetails;
import com.wemove.viewmodel.MoveRequestFormViewModel;
import com.wemove.viewmodel.MoverReviewAndRatingViewModel;

import java.util.List;
import java.util.Objects;


public class ReviewAndRatingFragment extends Fragment {
    private static final String TAG = "ReviewAndRatingFragment";
    private FragmentReviewAndRatingBinding binding;
    private MoverReviewAndRatingViewModel reviewAndRatingViewModel;

    private void setLoggedInUser() {
        SharedPreferences sharedPreferences = this.getActivity().getSharedPreferences("wemove", Context.MODE_PRIVATE);
        String userDetailsJson = sharedPreferences.getString("userDetails", null);
        Gson gson = new Gson();
        UserDetails userDetails = gson.fromJson(userDetailsJson, UserDetails.class);
        reviewAndRatingViewModel.setViewingUserDetails(userDetails);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        reviewAndRatingViewModel = new ViewModelProvider(requireActivity()).get(MoverReviewAndRatingViewModel.class);
        setLoggedInUser();
        if (getArguments().getString("action") != null && getArguments().getString("action").equals("CustomerPriceQuoteClicked")) {
            Gson gson = new Gson();
            PriceQuote priceQuote = gson.fromJson(getArguments().getString("price_quote"), PriceQuote.class);
            reviewAndRatingViewModel.setMover(priceQuote.getMovers());
            reviewAndRatingViewModel.setPriceQuote(priceQuote);
        }else if (getArguments().getString("action") != null && getArguments().getString("action").equals("CustomerWriteReviewClicked")) {
            Gson gson = new Gson();
            PriceQuote priceQuote = gson.fromJson(getArguments().getString("price_quote"), PriceQuote.class);
            reviewAndRatingViewModel.setMover(priceQuote.getMovers());
            reviewAndRatingViewModel.setPriceQuote(priceQuote);
        }


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_review_and_rating, container, false);
        UserDetails userDetails = reviewAndRatingViewModel.getMoverUserDetails().getValue();
        binding.tvMoverName.setText(userDetails.getFirstname());
        binding.tvMoverContact.setText(userDetails.getMobile());

        final Observer<List<Review>> moverReviews = reviews -> {
            if (reviews != null) {
                float avg_rating = reviewAndRatingViewModel.getAvgRating();
                binding.moverAvgRating.setRating(avg_rating);
                binding.ratingText.setText(String.format("%s/5.0 rating", avg_rating));
                bindAdapter(reviews);
            }
        };

        reviewAndRatingViewModel.getMoverReviews().observe(getViewLifecycleOwner(), moverReviews);

        if (getArguments().getString("action") != null
                && getArguments().getString("action").equals("CustomerPriceQuoteClicked")) {
            binding.writeReviewLayout.setVisibility(View.GONE);
            PriceQuote priceQuote = reviewAndRatingViewModel.getPriceQuote().getValue();
            if (priceQuote.getQuoteStatus().equals(QuoteStatus.PROPOSED)) {

                binding.approvePriceQuoteLayout.setVisibility(View.VISIBLE);
            } else {
                binding.approvePriceQuoteLayout.setVisibility(View.GONE);
            }
        }

        final Observer<Boolean> approveDeclineStatus = approveOrDecline -> {
            Log.i(TAG, "" + approveOrDecline);
            if (approveOrDecline) {
                Toast.makeText(getContext(), "Price Quote Status Updated", Toast.LENGTH_SHORT).show();
                NavHostFragment navHostFragment = (NavHostFragment) getActivity().getSupportFragmentManager().findFragmentById(R.id.customer_fragments_view_container);
                NavController navController = navHostFragment.getNavController();
                navController.navigateUp();
            } else {
                Toast.makeText(getContext(), "Some Error Occurred", Toast.LENGTH_SHORT).show();
                NavHostFragment navHostFragment = (NavHostFragment) getActivity().getSupportFragmentManager().findFragmentById(R.id.customer_fragments_view_container);
                NavController navController = navHostFragment.getNavController();
                navController.navigateUp();
            }


        };

        reviewAndRatingViewModel.getApproveOrDeclineStatus().observe(getViewLifecycleOwner(), approveDeclineStatus);

        binding.btnApprovePriceQuote.setOnClickListener(view -> {
            Gson gson = new Gson();
            PriceQuote priceQuote = gson.fromJson(getArguments().getString("price_quote"), PriceQuote.class);

            reviewAndRatingViewModel.approvePriceQuote(priceQuote.getMoveRequestId());
        });

        binding.btnDeclinePriceQuote.setOnClickListener(view -> {
            Gson gson = new Gson();
            PriceQuote priceQuote = gson.fromJson(getArguments().getString("price_quote"), PriceQuote.class);
            reviewAndRatingViewModel.declinePriceQuote(priceQuote.getMoveRequestId());
        });
        return binding.getRoot();
    }

    private void bindAdapter(List<Review> reviews) {
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this.getContext());
        binding.moverReviewsAndRatingRecyclerView.setLayoutManager(layoutManager);
        ReviewListAdapter listAdapter = new ReviewListAdapter(reviews, (review) -> {
            Log.i(TAG, "Review Clicked");
            //onPriceQuoteClicked(priceQuote);
        });
        binding.moverReviewsAndRatingRecyclerView.setAdapter(listAdapter);
        Log.i(TAG, "bindAdapter Reviews");
        listAdapter.notifyDataSetChanged();
    }
}