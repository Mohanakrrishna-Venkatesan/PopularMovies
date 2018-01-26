package com.asura.popularmovies.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.asura.popularmovies.R;
import com.asura.popularmovies.data.Movie;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class MovieListAdapter extends RecyclerView.Adapter<MovieListAdapter.MovieCardHolder> {

    final static String TAG = "MovieListAdapter";

    private Context mContext;
    private List<Movie> mMovieList = new ArrayList<>();

    public MovieListAdapter(@NonNull Context context) {
        mContext = context;
    }

    @Override
    public MovieCardHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_movie, parent, false);
        return new MovieCardHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MovieCardHolder holder, int position) {
        Movie movie = mMovieList.get(position);
        Log.i(TAG, movie.getPoster_path());
        Picasso.with(mContext).load(movie.getPoster_path())
                .into(holder.moviePoster);
        holder.movieLanguage.setText(movie.getLanguage());
        holder.movieVoteAverage.setText(movie.getVoteAverage() + "");
        holder.popularity.setText(movie.getPopularity() + "");
    }

    @Override
    public int getItemCount() {
        return mMovieList.size();
    }

    public void swapMovieList(List<Movie> movieList) {
        mMovieList.clear();
        mMovieList.addAll(movieList);
        notifyDataSetChanged();
    }

    public class MovieCardHolder extends RecyclerView.ViewHolder {
        public ImageView moviePoster;
        public TextView movieLanguage;
        public TextView movieVoteAverage;
        public TextView popularity;

        public MovieCardHolder(View itemView) {
            super(itemView);
            moviePoster = itemView.findViewById(R.id.movie_Thumbnail);
            movieLanguage = itemView.findViewById(R.id.movie_language);
            movieVoteAverage = itemView.findViewById(R.id.vote_average);
            popularity = itemView.findViewById(R.id.popularity);
        }
    }
}
