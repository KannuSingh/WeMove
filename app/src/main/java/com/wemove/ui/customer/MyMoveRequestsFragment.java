package com.wemove.ui.customer;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import com.wemove.R;
import com.wemove.adapters.InventoryItemAdapter;
import com.wemove.adapters.MoveRequestAdapter;
import com.wemove.databinding.FragmentMyMoveRequestsBinding;
import com.wemove.model.InventoryItemGroup;
import com.wemove.model.MoveRequest;
import com.wemove.model.MoveRequestDto;
import com.wemove.viewmodel.CustomerViewModel;
import com.wemove.viewmodel.MoveRequestFormViewModel;

import java.util.List;


public class MyMoveRequestsFragment extends Fragment {
    private static final String TAG = "MyMoveRequestsFragment";
    private FragmentMyMoveRequestsBinding binding;
    private CustomerViewModel customerViewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        customerViewModel = new ViewModelProvider(requireActivity()).get(CustomerViewModel.class);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_my_move_requests, container, false);
        Log.d("MyMoveRequestsFragment", "MyMoveRequestsFragment created/re-created!");

        //Setting move status array to spinner dropdown
        ArrayAdapter<CharSequence> moveRequestStatusAdapter = ArrayAdapter.createFromResource(getContext(),
                R.array.move_request_status, android.R.layout.simple_spinner_item);
        moveRequestStatusAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.moveRequestStatus.setAdapter(moveRequestStatusAdapter);


        SharedPreferences sharedPreferences = getContext().getSharedPreferences("wemove", Context.MODE_PRIVATE);
        String email = sharedPreferences.getString("email", null);
        String password = sharedPreferences.getString("password", null);

        customerViewModel.getAllMoveRequest(email, password);
        final Observer<List<MoveRequestDto>> moveRequestsObserver = moveRequestList -> {

            if (moveRequestList == null || moveRequestList.size() == 0) {

                binding.myMoveRequestsRecyclerView.setVisibility(View.GONE);
                binding.noMoveRequests.setVisibility(View.VISIBLE);
            } else {
                Log.i(TAG, moveRequestList.toString());
                bindAdapter(moveRequestList);
                binding.myMoveRequestsRecyclerView.setVisibility(View.VISIBLE);
                binding.noMoveRequests.setVisibility(View.GONE);
            }
        };

        customerViewModel.getActiveMoveRequests().observe(getViewLifecycleOwner(), moveRequestsObserver);

        // List<MoveRequest> moveRequests = customerViewModel.getActiveMoveRequests().getValue();


        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        binding.newMoveRequestFab.setOnClickListener(v -> createNewMoveRequest());
        //On newMoveRequest Button Clicked.
       // binding.newMoveRequest.setOnClickListener(v -> createNewMoveRequest());

        binding.moveRequestStatus.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                customerViewModel.filterMoveRequestByStatus(adapterView.getItemAtPosition(i).toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


    }





    private void createNewMoveRequest() {
        Navigation.findNavController(getView()).navigate(R.id.action_myMoveRequestsFragment_to_moveRequestFormFragment);
    }


    private void bindAdapter(List<MoveRequestDto> moveRequestList) {
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this.getContext());
        binding.myMoveRequestsRecyclerView.setLayoutManager(layoutManager);
        MoveRequestAdapter listAdapter = new MoveRequestAdapter(moveRequestList, (moveRequest) -> {
            Log.i(TAG,"Item Clicked");
            onMoveRequestClicked(moveRequest);
        });
        binding.myMoveRequestsRecyclerView.setAdapter(listAdapter);
        Log.i(TAG, "bindAdapter Move Requests");
        listAdapter.notifyDataSetChanged();
    }

    private void onMoveRequestClicked(MoveRequestDto moveRequest){
        customerViewModel.setSelectedMoveRequest(moveRequest);

        Navigation.findNavController(getView()).navigate(R.id.action_myMoveRequestsFragment_to_MRDetailCustomerViewFragment);
    }

}