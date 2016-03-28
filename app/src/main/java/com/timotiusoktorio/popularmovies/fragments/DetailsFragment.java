package com.timotiusoktorio.popularmovies.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.timotiusoktorio.popularmovies.Constants;
import com.timotiusoktorio.popularmovies.FetchMovieDetailsAsync;
import com.timotiusoktorio.popularmovies.R;
import com.timotiusoktorio.popularmovies.models.Movie;

import butterknife.Bind;
import butterknife.BindString;
import butterknife.ButterKnife;

/**
 * Created by Timotius on 2016-03-25.
 */

public class DetailsFragment extends Fragment implements FetchMovieDetailsAsync.OnMovieFetchedListener {

    private static final String ARG_MOVIE_ID = "ARG_MOVIE_ID";

    @Bind(R.id.poster_image_view) ImageView mPosterImageView;
    @Bind(R.id.title_text_view) TextView mTitleTextView;
    @Bind(R.id.release_date_text_view) TextView mReleaseDateTextView;
    @Bind(R.id.runtime_text_view) TextView mRuntimeTextView;
    @Bind(R.id.rating_text_view) TextView mRatingTextView;
    @Bind(R.id.overview_text_view) TextView mOverviewTextView;

    @BindString(R.string.string_format_release_date) String mReleaseDateFormat;
    @BindString(R.string.string_format_runtime) String mRuntimeFormat;
    @BindString(R.string.string_format_rating) String mRatingFormat;

    public static DetailsFragment newInstance(long movieId) {
        Bundle args = new Bundle();
        args.putLong(ARG_MOVIE_ID, movieId);
        DetailsFragment detailsFragment = new DetailsFragment();
        detailsFragment.setArguments(args);
        return detailsFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_details, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        new FetchMovieDetailsAsync(this).execute(getArguments().getLong(ARG_MOVIE_ID));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @Override
    public void onMovieFetched(Movie movie) {
        String posterUrl = Constants.TMDB_POSTER_BASE_URL_LARGE + movie.getPosterPath();
        Picasso.with(getActivity()).load(posterUrl).into(mPosterImageView, mPictureLoadedCallback);

        mTitleTextView.setText(movie.getTitle());
        mReleaseDateTextView.setText(String.format(mReleaseDateFormat, movie.getReleaseDate()));
        mRuntimeTextView.setText(String.format(mRuntimeFormat, movie.getRuntime()));
        mRatingTextView.setText(String.format(mRatingFormat, movie.getVoteAverage()));
        mOverviewTextView.setText(movie.getOverview());
    }

    private Callback mPictureLoadedCallback = new Callback() {
        @Override
        public void onSuccess() {
            View rootView = getView();
            if (null != rootView) rootView.setVisibility(View.VISIBLE);
        }

        @Override
        public void onError() {

        }
    };

}