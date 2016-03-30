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
import com.timotiusoktorio.popularmovies.R;
import com.timotiusoktorio.popularmovies.models.Movie;

import butterknife.Bind;
import butterknife.BindString;
import butterknife.ButterKnife;

/**
 * Created by Timotius on 2016-03-25.
 */

public class DetailsFragment extends Fragment {

    private static final String ARG_MOVIE = "ARG_MOVIE";

    @Bind(R.id.poster_image_view) ImageView mPosterImageView;
    @Bind(R.id.title_text_view) TextView mTitleTextView;
    @Bind(R.id.release_date_text_view) TextView mReleaseDateTextView;
    @Bind(R.id.rating_text_view) TextView mRatingTextView;
    @Bind(R.id.overview_text_view) TextView mOverviewTextView;

    @BindString(R.string.string_format_release_date) String mReleaseDateFormat;
    @BindString(R.string.string_format_rating) String mRatingFormat;

    public static DetailsFragment newInstance(Movie movie) {
        Bundle args = new Bundle();
        args.putParcelable(ARG_MOVIE, movie);
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
        Movie movie = getArguments().getParcelable(ARG_MOVIE);
        if (movie != null) {
            String posterUrl = Constants.TMDB_POSTER_BASE_URL_LARGE + movie.getPosterPath();
            Picasso.with(getActivity()).load(posterUrl).into(mPosterImageView, new Callback() {
                @Override
                public void onSuccess() {
                    View rootView = getView();
                    if (rootView != null) rootView.setVisibility(View.VISIBLE);
                }

                @Override
                public void onError() {

                }
            });

            mTitleTextView.setText(movie.getTitle());
            mReleaseDateTextView.setText(String.format(mReleaseDateFormat, movie.getReleaseDate()));
            mRatingTextView.setText(String.format(mRatingFormat, movie.getVoteAverage()));
            mOverviewTextView.setText(movie.getOverview());
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

}