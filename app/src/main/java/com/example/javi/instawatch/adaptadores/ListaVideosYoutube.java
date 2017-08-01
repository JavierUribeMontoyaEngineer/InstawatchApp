package com.example.javi.instawatch.adaptadores;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.javi.instawatch.R;
import com.example.javi.instawatch.modelo.VideoYoutube;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Javi on 21/02/2017.
 */

public class ListaVideosYoutube extends ArrayAdapter<VideoYoutube> {
    private final Activity context;
    private List<VideoYoutube> videos;

    public ListaVideosYoutube(Activity context, List<VideoYoutube> videos) {
        super(context,R.layout.list_single_video,videos);
        this.context = context;
        this.videos = videos;

    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.list_single_video, null, true);
        TextView txtTitle = (TextView) rowView.findViewById(R.id.txt);
        VideoYoutube videoYoutubeActual = videos.get(position);
        txtTitle.setText(videoYoutubeActual.getTitulo());
        ImageView imageView = (ImageView) rowView.findViewById(R.id.img);
        Picasso.with(context).load(videoYoutubeActual.getUrlImg()).into(imageView);
        return rowView;
    }
}
