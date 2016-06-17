package com.example.jennytlee.flickster;

import android.content.Context;
import android.content.res.Configuration;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.jennytlee.flickster.models.Movie;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import jp.wasabeef.picasso.transformations.RoundedCornersTransformation;

/**
 * Created by jennytlee on 6/15/16.
 */
public class MoviesAdapter extends ArrayAdapter<Movie> {

    public MoviesAdapter(Context context, ArrayList<Movie> movies) {
        super(context, R.layout.item_movie, movies);
    }

    private static class ViewHolder {
        TextView tvTitle;
        TextView tvOverview;
        ImageView ivBackdrop;
        ImageView ivPoster;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        boolean isLandscape = getContext().getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE;
        // Get the data item for this position
        Movie movie = getItem(position);

        ViewHolder viewHolder;

        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_movie, parent, false);

            // Lookup view for data population
            viewHolder.tvTitle = (TextView) convertView.findViewById(R.id.tvTitle);
            viewHolder.tvOverview = (TextView) convertView.findViewById(R.id.tvOverview);

            convertView.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        if (isLandscape) {
            viewHolder.ivBackdrop = (ImageView) convertView.findViewById(R.id.ivBackdrop);
            Picasso.with(getContext()).load(movie.getBackdropPath()).fit().placeholder(R.drawable.large_movie_poster).transform(new RoundedCornersTransformation(10, 10)).into(viewHolder.ivBackdrop);
        } else {
            viewHolder.ivPoster = (ImageView) convertView.findViewById(R.id.ivPoster);
            Picasso.with(getContext()).load(movie.getPosterPath()).fit().placeholder(R.drawable.large_movie_poster).transform(new RoundedCornersTransformation(10, 10)).into(viewHolder.ivPoster);
        }

        viewHolder.tvTitle.setText(movie.title);
        viewHolder.tvOverview.setText(movie.overview);

        // Return the completed view to render on screen
        return convertView;
    }
}
