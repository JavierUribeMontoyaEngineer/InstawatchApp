package com.example.javi.instawatch.modeloDTO;

import java.io.Serializable;

/**
 * Created by Javi on 21/02/2017.
 */

public class VideoDTO implements Serializable {

    String titulo;
    String url;
    int duracion;
    String usuario;
    long id;

    public VideoDTO(String titulo, String url, int duracion, String usuario) {
        this.titulo = titulo;
        this.url = url;
        this.duracion = duracion;
        this.usuario = usuario;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public String getUrl() {
        return url;
    }

    public int getDuracion() {
        return duracion;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setDuracion(int duracion) {
        this.duracion = duracion;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    @Override
    public String toString() {
        return "VideoDTO{" +
                "titulo='" + titulo + '\'' +
                ", url='" + url + '\'' +
                ", duracion=" + duracion +
                ", usuario='" + usuario + '\'' +
                ", id=" + id +
                '}';
    }
}
