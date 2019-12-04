package com.solanki.sahil.test_player.data.model;

import android.widget.ImageView;

import androidx.databinding.BindingAdapter;

import com.bumptech.glide.Glide;
import com.solanki.sahil.test_player.R;

public class Model_Items {

    private String title;
    private String media_url;
    private String thumbnail;


    public Model_Items(String title, String media_url, String thumbnail) {
        this.title = title;
        this.media_url = media_url;
        this.thumbnail = thumbnail;

    }

    public Model_Items() {
    }

    public String getTitle() {
        return title;
    }

    public String getMedia_url() {
        return media_url;
    }

    public String getThumbnail() {
        return thumbnail;
    }


    @BindingAdapter({ "thumbnail" })
    public static void loadImage(ImageView imageView, String imageURL) {
        Glide.with(imageView.getContext())
                .load(imageURL)
                .into(imageView);
    }

}
