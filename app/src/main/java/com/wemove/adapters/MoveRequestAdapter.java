package com.wemove.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.wemove.R;
import com.wemove.model.InventoryItemGroup;
import com.wemove.model.MoveRequest;

import java.util.List;

public class MoveRequestAdapter extends RecyclerView.Adapter<MoveRequestAdapter.MoveRequestViewHolder>{
    private final List<MoveRequest> moveRequestList;



    public interface OnItemClickListener {
        void onItemClick(MoveRequest moveRequest);
    }
    private final MoveRequestAdapter.OnItemClickListener listener;

    public MoveRequestAdapter(List<MoveRequest> moveRequestList, OnItemClickListener listener) {
        this.moveRequestList = moveRequestList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public MoveRequestViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.move_request,parent,false);
        return new MoveRequestAdapter.MoveRequestViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MoveRequestViewHolder holder, int position) {
        MoveRequest moveRequest = moveRequestList.get(position);
        holder.moveTitle.setText(moveRequest.getMoveTitle());
        holder.moveRequestStatus.setText(String.valueOf(moveRequest.getMoveRequestStatus()));
        holder.moveRequestType.setText(moveRequest.getMoveType());
        holder.moveRequestCreatedDate.setText(moveRequest.getCreatedOn());
        holder.pickupAddress.setText(String.format("%s, %s", moveRequest.getPickupAddress().getAddress1(), moveRequest.getPickupAddress().getCity()));
        holder.destinationAddress.setText(String.format("%s, %s", moveRequest.getDeliveryAddress().getAddress1(), moveRequest.getDeliveryAddress().getCity()));
    }

    @Override
    public int getItemCount() {
        return moveRequestList.size();
    }





    class MoveRequestViewHolder extends RecyclerView.ViewHolder{

        public TextView moveTitle;
        public TextView moveRequestCreatedDate;
        public TextView moveRequestType;
        public TextView pickupAddress;
        public TextView destinationAddress;
        public TextView moveRequestStatus;


        public MoveRequestViewHolder(@NonNull View moveRequestItemView) {
            super(moveRequestItemView);
            this.moveTitle = (TextView) moveRequestItemView.findViewById(R.id.tv_move_request_title);
            this.moveRequestCreatedDate = (TextView) moveRequestItemView.findViewById(R.id.tv_move_request_date);
            this.moveRequestType = (TextView) moveRequestItemView.findViewById(R.id.tv_move_request_type);
            this.moveRequestStatus = (TextView) moveRequestItemView.findViewById(R.id.tv_move_request_status);
            this.pickupAddress = (TextView) moveRequestItemView.findViewById(R.id.tv_mr_pickup_address);
            this.destinationAddress = (TextView) moveRequestItemView.findViewById(R.id.tv_mr_destination_address);
        }

        public void bind(final MoveRequest moveRequest, final MoveRequestAdapter.OnItemClickListener listener) {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    listener.onItemClick(moveRequest);
                }
            });
        }
    }
}
