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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import com.wemove.R;
import com.wemove.databinding.FragmentInventoryItemDetailBinding;
import com.wemove.model.InventoryItemGroup;
import com.wemove.viewmodel.MoveRequestFormViewModel;

import java.util.ArrayList;
import java.util.List;


public class InventoryItemDetailFragment extends Fragment {

    private FragmentInventoryItemDetailBinding binding;
    private MoveRequestFormViewModel moveRequestFormViewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_inventory_item_detail, container, false);
        Log.d("InventoryItemDetail", "InventoryItemDetailFragment created/re-created!");


        //Setting move type array to spinner dropdown
        ArrayAdapter<CharSequence> homeItemCategoryAdapter = ArrayAdapter.createFromResource(getContext(),
                R.array.home_item_category_array, android.R.layout.simple_spinner_item);
        homeItemCategoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.itemCategory.setAdapter(homeItemCategoryAdapter);



        // Inflate the layout for this fragment
        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        moveRequestFormViewModel = new ViewModelProvider(requireActivity()).get(MoveRequestFormViewModel.class);


        //On itemCategory spinner selected.
        binding.itemCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Log.i("InventoryItemDetail",adapterView.getItemAtPosition(i).toString());
                String selectedItem = adapterView.getItemAtPosition(i).toString();
                if("Bed Room".equals(selectedItem)){
                    hideVisibilityOfAllItems();
                    binding.bedroomItems.setVisibility(View.VISIBLE);
                }else if("Living Room".equals(selectedItem)){
                    hideVisibilityOfAllItems();
                    binding.livingRoomItems.setVisibility(View.VISIBLE);
                }else if("Kitchen".equals(selectedItem)){
                    hideVisibilityOfAllItems();
                    binding.kitchenItems.setVisibility(View.VISIBLE);
                }else if("Bathroom".equals(selectedItem)){
                    hideVisibilityOfAllItems();
                    binding.bathroomItems.setVisibility(View.VISIBLE);
                }else if("Outdoor".equals(selectedItem)){
                    hideVisibilityOfAllItems();
                    binding.outdoorItems.setVisibility(View.VISIBLE);
                }else if("Other".equals(selectedItem)){

                }
            }

            private void hideVisibilityOfAllItems(){
                binding.bedroomItems.setVisibility(View.GONE);
                binding.livingRoomItems.setVisibility(View.GONE);
                binding.bathroomItems.setVisibility(View.GONE);
                binding.kitchenItems.setVisibility(View.GONE);
                binding.outdoorItems.setVisibility(View.GONE);
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        binding.itemDetailsAdd.setOnClickListener(v -> onAdd());
    }

    private void onAdd(){
        String selectedCategory = binding.itemCategory.getSelectedItem().toString();
        List<String> items = new ArrayList<String>();
        switch(selectedCategory){
            case "Bed Room" :
                if(binding.bedroomBench.isChecked())
                    items.add(getString(R.string.bedroom_bench));
                if(binding.bedroomTable.isChecked())
                    items.add(getString(R.string.bedroom_table));
                if(binding.doubleBed.isChecked())
                    items.add(getString(R.string.double_bed));
                if(binding.doubleBedMattress.isChecked())
                    items.add(getString(R.string.double_bed_mattress));
                if(binding.wardrobe.isChecked())
                    items.add(getString(R.string.wardrobe));
                if(binding.singleBed.isChecked())
                    items.add(getString(R.string.single_bed));
                if(binding.singleBedMattress.isChecked())
                    items.add(getString(R.string.single_bed_mattress));
                if(binding.nightLamp.isChecked())
                    items.add(getString(R.string.night_lamp));

                break;
            case "Living Room" :
                if(binding.armChair.isChecked())
                    items.add(getString(R.string.arm_chair));
                if(binding.cardboardBox.isChecked())
                    items.add(getString(R.string.cardboard_box));
                if(binding.coffeeSideTable.isChecked())
                    items.add(getString(R.string.coffee_side_table));
                if(binding.diningTable.isChecked())
                    items.add(getString(R.string.dining_table));
                if(binding.diningChair.isChecked())
                    items.add(getString(R.string.dining_chair));
                if(binding.rug.isChecked())
                    items.add(getString(R.string.rug));
                if(binding.sofa2Seat.isChecked())
                    items.add(getString(R.string.sofa_2_seat));
                if(binding.sofa3Seat.isChecked())
                    items.add(getString(R.string.sofa_3_seat));
                if(binding.sofa4Seat.isChecked())
                    items.add(getString(R.string.sofa_4_seat));
                if(binding.sofaLShapedCorner.isChecked())
                    items.add(getString(R.string.sofa_l_shaped_corner));
                if(binding.tv.isChecked())
                    items.add(getString(R.string.tv));
                if(binding.tvStand.isChecked())
                    items.add(getString(R.string.tv_stand));
                break;
            case "Kitchen" :
                if(binding.dishwasher.isChecked())
                    items.add(getString(R.string.dishwasher));
                if(binding.extractorFan.isChecked())
                    items.add(getString(R.string.extractor_fan));
                if(binding.fridgeFreezer.isChecked())
                    items.add(getString(R.string.fridge_freezer));
                if(binding.kitchenChair.isChecked())
                    items.add(getString(R.string.kitchen_chair));
                if(binding.kitchenTable.isChecked())
                    items.add(getString(R.string.kitchen_table));
                if(binding.microwave.isChecked())
                    items.add(getString(R.string.microwave));
                if(binding.oven.isChecked())
                    items.add(getString(R.string.oven));
                if(binding.tumbleDryer.isChecked())
                    items.add(getString(R.string.tumble_dryer));
                if(binding.washingMachine.isChecked())
                    items.add(getString(R.string.washing_machine));

                break;
            case "Bathroom" :
                if(binding.bathroomCabinet.isChecked())
                    items.add(getString(R.string.bathroom_cabinet));
                if(binding.mirror.isChecked())
                    items.add(getString(R.string.mirror));

                break;
            case "Outdoor" :

                if(binding.gardenBench.isChecked())
                    items.add(getString(R.string.garden_bench));
                if(binding.gardenChair.isChecked())
                    items.add(getString(R.string.garden_chair));
                if(binding.gardenCornerSofa.isChecked())
                    items.add(getString(R.string.garden_corner_sofa));
                if(binding.gardenTable.isChecked())
                    items.add(getString(R.string.garden_table));
                if(binding.parasol.isChecked())
                    items.add(getString(R.string.parasol));
                break;
            case "Other" :
                break;
        }
      //  String otherDetails = binding.etOtherDetails.getText().toString();

        InventoryItemGroup inventoryItem = new InventoryItemGroup();
        inventoryItem.setItems(items);
        inventoryItem.setCategory(selectedCategory);
       // inventoryItem.setOtherDetails(otherDetails);

        moveRequestFormViewModel.addNewInventoryItem(inventoryItem);

        Navigation.findNavController(getView()).navigate(R.id.action_inventoryItemDetailFragment_to_homeInventoryFragment);

    }
}