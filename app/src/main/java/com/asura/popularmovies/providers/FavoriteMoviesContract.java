package com.asura.popularmovies.providers;

import android.net.Uri;
import android.provider.BaseColumns;

public class FavoriteMoviesContract {

    public static final String AUTHORITY = "com.asura.popularmovies.providers.FavoriteMoviesProvider";

    public static final Uri BASE_CONTENT_URI = Uri.parse( "content://" + AUTHORITY );

    public static final String PATH_FAVORITE_MOVIES = "favoriteMovies";

    public static final class FavoriteMovieEntry implements BaseColumns{

        public static final Uri CONTENT_URI = BASE_CONTENT_URI
                                                .buildUpon()
                                                .appendPath(PATH_FAVORITE_MOVIES)
                                                .build();

        public static final String TABLE_NAME = "FavoriteMovies";

        public static final String COLUMN_MOVIE_ID = "MOVIE_ID";
        public static final String COLUMN_MOVIE_TITLE = "MOVIE_TITLE";
        public static final String COLUMN_MOVIE_LANGUAGE = "MOVIE_LANGUAGE";
        public static final String COLUMN_MOVIE_RELEASE_DATE = "MOVIE_RELEASE_DATE";
        public static final String COLUMN_MOVIE_VOTING_AVERAGE = "MOVIE_VOTING_AVERAGE";
        public static final String COLUMN_MOVIE_POPULARITY = "MOVIE_POPULARITY";
        public static final String COLUMN_MOVIE_OVERVIEW = "MOVIE_OVERVIEW";
        public static final String COLUMN_MOVIE_THUMBNAIL = "MOVIE_THUMBNAIL";

        public static Uri buildFavoriteMovieUriWithID(long date) {
            return CONTENT_URI.buildUpon()
                    .appendPath(_ID)
                    .build();
        }

    }

}
