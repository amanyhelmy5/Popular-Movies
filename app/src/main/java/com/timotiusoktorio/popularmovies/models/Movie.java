package com.timotiusoktorio.popularmovies.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Timotius on 2016-03-24.
 */

public class Movie implements Parcelable {

    long id;
    String posterPath;

    public Movie() {}

    public Movie(long id, String posterPath) {
        this.id = id;
        this.posterPath = posterPath;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.id);
        dest.writeString(this.posterPath);
    }

    protected Movie(Parcel in) {
        this.id = in.readLong();
        this.posterPath = in.readString();
    }

    public static final Parcelable.Creator<Movie> CREATOR = new Parcelable.Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel source) {
            return new Movie(source);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };

}