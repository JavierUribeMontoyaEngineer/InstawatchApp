package com.example.javi.instawatch.modeloDTO;

import java.io.Serializable;

/**
 * Created by Javi on 14/02/2017.
 */

public class MunicipioDTO implements Serializable {

    private int id;
    private String nombre;

    public int getId() {
        return id;
    }
    public String getNombre() {
        return nombre;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    @Override
    public String toString() {
        return "MunicipioDTO{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                '}';
    }
}
