package com.serra.xavier.privaliaprova;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Class to adapt the data to the movieItem to every row.
 */
public class ListAdapter extends ArrayAdapter<MovieItem> {

    private static final android.graphics.BitmapFactory BitmapFactory = null ;
    private final Activity context;
    private final ArrayList<MovieItem> movieList;

    public ListAdapter (Activity context, ArrayList<MovieItem> dateItem) {
        super(context, R.layout.movie_item, dateItem);
        // TODO Auto-generated constructor stub

        this.context=context;
        this.movieList=dateItem;

    }

    /**
     * Method to link the elements from xml to every item from MovieItem
     * @param position
     * @param view
     * @param parent
     * @return
     */
    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater=context.getLayoutInflater();
        View miView=inflater.inflate(R.layout.movie_item, null,true);

        ImageView image = miView.findViewById(R.id.imageMovie);
        TextView txtTitle = miView.findViewById(R.id.titleMovie);
        TextView txtOverView = miView.findViewById(R.id.overviewMovie);


        txtTitle.setText(movieList.get(position).getTitle());
        txtOverView.setText(movieList.get(position).getOverView());

        image.setImageBitmap(movieList.get(position).getImage());

        return miView;

    };

}
