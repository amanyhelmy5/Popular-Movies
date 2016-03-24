package com.timotiusoktorio.popularmovies.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.timotiusoktorio.popularmovies.Constants;
import com.timotiusoktorio.popularmovies.R;
import com.timotiusoktorio.popularmovies.models.Movie;

import java.util.List;

/**
 * Created by Timotius on 2016-03-24.
 */

public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.ViewHolder> {

    private Context mContext;
    private List<Movie> mMovies;

    public MoviesAdapter(Context context, List<Movie> movies) {
        mContext = context;
        mMovies = movies;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.list_item_movies, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        String posterUrl = Constants.TMDB_POSTER_BASE_URL + mMovies.get(position).getPosterPath();
        Picasso.with(mContext).load(posterUrl).into(holder.movieImageView);
    }

    @Override
    public int getItemCount() {
        return mMovies.size();
    }

    public void clear() {
        mMovies.clear();
        notifyDataSetChanged();
    }

    public void addAll(List<Movie> movies) {
        mMovies.addAll(movies);
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView movieImageView;

        public ViewHolder(View itemView) {
            super(itemView);
            movieImageView = (ImageView) itemView.findViewById(R.id.movie_image_view);
        }

    }

}