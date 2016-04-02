package com.timotiusoktorio.popularmovies.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.timotiusoktorio.popularmovies.R;
import com.timotiusoktorio.popularmovies.models.Review;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Timotius on 2016-04-01.
 */

public class ReviewsAdapter extends RecyclerView.Adapter<ReviewsAdapter.ViewHolder> {

    private List<Review> mReviews;
    private OnReviewClickListener mOnReviewClickListener;

    public ReviewsAdapter(List<Review> reviews, OnReviewClickListener onReviewClickListener) {
        mReviews = reviews;
        mOnReviewClickListener = onReviewClickListener;
    }

    @Override
    public int getItemCount() {
        return mReviews.size();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_reviews, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Review review = mReviews.get(position);
        holder.authorTextView.setText(review.getAuthor());
        holder.contentTextView.setText(review.getContent());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { mOnReviewClickListener.onReviewClick(review.getUrl()); }
        });
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.author_text_view) TextView authorTextView;
        @Bind(R.id.content_text_view) TextView contentTextView;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

    }

    public interface OnReviewClickListener {
        void onReviewClick(String url);
    }

}