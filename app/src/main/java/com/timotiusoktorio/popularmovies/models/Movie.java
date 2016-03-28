package com.timotiusoktorio.popularmovies.models;

/**
 * Created by Timotius on 2016-03-24.
 */

public class Movie {

    long id;
    String posterPath;
    String title;
    String releaseDate;
    String overview;
    int runtime;
    double voteAverage;

    public Movie() {}

    public Movie(long id, String posterPath) {
        this.id = id;
        this.posterPath = posterPath;
    }

    public Movie(long id, String posterPath, String title, String releaseDate, String overview, int runtime, double voteAverage) {
        this.id = id;
        this.posterPath = posterPath;
        this.title = title;
        this.releaseDate = releaseDate;
        this.overview = overview;
        this.runtime = runtime;
        this.voteAverage = voteAverage;
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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public int getRuntime() {
        return runtime;
    }

    public void setRuntime(int runtime) {
        this.runtime = runtime;
    }

    public double getVoteAverage() {
        return voteAverage;
    }

    public void setVoteAverage(double voteAverage) {
        this.voteAverage = voteAverage;
    }

}