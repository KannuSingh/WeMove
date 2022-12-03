package com.wemove.ui.mover;

import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wemove.R;
import com.wemove.databinding.FragmentFindDeliveryBinding;
import com.wemove.databinding.FragmentMoverDashboardBinding;


public class MoverDashboardFragment extends Fragment {
    private static final String TAG = "MoverDashboardFragment";
    private FragmentMoverDashboardBinding binding;






    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_mover_dashboard, container, false);
        Log.d(TAG, "MoverDashboardFragment created/re-created!");

        binding.cardFindMoveRequest.setOnClickListener(view -> Navigation.findNavController(getView()).navigate(R.id.action_moverDashboardFragment_to_findDeliveryFragment));
        binding.cardMyMoveRequest.setOnClickListener(view -> Navigation.findNavController(getView()).navigate(R.id.action_moverDashboardFragment_to_favoriteDeliveryFragment));
        binding.cardHistoryMoveRequest.setOnClickListener(view -> Navigation.findNavController(getView()).navigate(R.id.action_moverDashboardFragment_to_pastDeliveryFragment));
        return binding.getRoot();
    }
}