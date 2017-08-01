package com.example.javi.instawatch.modeloDTO;

import java.io.Serializable;

/**
 * Created by Javi on 25/02/2017.
 */

public class ColaboradorDTO implements Serializable {
    private String login;
    private String nombre;
    private int id;


    public ColaboradorDTO(int id, String login, String nombre) {
        this.login = login;
        this.nombre = nombre;
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }


    public void setLogin(String login) {
        this.login = login;
    }


    public String getNombre() {
        return nombre;
    }


    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    @Override
    public String toString() {
        return "ColaboradorDTO{" +
                "login='" + login + '\'' +
                ", nombre='" + nombre + '\'' +
                '}';
    }
}
