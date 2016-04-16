package com.timotiusoktorio.popularmovies.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.TextView;

import com.timotiusoktorio.popularmovies.BuildConfig;
import com.timotiusoktorio.popularmovies.R;

import butterknife.Bind;
import butterknife.BindString;
import butterknife.ButterKnife;

/**
 * Created by Timotius on 2016-04-16.
 */

public class AboutActivity extends AppCompatActivity {

    @Bind(R.id.app_version_text_view) TextView mAppVersionTextView;
    @BindString(R.string.string_format_app_version) String mAppVersionFormat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        ButterKnife.bind(this);
        mAppVersionTextView.setText(String.format(mAppVersionFormat, BuildConfig.VERSION_NAME));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) finish();
        return true;
    }

}