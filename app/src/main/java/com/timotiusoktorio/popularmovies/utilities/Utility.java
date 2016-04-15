package com.timotiusoktorio.popularmovies.utilities;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.util.Log;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import com.timotiusoktorio.popularmovies.Constants;
import com.timotiusoktorio.popularmovies.R;
import com.timotiusoktorio.popularmovies.data.MovieContract;
import com.timotiusoktorio.popularmovies.models.Movie;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by Timotius on 2016-03-25.
 */

public class Utility {

    private static final String LOG_TAG = Utility.class.getSimpleName();
    private static final int TOTAL_COLUMNS_LARGE = 1;
    private static final int TOTAL_COLUMNS_PORT = 2;
    private static final int TOTAL_COLUMNS_LAND = 3;

    public static int getTotalColumns(Context context) {
        Configuration config = context.getResources().getConfiguration();
        if (config.orientation == Configuration.ORIENTATION_PORTRAIT) {
            if (config.screenWidthDp >= 600) return TOTAL_COLUMNS_LARGE;
            else return TOTAL_COLUMNS_PORT;
        } else return TOTAL_COLUMNS_LAND;
    }

    public static int getPreferredSortOption(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getInt(context.getString(R.string.pref_key_sort_options), Constants.SORT_OPTION_POPULAR);
    }

    public static void savePreferredSortOption(Context context, int value) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        preferences.edit().putInt(context.getString(R.string.pref_key_sort_options), value).apply();
    }

    public static boolean checkPersistedMovie(Context context, Movie movie) {
        boolean isMoviePersisted;
        Uri movieUri = MovieContract.MovieEntry.CONTENT_URI;
        String[] projection = new String[] { MovieContract.MovieEntry.COLUMN_TMDB_ID };
        String selection = MovieContract.MovieEntry.COLUMN_TMDB_ID + " = ?";
        String[] selectionArgs = new String[] { String.valueOf(movie.getId()) };

        Cursor cursor = context.getContentResolver().query(movieUri, projection, selection, selectionArgs, null);
        if (cursor == null) return false;
        try { isMoviePersisted = cursor.moveToFirst(); }
        finally { cursor.close(); }
        return isMoviePersisted;
    }

    public static void saveMovieToDatabase(final Context context, final Movie movie) {
        ContentValues movieCV = new ContentValues();
        movieCV.put(MovieContract.MovieEntry.COLUMN_TMDB_ID, movie.getId());
        movieCV.put(MovieContract.MovieEntry.COLUMN_POSTER_PATH, movie.getPosterPath());
        movieCV.put(MovieContract.MovieEntry.COLUMN_TITLE, movie.getTitle());
        movieCV.put(MovieContract.MovieEntry.COLUMN_RELEASE_DATE, movie.getReleaseDate());
        movieCV.put(MovieContract.MovieEntry.COLUMN_OVERVIEW, movie.getOverview());
        movieCV.put(MovieContract.MovieEntry.COLUMN_VOTE_AVERAGE, movie.getVoteAverage());
        context.getContentResolver().insert(MovieContract.MovieEntry.CONTENT_URI, movieCV);

        final String posterPath = movie.getPosterPath();
        final String posterUrl = Constants.TMDB_MOVIE_POSTER_URL + posterPath;
        Picasso.with(context).load(posterUrl).into(new Target() {
            @Override
            public void onBitmapLoaded(final Bitmap bitmap, Picasso.LoadedFrom from) {
                new Thread(new Runnable() {
                    @Override
                    public void run() { saveImageToDeviceStorage(context, bitmap, posterPath); }
                }).run();
            }

            @Override
            public void onBitmapFailed(Drawable errorDrawable) {

            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {

            }
        });
    }

    public static void deleteMovieFromDatabase(Context context, Movie movie) {
        String movieWhere = MovieContract.MovieEntry.COLUMN_TMDB_ID + " = ?";
        String[] whereArgs = new String[] { String.valueOf(movie.getId()) };
        context.getContentResolver().delete(MovieContract.MovieEntry.CONTENT_URI, movieWhere, whereArgs);
        deleteImageFromDeviceStorage(context, movie.getPosterPath());
    }

    public static void saveImageToDeviceStorage(Context context, Bitmap bitmap, String name) {
        if (isExternalStorageWritable()) {
            File file = new File(context.getExternalFilesDir(null), name);
            try {
                FileOutputStream outputStream = new FileOutputStream(file);
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
                outputStream.close();
                Log.d(LOG_TAG, "Image is successfully downloaded to " + file.getAbsolutePath());
            } catch (IOException e) {
                Log.e(LOG_TAG, "Failed to save movie poster to device!", e);
            }
        }
    }

    public static void deleteImageFromDeviceStorage(Context context, String name) {
        if (isExternalStorageReadable()) {
            File file = new File(context.getExternalFilesDir(null), name);
            if (file.exists()) file.delete();
            if (!file.exists()) Log.d(LOG_TAG, "Image is successfully deleted from " + file.getAbsolutePath());
        }
    }

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }

    public static boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        return (Environment.MEDIA_MOUNTED.equals(state));
    }

    public static boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();
        return (Environment.MEDIA_MOUNTED.equals(state) || Environment.MEDIA_MOUNTED_READ_ONLY.equals(state));
    }

}