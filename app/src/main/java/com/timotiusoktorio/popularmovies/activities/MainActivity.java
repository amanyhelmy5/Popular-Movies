package com.timotiusoktorio.popularmovies.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.timotiusoktorio.popularmovies.R;
import com.timotiusoktorio.popularmovies.adapters.MoviesAdapter;
import com.timotiusoktorio.popularmovies.fragments.DetailsFragment;
import com.timotiusoktorio.popularmovies.fragments.MoviesFragment;
import com.timotiusoktorio.popularmovies.models.Movie;

public class MainActivity extends AppCompatActivity
        implements MoviesFragment.OnSortOptionChangeListener, MoviesAdapter.OnItemClickListener {

    public static final String INTENT_EXTRA_MOVIE = "INTENT_EXTRA_MOVIE";
    public static final String TAG_DETAILS_FRAGMENT = DetailsFragment.class.getSimpleName();

    private boolean mUseTwoPaneLayout = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (findViewById(R.id.frame_layout) != null) mUseTwoPaneLayout = true;
    }

    @Override
    public void onSortOptionChange() {
        DetailsFragment detailsFragment = (DetailsFragment) getSupportFragmentManager().findFragmentByTag(TAG_DETAILS_FRAGMENT);
        if (detailsFragment != null) getSupportFragmentManager().beginTransaction().remove(detailsFragment).commit();
    }

    @Override
    public void onItemClick(Movie movie) {
        if (mUseTwoPaneLayout) {
            // Only instantiate new DetailsFragment if the user clicks a different movie.
            // This is to avoid expensive network operations to fetch the same movie data.
            if (movie.getId() != getClickedMovieId()) {
                DetailsFragment detailsFragment = DetailsFragment.newInstance(movie);
                getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout, detailsFragment, TAG_DETAILS_FRAGMENT).commit();
            }
        } else {
            Intent intent = new Intent(this, DetailsActivity.class);
            intent.putExtra(INTENT_EXTRA_MOVIE, movie);
            startActivity(intent);
        }
    }

    private long getClickedMovieId() {
        DetailsFragment detailsFragment = (DetailsFragment) getSupportFragmentManager().findFragmentByTag(TAG_DETAILS_FRAGMENT);
        return (detailsFragment != null) ? detailsFragment.getMovieId() : -1;
    }

}