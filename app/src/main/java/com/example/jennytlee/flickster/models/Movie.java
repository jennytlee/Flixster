package com.example.jennytlee.flickster.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by jennytlee on 6/15/16.
 */
public class Movie {
    public String title;
    public String posterPath;
    public String overview;
    public String backdropPath;
    public int numvotes;
    public double rating;
    public String releaseDate;


    public String getPosterPath() {
        return String.format("https://image.tmdb.org/t/p/w342/%s", posterPath);
    }

    public String getBackdropPath() {
        return String.format("https://image.tmdb.org/t/p/w342/%s", backdropPath);
    }

    public Movie(JSONObject jsonObject) throws JSONException {
        this.title = jsonObject.getString("title");
        this.posterPath = jsonObject.getString("poster_path");
        this.overview = jsonObject.getString("overview");
        this.backdropPath = jsonObject.getString("backdrop_path");
        this.numvotes = jsonObject.getInt("vote_count");
        this.rating = jsonObject.getDouble("vote_average");
        this.releaseDate = jsonObject.getString("release_date");
    }


    public static ArrayList<Movie> fromJsonArray(JSONArray array) {
        ArrayList<Movie> results = new ArrayList<>();

        for (int i = 0; i < array.length(); i++) {
            try {
                results.add(new Movie(array.getJSONObject(i)));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return results;
    }


}
