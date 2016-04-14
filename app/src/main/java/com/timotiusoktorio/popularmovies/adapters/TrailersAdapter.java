package com.timotiusoktorio.popularmovies.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.timotiusoktorio.popularmovies.Constants;
import com.timotiusoktorio.popularmovies.R;
import com.timotiusoktorio.popularmovies.models.Trailer;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Timotius on 2016-03-31.
 */

public class TrailersAdapter extends RecyclerView.Adapter<TrailersAdapter.ViewHolder> {

    private Context mContext;
    private List<Trailer> mTrailers;
    private OnTrailerClickListener mOnTrailerClickListener;

    public TrailersAdapter(Context context, List<Trailer> trailers, OnTrailerClickListener onTrailerClickListener) {
        mContext = context;
        mTrailers = trailers;
        mOnTrailerClickListener = onTrailerClickListener;
    }

    @Override
    public int getItemCount() {
        return mTrailers.size();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_trailers, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        final Trailer trailer = mTrailers.get(position);
        final String trailerKey = trailer.getKey();

        String trailerPosterUrl = Constants.YOUTUBE_IMAGE_URL_PREFIX + trailerKey + Constants.YOUTUBE_IMAGE_URL_SUFFIX;
        Picasso.with(mContext).load(trailerPosterUrl).into(holder.trailerPosterImageView);

        holder.trailerLabelTextView.setText(trailer.getName());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { mOnTrailerClickListener.onTrailerClick(trailerKey); }
        });
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.trailer_poster_image_view) ImageView trailerPosterImageView;
        @Bind(R.id.trailer_label_text_view) TextView trailerLabelTextView;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

    }

    public interface OnTrailerClickListener {
        void onTrailerClick(String key);
    }

}