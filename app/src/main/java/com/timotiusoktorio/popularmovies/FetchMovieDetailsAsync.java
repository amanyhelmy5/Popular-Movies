package com.timotiusoktorio.popularmovies;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import com.timotiusoktorio.popularmovies.adapters.DetailsAdapter;
import com.timotiusoktorio.popularmovies.models.Movie;
import com.timotiusoktorio.popularmovies.models.Review;
import com.timotiusoktorio.popularmovies.models.Trailer;

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

import static com.timotiusoktorio.popularmovies.Constants.*;

/**
 * Created by Timotius on 2016-03-25.
 */

public class FetchMovieDetailsAsync extends AsyncTask<Movie, Void, Movie> {

    private static final String LOG_TAG = FetchMovieDetailsAsync.class.getSimpleName();

    private DetailsAdapter mAdapter;

    public FetchMovieDetailsAsync(DetailsAdapter adapter) {
        mAdapter = adapter;
    }

    @Override
    protected Movie doInBackground(Movie... params) {
        Movie movie = params[0];

        String trailersJsonString = fetchRawMovieData(TMDB_MOVIE_URL + movie.getId() + TMDB_VIDEOS_URL_SUFFIX);
        movie.setTrailers(getTrailersFromJsonString(trailersJsonString));

        String reviewsJsonString = fetchRawMovieData(TMDB_MOVIE_URL + movie.getId() + TMDB_REVIEWS_URL_SUFFIX);
        movie.setReviews(getReviewsFromJsonString(reviewsJsonString));

        return movie;
    }

    @Override
    protected void onPostExecute(Movie movie) {
        super.onPostExecute(movie);
        mAdapter.setMovie(movie);
        mAdapter.notifyDataSetChanged();
    }

    private String fetchRawMovieData(String movieUrl) {
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        String resultJsonString = null;

        try {
            Uri uri = Uri.parse(movieUrl).buildUpon().appendQueryParameter(TMDB_PARAM_API_KEY, BuildConfig.TMDB_API_KEY).build();
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

        return resultJsonString;
    }

    private List<Trailer> getTrailersFromJsonString(String jsonString) {
        List<Trailer> trailers = new ArrayList<>();
        try {
            JSONObject root = new JSONObject(jsonString);
            JSONArray results = root.getJSONArray(TMDB_JSON_RESULTS);
            for (int i = 0; i < results.length(); i++) {
                JSONObject object = results.getJSONObject(i);
                String key = object.getString(TMDB_JSON_KEY);
                String name = object.getString(TMDB_JSON_NAME);
                String type = object.getString(TMDB_JSON_TYPE);
                Trailer trailer = new Trailer(key, name, type);
                trailers.add(trailer);
            }
            Log.d(LOG_TAG, "Total trailers created: " + trailers.size());
        }
        catch (JSONException e) {
            Log.e(LOG_TAG, e.getMessage(), e);
        }
        return trailers;
    }

    private List<Review> getReviewsFromJsonString(String jsonString) {
        List<Review> reviews = new ArrayList<>();
        try {
            JSONObject root = new JSONObject(jsonString);
            JSONArray results = root.getJSONArray(TMDB_JSON_RESULTS);
            for (int i = 0; i < results.length(); i++) {
                JSONObject object = results.getJSONObject(i);
                String author = object.getString(TMDB_JSON_AUTHOR);
                String content = object.getString(TMDB_JSON_CONTENT);
                String url = object.getString(TMDB_JSON_URL);
                Review review = new Review(author, content, url);
                reviews.add(review);
            }
            Log.d(LOG_TAG, "Total reviews created: " + reviews.size());
        }
        catch (JSONException e) {
            Log.e(LOG_TAG, e.getMessage(), e);
        }
        return reviews;
    }

}