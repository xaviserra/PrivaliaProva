package com.serra.xavier.privaliaprova;

import android.graphics.Bitmap;

/**
 * Class with the data to show from every movie.
 * Constructor, getters and setters.
 */
public class MovieItem {

    public String title; //Contain title (YEAR)
    public String overView;
    public Bitmap image;


    public MovieItem(String title, String overview, Bitmap image){
        this.title = title;
        this.overView = overview;
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getOverView() {
        return overView;
    }

    public void setOverView(String overView) {
        this.overView = overView;
    }

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }
}
