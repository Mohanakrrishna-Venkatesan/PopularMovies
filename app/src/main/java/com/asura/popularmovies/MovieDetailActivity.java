package com.asura.popularmovies;

import android.content.ContentValues;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.asura.popularmovies.adapters.ItemAdapter;
import com.asura.popularmovies.data.FavoriteMovie;
import com.asura.popularmovies.data.Movie;
import com.asura.popularmovies.utils.NetworkUtils;
import com.github.ivbaranov.mfb.MaterialFavoriteButton;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.asura.popularmovies.providers.FavoriteMoviesContract.FavoriteMovieEntry;
import static com.asura.popularmovies.providers.FavoriteMoviesContract.FavoriteMovieEntry.COLUMN_MOVIE_ID;
import static com.asura.popularmovies.providers.FavoriteMoviesContract.FavoriteMovieEntry.CONTENT_URI;

public class MovieDetailActivity extends AppCompatActivity implements MaterialFavoriteButton.OnFavoriteChangeListener{

    private static final String TAG = "MovieDetailActivity";

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

    @BindView(R.id.favorite)
    public MaterialFavoriteButton favoriteButton;

    @BindView(R.id.trailers)
    public RecyclerView trailers;

    @BindView(R.id.reviews)
    public RecyclerView reviews;

    @BindView(R.id.trailerLayout)
    public LinearLayout trailerLayout;

    @BindView(R.id.reviewLayout)
    public LinearLayout reviewLayout;

    private Movie movie;

    private Handler mHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        HandlerThread handlerThread = new HandlerThread("Handler Thread");
        handlerThread.start();
        mHandler = new Handler(handlerThread.getLooper());

        movie = (Movie) getIntent().getParcelableExtra(EXTRA_MOVIE);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ButterKnife.bind(this);

        mTitleTextView.setText(movie.getOriginal_title());

        setPosterImage();

        mReleaseDateTextView.setText(movie.getReleaseDate());

        mRating.setText(movie.getVoteAverage() + "/10");

        mOverview.setText(movie.getOverview());
        mOverview.setMovementMethod(new ScrollingMovementMethod());

        favoriteButton.setOnFavoriteChangeListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();

        displayTrailersAndReviews();
    }

    private void displayTrailersAndReviews() {
        if(NetworkUtils.isNetworkAvailable(this)){

            reviewLayout.setVisibility(View.VISIBLE);
            trailerLayout.setVisibility(View.VISIBLE);

            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    final List<String> trailerList = NetworkUtils.getMovieTrailers(movie.getId());
                    final List<String> reviewList = NetworkUtils.getMovieReviews(movie.getId());

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            trailers.setLayoutManager(new LinearLayoutManager(MovieDetailActivity.this));
                            trailers.setItemAnimator(new DefaultItemAnimator());
                            trailers.setAdapter(new ItemAdapter(MovieDetailActivity.this
                            ,ItemAdapter.TRAILER,trailerList));

                            reviews.setLayoutManager(new LinearLayoutManager(MovieDetailActivity.this));
                            reviews.setItemAnimator(new DefaultItemAnimator());
                            reviews.setAdapter(new ItemAdapter(MovieDetailActivity.this
                                    ,ItemAdapter.REVIEW,reviewList));
                        }
                    });
                }
            });

        }else{
            reviewLayout.setVisibility(View.GONE);
            trailerLayout.setVisibility(View.GONE);
        }
    }

    private void setPosterImage(){
        if(movie instanceof FavoriteMovie){
            mImageView.setImageBitmap(((FavoriteMovie)movie).getImagePath());
            favoriteButton.setFavorite(true);
        }else{
            Picasso.with(this).load(movie.getPoster_path())
                    .into(mImageView);
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    if(checkIsFavorite()){
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                favoriteButton.setFavorite(true);
                            }
                        });
                    }
                }
            });
        }
    }

    private boolean checkIsFavorite() {
        Cursor cursor = getContentResolver().query(CONTENT_URI,
                null,
                null,
                null,
                null);
        if(cursor.moveToFirst()){
            do{
                if(cursor.getString(cursor.getColumnIndex(COLUMN_MOVIE_ID)).equals(movie.getId())){
                    return true;
                }
            }while (cursor.moveToNext());
        }
        return false;
    }

    @Override
    public void onFavoriteChanged(MaterialFavoriteButton buttonView, boolean favorite) {
        if(favorite){
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    insertMovieToFavoriteList();
                }
            });
        }
        else{
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    removeMovieFromFavoriteList();
                }
            });
        }
    }

    private void removeMovieFromFavoriteList() {
        String selectionClause = FavoriteMovieEntry.COLUMN_MOVIE_ID + "=?";
        String[] selectionArgs = new String[]{movie.getId()};
        getContentResolver().delete(FavoriteMovieEntry.CONTENT_URI,selectionClause,selectionArgs);
    }

    private void insertMovieToFavoriteList() {
        ContentValues contentValues = new ContentValues();

        contentValues.put(FavoriteMovieEntry.COLUMN_MOVIE_ID,movie.getId());
        contentValues.put(FavoriteMovieEntry.COLUMN_MOVIE_TITLE,movie.getMovieName());
        contentValues.put(FavoriteMovieEntry.COLUMN_MOVIE_LANGUAGE,movie.getLanguage());
        contentValues.put(FavoriteMovieEntry.COLUMN_MOVIE_RELEASE_DATE,movie.getReleaseDate());
        contentValues.put(FavoriteMovieEntry.COLUMN_MOVIE_VOTING_AVERAGE,movie.getVoteAverage());
        contentValues.put(FavoriteMovieEntry.COLUMN_MOVIE_POPULARITY,movie.getPopularity());
        contentValues.put(FavoriteMovieEntry.COLUMN_MOVIE_OVERVIEW,movie.getOverview());
        contentValues.put(FavoriteMovieEntry.COLUMN_MOVIE_THUMBNAIL,getImageByteArray());
        Uri uri = getContentResolver().insert(FavoriteMovieEntry.CONTENT_URI,contentValues);

        Log.i(TAG,uri.toString());
    }

    private byte[] getImageByteArray() {
        Bitmap mMovieImage = null;
        try {
            mMovieImage = Picasso.with(this).load(movie.getPoster_path()).get();
        } catch (IOException e) {
            e.printStackTrace();
        }
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        mMovieImage.compress(Bitmap.CompressFormat.JPEG,50,outputStream);
        return outputStream.toByteArray();
    }

}
