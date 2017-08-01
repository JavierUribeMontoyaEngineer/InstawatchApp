package com.example.javi.instawatch.modelo;

/**
 * Created by Javi on 20/02/2017.
 */

public class VideoYoutube {

    private String id;
    private String titulo;
    private String urlImg;
    private String url;


    public String getUrl() {
        return "https://www.youtube.com/watch?v="+getId();
    }

    public VideoYoutube(String id, String titulo, String urlImg) {
        this.id = id;
        this.titulo = titulo;
        this.urlImg = urlImg;
    }

    public String getId() {
        return id;
    }

    public String getUrlImg() {
        return urlImg;
    }

    public String getTitulo() {
        return titulo;
    }

    @Override
    public String toString() {
        return titulo;
    }
}
