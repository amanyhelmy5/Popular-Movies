package com.timotiusoktorio.popularmovies;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import com.timotiusoktorio.popularmovies.models.Movie;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Timotius on 2016-03-25.
 */

public class FetchMovieDetailsAsync extends AsyncTask<Long, Void, Movie> {

    private static final String LOG_TAG = FetchMovieDetailsAsync.class.getSimpleName();

    private OnMovieFetchedListener mOnMovieFetchedListener;

    public FetchMovieDetailsAsync(OnMovieFetchedListener onMovieFetchedListener) {
        mOnMovieFetchedListener = onMovieFetchedListener;
    }

    @Override
    protected Movie doInBackground(Long... params) {
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        Movie movie = null;

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
            movie = getMovieFromJsonString(resultJsonString);
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

        return movie;
    }

    @Override
    protected void onPostExecute(Movie movie) {
        super.onPostExecute(movie);
        mOnMovieFetchedListener.onMovieFetched(movie);
    }

    private Movie getMovieFromJsonString(String jsonString) throws JSONException {
        JSONObject root = new JSONObject(jsonString);
        long id = root.getLong(Constants.TMDB_JSON_ID);
        String posterPath = root.getString(Constants.TMDB_JSON_POSTER_PATH);
        String title = root.getString(Constants.TMDB_JSON_TITLE);
        String releaseDate = root.getString(Constants.TMDB_JSON_RELEASE_DATE);
        String overview = root.getString(Constants.TMDB_JSON_OVERVIEW);
        int runtime = root.getInt(Constants.TMDB_JSON_RUNTIME);
        double voteAverage = root.getDouble(Constants.TMDB_JSON_VOTE_AVERAGE);
        return new Movie(id, posterPath, title, releaseDate, overview, runtime, voteAverage);
    }

    public interface OnMovieFetchedListener {
        void onMovieFetched(Movie movie);
    }

}