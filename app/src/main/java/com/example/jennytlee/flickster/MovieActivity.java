package com.example.jennytlee.flickster;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.jennytlee.flickster.models.Movie;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class MovieActivity extends AppCompatActivity {

    ArrayList<Movie> movies;
    MoviesAdapter adapter;
    ListView lvMovies;
    SwipeRefreshLayout swipeContainer;
    String ytUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie);

        movies = new ArrayList<>();
        // Generate ListView to populate
        lvMovies = (ListView) findViewById(R.id.lvMovies);

        // Create ArrayAdapter
        //ArrayAdapter<Movie> adapter = new ArrayAdapter<Movie>(this, android.R.layout.simple_list_item_1, movies);
        adapter = new MoviesAdapter(this, movies);

        // Associate ArrayAdapter with ListView
        if (lvMovies != null) {
            lvMovies.setAdapter(adapter);
        }

        swipeContainer = (SwipeRefreshLayout) findViewById(R.id.swipeContainer);

        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                fetchMovies();
            }
        });

        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        fetchMovies();
        setupListViewListener();
    }

    public void fetchMovies() {
        String url = "https://api.themoviedb.org/3/movie/now_playing?api_key=a07e22bc18f5cb106bfe4cc1f83ad8ed";

        AsyncHttpClient client = new AsyncHttpClient();

        client.get(url, new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                JSONArray movieJsonResults = null;

                try {
                    movies.clear();
                    movieJsonResults = response.getJSONArray("results");
                    movies.addAll(Movie.fromJsonArray(movieJsonResults));
                    adapter.notifyDataSetChanged();
                    swipeContainer.setRefreshing(false);
                } catch (JSONException e) {
                    e.printStackTrace();
                    swipeContainer.setRefreshing(false);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
            }
        });
    }

    public void setupListViewListener() {
        lvMovies.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapter, View view, int pos, long id) {
                        onClick(view, pos);
                    }
                }
        );
    }

    public String fetchTrailer(int pos) {
        String url = movies.get(pos).getVideoPath();
        AsyncHttpClient client = new AsyncHttpClient();

        client.get(url, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                JSONArray videoResults = null;

                try {
                    videoResults = response.getJSONArray("results");

                    ytUrl = videoResults.getJSONObject(0).getString("key");


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        return ytUrl;
    }

    public void onClick(View view, int pos) {
        String movTitle = movies.get(pos).title;
        String synopsis = movies.get(pos).overview;
        String rating = String.valueOf(movies.get(pos).rating/2.0);
        String numvotes = "(" + String.valueOf(movies.get(pos).numvotes) + ")";
        String releaseDate = movies.get(pos).releaseDate;
        String videoUrl = fetchTrailer(pos);
        String backdrop = movies.get(pos).getBackdropPath();

        Intent i = new Intent(MovieActivity.this, PopupActivity.class);
        i.putExtra("movTitle", movTitle);
        i.putExtra("synopsis", synopsis);
        i.putExtra("rating", rating);
        i.putExtra("numvotes", numvotes);
        i.putExtra("releaseDate", releaseDate);
        i.putExtra("videoUrl", videoUrl);
        i.putExtra("backdrop", backdrop);
        startActivity(i);
    }
}
