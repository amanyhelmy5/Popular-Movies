package com.timotiusoktorio.popularmovies.utilities;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.timotiusoktorio.popularmovies.Constants;
import com.timotiusoktorio.popularmovies.R;

/**
 * Created by Timotius on 2016-03-25.
 */

public class Utility {

    public static int getPreferredSortOption(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getInt(context.getString(R.string.pref_key_sort_options), Constants.SORT_OPTION_POPULAR);
    }

    public static void savePreferredSortOption(Context context, int value) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        preferences.edit().putInt(context.getString(R.string.pref_key_sort_options), value).apply();
    }

}