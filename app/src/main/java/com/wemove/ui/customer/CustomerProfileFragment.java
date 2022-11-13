package com.wemove.ui.customer;

import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wemove.R;
import com.wemove.databinding.FragmentCustomerProfileBinding;


public class CustomerProfileFragment extends Fragment {
    private FragmentCustomerProfileBinding binding;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_customer_profile, container, false);
        Log.d("MyMoveRequestsFragment", "MyMoveRequestsFragment created/re-created!");

        return binding.getRoot();
    }
}