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
import com.wemove.databinding.FragmentPastDeliveryBinding;
import com.wemove.model.MoveRequestDto;
import com.wemove.viewmodel.MoverViewModel;

import java.util.List;


public class PastDeliveryFragment extends Fragment {
    private static final String TAG = "PastDeliveryFragment";
    private FragmentPastDeliveryBinding binding;
    private MoverViewModel moverViewModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        moverViewModel = new ViewModelProvider(requireActivity()).get(MoverViewModel.class);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_past_delivery, container, false);
        SharedPreferences sharedPreferences = requireContext().getSharedPreferences("wemove", Context.MODE_PRIVATE);
        String email = sharedPreferences.getString("email", null);
        String password = sharedPreferences.getString("password", null);
        moverViewModel.getMyMoveRequestDelivery(email, password);

        final Observer<List<MoveRequestDto>> myPastMoveRequestsDeliveriesObserver = moveRequestList -> {
            Log.i(TAG, moveRequestList.toString());
            if (moveRequestList.size() == 0) {
                binding.myPastDeliveriesRecyclerView.setVisibility(View.GONE);
                binding.noDeliveriesMsg.setVisibility(View.VISIBLE);
            } else {
                bindAdapter(moveRequestList);
                binding.myPastDeliveriesRecyclerView.setVisibility(View.VISIBLE);
                binding.noDeliveriesMsg.setVisibility(View.GONE);
            }
        };

        moverViewModel.getMyPastMoveRequestsDelivery().observe(getViewLifecycleOwner(), myPastMoveRequestsDeliveriesObserver);


        return binding.getRoot();
    }

    private void bindAdapter(List<MoveRequestDto> moveRequestList) {
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this.getContext());
        binding.myPastDeliveriesRecyclerView.setLayoutManager(layoutManager);
        DeliveriesAdapter listAdapter = new DeliveriesAdapter(moveRequestList, this::onMoveRequestClicked);
        binding.myPastDeliveriesRecyclerView.setAdapter(listAdapter);
        Log.i(TAG, "bindAdapter Move Requests");
        listAdapter.notifyDataSetChanged();
    }

    private void onMoveRequestClicked(MoveRequestDto moveRequestDto){
        moverViewModel.setSelectedMoveRequest(moveRequestDto);
        Log.i(TAG, "Move request clicked/selected");
        Navigation.findNavController(getView()).navigate(R.id.action_pastDeliveryFragment_to_MRDetailMoverViewFragment);
    }
}