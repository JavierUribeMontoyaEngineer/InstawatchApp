package com.example.javi.instawatch.modeloDTO;

import java.io.Serializable;

/**
 * Created by Javi on 06/03/2017.
 */

public class TokenDTO implements Serializable {

    private String token;
    private String idDevice;
    private String usuario;

    public TokenDTO(String token, String idDevice, String usuario) {
        this.token = token;
        this.idDevice = idDevice;
        this.usuario = usuario;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getIdDevice() {
        return idDevice;
    }

    public void setIdDevice(String idDevice) {
        this.idDevice = idDevice;
    }

    @Override
    public String toString() {
        return "TokenDTO [token=" + token + ", idDevice=" + idDevice + ", usuario=" + usuario + "]";
    }


}
