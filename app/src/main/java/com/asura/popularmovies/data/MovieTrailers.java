package com.asura.popularmovies.data;

public class MovieTrailers {
    private String id;
    private Trailer[] results;

    public Trailer[] getResults() {
        return results;
    }

    public void setResults(Trailer[] results) {
        this.results = results;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
