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
import com.wemove.adapters.MoveRequestAdapter;
import com.wemove.databinding.FragmentFindDeliveryBinding;
import com.wemove.databinding.FragmentMyMoveRequestsBinding;
import com.wemove.model.MoveRequest;
import com.wemove.model.MoveRequestDto;
import com.wemove.viewmodel.CustomerViewModel;
import com.wemove.viewmodel.MoverViewModel;

import java.util.List;
import java.util.Objects;


public class FindDeliveryFragment extends Fragment {

    private static final String TAG = "FindDeliveryFragment";
    private FragmentFindDeliveryBinding binding;
    private MoverViewModel moverViewModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        moverViewModel = new ViewModelProvider(requireActivity()).get(MoverViewModel.class);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_find_delivery, container, false);
        Log.d(TAG, "FindDeliveryFragment created/re-created!");

        SharedPreferences sharedPreferences = requireContext().getSharedPreferences("wemove", Context.MODE_PRIVATE);
        String email = sharedPreferences.getString("email", null);
        String password = sharedPreferences.getString("password", null);

        moverViewModel.getAllMoveRequestDelivery(email, password);

        final Observer<List<MoveRequestDto>> moveRequestsDeliveriesObserver = moveRequestList -> {
            Log.i(TAG, moveRequestList.toString());
            if (moveRequestList.size() == 0) {
                binding.myDeliveriesRecyclerView.setVisibility(View.GONE);
                binding.noDeliveries.setVisibility(View.VISIBLE);
            } else {
                bindAdapter(moveRequestList);
                binding.myDeliveriesRecyclerView.setVisibility(View.VISIBLE);
                binding.noDeliveries.setVisibility(View.GONE);
            }
        };

        moverViewModel.getActiveMoveRequestsDelivery().observe(getViewLifecycleOwner(), moveRequestsDeliveriesObserver);

        return binding.getRoot();
    }

    private void bindAdapter(List<MoveRequestDto> moveRequestList) {
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this.getContext());
        binding.myDeliveriesRecyclerView.setLayoutManager(layoutManager);
        DeliveriesAdapter listAdapter = new DeliveriesAdapter(moveRequestList, this::onMoveRequestClicked);
        binding.myDeliveriesRecyclerView.setAdapter(listAdapter);
        Log.i(TAG, "bindAdapter Deliveries Move Requests");
        listAdapter.notifyDataSetChanged();
    }
    private void onMoveRequestClicked(MoveRequestDto moveRequestDto){
        moverViewModel.setSelectedMoveRequest(moveRequestDto);
        Log.i(TAG, "Move request clicked/selected");
        Navigation.findNavController(getView()).navigate(R.id.action_findDeliveryFragment_to_MRDetailMoverViewFragment);
    }
}