package com.asura.popularmovies.providers;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import static com.asura.popularmovies.providers.FavoriteMoviesContract.FavoriteMovieEntry.CONTENT_URI;
import static com.asura.popularmovies.providers.FavoriteMoviesContract.FavoriteMovieEntry.TABLE_NAME;

public class FavoriteMoviesProvider extends ContentProvider {

    private static final String TAG = "FavoriteMoviesProvider";

    private SQLiteDatabase mSQLiteDatabase;

    private FavoriteMoviesDbHelper mDatabaseHelper;

    private static final int FAVORITE_MOVIES = 200;
    private static final int FAVORITE_MOVIES_ID = 201;


    private static final UriMatcher URI_MATCHER;
    static{
        URI_MATCHER = new UriMatcher(UriMatcher.NO_MATCH);
        URI_MATCHER.addURI(FavoriteMoviesContract.AUTHORITY,
                FavoriteMoviesContract.PATH_FAVORITE_MOVIES, FAVORITE_MOVIES);
        URI_MATCHER.addURI(FavoriteMoviesContract.AUTHORITY,
                FavoriteMoviesContract.PATH_FAVORITE_MOVIES + "/#", FAVORITE_MOVIES_ID);
    }

    @Override
    public boolean onCreate() {

        mDatabaseHelper = new FavoriteMoviesDbHelper(getContext());
        mSQLiteDatabase = mDatabaseHelper.getWritableDatabase();

        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection,
                        @Nullable String[] selectionArgs, @Nullable String sortOrder) {

        int match = URI_MATCHER.match(uri);

        Cursor cursor;

        switch (match){
            case FAVORITE_MOVIES:
                cursor = mSQLiteDatabase.query(TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            default:
                throw new UnsupportedOperationException("Unknown Uri " + uri);
        }

        cursor.setNotificationUri(getContext().getContentResolver(),uri);

        return cursor;

    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {

        int match = URI_MATCHER.match(uri);
        switch (match){
            case FAVORITE_MOVIES:
                long rowID = mSQLiteDatabase.insert(TABLE_NAME,"",contentValues);

                if (rowID > 0) {
                    Uri _uri = ContentUris.withAppendedId(CONTENT_URI, rowID);
                    getContext().getContentResolver().notifyChange(_uri, null);
                    Log.d(TAG,"Insert successful");
                    return _uri;
                }

                throw new SQLException("Failed to add a record into " + uri);
            default:
                throw new UnsupportedOperationException("Unknown Uri " + uri);
        }

    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String s, @Nullable String[] strings) {

        int match = URI_MATCHER.match(uri);
        switch (match){
            case FAVORITE_MOVIES:
                int rowID = mSQLiteDatabase.delete(TABLE_NAME,s,strings);

                if (rowID > 0) {
                    Log.d(TAG,"Delete successful");
                    return rowID;
                }

                String argument = "";
                for(String a : strings){
                    argument = argument + " ";
                }
                throw new SQLException("Failed to delete " + uri + " " + s + " " + argument);
            default:
                throw new UnsupportedOperationException("Unknown Uri " + uri);
        }
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String s, @Nullable String[] strings) {
        throw new UnsupportedOperationException("Unknown Uri " + uri);
    }
}
