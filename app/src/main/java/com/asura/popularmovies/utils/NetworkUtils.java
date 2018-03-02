package com.asura.popularmovies.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.util.Log;

import com.asura.popularmovies.data.MovieReviews;
import com.asura.popularmovies.data.MovieTrailers;
import com.asura.popularmovies.data.Review;
import com.asura.popularmovies.data.Trailer;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class NetworkUtils {

    final static String TAG = "NetworkUtils";

    public final static String BASE_URL = "http://image.tmdb.org/t/p/";

    private static final String YOUTUBE_URL = "https://www.youtube.com/";
    private static final String YOUTUBE_WATCH = "watch";
    private static final String YOUTUBE_PARAM = "v";

    public final static String MOVIE_DB_BASE_URL = "http://api.themoviedb.org/3/movie";

    public final static String W92 = "w92";
    public final static String W154 = "w154";
    public final static String W185 = "w185";
    public final static String W342 = "w342";
    public final static String W500 = "w500";
    public final static String W780 = "w780";
    public final static String ORIGINAL = "original";

    //TODO - Users need to add their Movie DB API key in the following variable
    private final static String MOVIE_DB_API_KEY = "";
    private final static String PARAM_API_KEY = "api_key";

    private final static String POPULAR = "popular";
    private final static String TOP_RATED = "top_rated";
    private final static String TRAILERS = "videos";
    private final static String REVIEWS = "reviews";

    /**
     * @param imagePath - the relative path of the image
     * @return - returns the absolute path of the image
     */
    public static String getURLString(String imagePath) {
        return getURLString(imagePath, W185);
    }

    /**
     * @param imagePath - the relative path of the image
     * @param size      - should be one among W92, WW92, W154, W185, W342, W500, W780 or ORIGINAL
     * @return - returns the absolute path of the image
     */
    //Here the parameter size
    public static String getURLString(String imagePath, String size) {
        return BASE_URL + size + imagePath;
    }


    public static String getPopularMoviesList() {
        Uri uri = Uri.parse(MOVIE_DB_BASE_URL)
                .buildUpon()
                .appendPath(POPULAR)
                .appendQueryParameter(PARAM_API_KEY, MOVIE_DB_API_KEY)
                .build();
        URL url = null;
        try {
            url = new URL(uri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return getResponseFromHttpUrl(url);
    }

    public static String getTopRatedMoviesList() {
        Uri uri = Uri.parse(MOVIE_DB_BASE_URL)
                .buildUpon()
                .appendPath(TOP_RATED)
                .appendQueryParameter(PARAM_API_KEY, MOVIE_DB_API_KEY)
                .build();
        URL url = null;
        try {
            url = new URL(uri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return getResponseFromHttpUrl(url);
    }

    public static List<String> getMovieTrailers(String movieId){
        List<String> trailersList = new ArrayList<>();
        Uri uri = Uri.parse(MOVIE_DB_BASE_URL)
                .buildUpon()
                .appendPath(movieId)
                .appendPath(TRAILERS)
                .appendQueryParameter(PARAM_API_KEY, MOVIE_DB_API_KEY)
                .build();
        URL url = null;
        try {
            url = new URL(uri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        Gson gson = new GsonBuilder().create();
        Trailer[] trailers = gson.fromJson(getResponseFromHttpUrl(url), MovieTrailers.class).getResults();
        for (Trailer trailer : trailers){
            Uri trailerUri = Uri.parse(YOUTUBE_URL)
                    .buildUpon()
                    .appendPath(YOUTUBE_WATCH)
                    .appendQueryParameter(YOUTUBE_PARAM, trailer.getKey())
                    .build();
            try {
                URL trailerUrl = new URL(trailerUri.toString());
                trailersList.add(trailerUrl.toString());
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        }
        return trailersList;
    }

    public static String getResponseFromHttpUrl(URL url) {
        HttpURLConnection urlConnection = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        } catch (IOException exception) {
            Log.e(TAG, exception.getMessage());
            exception.printStackTrace();
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }
        return "No Response";
    }

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    public static List<String> getMovieReviews(String movieId) {
        List<String> reviewsList = new ArrayList<>();
        Uri uri = Uri.parse(MOVIE_DB_BASE_URL)
                .buildUpon()
                .appendPath(movieId)
                .appendPath(REVIEWS)
                .appendQueryParameter(PARAM_API_KEY, MOVIE_DB_API_KEY)
                .build();
        URL url = null;
        try {
            url = new URL(uri.toString());
            Log.i(TAG,url.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        Gson gson = new GsonBuilder().create();
        Review[] reviews = gson.fromJson(getResponseFromHttpUrl(url), MovieReviews.class).getResults();
        Log.i(TAG,getResponseFromHttpUrl(url));
        for (Review review : reviews){
            reviewsList.add(review.getUrl());
        }
        return reviewsList;
    }
}
