package com.wemove.adapters;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.wemove.R;
import com.wemove.model.MRStatusItem;

import java.util.List;

public class MoveStatusAdapter extends RecyclerView.Adapter<MoveStatusAdapter.MoveStatusHolder> {
    private final List<MRStatusItem> mrStatusItems;
    private final MoveStatusAdapter.OnItemClickListener listener;

    public MoveStatusAdapter(List<MRStatusItem> mrStatusItems, OnItemClickListener listener) {
        this.mrStatusItems = mrStatusItems;
        this.listener = listener;
    }

    @NonNull
    @Override
    public MoveStatusHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.timeline_item,parent,false);
        return new MoveStatusAdapter.MoveStatusHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MoveStatusHolder holder, int position) {
        MRStatusItem mrStatusItem = mrStatusItems.get(position);

        holder.itemDate.setText(mrStatusItem.getCreatedOn().toString());
        holder.itemStatus.setText(mrStatusItem.getMoveStatus().name());

        holder.bind(mrStatusItem,listener);

    }

    @Override
    public int getItemCount() {
        return mrStatusItems.size();
    }

    public interface OnItemClickListener {
        void onItemClick(MRStatusItem statusItem);
    }



    class MoveStatusHolder extends RecyclerView.ViewHolder{

        public TextView itemStatus;
        public TextView itemDate;
        //public TextView quoteStatus;



        public MoveStatusHolder(@NonNull View moveStatusItemView) {
            super(moveStatusItemView);
            this.itemStatus = (TextView) moveStatusItemView.findViewById(R.id.item_status);
            this.itemDate = (TextView) moveStatusItemView.findViewById(R.id.item_date);
           // this.quoteStatus = (TextView) priceQuotationItemView.findViewById(R.id.tv_quote_status);

        }

        public void bind(final MRStatusItem statusItem, final MoveStatusAdapter.OnItemClickListener listener) {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    Log.i("MoveStatusAdapter","Item Clicked !");
                    listener.onItemClick(statusItem);
                }
            });
        }
    }
}
