package com.timotiusoktorio.popularmovies.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.timotiusoktorio.popularmovies.Constants;
import com.timotiusoktorio.popularmovies.FetchMovieDetailsAsync;
import com.timotiusoktorio.popularmovies.R;
import com.timotiusoktorio.popularmovies.adapters.DetailsAdapter;
import com.timotiusoktorio.popularmovies.adapters.ReviewsAdapter;
import com.timotiusoktorio.popularmovies.adapters.TrailersAdapter;
import com.timotiusoktorio.popularmovies.models.Movie;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Timotius on 2016-03-25.
 */

public class DetailsFragment extends Fragment implements TrailersAdapter.OnTrailerClickListener, ReviewsAdapter.OnReviewClickListener {

    private static final String ARG_MOVIE = "ARG_MOVIE";

    private DetailsAdapter mAdapter;

    @Bind(R.id.recycler_view) RecyclerView mRecyclerView;

    public static DetailsFragment newInstance(Movie movie) {
        Bundle args = new Bundle();
        args.putParcelable(ARG_MOVIE, movie);
        DetailsFragment detailsFragment = new DetailsFragment();
        detailsFragment.setArguments(args);
        return detailsFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAdapter = new DetailsAdapter(getActivity());
        mAdapter.setOnTrailerClickListener(this);
        mAdapter.setOnReviewClickListener(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_details, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Movie movie = getArguments().getParcelable(ARG_MOVIE);
        if (movie != null) new FetchMovieDetailsAsync(mAdapter).execute(movie);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @Override
    public void onTrailerClick(String key) {
        Uri trailerUri = Uri.parse(Constants.YOUTUBE_VIDEO_URL + key);
        Intent intent = new Intent(Intent.ACTION_VIEW, trailerUri);
        if (intent.resolveActivity(getActivity().getPackageManager()) != null) startActivity(intent);
    }

    @Override
    public void onReviewClick(String url) {
        System.out.println("Opening review url: " + url);
    }

}