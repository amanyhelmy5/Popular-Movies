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
import com.timotiusoktorio.popularmovies.utilities.Utility;

import java.io.File;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Timotius on 2016-03-24.
 */

public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.ViewHolder> {

    private Context mContext;
    private List<Movie> mMovies;
    private OnItemClickListener mOnItemClickListener;

    public MoviesAdapter(Context context, List<Movie> movies) {
        mContext = context;
        mMovies = movies;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    public void clear() {
        mMovies.clear();
        notifyDataSetChanged();
    }

    public void addAll(List<Movie> movies) {
        mMovies.addAll(movies);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return mMovies.size();
    }

    @Override
    public long getItemId(int position) {
        return mMovies.get(position).getId();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(mContext).inflate(R.layout.list_item_movies, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Movie movie = mMovies.get(position);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { mOnItemClickListener.onItemClick(movie); }
        });

        String posterPath = movie.getPosterPath();
        String posterUrl = Constants.TMDB_MOVIE_POSTER_URL + posterPath;
        if (Utility.isExternalStorageReadable()) {
            File posterFile = new File(mContext.getExternalFilesDir(null), posterPath);
            if (posterFile.exists()) Picasso.with(mContext).load(posterFile).into(holder.movieImageView);
            else Picasso.with(mContext).load(posterUrl).into(holder.movieImageView);
        } else Picasso.with(mContext).load(posterUrl).into(holder.movieImageView);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.movie_image_view) ImageView movieImageView;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

    }

    public interface OnItemClickListener {
        void onItemClick(Movie movie);
    }

}