package com.timotiusoktorio.popularmovies.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.timotiusoktorio.popularmovies.R;
import com.timotiusoktorio.popularmovies.fragments.DetailsFragment;
import com.timotiusoktorio.popularmovies.models.Movie;

/**
 * Created by Timotius on 2016-03-25.
 */

public class DetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        if (savedInstanceState == null) {
            Movie movie = getIntent().getParcelableExtra(MainActivity.INTENT_EXTRA_MOVIE);
            DetailsFragment detailsFragment = DetailsFragment.newInstance(movie);
            getSupportFragmentManager().beginTransaction().add(R.id.frame_layout, detailsFragment).commit();
        }
    }

}