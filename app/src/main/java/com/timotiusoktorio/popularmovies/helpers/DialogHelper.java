package com.timotiusoktorio.popularmovies.helpers;

import android.content.Context;

import com.afollestad.materialdialogs.MaterialDialog;
import com.timotiusoktorio.popularmovies.Constants;
import com.timotiusoktorio.popularmovies.R;

import static com.afollestad.materialdialogs.MaterialDialog.ListCallbackSingleChoice;

/**
 * Created by Timotius on 2016-03-25.
 */

public class DialogHelper {

    public static void instantiateSortPickerDialog(Context context, int selectedIndex, ListCallbackSingleChoice callback) {
        MaterialDialog.Builder builder = new MaterialDialog.Builder(context);
        builder.title(R.string.dialog_title_sort_picker);
        builder.items(Constants.SORT_OPTIONS);
        builder.itemsCallbackSingleChoice(selectedIndex, callback);
        builder.show();
    }

}