package com.wemove.ui.customer;

import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
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

import com.google.gson.Gson;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;
import com.wemove.R;
import com.wemove.adapters.CustomExpandableListAdapter;
import com.wemove.adapters.MoveRequestAdapter;
import com.wemove.adapters.QuotationListAdapter;
import com.wemove.databinding.FragmentMRDetailCustomerViewBinding;
import com.wemove.model.MoveRequest;
import com.wemove.model.MoveRequestDto;
import com.wemove.model.MoveStatus;
import com.wemove.model.PriceQuote;
import com.wemove.viewmodel.CustomerViewModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class MRDetailCustomerViewFragment extends Fragment {

    private static final String TAG = "MRDtCustomerFragment";
    private FragmentMRDetailCustomerViewBinding binding;
    private CustomerViewModel customerViewModel;

    private ExpandableListAdapter expandableListAdapter;
    private List<String> expandableListTitle;
    private HashMap<String, List<String>> expandableListDetail;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        customerViewModel = new ViewModelProvider(requireActivity()).get(CustomerViewModel.class);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_m_r_detail_customer_view, container, false);
        MoveRequest moveRequest = customerViewModel.getSelectedMoveRequest().getValue().getMoveRequest();
        binding.tvMoveRequestTitle.setText(moveRequest.getMoveTitle());
        binding.tvMoveRequestDate.setText((moveRequest.getMoveDate()));
        binding.tvMoveRequestType.setText(moveRequest.getMoveType());
        binding.tvMrPickupAddress.setText(String.format("%s, %s", moveRequest.getPickupAddress().getAddress1(), moveRequest.getPickupAddress().getCity()));
        binding.tvMrDestinationAddress.setText(String.format("%s, %s", moveRequest.getDeliveryAddress().getAddress1(), moveRequest.getDeliveryAddress().getCity()));
        binding.tvMoveRequestStatus.setText(moveRequest.getMoveRequestStatus().name());
        expandableListDetail = customerViewModel.getInventoryDataOfSelectedMoveRequest();
        expandableListTitle = new ArrayList<String>(expandableListDetail.keySet());
        expandableListAdapter = new CustomExpandableListAdapter(getContext(), expandableListTitle, expandableListDetail);
        binding.expandableListView.setAdapter(expandableListAdapter);
        binding.expandableListView.setOnGroupClickListener((expandableListView, view, i, l) -> {
            setListViewHeight(expandableListView,i);
            return false;
        });

        bindAdapter(customerViewModel.getSelectedMoveRequest().getValue().getPriceQuotes());

        if(moveRequest!=null
        && !moveRequest.getMoveRequestStatus().equals(MoveStatus.CREATED)
        &&!moveRequest.getMoveRequestStatus().equals(MoveStatus.SUGGESTED)
        && !moveRequest.getMoveRequestStatus().equals(MoveStatus.CANCELLED)){
            try {
                String text = String.format("%s%s%s", moveRequest.getMoveRequestId(), moveRequest.getMoveRequestOwner(), moveRequest.getPickupAddress().getAddress1());
                MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
                BitMatrix bitMatrix = multiFormatWriter.encode(text, BarcodeFormat.QR_CODE, 600, 600);

                BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
                Bitmap bitmap = barcodeEncoder.createBitmap(bitMatrix);

                binding.moverOwnerQrCode.setImageBitmap(bitmap);
            } catch (WriterException e) {
                e.printStackTrace();
            }

        }

        return binding.getRoot();
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

    private void bindAdapter(List<PriceQuote> priceQuoteList) {
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this.getContext());
        binding.moveRequestQuotesRecyclerView.setLayoutManager(layoutManager);
        QuotationListAdapter listAdapter = new QuotationListAdapter(priceQuoteList, (priceQuote) -> {
            Log.i(TAG,"Item Clicked");
            onPriceQuoteClicked(priceQuote);
        });
        binding.moveRequestQuotesRecyclerView.setAdapter(listAdapter);
        Log.i(TAG, "bindAdapter Move Requests");
        listAdapter.notifyDataSetChanged();
    }

    private void onPriceQuoteClicked(PriceQuote priceQuote){
        //customerViewModel.setSelectedMoveRequest(moveRequest);
        Bundle bundle = new Bundle();
        bundle.putString("action","CustomerPriceQuoteClicked");
        Gson gson = new Gson();
        bundle.putString("price_quote",gson.toJson(priceQuote));
        //customerViewModel.setSelectedPriceQuote(priceQuote);
        Navigation.findNavController(getView()).navigate(R.id.action_MRDetailCustomerViewFragment_to_reviewAndRatingFragment,bundle);
    }
}