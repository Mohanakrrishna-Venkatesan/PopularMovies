package com.asura.popularmovies;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.method.ScrollingMovementMethod;
import android.widget.ImageView;
import android.widget.TextView;

import com.asura.popularmovies.data.Movie;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MovieDetailActivity extends AppCompatActivity {

    private final static String EXTRA_MOVIE = "EXTRA_MOVIE";

    @BindView(R.id.movie_title)
    public TextView mTitleTextView;

    @BindView(R.id.movie_Thumbnail)
    public ImageView mImageView;

    @BindView(R.id.movie_release_data)
    public TextView mReleaseDateTextView;

    @BindView(R.id.movie_rating)
    public TextView mRating;

    @BindView(R.id.movie_overview)
    public TextView mOverview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Movie movie = (Movie) getIntent().getSerializableExtra(EXTRA_MOVIE);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ButterKnife.bind(this);

        mTitleTextView.setText(movie.getOriginal_title());

        Picasso.with(this).load(movie.getPoster_path())
                .into(mImageView);

        mReleaseDateTextView.setText(movie.getReleaseDate());

        mRating.setText(movie.getVoteAverage() + "/10");

        mOverview.setText(movie.getOverview());
        mOverview.setMovementMethod(new ScrollingMovementMethod());
    }

}
