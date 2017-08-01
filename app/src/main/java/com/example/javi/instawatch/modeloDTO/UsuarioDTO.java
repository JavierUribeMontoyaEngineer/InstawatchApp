package com.example.javi.instawatch.modeloDTO;

import java.io.Serializable;

/**
 * Created by Javi on 15/02/2017.
 */

public class UsuarioDTO implements Serializable {

    String nombre;
    String primerApellido;
    String segundoApellido;
    String dni;
    int idProvincia;
    int idMunicipio;
    String tlf;
    String email;
    String edad;
    String nombreUsuario;
    String password;
    String confirmPassword;


    public UsuarioDTO(String nombre, String primerApellido, String segundoApellido,
                      String dni, int idProvincia, int idMunicipio, String tlf,
                      String email, String edad, String nombreUsuario, String password) {
        this.nombre = nombre;
        this.primerApellido = primerApellido;
        this.segundoApellido = segundoApellido;
        this.dni = dni;
        this.idProvincia = idProvincia;
        this.idMunicipio = idMunicipio;
        this.tlf = tlf;
        this.email = email;
        this.edad = edad;
        this.nombreUsuario = nombreUsuario;
        this.password = password;
    }

    public String getPrimerApellido() {
        return primerApellido;
    }

    public String getNombre() {
        return nombre;
    }

    public String getSegundoApellido() {
        return segundoApellido;
    }

    public String getDni() {
        return dni;
    }

    public int getIdProvincia() {
        return idProvincia;
    }

    public void setIdProvincia(int idProvincia) {
        this.idProvincia = idProvincia;
    }

    public int getIdMunicipio() {
        return idMunicipio;
    }

    public void setIdMunicipio(int idMunicipio) {
        this.idMunicipio = idMunicipio;
    }

    public String getTlf() {
        return tlf;
    }

    public String getEmail() {
        return email;
    }

    public String getEdad() {
        return edad;
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public String getPassword() {
        return password;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setPrimerApellido(String primerApellido) {
        this.primerApellido = primerApellido;
    }

    public void setSegundoApellido(String segundoApellido) {
        this.segundoApellido = segundoApellido;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }


    public void setTlf(String tlf) {
        this.tlf = tlf;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setEdad(String edad) {
        this.edad = edad;
    }

    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }
}
