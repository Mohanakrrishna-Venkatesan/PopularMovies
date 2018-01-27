package com.asura.popularmovies.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

public class NetworkUtils {

    final static String TAG = "NetworkUtils";

    public final static String BASE_URL = "http://image.tmdb.org/t/p/";

    public final static String MOVIE_DB_BASE_URL = "http://api.themoviedb.org/3/discover/movie";

    public final static String W92 = "w92";
    public final static String W154 = "w154";
    public final static String W185 = "w185";
    public final static String W342 = "w342";
    public final static String W500 = "w500";
    public final static String W780 = "w780";
    public final static String ORIGINAL = "original";

    private final static String MOVIE_DB_API_KEY = "2311efeb2d1f5182aaf24069f0e27e3b";
    private final static String PARAM_API_KEY = "api_key";
    private final static String PARAM_SORT_BY = "sort_by";

    private final static String POPULARITY_DESC = "popularity.desc";
    private final static String TOP_RATED = "vote_average.desc";

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
        Uri uri = Uri.parse(MOVIE_DB_BASE_URL).buildUpon()
                .appendQueryParameter(PARAM_SORT_BY, POPULARITY_DESC)
                .appendQueryParameter(PARAM_API_KEY, MOVIE_DB_API_KEY).build();
        URL url = null;
        try {
            url = new URL(uri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return getResponseFromHttpUrl(url);
    }

    public static String getTopRatedMoviesList() {
        Uri uri = Uri.parse(MOVIE_DB_BASE_URL).buildUpon()
                .appendQueryParameter(PARAM_SORT_BY, TOP_RATED)
                .appendQueryParameter(PARAM_API_KEY, MOVIE_DB_API_KEY).build();
        URL url = null;
        try {
            url = new URL(uri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return getResponseFromHttpUrl(url);
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

}
