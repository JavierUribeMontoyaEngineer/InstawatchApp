package com.example.javi.instawatch.controlador;

import android.util.Log;

import com.example.javi.instawatch.Config;
import com.example.javi.instawatch.modelo.PeticionRest;
import com.example.javi.instawatch.modeloDTO.ColaboradorDTO;
import com.example.javi.instawatch.modeloDTO.MensajeVideoDTO;
import com.example.javi.instawatch.modeloDTO.PeticionDTO;
import com.example.javi.instawatch.modeloDTO.TokenDTO;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Created by Javi on 06/03/2017.
 */

public class ControladorColaboraciones {
    private static final String METODO_GET = "GET";
    private static final String METODO_POST = "POST";
    private static final String METODO_DELETE = "DELETE";

    public void registrarToken(String refreshedToken, String idDevice, String usuario) {
        URL url = null;
        try {
            url = new URL(Config.IP_SERVIDOR + "/instawatch/rest/colaboraciones/token");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        TokenDTO tokenDTO = new TokenDTO(refreshedToken, idDevice, usuario);
        PeticionRest peticionRest = new PeticionRest();
        Gson gson = new Gson();
        peticionRest.execute(url, METODO_POST, gson.toJson(tokenDTO));
    }

    public void borrarToken(String usuario, String idDevice) {
        URL url = null;
        try {
            url = new URL(Config.IP_SERVIDOR + "/instawatch/rest/colaboraciones/token");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        PeticionRest peticionRest = new PeticionRest();
        Gson gson = new Gson();
        TokenDTO tokenDTO = new TokenDTO(null, idDevice, usuario);
        Log.d("2", "borrar token");
        peticionRest.execute(url, METODO_DELETE, gson.toJson(tokenDTO));
    }

    public List<PeticionDTO> getPeticiones(String usuario) {
        List<PeticionDTO> peticionesDTO;

        URL url = null;
        try {
            url = new URL(Config.IP_SERVIDOR + "/instawatch/rest/colaboraciones/peticiones/" + usuario);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        PeticionRest peticionRest = new PeticionRest();
        String peticionesJSONString = null;
        try {
            peticionesJSONString = peticionRest.execute(url, PeticionRest.METODO_GET).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        Gson gson = new Gson();
        Type type = new TypeToken<List<PeticionDTO>>() {
        }.getType();
        peticionesDTO = new Gson().fromJson(peticionesJSONString, type);
        return peticionesDTO;
    }

    public List<ColaboradorDTO> getColaboradores(String usuario) {
        List<ColaboradorDTO> colaboradoresDTO;

        URL url = null;
        try {
            url = new URL(Config.IP_SERVIDOR + "/instawatch/rest/colaboraciones/" + usuario);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        PeticionRest peticionRest = new PeticionRest();
        String colaboracionesJSONString = null;
        try {
            colaboracionesJSONString = peticionRest.execute(url, PeticionRest.METODO_GET).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        Gson gson = new Gson();
        Type type = new TypeToken<List<ColaboradorDTO>>() {
        }.getType();
        colaboradoresDTO = new Gson().fromJson(colaboracionesJSONString, type);
        return colaboradoresDTO;
    }

    public List<MensajeVideoDTO> getMensajesVideo(String usuario) {
        List<MensajeVideoDTO> mensajesDTO;

        URL url = null;
        try {
            url = new URL(Config.IP_SERVIDOR + "/instawatch/rest/colaboraciones/mensajes/" + usuario);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        PeticionRest peticionRest = new PeticionRest();
        String colaboracionesJSONString = null;
        try {
            colaboracionesJSONString = peticionRest.execute(url, PeticionRest.METODO_GET).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        Gson gson = new Gson();
        Type type = new TypeToken<List<MensajeVideoDTO>>() {
        }.getType();
        mensajesDTO = new Gson().fromJson(colaboracionesJSONString, type);
        return mensajesDTO;
    }


    public void borrarPeticion(PeticionDTO peticionDTO) {
        URL url = null;
        try {
            url = new URL(Config.IP_SERVIDOR + "/instawatch/rest/colaboraciones/peticion");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        PeticionRest peticionRest = new PeticionRest();
        Gson gson = new Gson();
        peticionRest.execute(url, METODO_DELETE, gson.toJson(peticionDTO));
    }


    public void borrarColaboracion(ColaboradorDTO colaboradorDTO) {
        URL url = null;
        try {
            url = new URL(Config.IP_SERVIDOR + "/instawatch/rest/colaboraciones");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        PeticionRest peticionRest = new PeticionRest();
        Gson gson = new Gson();
        peticionRest.execute(url, METODO_DELETE, gson.toJson(colaboradorDTO));
    }

    public void borrarMensaje(MensajeVideoDTO mensajeVideoDTO) {
        URL url = null;
        try {
            url = new URL(Config.IP_SERVIDOR + "/instawatch/rest/colaboraciones/mensaje");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        PeticionRest peticionRest = new PeticionRest();
        Gson gson = new Gson();
        peticionRest.execute(url, METODO_DELETE, gson.toJson(mensajeVideoDTO));
    }

    public boolean isColaborador(String usuarioActual, String usuarioSeleccionado) {
        String isColaborador = "";

        URL url = null;
        try {
            url = new URL(Config.IP_SERVIDOR + "/instawatch/rest/colaboraciones/" + usuarioActual + "/" + usuarioSeleccionado);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        PeticionRest peticionRest = new PeticionRest();
        try {
            isColaborador = peticionRest.execute(url, PeticionRest.METODO_GET).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        Log.d("1",isColaborador);
        if (isColaborador.equals("true"))
            return true;
        else
            return false;

    }
}
