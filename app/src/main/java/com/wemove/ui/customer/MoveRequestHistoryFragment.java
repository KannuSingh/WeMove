package com.wemove.ui.customer;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.Nullable;
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
import com.wemove.adapters.MoveRequestAdapter;
import com.wemove.databinding.FragmentMoveRequestHistoryBinding;
import com.wemove.databinding.FragmentMyMoveRequestsBinding;
import com.wemove.model.MoveRequestDto;
import com.wemove.viewmodel.CustomerViewModel;

import java.util.List;


public class MoveRequestHistoryFragment extends Fragment {
    private FragmentMoveRequestHistoryBinding binding;
    private static final String TAG = "MoveRequestHistory";
    private CustomerViewModel customerViewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        customerViewModel = new ViewModelProvider(requireActivity()).get(CustomerViewModel.class);

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_move_request_history, container, false);
        Log.d("MoveRequestsHistory", "MoveRequestHistoryFragment created/re-created!");

        SharedPreferences sharedPreferences = getContext().getSharedPreferences("wemove", Context.MODE_PRIVATE);
        String email = sharedPreferences.getString("email", null);
        String password = sharedPreferences.getString("password", null);

        customerViewModel.getAllMoveRequest(email, password);
        final Observer<List<MoveRequestDto>> completedMoveRequestsObserver = moveRequestList -> {

            if (moveRequestList == null || moveRequestList.size() == 0) {

                binding.myCompletedMrRecyclerView.setVisibility(View.GONE);
                binding.noMoveRequestsMsg.setVisibility(View.VISIBLE);
            } else {
                bindAdapter(moveRequestList);
                binding.myCompletedMrRecyclerView.setVisibility(View.VISIBLE);
                binding.noMoveRequestsMsg.setVisibility(View.GONE);
            }
        };

        customerViewModel.getCompletedMoveRequests().observe(getViewLifecycleOwner(), completedMoveRequestsObserver);


        return binding.getRoot();
    }

    private void bindAdapter(List<MoveRequestDto> moveRequestList) {
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this.getContext());
        binding.myCompletedMrRecyclerView.setLayoutManager(layoutManager);
        MoveRequestAdapter listAdapter = new MoveRequestAdapter(moveRequestList, (moveRequest) -> {
            Log.i(TAG,"Item Clicked");
            onMoveRequestClicked(moveRequest);
        });
        binding.myCompletedMrRecyclerView.setAdapter(listAdapter);
        Log.i(TAG, "bindAdapter Move Requests");
        listAdapter.notifyDataSetChanged();
    }

    private void onMoveRequestClicked(MoveRequestDto moveRequest){
        customerViewModel.setSelectedMoveRequest(moveRequest);
        Navigation.findNavController(getView()).navigate(R.id.action_moveRequestHistoryFragment_to_MRDetailCustomerViewFragment);
    }
}