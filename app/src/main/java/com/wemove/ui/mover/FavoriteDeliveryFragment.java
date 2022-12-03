package com.wemove.ui.mover;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wemove.R;
import com.wemove.adapters.DeliveriesAdapter;
import com.wemove.databinding.FragmentFavoriteDeliveryBinding;
import com.wemove.databinding.FragmentFindDeliveryBinding;
import com.wemove.model.MoveRequestDto;
import com.wemove.viewmodel.MoverViewModel;

import java.util.List;


public class FavoriteDeliveryFragment extends Fragment {
    private static final String TAG = "FavoDeliveryFragment";
    private FragmentFavoriteDeliveryBinding binding;
    private MoverViewModel moverViewModel;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        moverViewModel = new ViewModelProvider(requireActivity()).get(MoverViewModel.class);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_favorite_delivery, container, false);
        SharedPreferences sharedPreferences = requireContext().getSharedPreferences("wemove", Context.MODE_PRIVATE);
        String email = sharedPreferences.getString("email", null);
        String password = sharedPreferences.getString("password", null);
        moverViewModel.getMyMoveRequestDelivery(email, password);

        final Observer<List<MoveRequestDto>> myMoveRequestsDeliveriesObserver = moveRequestList -> {
            Log.i(TAG, moveRequestList.toString());
            if (moveRequestList.size() == 0) {
                binding.myFavDeliveriesRecyclerView.setVisibility(View.GONE);
                binding.noDeliveriesMsg.setVisibility(View.VISIBLE);
            } else {
                bindAdapter(moveRequestList);
                binding.myFavDeliveriesRecyclerView.setVisibility(View.VISIBLE);
                binding.noDeliveriesMsg.setVisibility(View.GONE);
            }
        };

        moverViewModel.getMyMoveRequestsDelivery().observe(getViewLifecycleOwner(), myMoveRequestsDeliveriesObserver);


        return binding.getRoot();
    }

    private void bindAdapter(List<MoveRequestDto> moveRequestList) {
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this.getContext());
        binding.myFavDeliveriesRecyclerView.setLayoutManager(layoutManager);
        DeliveriesAdapter listAdapter = new DeliveriesAdapter(moveRequestList, this::onMoveRequestClicked);
        binding.myFavDeliveriesRecyclerView.setAdapter(listAdapter);
        Log.i(TAG, "bindAdapter Move Requests");
        listAdapter.notifyDataSetChanged();
    }

    private void onMoveRequestClicked(MoveRequestDto moveRequestDto){
        moverViewModel.setSelectedMoveRequest(moveRequestDto);
        Log.i(TAG, "Move request clicked/selected");
        Navigation.findNavController(getView()).navigate(R.id.action_favoriteDeliveryFragment_to_MRDetailMoverViewFragment);
    }
}