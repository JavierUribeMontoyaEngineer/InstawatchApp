package com.example.javi.instawatch.modeloDTO;

import java.io.Serializable;

/**
 * Created by Javi on 25/02/2017.
 */

public class PeticionDTO implements Serializable {
    private int id;
    private String destinatario;
    private String remitente;
    private String contrasenia;
    private boolean haLeido;
    private boolean rolPersonal;
    private boolean rolDominios;
    private boolean rolVideos;

    public PeticionDTO(int id, String destinatario, String remitente, String contrasenia, boolean rolPersonal,
                       boolean rolDominios, boolean rolVideos) {
        this.id = id;
        this.destinatario = destinatario;
        this.remitente = remitente;
        this.contrasenia = contrasenia;
        this.rolPersonal = rolPersonal;
        this.rolDominios = rolDominios;
        this.rolVideos = rolVideos;
        haLeido = false;
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

    public String getRemitente() {
        return remitente;
    }

    public String getContrasenia() {
        return contrasenia;
    }

    public boolean isHaLeido() {
        return haLeido;
    }

    public void setDestinatario(String destinatario) {
        this.destinatario = destinatario;
    }

    public void setRemitente(String remitente) {
        this.remitente = remitente;
    }

    public void setContrasenia(String contrasenia) {
        this.contrasenia = contrasenia;
    }

    public void setHaLeido(boolean haLeido) {
        this.haLeido = haLeido;
    }

    public boolean isRolDominios() {
        return rolDominios;
    }

    public boolean isRolPersonal() {
        return rolPersonal;
    }

    public boolean isRolVideos() {
        return rolVideos;
    }

    public void setRolDominios(boolean rolDominios) {
        this.rolDominios = rolDominios;
    }

    public void setRolPersonal(boolean rolPersonal) {
        this.rolPersonal = rolPersonal;
    }

    public void setRolVideos(boolean rolVideos) {
        this.rolVideos = rolVideos;
    }

    @Override
    public String toString() {
        return "PeticionDTO [id=" + id + ", destinatario=" + destinatario + ", remitente=" + remitente
                + ", contrasenia=" + contrasenia + ", haLeido=" + haLeido + ", rolPersonal=" + rolPersonal
                + ", rolDominios=" + rolDominios + ", rolVideos=" + rolVideos + "]";
    }



}