package com.wemove.adapters;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.wemove.R;
import com.wemove.model.PriceQuote;
import com.wemove.model.Review;

import java.util.List;

public class ReviewListAdapter extends RecyclerView.Adapter<ReviewListAdapter.ReviewViewHolder>{
    private final List<Review> reviews;
    private final ReviewListAdapter.OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(Review reviews);
    }
    public ReviewListAdapter(List<Review> reviews, ReviewListAdapter.OnItemClickListener listener) {
        this.reviews = reviews;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ReviewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.review_and_rating_item,parent,false);
        return new ReviewListAdapter.ReviewViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewViewHolder holder, int position) {
        Review review = reviews.get(position);
        holder.customerName.setText(review.getCustomerEmail());
        holder.review.setText(String.format("%s",review.getReview()));
        holder.rating.setRating(review.getRating());
        holder.bind(review,listener);
    }

    @Override
    public int getItemCount() {
        return reviews.size();
    }

    class ReviewViewHolder extends RecyclerView.ViewHolder{

        public TextView customerName;
        public TextView review;
        public RatingBar rating;



        public ReviewViewHolder(@NonNull View reviewAndRatingItemView) {
            super(reviewAndRatingItemView);
            this.customerName = (TextView) reviewAndRatingItemView.findViewById(R.id.tv_customer_name);
            this.review = (TextView) reviewAndRatingItemView.findViewById(R.id.tv_customer_review);
            this.rating = (RatingBar) reviewAndRatingItemView.findViewById(R.id.customer_rating);

        }

        public void bind(final Review review, final ReviewListAdapter.OnItemClickListener listener) {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    Log.i("QuotationListAdapter","Item Clicked !");
                    listener.onItemClick(review);
                }
            });
        }
    }
}
