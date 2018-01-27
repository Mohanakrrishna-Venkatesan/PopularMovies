package com.asura.popularmovies;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.method.ScrollingMovementMethod;
import android.widget.ImageView;
import android.widget.TextView;

import com.asura.popularmovies.data.Movie;
import com.squareup.picasso.Picasso;

public class MovieDetailActivity extends AppCompatActivity {

    private final static String EXTRA_MOVIE = "EXTRA_MOVIE";

    private TextView mTitleTextView;
    private ImageView mImageView;
    private TextView mReleaseDateTextView;
    private TextView mRating;
    private TextView mOverview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Movie movie = (Movie) getIntent().getSerializableExtra(EXTRA_MOVIE);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mTitleTextView = findViewById(R.id.movie_title);
        mTitleTextView.setText(movie.getOriginal_title());

        mImageView = findViewById(R.id.movie_Thumbnail);
        Picasso.with(this).load(movie.getPoster_path())
                .into(mImageView);

        mReleaseDateTextView = findViewById(R.id.movie_release_data);
        mReleaseDateTextView.setText(movie.getReleaseDate());

        mRating = findViewById(R.id.movie_rating);
        mRating.setText(movie.getVoteAverage() + "/10");

        mOverview = findViewById(R.id.movie_overview);
        mOverview.setText(movie.getOverview());
        mOverview.setMovementMethod(new ScrollingMovementMethod());
    }

}
