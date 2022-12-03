package com.wemove.ui.customer;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wemove.MainActivity;
import com.wemove.R;
import com.wemove.databinding.FragmentCustomerProfileBinding;
import com.wemove.viewmodel.CustomerViewModel;


public class CustomerProfileFragment extends Fragment {
    private FragmentCustomerProfileBinding binding;
    private CustomerViewModel customerViewModel;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        customerViewModel = new ViewModelProvider(requireActivity()).get(CustomerViewModel.class);

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_customer_profile, container, false);
        Log.d("CustomerProfileFragment", "CustomerProfileFragment created/re-created!");
        binding.etEmail.setText(customerViewModel.getUserDetails().getValue().getEmail());
        binding.etMobile.setText(customerViewModel.getUserDetails().getValue().getMobile());
        binding.etCity.setText(customerViewModel.getUserDetails().getValue().getAddress().getCity());
        binding.etName.setText(customerViewModel.getUserDetails().getValue().getFirstname());

        binding.logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                removedLoggedInUserDetails();
                Intent intent = new Intent(getActivity(), MainActivity.class);
                startActivity(intent);
                getActivity().finish();
            }
        });
        return binding.getRoot();
    }

    private void removedLoggedInUserDetails() {
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("wemove", Context.MODE_PRIVATE);
        SharedPreferences.Editor sharedEditor = sharedPreferences.edit();
        sharedEditor.clear();
        sharedEditor.commit();
        sharedEditor.apply();
    }
}