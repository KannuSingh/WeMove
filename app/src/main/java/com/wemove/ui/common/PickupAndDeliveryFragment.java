package com.wemove.ui.common;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.wemove.R;
import com.wemove.databinding.FragmentPickupAndDeliveryBinding;
import com.wemove.viewmodel.MoveRequestFormViewModel;


public class PickupAndDeliveryFragment extends Fragment {

    private FragmentPickupAndDeliveryBinding binding;
    private MoveRequestFormViewModel moveRequestFormViewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_pickup_and_delivery, container, false);
        Log.d("Pickup&DeliveryFragment", "PickupAndDeliveryFragment created/re-created!");

        //Setting move type array to spinner dropdown
        ArrayAdapter<CharSequence> floorLevelAdapter = ArrayAdapter.createFromResource(getContext(),
                R.array.floor_level, android.R.layout.simple_spinner_item);
        floorLevelAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        binding.pickupFloorLevel.setAdapter(floorLevelAdapter);
        binding.deliveryFloorLevel.setAdapter(floorLevelAdapter);

        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        moveRequestFormViewModel = new ViewModelProvider(requireActivity()).get(MoveRequestFormViewModel.class);


        //On pickupAndDeliveryNext Button Clicked.
        binding.pickupAndDeliveryNext.setOnClickListener(v ->
        {
            moveRequestFormViewModel.setMoveRequestDeliveryAddress(
                    binding.etDeliveryAddress1.getText().toString(),
                    binding.etDeliveryCity.getText().toString(),
                    binding.etDeliveryPostalcode.getText().toString()
            );

            moveRequestFormViewModel.setMoveRequestPickupAddress(
                    binding.etPickupAddress1.getText().toString(),
                    binding.etPickupCity.getText().toString(),
                    binding.etPickupPostalcode.getText().toString()
            );
            moveRequestFormViewModel.setPickupFloorLevel(binding.pickupFloorLevel.getSelectedItem().toString());
            moveRequestFormViewModel.setDeliveryFloorLevel(binding.deliveryFloorLevel.getSelectedItem().toString());

            Navigation.findNavController(getView()).navigate(R.id.action_pickupAndDeliveryFragment_to_homeInventoryFragment);
        });
    }
}