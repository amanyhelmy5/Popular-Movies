package com.timotiusoktorio.popularmovies.fragments;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.ShareActionProvider;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.timotiusoktorio.popularmovies.Constants;
import com.timotiusoktorio.popularmovies.FetchMovieDetailsAsync;
import com.timotiusoktorio.popularmovies.R;
import com.timotiusoktorio.popularmovies.adapters.DetailsAdapter;
import com.timotiusoktorio.popularmovies.adapters.TrailersAdapter;
import com.timotiusoktorio.popularmovies.models.Movie;
import com.timotiusoktorio.popularmovies.models.Trailer;
import com.timotiusoktorio.popularmovies.utilities.Utility;

import java.util.List;

import butterknife.Bind;
import butterknife.BindDrawable;
import butterknife.BindString;
import butterknife.ButterKnife;

/**
 * Created by Timotius on 2016-03-25.
 */

public class DetailsFragment extends Fragment
        implements FetchMovieDetailsAsync.FetchMovieDetailsCallback, TrailersAdapter.OnTrailerClickListener {

    private static final String ARG_MOVIE = "ARG_MOVIE";

    private Movie mMovie;
    private DetailsAdapter mAdapter;
    private boolean mIsMoviePersisted;
    private ShareActionProvider mShareActionProvider;

    @Bind(R.id.recycler_view) RecyclerView mRecyclerView;
    @BindDrawable(R.drawable.ic_heart_outline) Drawable mFavoriteIcon;
    @BindDrawable(R.drawable.ic_heart) Drawable mFavoriteIconActive;
    @BindString(R.string.string_format_share_trailer) String mShareTrailerFormat;

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
        setHasOptionsMenu(true);

        mMovie = getArguments().getParcelable(ARG_MOVIE);
        mAdapter = new DetailsAdapter(getActivity());
        mIsMoviePersisted = Utility.checkPersistedMovie(getActivity(), mMovie);
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
        new FetchMovieDetailsAsync(getActivity(), this).execute(mMovie);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_fragment_details, menu);

        MenuItem favoriteItem = menu.findItem(R.id.action_favorite);
        favoriteItem.setIcon(mIsMoviePersisted ? mFavoriteIconActive : mFavoriteIcon);

        MenuItem shareItem = menu.findItem(R.id.action_share);
        mShareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(shareItem);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: getActivity().finish(); return true;
            case R.id.action_favorite: toggleFavorite(item); return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @Override
    public void onMovieDetailsFetched(Movie movie) {
        mAdapter.setMovie(movie);
        mAdapter.setOnTrailerClickListener(this);

        List<Trailer> trailers = movie.getTrailers();
        if (trailers != null) {
            String firstTrailerUrl = Constants.YOUTUBE_VIDEO_URL + trailers.get(0).getKey();
            String shareText = String.format(mShareTrailerFormat, mMovie.getTitle(), firstTrailerUrl);
            prepareShareActionProvider(shareText);
        }
    }

    @Override
    public void onTrailerClick(String key) {
        Uri trailerUri = Uri.parse(Constants.YOUTUBE_VIDEO_URL + key);
        Intent intent = new Intent(Intent.ACTION_VIEW, trailerUri);
        if (intent.resolveActivity(getActivity().getPackageManager()) != null) startActivity(intent);
    }

    public long getMovieId() {
        return mMovie.getId();
    }

    private void toggleFavorite(MenuItem item) {
        if (mIsMoviePersisted) {
            Utility.deleteMovieFromDatabase(getActivity(), mMovie);
            Snackbar.make(mRecyclerView, R.string.msg_removed_from_favorites, Snackbar.LENGTH_SHORT).show();
        } else {
            Utility.saveMovieToDatabase(getActivity(), mMovie);
            Snackbar.make(mRecyclerView, R.string.msg_saved_to_favorites, Snackbar.LENGTH_SHORT).show();
        }
        mIsMoviePersisted = !mIsMoviePersisted;
        item.setIcon(mIsMoviePersisted ? mFavoriteIconActive : mFavoriteIcon);
    }

    private void prepareShareActionProvider(String shareText) {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_TEXT, shareText);
        shareIntent.setType("text/plain");
        mShareActionProvider.setShareIntent(shareIntent);
    }

}