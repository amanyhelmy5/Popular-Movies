package com.timotiusoktorio.popularmovies;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;

import com.timotiusoktorio.popularmovies.adapters.MoviesAdapter;
import com.timotiusoktorio.popularmovies.models.Movie;
import com.timotiusoktorio.popularmovies.utilities.Utility;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static com.timotiusoktorio.popularmovies.data.MovieContract.MovieEntry;

/**
 * Created by Timotius on 2016-03-23.
 */

public class FetchMoviesAsync extends AsyncTask<Void, Void, List<Movie>> {

    private static final String LOG_TAG = FetchMoviesAsync.class.getSimpleName();

    private Context mContext;
    private MoviesAdapter mAdapter;
    private SwipeRefreshLayout mSwipeRefreshLayout;

    public FetchMoviesAsync(Context context, MoviesAdapter adapter, SwipeRefreshLayout swipeRefreshLayout) {
        mContext = context;
        mAdapter = adapter;
        mSwipeRefreshLayout = swipeRefreshLayout;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        mSwipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {mSwipeRefreshLayout.setRefreshing(true);
            }
        });
        mAdapter.clear();
    }

    @Override
    protected List<Movie> doInBackground(Void... params) {
        int prefSortOption = Utility.getPreferredSortOption(mContext);
        return (prefSortOption != Constants.SORT_OPTION_FAVORITES) ? fetchMoviesFromAPI() : getMoviesFromDatabase();
    }

    @Override
    protected void onPostExecute(List<Movie> movies) {
        super.onPostExecute(movies);
        if (movies != null && movies.size() > 0) mAdapter.addAll(movies);
        mSwipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {mSwipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    private List<Movie> fetchMoviesFromAPI() {
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        List<Movie> movies = null;

        if (Utility.isNetworkAvailable(mContext)) {
            try {
                String moviesUrl = getMoviesUrl();
                Uri uri = Uri.parse(moviesUrl).buildUpon().appendQueryParameter(Constants.TMDB_PARAM_API_KEY, BuildConfig.TMDB_API_KEY).build();
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
                movies = getMoviesFromJsonString(resultJsonString);
            }
            catch (IOException | JSONException e) {
                Log.e(LOG_TAG, e.getMessage(), e);
            }
            finally {
                if (urlConnection != null) urlConnection.disconnect();
                if (reader != null) {
                    try { reader.close(); }
                    catch (IOException e) { Log.e(LOG_TAG, e.getMessage(), e); }
                }
            }
        }

        return movies;
    }

    private String getMoviesUrl() {
        switch (Utility.getPreferredSortOption(mContext)) {
            case Constants.SORT_OPTION_POPULAR: return Constants.TMDB_MOVIE_URL_POPULAR;
            case Constants.SORT_OPTION_TOP_RATED: return Constants.TMDB_MOVIE_URL_TOP_RATED;
        }
        return null;
    }

    private List<Movie> getMoviesFromJsonString(String jsonString) throws JSONException {
        List<Movie> movies = new ArrayList<>();
        JSONObject root = new JSONObject(jsonString);
        JSONArray results = root.getJSONArray(Constants.TMDB_JSON_RESULTS);
        for (int i = 0; i < results.length(); i++) {
            JSONObject object = results.getJSONObject(i);
            long id = object.getLong(Constants.TMDB_JSON_ID);
            String posterPath = object.getString(Constants.TMDB_JSON_POSTER_PATH);
            String title = object.getString(Constants.TMDB_JSON_TITLE);
            String releaseDate = object.getString(Constants.TMDB_JSON_RELEASE_DATE);
            String overview = object.getString(Constants.TMDB_JSON_OVERVIEW);
            double voteAverage = object.getDouble(Constants.TMDB_JSON_VOTE_AVERAGE);
            Movie movie = new Movie(id, posterPath, title, releaseDate, overview, voteAverage);
            movies.add(movie);
        }
        Log.d(LOG_TAG, "Total movies fetched from API: " + movies.size());
        return movies;
    }

    private List<Movie> getMoviesFromDatabase() {
        List<Movie> movies = new ArrayList<>();
        Cursor cursor = mContext.getContentResolver().query(MovieEntry.CONTENT_URI, null, null, null, MovieEntry._ID + " DESC");
        try {
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    long id = cursor.getLong(cursor.getColumnIndex(MovieEntry.COLUMN_TMDB_ID));
                    String posterPath = cursor.getString(cursor.getColumnIndex(MovieEntry.COLUMN_POSTER_PATH));
                    String title = cursor.getString(cursor.getColumnIndex(MovieEntry.COLUMN_TITLE));
                    String releaseDate = cursor.getString(cursor.getColumnIndex(MovieEntry.COLUMN_RELEASE_DATE));
                    String overview = cursor.getString(cursor.getColumnIndex(MovieEntry.COLUMN_OVERVIEW));
                    double voteAverage = cursor.getDouble(cursor.getColumnIndex(MovieEntry.COLUMN_VOTE_AVERAGE));
                    Movie movie = new Movie(id, posterPath, title, releaseDate, overview, voteAverage);
                    movies.add(movie);
                } while (cursor.moveToNext());
                Log.d(LOG_TAG, "Total movies fetched from database: " + movies.size());
            }
        }
        finally { if (cursor != null) cursor.close(); }
        return movies;
    }

}