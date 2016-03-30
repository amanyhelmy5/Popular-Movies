package com.timotiusoktorio.popularmovies;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Timotius on 2016-03-25.
 */

public class FetchMovieDetailsAsync extends AsyncTask<Long, Void, Void> {

    private static final String LOG_TAG = FetchMovieDetailsAsync.class.getSimpleName();

    @Override
    protected Void doInBackground(Long... params) {
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        try {
            String movieUrl = Constants.TMDB_MOVIE_BASE_URL + params[0];
            Uri uri = Uri.parse(movieUrl).buildUpon().appendQueryParameter(Constants.TMDB_PARAM_API_KEY, BuildConfig.TMDB_API_KEY).build();
            URL url = new URL(uri.toString());
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            InputStream inputStream = urlConnection.getInputStream();
            if (inputStream == null) return null;
            reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            StringBuilder builder = new StringBuilder();
            while ((line = reader.readLine()) != null) builder.append(line).append("\n");

            if (builder.length() == 0) return null;
            String resultJsonString = builder.toString();
        }
        catch (IOException e) {
            Log.e(LOG_TAG, e.getMessage(), e);
        }
        finally {
            if (urlConnection != null) urlConnection.disconnect();
            if (reader != null) {
                try { reader.close(); }
                catch (IOException e) { Log.e(LOG_TAG, e.getMessage(), e); }
            }
        }

        return null;
    }

}