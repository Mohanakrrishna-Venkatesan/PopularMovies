package com.asura.popularmovies.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.asura.popularmovies.MovieDetailActivity;
import com.asura.popularmovies.R;
import com.asura.popularmovies.data.Movie;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MovieListAdapter extends RecyclerView.Adapter<MovieListAdapter.MovieCardHolder> {

    final static String TAG = "MovieListAdapter";

    private Context mContext;
    private List<Movie> mMovieList = new ArrayList<>();

    private final static String EXTRA_MOVIE = "EXTRA_MOVIE";

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
        holder.movieVoteAverage.setText(movie.getVoteAverage() + "/10");
        holder.popularity.setText(movie.getPopularity() + "");

        holder.position = position;
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

        @BindView(R.id.movie_Thumbnail)
        public ImageView moviePoster;

        @BindView(R.id.movie_language)
        public TextView movieLanguage;

        @BindView(R.id.vote_average)
        public TextView movieVoteAverage;

        @BindView(R.id.popularity)
        public TextView popularity;

        public int position;

        public MovieCardHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);
        }

        @OnClick(R.id.movie_Thumbnail)
        public void onClick() {
            Intent intent = new Intent(mContext, MovieDetailActivity.class);
            intent.putExtra(EXTRA_MOVIE, mMovieList.get(position));
            mContext.startActivity(intent);
        }
    }
}
