package com.timotiusoktorio.popularmovies.adapters;

import android.support.v4.widget.TextViewCompat;
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

    private static final int REVIEW_CONTENT_FULL = 100;
    private static final int REVIEW_CONTENT_SHORT = 2;

    private List<Review> mReviews;

    public ReviewsAdapter(List<Review> reviews) {
        mReviews = reviews;
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
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @Bind(R.id.author_text_view) TextView authorTextView;
        @Bind(R.id.content_text_view) TextView contentTextView;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int contentTextViewMaxLines = TextViewCompat.getMaxLines(contentTextView);
            contentTextView.setMaxLines((contentTextViewMaxLines == REVIEW_CONTENT_SHORT) ? REVIEW_CONTENT_FULL : REVIEW_CONTENT_SHORT);
        }

    }

}