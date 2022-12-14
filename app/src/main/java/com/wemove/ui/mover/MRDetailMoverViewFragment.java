package com.wemove.ui.mover;

import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
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
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.Toast;

import com.google.android.material.tabs.TabLayout;
import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;
import com.wemove.R;
import com.wemove.adapters.CustomExpandableListAdapter;
import com.wemove.adapters.MoveStatusAdapter;
import com.wemove.databinding.FragmentMRDetailMoverViewBinding;
import com.wemove.model.MRStatusItem;
import com.wemove.model.MoveRequest;
import com.wemove.model.MoveStatus;
import com.wemove.model.PriceQuote;
import com.wemove.model.QuoteStatus;
import com.wemove.model.WemoveCaptureActivity;
import com.wemove.viewmodel.MoverViewModel;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class MRDetailMoverViewFragment extends Fragment {
    private static final String TAG = "MRDetailMoverFragment";
    private FragmentMRDetailMoverViewBinding binding;
    private MoverViewModel moverViewModel;


    ActivityResultLauncher<ScanOptions> barLauncher = registerForActivityResult(new ScanContract(), result ->{
        if(result.getContents()!= null){
            Log.i(TAG,result.getContents());
            boolean verificationResult = moverViewModel.verifyQRCodeData(result.getContents());
            if(verificationResult){
                Log.i(TAG,"Change Status");
                Toast.makeText(getContext(), "Verification Successful : Status updated to PICKUP STARTED", Toast.LENGTH_LONG).show();
                moverViewModel.updateMoveStatus(MoveStatus.PICKUP_STARTED);
            }else{
                Toast.makeText(getContext(), "Incorrect QR :Verification Failed", Toast.LENGTH_LONG).show();
            }
        }
    });

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        moverViewModel = new ViewModelProvider(requireActivity()).get(MoverViewModel.class);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_m_r_detail_mover_view, container, false);

        Log.d(TAG, "MRDetailMoverViewFragment created/re-created!");

        binding.fabStartPickup.setVisibility(View.GONE);
        binding.fabOnRoute.setVisibility(View.GONE);
        binding.fabReachedDestination.setVisibility(View.GONE);
        binding.fabMoveDelivered.setVisibility(View.GONE);

        MoveRequest moveRequest = moverViewModel.getSelectedMoveRequest().getValue().getMoveRequest();
        List<PriceQuote> priceQuotes = moverViewModel.getSelectedMoveRequest().getValue().getPriceQuotes();
        binding.tvMoveRequestTitle.setText(moveRequest.getMoveTitle());
        binding.tvMoveRequestDate.setText((moveRequest.getMoveDate()));
        binding.tvMoveRequestType.setText(moveRequest.getMoveType());
        binding.tvMrPickupAddress.setText(String.format("%s, %s", moveRequest.getPickupAddress().getAddress1(), moveRequest.getPickupAddress().getCity()));
        binding.tvMrDestinationAddress.setText(String.format("%s, %s", moveRequest.getDeliveryAddress().getAddress1(), moveRequest.getDeliveryAddress().getCity()));
        binding.tvMoveRequestStatus.setText(moveRequest.getMoveRequestStatus().name());
        //binding.moveQuoteCount.setText(priceQuotes.size());





        Log.i(TAG,"Getting expandableListDetail");
        HashMap<String, List<String>> expandableListDetail = moverViewModel.getInventoryDataOfSelectedMoveRequest();
        List<String> expandableListTitle = new ArrayList<String>(expandableListDetail.keySet());
        ExpandableListAdapter expandableListAdapter = new CustomExpandableListAdapter(getContext(), expandableListTitle, expandableListDetail);
        binding.expandableListView.setAdapter(expandableListAdapter);
        binding.expandableListView.setOnGroupClickListener((expandableListView, view, i, l) -> {
            setListViewHeight(expandableListView, i);
            return false;
        });

        binding.quotationFormLayout.setVisibility(View.GONE);
        binding.moverQuotationLayout.setVisibility(View.GONE);
        PriceQuote _selectedMRUserPriceQuote = moverViewModel.getUserPriceQuote().getValue();
        if (_selectedMRUserPriceQuote != null) {
            Log.i(TAG, " User Price Quote");
            //binding.quotationFormLayout.setVisibility(View.VISIBLE);
            binding.tvMoverQuotation.setText(String.format(" %s", _selectedMRUserPriceQuote.getPrice()));
            binding.tvMoverQuotationStatus.setText(_selectedMRUserPriceQuote.getQuoteStatus().name());
            binding.moverQuotationLayout.setVisibility(View.VISIBLE);
        } else {
            binding.quotationFormLayout.setVisibility(View.VISIBLE);
        }
        if(moverViewModel.getSelectedMoveRequestStatus().getValue().equals(MoveStatus.ACCEPTED) && _selectedMRUserPriceQuote.getQuoteStatus().equals(QuoteStatus.ACCEPTED)) {
            binding.fabStartPickup.setVisibility(View.VISIBLE);
        }else if(moverViewModel.getSelectedMoveRequestStatus().getValue().equals(MoveStatus.PICKUP_STARTED)){
            binding.fabOnRoute.setVisibility(View.VISIBLE);
        }else if(moverViewModel.getSelectedMoveRequestStatus().getValue().equals(MoveStatus.ON_ROUTE)){
            binding.fabReachedDestination.setVisibility(View.VISIBLE);
        }else if(moverViewModel.getSelectedMoveRequestStatus().getValue().equals(MoveStatus.REACHED)){
            binding.fabMoveDelivered.setVisibility(View.VISIBLE);
        }

        binding.fabStartPickup.setOnClickListener(this::verifyMoveOwner);
        binding.fabMoveDelivered.setOnClickListener(view ->{
            moverViewModel.updateMoveStatus(MoveStatus.FINISHED);
            Toast.makeText(getContext(), "Status Updated", Toast.LENGTH_SHORT).show();
            Navigation.findNavController(getView()).navigate(R.id.action_MRDetailMoverViewFragment_to_favoriteDeliveryFragment);

        });
        binding.fabReachedDestination.setOnClickListener(view -> {
            moverViewModel.updateMoveStatus(MoveStatus.REACHED);
            Toast.makeText(getContext(), "Status Updated", Toast.LENGTH_SHORT).show();
            Navigation.findNavController(getView()).navigate(R.id.action_MRDetailMoverViewFragment_to_favoriteDeliveryFragment);
        });

        binding.fabOnRoute.setOnClickListener(view -> {
            moverViewModel.updateMoveStatus(MoveStatus.ON_ROUTE);
            Toast.makeText(getContext(), "Status Updated", Toast.LENGTH_SHORT).show();
            Navigation.findNavController(getView()).navigate(R.id.action_MRDetailMoverViewFragment_to_favoriteDeliveryFragment);
        });

        final Observer<MoveStatus> moveRequestStatusUpdate = moveStatus -> {
            if(moveStatus != null){
                binding.tvMoveRequestStatus.setText(moveStatus.name());
            }
        };
        moverViewModel.getSelectedMoveRequestStatus().observe(getViewLifecycleOwner(),moveRequestStatusUpdate);

        final Observer<Boolean> priceQuoteSubmissionResponse = submissionResponse -> {

            if (submissionResponse == null) {

            } else if (submissionResponse) {
                binding.quotationFormLayout.setVisibility(View.GONE);
                binding.moverQuotationLayout.setVisibility(View.VISIBLE);
                Toast.makeText(getContext(), "Successfully Submitted Quotation", Toast.LENGTH_SHORT).show();
            } else {
                binding.quotationFormLayout.setVisibility(View.VISIBLE);
                binding.moverQuotationLayout.setVisibility(View.GONE);
                Toast.makeText(getContext(), "Failed to Submit Quotation", Toast.LENGTH_SHORT).show();
            }
        };

        moverViewModel.getPriceQuoteSubmitResponseFlag().observe(getViewLifecycleOwner(), priceQuoteSubmissionResponse);

        final Observer<PriceQuote> userPriceQuoteObserver = priceQuote -> {
            if (priceQuote != null) {
                binding.tvMoverQuotation.setText(String.format(" %s", priceQuote.getPrice()));
                binding.tvMoverQuotationStatus.setText(priceQuote.getQuoteStatus().name());
            }
        };

        moverViewModel.getUserPriceQuote().observe(getViewLifecycleOwner(), userPriceQuoteObserver);

        moverViewModel.getMRStatusList().observe(getViewLifecycleOwner(),mrStatusItems -> {
            if(mrStatusItems != null){
                bindAdapter(mrStatusItems);
            }
        });

        binding.btnSubmitQuotationPrice.setOnClickListener(view -> {

            try {
                double quotedPrice = Double.parseDouble(binding.etQuotePrice.getText().toString());
                moverViewModel.submitPriceQuote(quotedPrice);
            } catch (NullPointerException e) {
                Log.e(TAG, e.getMessage());
            }
        });



        return binding.getRoot();
    }

    private void verifyMoveOwner(View view) {
        ScanOptions scanOptions = new ScanOptions();
        scanOptions.setPrompt("Volume up to turn on flash");
        scanOptions.setBeepEnabled(true);
        scanOptions.setOrientationLocked(true);
        scanOptions.setCaptureActivity(WemoveCaptureActivity.class);
        barLauncher.launch(scanOptions);
        //Navigation.findNavController(getView()).navigate(R.id.action_favoriteDeliveryFragment_to_MRDetailMoverViewFragment);

    }



    private void bindAdapter(List<MRStatusItem> mrStatusItems) {
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this.getContext());
        binding.timelineListView.setLayoutManager(layoutManager);
        MoveStatusAdapter listAdapter = new MoveStatusAdapter(mrStatusItems, this::onMoveRequestStatusClicked, getContext());
        binding.timelineListView.setAdapter(listAdapter);
        Log.i(TAG, "bindAdapter Move Status");
        listAdapter.notifyDataSetChanged();
    }

    private void onMoveRequestStatusClicked(MRStatusItem mrStatusItem){
        //moverViewModel.setSelectedMoveRequest(moveRequestDto);
        //Navigation.findNavController(getView()).navigate(R.id.action_favoriteDeliveryFragment_to_MRDetailMoverViewFragment);
    }

    private void setListViewHeight(ExpandableListView listView,
                                   int group) {
        ExpandableListAdapter listAdapter = (ExpandableListAdapter) listView.getExpandableListAdapter();
        int totalHeight = 0;
        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(),
                View.MeasureSpec.EXACTLY);
        for (int i = 0; i < listAdapter.getGroupCount(); i++) {
            View groupItem = listAdapter.getGroupView(i, false, null, listView);
            groupItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);

            totalHeight += groupItem.getMeasuredHeight();

            if (((listView.isGroupExpanded(i)) && (i != group))
                    || ((!listView.isGroupExpanded(i)) && (i == group))) {
                for (int j = 0; j < listAdapter.getChildrenCount(i); j++) {
                    View listItem = listAdapter.getChildView(i, j, false, null,
                            listView);
                    listItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);

                    totalHeight += listItem.getMeasuredHeight();

                }
            }
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        int height = totalHeight
                + (listView.getDividerHeight() * (listAdapter.getGroupCount() - 1));
        if (height < 10)
            height = 200;
        params.height = height;
        listView.setLayoutParams(params);
        listView.requestLayout();

    }
}