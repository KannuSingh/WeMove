package com.wemove.adapters;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.wemove.R;
import com.wemove.model.MoveRequestDto;
import com.wemove.model.PriceQuote;

import java.util.List;

public class QuotationListAdapter extends RecyclerView.Adapter<QuotationListAdapter.QuotationViewHolder>{
    private final List<PriceQuote> priceQuoteList;
    private final QuotationListAdapter.OnItemClickListener listener;

    public QuotationListAdapter(List<PriceQuote> priceQuoteList, QuotationListAdapter.OnItemClickListener listener) {
        this.priceQuoteList = priceQuoteList;
        this.listener = listener;
    }


    @NonNull
    @Override
    public QuotationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.quote_item,parent,false);
        return new QuotationListAdapter.QuotationViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull QuotationViewHolder holder, int position) {
        PriceQuote priceQuote = priceQuoteList.get(position);
        holder.moverName.setText(priceQuote.getMovers().getFirstname());
        holder.quotePrice.setText(String.format(" %s",priceQuote.getPrice()));
        holder.quoteStatus.setText(priceQuote.getQuoteStatus().name());
        holder.bind(priceQuote,listener);
    }

    @Override
    public int getItemCount() {
        return priceQuoteList.size();
    }

    public interface OnItemClickListener {
        void onItemClick(PriceQuote priceQuote);
    }



    class QuotationViewHolder extends RecyclerView.ViewHolder{

        public TextView moverName;
        public TextView quotePrice;
        public TextView quoteStatus;



        public QuotationViewHolder(@NonNull View priceQuotationItemView) {
            super(priceQuotationItemView);
            this.moverName = (TextView) priceQuotationItemView.findViewById(R.id.tv_quote_mover_name);
            this.quotePrice = (TextView) priceQuotationItemView.findViewById(R.id.tv_quote_price);
            this.quoteStatus = (TextView) priceQuotationItemView.findViewById(R.id.tv_quote_status);

        }

        public void bind(final PriceQuote priceQuote, final QuotationListAdapter.OnItemClickListener listener) {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    Log.i("QuotationListAdapter","Item Clicked !");
                    listener.onItemClick(priceQuote);
                }
            });
        }
    }
}
