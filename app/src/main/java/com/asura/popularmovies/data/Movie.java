package com.asura.popularmovies.data;

import android.os.Parcel;
import android.os.Parcelable;

import com.asura.popularmovies.utils.NetworkUtils;

public class Movie implements Parcelable {
    private String title;
    private String poster_path;
    private String original_language;
    private String original_title;
    private int[] genre_ids;
    private String backdrop_path;
    private String id;
    private double vote_average;
    private int vote_count;
    private boolean video;
    private boolean adult;
    private double popularity;
    private String overview;
    private String release_date;

    public Movie(){

    }

    public Movie(Parcel in) {
        title = in.readString();
        poster_path = in.readString();
        original_language = in.readString();
        original_title = in.readString();
        genre_ids = in.createIntArray();
        backdrop_path = in.readString();
        id = in.readString();
        vote_average = in.readDouble();
        vote_count = in.readInt();
        video = in.readByte() != 0;
        adult = in.readByte() != 0;
        popularity = in.readDouble();
        overview = in.readString();
        release_date = in.readString();
    }

    public static final Creator<Movie> CREATOR = new Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    private static final String BASE_URL = "http://image.tmdb.org/t/p/w185";

    public String getMovieName() {
        return title;
    }

    public void setMovieName(String movieName) {
        this.title = movieName;
    }


    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getReleaseDate() {
        return release_date;
    }

    public void setReleaseDate(String releaseDate) {
        this.release_date = releaseDate;
    }

    public String getLanguage() {
        return original_language;
    }

    public void setLanguage(String language) {
        this.original_language = language;
    }

    public double getVoteAverage() {
        return vote_average;
    }

    public void setVoteAverage(double voteAverage) {
        this.vote_average = voteAverage;
    }

    public int getVoteCount() {
        return vote_count;
    }

    public void setVoteCount(int voteCount) {
        this.vote_count = voteCount;
    }

    public boolean isVideo() {
        return video;
    }

    public void setVideo(boolean video) {
        this.video = video;
    }

    public double getPopularity() {
        return Math.round(popularity * 100.00) / 100;
    }

    public void setPopularity(double popularity) {
        this.popularity = popularity;
    }

    public String getOriginal_title() {
        return original_title;
    }

    public void setOriginal_title(String original_title) {
        this.original_title = original_title;
    }

    public int[] getGenre_ids() {
        return genre_ids;
    }

    public void setGenre_ids(int[] genre_ids) {
        this.genre_ids = genre_ids;
    }

    public String getBackdrop_path() {
        return backdrop_path;
    }

    public void setBackdrop_path(String backdrop_path) {
        this.backdrop_path = backdrop_path;
    }

    public boolean isAdult() {
        return adult;
    }

    public void setAdult(boolean adult) {
        this.adult = adult;
    }

    public String getPoster_path() {
        return NetworkUtils.getURLString(poster_path);
    }

    public void setPoster_path(String poster_path) {
        this.poster_path = poster_path;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(title);
        parcel.writeString(poster_path);
        parcel.writeString(original_language);
        parcel.writeString(original_title);
        parcel.writeIntArray(genre_ids);
        parcel.writeString(backdrop_path);
        parcel.writeString(id);
        parcel.writeDouble(vote_average);
        parcel.writeInt(vote_count);
        parcel.writeByte((byte) (video ? 1 : 0));
        parcel.writeByte((byte) (adult ? 1 : 0));
        parcel.writeDouble(popularity);
        parcel.writeString(overview);
        parcel.writeString(release_date);
    }
}
