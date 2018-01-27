package com.asura.popularmovies;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.asura.popularmovies.adapters.MovieListAdapter;
import com.asura.popularmovies.data.Movie;
import com.asura.popularmovies.utils.NetworkUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.asura.popularmovies.MovieListQueryTask.POPULAR;
import static com.asura.popularmovies.MovieListQueryTask.TOP_RATED;


public class MainActivity extends AppCompatActivity implements MovieListQueryTask.OnResponseListener {

    final static String TAG = "MainActivity";

    @BindView(R.id.imageList)
    public RecyclerView mImageList;

    @BindView(R.id.error_view)
    public TextView mErrorTextView;

    private MovieListQueryTask mMovieListQueryTask = null;

    private ProgressDialog mProgressBar;

    private MovieListAdapter mMovieListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        GridLayoutManager layoutManager = new GridLayoutManager(getApplicationContext(), 2);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mImageList.setLayoutManager(layoutManager);
        mImageList.setItemAnimator(new DefaultItemAnimator());
        mMovieListAdapter = new MovieListAdapter(this);
        mImageList.setAdapter(mMovieListAdapter);

        mMovieListQueryTask = new MovieListQueryTask(this);

        if (NetworkUtils.isNetworkAvailable(this)) {
            mMovieListQueryTask.execute(POPULAR);
        } else {
            showErrorView();
        }
    }

    private void showErrorView() {
        mImageList.setVisibility(View.GONE);
        mErrorTextView.setVisibility(View.VISIBLE);
    }

    @Override
    public void showProgressBar() {
        mProgressBar = new ProgressDialog(this);
        mProgressBar.setCancelable(false);
        mProgressBar.setMessage(getString(R.string.fetching_movies));
        mProgressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mProgressBar.show();
    }

    @Override
    public void disableProgressBar() {
        mProgressBar.dismiss();
        if (mImageList.getVisibility() == View.GONE) {
            mImageList.setVisibility(View.VISIBLE);
            mErrorTextView.setVisibility(View.GONE);
        }
    }

    @Override
    public void onResponse(List<Movie> movieList) {
        mMovieListAdapter.swapMovieList(movieList);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.sort_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        mMovieListQueryTask = new MovieListQueryTask(this);
        switch (item.getItemId()) {
            case R.id.popular:
                if (NetworkUtils.isNetworkAvailable(this)) {
                    mMovieListQueryTask.execute(POPULAR);
                } else {
                    showErrorView();
                }
                return true;
            case R.id.top_rated:
                if (NetworkUtils.isNetworkAvailable(this)) {
                    mMovieListQueryTask.execute(TOP_RATED);
                } else {
                    showErrorView();
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }
}
