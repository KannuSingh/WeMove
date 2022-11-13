package com.wemove.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.wemove.R;
import com.wemove.model.InventoryItemGroup;

import java.util.List;

public class InventoryItemAdapter extends RecyclerView.Adapter<InventoryItemAdapter.InventoryItemViewHolder> {

    private final List<InventoryItemGroup> inventoryItemGroups;

    public interface OnItemClickListener {
        void onItemClick(InventoryItemGroup inventoryItemGroup);
    }
    private final InventoryItemAdapter.OnItemClickListener listener;

    public InventoryItemAdapter(List<InventoryItemGroup> inventoryItemGroups, InventoryItemAdapter.OnItemClickListener listener){
        super();
        this.inventoryItemGroups = inventoryItemGroups;
        this.listener = listener;
    }

    @NonNull
    @Override
    public InventoryItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.inventory_item_layout,parent,false);
        return new InventoryItemAdapter.InventoryItemViewHolder(v);

    }

    @Override
    public void onBindViewHolder(@NonNull InventoryItemViewHolder holder, int position) {
        InventoryItemGroup inventoryItemGroup = inventoryItemGroups.get(position);
        holder.itemCategory.setText(inventoryItemGroup.getCategory());
        holder.itemCount.setText(String.valueOf(inventoryItemGroup.getItems().size()));
    }



    @Override
    public int getItemCount() {
        return inventoryItemGroups.size();
    }

    class InventoryItemViewHolder extends RecyclerView.ViewHolder{

        public TextView itemCategory;
        public TextView itemCount;

        public InventoryItemViewHolder(@NonNull View inventoryItemView) {
            super(inventoryItemView);
            this.itemCategory = (TextView) inventoryItemView.findViewById(R.id.tv_item_category);
            this.itemCount = (TextView) inventoryItemView.findViewById(R.id.tv_item_count);
        }

        public void bind(final InventoryItemGroup inventoryItemGroup, final InventoryItemAdapter.OnItemClickListener listener) {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    listener.onItemClick(inventoryItemGroup);
                }
            });
        }
    }
}
