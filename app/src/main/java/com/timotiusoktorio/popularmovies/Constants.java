package com.timotiusoktorio.popularmovies;

/**
 * Created by Timotius on 2016-03-23.
 */

public class Constants {

    public static final String TMDB_MOVIE_BASE_URL = "http://api.themoviedb.org/3/movie/";
    public static final String TMDB_MOVIE_URL_POPULAR = TMDB_MOVIE_BASE_URL + "popular";
    public static final String TMDB_MOVIE_URL_TOP_RATED = TMDB_MOVIE_BASE_URL + "top_rated";
    public static final String TMDB_POSTER_BASE_URL = "http://image.tmdb.org/t/p/w185/";
    public static final String TMDB_POSTER_BASE_URL_LARGE = "http://image.tmdb.org/t/p/w342/";
    public static final String TMDB_PARAM_API_KEY = "api_key";
    public static final String TMDB_JSON_RESULTS = "results";
    public static final String TMDB_JSON_ID = "id";
    public static final String TMDB_JSON_POSTER_PATH = "poster_path";
    public static final String TMDB_JSON_TITLE = "title";
    public static final String TMDB_JSON_RELEASE_DATE = "release_date";
    public static final String TMDB_JSON_OVERVIEW = "overview";
    public static final String TMDB_JSON_RUNTIME = "runtime";
    public static final String TMDB_JSON_VOTE_AVERAGE = "vote_average";

    public static final String[] SORT_OPTIONS = new String[] {"Popularity", "Top Rated"};
    public static final int SORT_OPTION_POPULAR = 0;
    public static final int SORT_OPTION_TOP_RATED = 1;

}