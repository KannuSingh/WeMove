package com.wemove.ui.customer;

import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wemove.R;
import com.wemove.databinding.FragmentMoveRequestHistoryBinding;


public class MoveRequestHistoryFragment extends Fragment {
    private FragmentMoveRequestHistoryBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_move_request_history, container, false);
        Log.d("MyMoveRequestsFragment", "MyMoveRequestsFragment created/re-created!");

        return binding.getRoot();
    }
}