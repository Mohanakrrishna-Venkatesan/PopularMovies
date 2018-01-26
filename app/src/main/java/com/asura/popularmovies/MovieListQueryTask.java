package com.asura.popularmovies;

import android.os.AsyncTask;
import android.util.Log;

import com.asura.popularmovies.data.Movie;
import com.asura.popularmovies.data.MovieListResponse;
import com.asura.popularmovies.utils.NetworkUtils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MovieListQueryTask extends AsyncTask<String, Void, List<Movie>> {

    final static String TAG = "MovieListQueryTask";

    public final static String POPULAR = "POPULAR";
    public final static String TOP_RATED = "TOP_RATED";

    private OnResponseListener mListener;

    public interface OnResponseListener {
        void showProgressBar();

        void disableProgressBar();

        void onResponse(List<Movie> movieList);
    }

    public MovieListQueryTask(OnResponseListener listener) {
        this.mListener = listener;
    }

    @Override
    protected void onPreExecute() {
        mListener.showProgressBar();
    }

    @Override
    protected List<Movie> doInBackground(String... strings) {
        String response = "";
        Movie[] movieList;
        Gson gson = new GsonBuilder().create();
        Log.i(TAG, response);
        switch (strings[0]) {
            case POPULAR:
                response = NetworkUtils.getPopularMoviesList();
                Log.i(TAG, response);
                movieList = gson.fromJson(response, MovieListResponse.class).getResults();
                break;
            case TOP_RATED:
                response = NetworkUtils.getTopRatedMoviesList();
                movieList = gson.fromJson(response, MovieListResponse.class).getResults();
                break;
            default:
                Log.i(TAG, "We currently looking only for Top Rated or popular movies");
                return new ArrayList<>();
        }
        return Arrays.asList(movieList);
    }

    @Override
    protected void onPostExecute(List<Movie> movieList) {
        mListener.disableProgressBar();
        mListener.onResponse(movieList);
    }
}
