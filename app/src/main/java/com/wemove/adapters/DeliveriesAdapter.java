package com.wemove.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.wemove.R;
import com.wemove.model.MoveRequest;
import com.wemove.model.MoveRequestDto;

import java.util.List;

public class DeliveriesAdapter extends RecyclerView.Adapter<DeliveriesAdapter.MoveRequestViewHolder>{
    private final List<MoveRequestDto> moveRequestList;



    public interface OnItemClickListener {
        void onItemClick(MoveRequestDto moveRequestDto);
    }
    private final DeliveriesAdapter.OnItemClickListener listener;

    public DeliveriesAdapter(List<MoveRequestDto> moveRequestList, DeliveriesAdapter.OnItemClickListener listener) {
        this.moveRequestList = moveRequestList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public DeliveriesAdapter.MoveRequestViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.move_request,parent,false);
        return new DeliveriesAdapter.MoveRequestViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull DeliveriesAdapter.MoveRequestViewHolder holder, int position) {
        MoveRequestDto moveRequestDto = moveRequestList.get(position);
        MoveRequest moveRequest = moveRequestDto.getMoveRequest();
        holder.moveTitle.setText(moveRequest.getMoveTitle());
        holder.moveRequestStatus.setText(String.valueOf(moveRequest.getMoveRequestStatus()));
        holder.moveRequestType.setText(moveRequest.getMoveType());
        holder.moveRequestCreatedDate.setText(moveRequest.getCreatedOn());
        holder.pickupAddress.setText(String.format("%s, %s", moveRequest.getPickupAddress().getAddress1(), moveRequest.getPickupAddress().getCity()));
        holder.destinationAddress.setText(String.format("%s, %s", moveRequest.getDeliveryAddress().getAddress1(), moveRequest.getDeliveryAddress().getCity()));
        holder.moveRequestQuoteCount.setText(String.format(" %s Quote",moveRequestDto.getPriceQuotes().size()));
        holder.bind(moveRequestDto,listener);
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
        public TextView moveRequestQuoteCount;



        public MoveRequestViewHolder(@NonNull View moveRequestItemView) {
            super(moveRequestItemView);
            this.moveTitle = (TextView) moveRequestItemView.findViewById(R.id.tv_move_request_title);
            this.moveRequestCreatedDate = (TextView) moveRequestItemView.findViewById(R.id.tv_move_request_date);
            this.moveRequestType = (TextView) moveRequestItemView.findViewById(R.id.tv_move_request_type);
            this.moveRequestStatus = (TextView) moveRequestItemView.findViewById(R.id.tv_move_request_status);
            this.pickupAddress = (TextView) moveRequestItemView.findViewById(R.id.tv_mr_pickup_address);
            this.destinationAddress = (TextView) moveRequestItemView.findViewById(R.id.tv_mr_destination_address);
            this.moveRequestQuoteCount = (TextView) moveRequestItemView.findViewById(R.id.move_request_quote_count);
        }

        public void bind(final MoveRequestDto moveRequestDto, final DeliveriesAdapter.OnItemClickListener listener) {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    listener.onItemClick(moveRequestDto);
                }
            });
        }
    }
}
