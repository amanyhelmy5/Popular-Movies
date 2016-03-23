package com.timotiusoktorio.popularmovies;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String tmdbApiKey = BuildConfig.TMDB_API_KEY;
        System.out.println("TMDB Api Key: " + tmdbApiKey);
    }

}