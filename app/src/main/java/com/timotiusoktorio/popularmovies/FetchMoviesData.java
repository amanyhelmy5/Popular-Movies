package com.timotiusoktorio.popularmovies;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Timotius on 2016-03-23.
 */

public class FetchMoviesData extends AsyncTask<Void, Void, Void> {

    private static final String LOG_TAG = FetchMoviesData.class.getSimpleName();

    private Context mContext;

    public FetchMoviesData(Context context) {
        mContext = context;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected Void doInBackground(Void... params) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(mContext);
        String prefSortOption = preferences.getString(mContext.getString(R.string.pref_key_sort_options), mContext.getString(R.string.pref_value_sort_options_popular));
        String moviesUrl = Constants.TMDB_BASE_URL + prefSortOption;

        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        String resultJsonString;

        try {
            Uri uri = Uri.parse(moviesUrl).buildUpon().appendQueryParameter(Constants.PARAM_API_KEY, BuildConfig.TMDB_API_KEY).build();
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
            resultJsonString = builder.toString();
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