package com.timotiusoktorio.popularmovies.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.timotiusoktorio.popularmovies.FetchMoviesAsync;
import com.timotiusoktorio.popularmovies.R;
import com.timotiusoktorio.popularmovies.adapters.MoviesAdapter;
import com.timotiusoktorio.popularmovies.models.Movie;

import java.util.ArrayList;

/**
 * Created by Timotius on 2016-03-23.
 */

public class MoviesFragment extends Fragment {

    private MoviesAdapter mAdapter;
    private SwipeRefreshLayout mSwipeRefreshLayout;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        mAdapter = new MoviesAdapter(getActivity(), new ArrayList<Movie>());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_movies, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh_layout);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorAccent);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() { fetchMovies(); }
        });
        mSwipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {mSwipeRefreshLayout.setRefreshing(true);
            }
        });

        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        recyclerView.setAdapter(mAdapter);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        recyclerView.setHasFixedSize(true);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        fetchMovies();
    }

    private void fetchMovies() {
        new FetchMoviesAsync(getActivity(), mAdapter, mSwipeRefreshLayout).execute();
    }

}