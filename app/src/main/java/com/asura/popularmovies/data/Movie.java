package com.asura.popularmovies.data;

import com.asura.popularmovies.utils.NetworkUtils;

import java.io.Serializable;

public class Movie implements Serializable {
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
}
