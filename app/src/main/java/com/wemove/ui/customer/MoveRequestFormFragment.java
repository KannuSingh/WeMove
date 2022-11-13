package com.wemove.ui.customer;

import android.app.DatePickerDialog;
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
import android.widget.DatePicker;

import com.wemove.R;
import com.wemove.databinding.FragmentMoveRequestFormBinding;
import com.wemove.model.MoveHelp;
import com.wemove.viewmodel.MoveRequestFormViewModel;

import java.util.Calendar;

public class MoveRequestFormFragment extends Fragment {
    private FragmentMoveRequestFormBinding binding;
    private MoveRequestFormViewModel moveRequestFormViewModel;

    private  DatePickerDialog datePickerDialog;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_move_request_form, container, false);
        Log.d("MoveRequestFormFragment", "MoveRequestFormFragment created/re-created!");


        //Setting move type array to spinner dropdown
        ArrayAdapter<CharSequence> moveTypeAdapter = ArrayAdapter.createFromResource(getContext(),
                R.array.move_type_array, android.R.layout.simple_spinner_item);
        moveTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.moveType.setAdapter(moveTypeAdapter);







        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        moveRequestFormViewModel = new ViewModelProvider(requireActivity()).get(MoveRequestFormViewModel.class);





        binding.selectMoveDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar cal = Calendar.getInstance();
                int day = cal.get(Calendar.DAY_OF_MONTH);
                int month = cal.get(Calendar.MONTH);
                int year = cal.get(Calendar.YEAR);

                datePickerDialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener(){
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        binding.etMoveDate.setText(dayOfMonth + "/" + month + "/" + year);
                    }
                }, year, month, day);

                datePickerDialog.show();
            }
        });


        //On moveRequestFormNext Button Clicked.
        binding.moveRequestFormOneNext.setOnClickListener(v ->
                                    {
                                        MoveHelp movehelp = MoveHelp.NO_HELPER;
                                        if(binding.rgMovingHelp.getCheckedRadioButtonId() == R.id.rb_no_help){
                                            movehelp = MoveHelp.NO_HELPER;
                                        }else if(binding.rgMovingHelp.getCheckedRadioButtonId() == R.id.rb_driver_help){
                                            movehelp = MoveHelp.ONLY_DRIVER;
                                        }else if(binding.rgMovingHelp.getCheckedRadioButtonId() == R.id.rb_driver_plus_one_helper){
                                            movehelp = MoveHelp.DRIVER_PLUS_1HELPER;
                                        }else if(binding.rgMovingHelp.getCheckedRadioButtonId() == R.id.rb_driver_plus_two_helper){
                                            movehelp = MoveHelp.DRIVER_PLUS_2HELPER;
                                        }
                                        moveRequestFormViewModel.setMoveRequestTitle(binding.etMoveRequestTitle.getText().toString());
                                        moveRequestFormViewModel.setMoveRequestType(binding.moveType.getSelectedItem().toString());
                                        moveRequestFormViewModel.setMoveRequestMoveDate(binding.etMoveDate.getText().toString());
                                        moveRequestFormViewModel.setMoveRequestMoveHelp(movehelp);

                                        Navigation.findNavController(getView()).navigate(R.id.action_moveRequestFormFragment_to_pickupAndDeliveryFragment);
                                    });


    }
}