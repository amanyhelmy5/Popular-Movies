package com.timotiusoktorio.popularmovies.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.timotiusoktorio.popularmovies.R;
import com.timotiusoktorio.popularmovies.fragments.DetailsFragment;
import com.timotiusoktorio.popularmovies.fragments.MoviesFragment;

/**
 * Created by Timotius on 2016-03-25.
 */

public class DetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        if (savedInstanceState == null) {
            long movieId = getIntent().getLongExtra(MoviesFragment.INTENT_EXTRA_MOVIE_ID, -1);
            DetailsFragment detailsFragment = DetailsFragment.newInstance(movieId);
            getSupportFragmentManager().beginTransaction().add(R.id.frame_layout, detailsFragment).commit();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) finish();
        return true;
    }

}