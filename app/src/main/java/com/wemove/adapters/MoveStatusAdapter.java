package com.wemove.adapters;

import android.content.Context;
import android.content.res.Resources;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.wemove.R;
import com.wemove.model.MRStatusItem;

import java.util.List;

public class MoveStatusAdapter extends RecyclerView.Adapter<MoveStatusAdapter.MoveStatusHolder> {
    private final List<MRStatusItem> mrStatusItems;
    private final MoveStatusAdapter.OnItemClickListener listener;
    private final Context context;

    public MoveStatusAdapter(List<MRStatusItem> mrStatusItems, OnItemClickListener listener, Context context) {
        this.mrStatusItems = mrStatusItems;
        this.listener = listener;
        this.context = context;
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
        Resources res = context.getResources();
        switch(mrStatusItem.getMoveStatus()){

            case FINISHED:
                holder.itemDate.setText(mrStatusItem.getCreatedOn().toString());
                holder.itemStatus.setText(mrStatusItem.getMoveStatus().name());
                holder.lineView.setVisibility(View.GONE);
                holder.circleView.setBackground(ResourcesCompat.getDrawable(res,R.drawable.ic_circle_green,null));
                break;

            case CANCELLED:
                holder.itemDate.setText(mrStatusItem.getCreatedOn().toString());
                holder.itemStatus.setText(mrStatusItem.getMoveStatus().name());
                holder.lineView.setVisibility(View.GONE);
                holder.circleView.setBackground(ResourcesCompat.getDrawable(res,R.drawable.ic_circle_red,null));

                break;
            default:
                holder.itemDate.setText(mrStatusItem.getCreatedOn().toString());
                holder.itemStatus.setText(mrStatusItem.getMoveStatus().name());
                holder.circleView.setBackground(ResourcesCompat.getDrawable(res,R.drawable.ic_circle,null));

                break;
        }


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
        public View circleView;
        public View lineView;
        //public TextView quoteStatus;



        public MoveStatusHolder(@NonNull View moveStatusItemView) {
            super(moveStatusItemView);
            this.itemStatus = (TextView) moveStatusItemView.findViewById(R.id.item_status);
            this.itemDate = (TextView) moveStatusItemView.findViewById(R.id.item_date);
            this.circleView = moveStatusItemView.findViewById(R.id.status_circle);
            this.lineView = moveStatusItemView.findViewById(R.id.status_line);
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
