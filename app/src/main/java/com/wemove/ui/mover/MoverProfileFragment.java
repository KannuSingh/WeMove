package com.wemove.ui.mover;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wemove.MainActivity;
import com.wemove.R;
import com.wemove.databinding.FragmentMoverProfileBinding;
import com.wemove.viewmodel.MoverViewModel;

public class MoverProfileFragment extends Fragment {

    private FragmentMoverProfileBinding binding;
    private MoverViewModel moverViewModel;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        moverViewModel = new ViewModelProvider(requireActivity()).get(MoverViewModel.class);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_mover_profile, container, false);
        Log.d("MoverProfileFragment", "MoverProfileFragment created/re-created!");

        binding.etEmail.setText(moverViewModel.getUserDetails().getValue().getEmail());
        binding.etMobile.setText(moverViewModel.getUserDetails().getValue().getMobile());
        binding.etName.setText(moverViewModel.getUserDetails().getValue().getFirstname());


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