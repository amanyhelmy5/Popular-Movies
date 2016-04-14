package com.timotiusoktorio.popularmovies.adapters;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.timotiusoktorio.popularmovies.Constants;
import com.timotiusoktorio.popularmovies.R;
import com.timotiusoktorio.popularmovies.models.Movie;
import com.timotiusoktorio.popularmovies.models.Review;
import com.timotiusoktorio.popularmovies.models.Trailer;
import com.timotiusoktorio.popularmovies.utilities.DividerItemDecoration;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Timotius on 2016-03-31.
 */

public class DetailsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TOTAL_ITEM_TYPES = 3;
    private static final int TYPE_INFO = 0, TYPE_TRAILERS = 1, TYPE_REVIEWS = 2;

    private Context mContext;
    private Movie mMovie;
    private TrailersAdapter.OnTrailerClickListener mOnTrailerClickListener;

    public DetailsAdapter(Context context) {
        mContext = context;
    }

    public void setMovie(Movie movie) {
        mMovie = movie;
    }

    public void setOnTrailerClickListener(TrailersAdapter.OnTrailerClickListener onTrailerClickListener) {
        mOnTrailerClickListener = onTrailerClickListener;
    }

    @Override
    public int getItemCount() {
        return (mMovie == null) ? RecyclerView.NO_POSITION : TOTAL_ITEM_TYPES;
    }

    @Override
    public int getItemViewType(int position) {
        switch (position) {
            case 0: return TYPE_INFO;
            case 1: return TYPE_TRAILERS;
            case 2: return TYPE_REVIEWS;
        }
        return -1;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        switch (viewType) {
            case TYPE_INFO:
                View movieInfoItemView = inflater.inflate(R.layout.list_item_movie_info, parent, false);
                return new MovieInfoViewHolder(movieInfoItemView);
            case TYPE_TRAILERS:
                View movieTrailersItemView = inflater.inflate(R.layout.list_item_movie_trailers, parent, false);
                return new MovieTrailersViewHolder(movieTrailersItemView);
            case TYPE_REVIEWS:
                View movieReviewsItemView = inflater.inflate(R.layout.list_item_movie_reviews, parent, false);
                return new MovieReviewsViewHolder(movieReviewsItemView);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch (holder.getItemViewType()) {
            case TYPE_INFO:
                bindMovieInfoViewHolder((MovieInfoViewHolder) holder);
                break;
            case TYPE_TRAILERS:
                List<Trailer> trailers = mMovie.getTrailers();
                if (trailers != null && trailers.size() > 0) bindMovieTrailersViewHolder((MovieTrailersViewHolder) holder, trailers);
                break;
            case TYPE_REVIEWS:
                List<Review> reviews = mMovie.getReviews();
                if (reviews != null && reviews.size() > 0) bindMovieReviewsViewHolder((MovieReviewsViewHolder) holder, reviews);
                break;
        }
    }

    private void bindMovieInfoViewHolder(MovieInfoViewHolder holder) {
        String posterUrl = Constants.TMDB_MOVIE_POSTER_URL + mMovie.getPosterPath();
        Picasso.with(mContext).load(posterUrl).into(holder.posterImageView);

        String releaseDateFormat = mContext.getString(R.string.string_format_release_date);
        holder.releaseDateTextView.setText(String.format(releaseDateFormat, mMovie.getReleaseDate()));

        String ratingFormat = mContext.getString(R.string.string_format_rating);
        holder.ratingTextView.setText(String.format(ratingFormat, mMovie.getVoteAverage()));

        holder.overviewTextView.setText(mMovie.getOverview());
        holder.titleTextView.setText(mMovie.getTitle());
    }

    private void bindMovieTrailersViewHolder(MovieTrailersViewHolder holder, List<Trailer> trailers) {
        holder.itemView.setVisibility(View.VISIBLE);
        holder.trailersRecyclerView.setAdapter(new TrailersAdapter(mContext, trailers, mOnTrailerClickListener));
        holder.trailersRecyclerView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false));
        holder.trailersRecyclerView.setHasFixedSize(true);
    }

    private void bindMovieReviewsViewHolder(MovieReviewsViewHolder holder, List<Review> reviews) {
        holder.itemView.setVisibility(View.VISIBLE);
        holder.reviewsRecyclerView.setAdapter(new ReviewsAdapter(reviews));
        holder.reviewsRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        holder.reviewsRecyclerView.addItemDecoration(new DividerItemDecoration(mContext, DividerItemDecoration.VERTICAL_LIST));
    }

    public static class MovieInfoViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.poster_image_view) ImageView posterImageView;
        @Bind(R.id.title_text_view) TextView titleTextView;
        @Bind(R.id.release_date_text_view) TextView releaseDateTextView;
        @Bind(R.id.rating_text_view) TextView ratingTextView;
        @Bind(R.id.overview_text_view) TextView overviewTextView;

        public MovieInfoViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

    }

    public static class MovieTrailersViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.trailers_recycler_view) RecyclerView trailersRecyclerView;

        public MovieTrailersViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

    }

    public static class MovieReviewsViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.reviews_recycler_view) RecyclerView reviewsRecyclerView;

        public MovieReviewsViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

    }

}