package com.asura.popularmovies;

import android.app.LoaderManager;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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

import static com.asura.popularmovies.MovieListQueryTaskLoader.FAVORITES;
import static com.asura.popularmovies.MovieListQueryTaskLoader.POPULAR;
import static com.asura.popularmovies.MovieListQueryTaskLoader.TOP_RATED;


public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<Movie>>{

    final static String TAG = "MainActivity";

    @BindView(R.id.imageList)
    public RecyclerView mImageList;

    @BindView(R.id.error_view)
    public TextView mErrorTextView;

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


        Log.i(TAG,"called here in onCreate");
        if (NetworkUtils.isNetworkAvailable(this)) {
            getLoaderManager().initLoader(POPULAR,null,this);
        } else {
            showErrorView(R.string.no_network);
        }
    }

    private void showErrorView(int errorTextId) {
        mImageList.setVisibility(View.GONE);
        mErrorTextView.setVisibility(View.VISIBLE);
        mErrorTextView.setText(errorTextId);
    }

    private void showProgressBar() {
        mProgressBar = new ProgressDialog(this);
        mProgressBar.setCancelable(false);
        mProgressBar.setMessage(getString(R.string.fetching_movies));
        mProgressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mProgressBar.show();
    }

    private void disableProgressBar() {
        mProgressBar.dismiss();
        if (mImageList.getVisibility() == View.GONE) {
            mImageList.setVisibility(View.VISIBLE);
            mErrorTextView.setVisibility(View.GONE);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.sort_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.popular:
                if (NetworkUtils.isNetworkAvailable(this)) {
                    getLoaderManager().destroyLoader(TOP_RATED);
                    getLoaderManager().destroyLoader(FAVORITES);
                    getLoaderManager().initLoader(POPULAR,null,this);
                } else {
                    showErrorView(R.string.no_network);
                }
                return true;
            case R.id.top_rated:
                if (NetworkUtils.isNetworkAvailable(this)) {
                    getLoaderManager().destroyLoader(FAVORITES);
                    getLoaderManager().destroyLoader(POPULAR);
                    getLoaderManager().initLoader(TOP_RATED,null,this);
                } else {
                    showErrorView(R.string.no_network);
                }
                return true;
            case R.id.favorites:
                getLoaderManager().destroyLoader(POPULAR);
                getLoaderManager().destroyLoader(TOP_RATED);
                getLoaderManager().initLoader(FAVORITES,null,this);
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    @Override
    public Loader<List<Movie>> onCreateLoader(int id, Bundle bundle) {
        showProgressBar();
        return new MovieListQueryTaskLoader(this, id);
    }

    @Override
    public void onLoadFinished(Loader<List<Movie>> loader, List<Movie> movieList) {

        disableProgressBar();
        Log.i(TAG,"called here");
        mMovieListAdapter.swapMovieList(movieList);

        if(loader.getId()==FAVORITES&&movieList.size()==0){
            showErrorView(R.string.no_favorites);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<Movie>> loader) {

    }
}
