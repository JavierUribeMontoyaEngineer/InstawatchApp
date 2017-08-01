package com.example.javi.instawatch.modeloDTO;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Javi on 14/03/2017.
 */

public class MensajeVideoDTO implements Serializable {

    private int id;
    private String destinatario;

    private String remitente;

    private ArrayList<String> videos;

    public MensajeVideoDTO(int id, String destinatario, String remitente, ArrayList<String> videos) {
        this.destinatario = destinatario;
        this.remitente = remitente;
        this.videos = videos;
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDestinatario() {
        return destinatario;
    }

    public void setDestinatario(String destinatario) {
        this.destinatario = destinatario;
    }

    public String getRemitente() {
        return remitente;
    }

    public void setRemitente(String remitente) {
        this.remitente = remitente;
    }

    public ArrayList<String> getVideos() {
        return videos;
    }

    public void setVideos(ArrayList<String> videos) {
        this.videos = videos;
    }

    @Override
    public String toString() {
        return "MensajeVideoDTO{" +
                "id=" + id +
                ", destinatario='" + destinatario + '\'' +
                ", remitente='" + remitente + '\'' +
                ", videos=" + videos +
                '}';
    }
}
