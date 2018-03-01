package com.asura.popularmovies.providers;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.asura.popularmovies.providers.FavoriteMoviesContract.FavoriteMovieEntry;


public class FavoriteMoviesDbHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "Favorites.db";
    private static final int DATABASE_VERSION = 1;

    public FavoriteMoviesDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        final String CREATE_DB_TABLE =
                "CREATE TABLE " + FavoriteMovieEntry.TABLE_NAME + " (" +

                        FavoriteMovieEntry._ID               + " INTEGER PRIMARY KEY AUTOINCREMENT, " +

                        FavoriteMovieEntry.COLUMN_MOVIE_ID + " TEXT NOT NULL, "                        +

                        FavoriteMovieEntry.COLUMN_MOVIE_TITLE + " TEXT NOT NULL, "                    +

                        FavoriteMovieEntry.COLUMN_MOVIE_LANGUAGE   + " TEXT NOT NULL, "               +

                        FavoriteMovieEntry.COLUMN_MOVIE_RELEASE_DATE   + " TEXT NOT NULL, "           +

                        FavoriteMovieEntry.COLUMN_MOVIE_VOTING_AVERAGE   + " REAL NOT NULL, "         +

                        FavoriteMovieEntry.COLUMN_MOVIE_POPULARITY   + " REAL NOT NULL, "             +

                        FavoriteMovieEntry.COLUMN_MOVIE_OVERVIEW + " TEXT NOT NULL, "                  +

                        FavoriteMovieEntry.COLUMN_MOVIE_THUMBNAIL + " BLOB DEFAULT NULL, "                  +

                        " UNIQUE (" + FavoriteMovieEntry.COLUMN_MOVIE_ID + ") ON CONFLICT REPLACE);" ;


        sqLiteDatabase.execSQL(CREATE_DB_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + FavoriteMovieEntry.TABLE_NAME);
    }
}
