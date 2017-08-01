package com.example.javi.instawatch.controlador;

import com.example.javi.instawatch.Config;
import com.example.javi.instawatch.modelo.PeticionRest;
import com.example.javi.instawatch.modeloDTO.VideoDTO;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.concurrent.ExecutionException;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by Javi on 17/02/2017.
 */

public class ControladorVideos {
    private List<VideoDTO> videosDTO;

    public int guardarVideo(VideoDTO videoDTO) {
        URL url = null;
        int codeResponse = HttpsURLConnection.HTTP_INTERNAL_ERROR;
        try {
            url = new URL(Config.IP_SERVIDOR + "/instawatch/rest/videos");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        PeticionRest peticionRest = new PeticionRest();
        Gson gson = new Gson();

        try {
            codeResponse = Integer.valueOf(peticionRest.execute(url, PeticionRest.METODO_POST, gson.toJson(videoDTO)).get());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return codeResponse;
    }

    public String[] getNombresVideos(String nombreUsuario) {
        List<VideoDTO> videosDTO = getVideos(nombreUsuario);
        String[] nombresVideos = new String[videosDTO.size()];
        int i = 0;
        for (VideoDTO videoDTO : videosDTO) {
            nombresVideos[i] = videoDTO.getTitulo();
            i++;
        }
        return nombresVideos;
    }

    public List<VideoDTO> getVideos(String nombreUsuario) {
        URL url = null;
        try {
            url = new URL(Config.IP_SERVIDOR + "/instawatch/rest/videos/" + nombreUsuario);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        PeticionRest peticionRest = new PeticionRest();
        String videosJSONString = null;
        try {
            videosJSONString = peticionRest.execute(url, PeticionRest.METODO_GET).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        Gson gson = new Gson();
        Type type = new TypeToken<List<VideoDTO>>() {
        }.getType();
        videosDTO = new Gson().fromJson(videosJSONString, type);
        return videosDTO;
    }

    public int editarVideo(VideoDTO videoDTO) {
        URL url = null;
        int codeResponse = HttpsURLConnection.HTTP_INTERNAL_ERROR;
        try {
            url = new URL(Config.IP_SERVIDOR + "/instawatch/rest/videos");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        PeticionRest peticionRest = new PeticionRest();
        Gson gson = new Gson();

        try {
            codeResponse = Integer.valueOf(peticionRest.execute(url, PeticionRest.METODO_PUT, gson.toJson(videoDTO)).get());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return codeResponse;
    }

    public int borrarVideo(VideoDTO videoDTO) {
        URL url = null;
        int codeResponse = HttpsURLConnection.HTTP_INTERNAL_ERROR;
        try {
            url = new URL(Config.IP_SERVIDOR + "/instawatch/rest/videos");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        PeticionRest peticionRest = new PeticionRest();
        Gson gson = new Gson();

        try {
            codeResponse = Integer.valueOf(peticionRest.execute(url, PeticionRest.METODO_DELETE, gson.toJson(videoDTO)).get());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return codeResponse;
    }
}
