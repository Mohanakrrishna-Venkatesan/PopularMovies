package com.asura.popularmovies;

import android.annotation.SuppressLint;
import android.content.AsyncTaskLoader;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;

import com.asura.popularmovies.data.FavoriteMovie;
import com.asura.popularmovies.data.Movie;
import com.asura.popularmovies.data.MovieListResponse;
import com.asura.popularmovies.providers.FavoriteMoviesContract;
import com.asura.popularmovies.utils.NetworkUtils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.asura.popularmovies.providers.FavoriteMoviesContract.FavoriteMovieEntry.COLUMN_MOVIE_ID;
import static com.asura.popularmovies.providers.FavoriteMoviesContract.FavoriteMovieEntry.COLUMN_MOVIE_LANGUAGE;
import static com.asura.popularmovies.providers.FavoriteMoviesContract.FavoriteMovieEntry.COLUMN_MOVIE_OVERVIEW;
import static com.asura.popularmovies.providers.FavoriteMoviesContract.FavoriteMovieEntry.COLUMN_MOVIE_POPULARITY;
import static com.asura.popularmovies.providers.FavoriteMoviesContract.FavoriteMovieEntry.COLUMN_MOVIE_RELEASE_DATE;
import static com.asura.popularmovies.providers.FavoriteMoviesContract.FavoriteMovieEntry.COLUMN_MOVIE_THUMBNAIL;
import static com.asura.popularmovies.providers.FavoriteMoviesContract.FavoriteMovieEntry.COLUMN_MOVIE_TITLE;
import static com.asura.popularmovies.providers.FavoriteMoviesContract.FavoriteMovieEntry.COLUMN_MOVIE_VOTING_AVERAGE;
import static com.asura.popularmovies.providers.FavoriteMoviesContract.FavoriteMovieEntry.CONTENT_URI;

public class MovieListQueryTaskLoader extends AsyncTaskLoader<List<Movie>> {

    final static String TAG = "MovieListQueryTaskLoader";

    public final static int POPULAR = 0;
    public final static int TOP_RATED = 1;
    public final static int FAVORITES = 2;

    //Default set to show popular movies list
    private int mLoaderOption;

    private List<Movie> mMovieList;

    public MovieListQueryTaskLoader(Context context, int option) {
        super(context);
        mLoaderOption = option;
    }

    @Override
    protected void onStartLoading() {
        if (mMovieList != null) {
            deliverResult(mMovieList);
        } else {
            forceLoad();
        }
    }

    @SuppressLint("LongLogTag")
    @Override
    public List<Movie> loadInBackground() {
        String response = "";
        Movie[] movieList;
        Gson gson = new GsonBuilder().create();
        Log.d(TAG, response);
        switch (mLoaderOption) {
            case POPULAR:
                response = NetworkUtils.getPopularMoviesList();
                Log.d(TAG, response);
                movieList = gson.fromJson(response, MovieListResponse.class).getResults();
                break;
            case TOP_RATED:
                response = NetworkUtils.getTopRatedMoviesList();
                Log.d(TAG, response);
                movieList = gson.fromJson(response, MovieListResponse.class).getResults();
                break;
            case FAVORITES:
                return loadFavoriteMovies();
            default:
                Log.i(TAG, "We currently looking only for Top Rated or popular movies");
                return new ArrayList<>();
        }
        mMovieList = Arrays.asList(movieList);
        return mMovieList;
    }

    private List<Movie> loadFavoriteMovies() {
        List<Movie> movies = new ArrayList<>();
        Cursor cursor = getContext().getContentResolver().query(CONTENT_URI,
                null,
                null,
                null,
                COLUMN_MOVIE_ID);

        if(cursor.moveToFirst()){
            do{
                FavoriteMovie movie = new FavoriteMovie();
                movie.setId(cursor.getString(cursor.getColumnIndex(COLUMN_MOVIE_ID)));
                movie.setOriginal_title(cursor.getString(cursor.getColumnIndex(COLUMN_MOVIE_TITLE)));
                movie.setLanguage(cursor.getString(cursor.getColumnIndex(COLUMN_MOVIE_LANGUAGE)));
                movie.setReleaseDate(cursor.getString(cursor.getColumnIndex(COLUMN_MOVIE_RELEASE_DATE)));
                movie.setVoteAverage(cursor.getDouble(cursor.getColumnIndex(COLUMN_MOVIE_VOTING_AVERAGE)));
                movie.setPopularity(cursor.getDouble(cursor.getColumnIndex(COLUMN_MOVIE_POPULARITY)));
                movie.setOverview(cursor.getString(cursor.getColumnIndex(COLUMN_MOVIE_OVERVIEW)));
                movie.setImagePath(getBitmap(cursor.getBlob(cursor.getColumnIndex(COLUMN_MOVIE_THUMBNAIL))));
                movies.add(movie);
            }while (cursor.moveToNext());
        }
        return movies;
    }

    private Bitmap getBitmap(byte[] byteArray){
        return BitmapFactory.decodeByteArray(byteArray,0,byteArray.length);
    }
}
