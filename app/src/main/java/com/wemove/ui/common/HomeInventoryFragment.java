package com.wemove.ui.common;

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
import android.widget.Toast;

import com.wemove.R;
import com.wemove.adapters.InventoryItemAdapter;
import com.wemove.databinding.FragmentHomeInventoryBinding;
import com.wemove.model.InventoryItemGroup;
import com.wemove.model.UserDetails;
import com.wemove.viewmodel.MoveRequestFormViewModel;

import java.util.List;


public class HomeInventoryFragment extends Fragment {
    private static final String TAG= "HomeInventoryFragment";
    private FragmentHomeInventoryBinding binding;
    private MoveRequestFormViewModel moveRequestFormViewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        moveRequestFormViewModel = new ViewModelProvider(requireActivity()).get(MoveRequestFormViewModel.class);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home_inventory, container, false);
        Log.d("HomeInventoryFragment", "HomeInventoryFragment created/re-created!");



        List<InventoryItemGroup> inventory = moveRequestFormViewModel.getInventory().getValue();
        if(inventory == null || inventory.size() == 0 ){
            binding.createRequest.setVisibility(View.INVISIBLE);
        }
        else{
            bindAdapter(inventory);
            binding.createRequest.setVisibility(View.VISIBLE);
        }

        final Observer<Boolean> createMoveRequestFlagObserver = new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean _createMoveRequestFlag) {
                if (Boolean.TRUE.equals(_createMoveRequestFlag)) {
                    Toast.makeText(getContext(),"Move Request Creation Successfully",Toast.LENGTH_SHORT).show();
                    //Log.i(TAG,getView().toString());
                    Navigation.findNavController(binding.getRoot()).navigate(R.id.action_homeInventoryFragment_to_myMoveRequestsFragment);

                }else if(Boolean.FALSE.equals(_createMoveRequestFlag)) {
                    Toast.makeText(getContext(),"Move Request Creation Failed",Toast.LENGTH_SHORT).show();
                }
                Log.i(TAG,"Navigating to Dashboard");
            }
        };


        moveRequestFormViewModel.isCreateMoveRequestSuccessful().observe(getViewLifecycleOwner(),createMoveRequestFlagObserver);




        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {


        binding.createRequest.setOnClickListener(view1 -> createMoveRequest());
        //On pickupAndDeliveryNext Button Clicked.
        binding.addNewItem.setOnClickListener(v -> Navigation.findNavController(getView()).navigate(R.id.action_homeInventoryFragment_to_inventoryItemDetailFragment));
    }

    private void bindAdapter(List<InventoryItemGroup> inventoryItemGroups) {
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this.getContext());
        binding.homeInventoryRecyclerView.setLayoutManager(layoutManager);
        InventoryItemAdapter listAdapter = new InventoryItemAdapter(inventoryItemGroups, (inventoryItemGroup) -> {

        });
        binding.homeInventoryRecyclerView.setAdapter(listAdapter);
        Log.i(TAG,"bindAdapter HomeInventoryFragment");
        listAdapter.notifyDataSetChanged();
    }

    private void createMoveRequest(){
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("wemove", Context.MODE_PRIVATE);
        String email = sharedPreferences.getString("email", null);
        String password = sharedPreferences.getString("password", null);

        moveRequestFormViewModel.createMoveRequest(email, password);
    }
}