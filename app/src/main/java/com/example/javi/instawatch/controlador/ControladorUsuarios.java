package com.example.javi.instawatch.controlador;

import android.util.Log;

/**
 * Created by Javi on 08/02/2017.
 */

import com.example.javi.instawatch.Config;
import com.example.javi.instawatch.activities.LoginActivity;
import com.example.javi.instawatch.modelo.AcabadoAsynTaskListener;
import com.example.javi.instawatch.modelo.PeticionRest;
import com.example.javi.instawatch.modeloDTO.AutenticacionDTO;
import com.example.javi.instawatch.modeloDTO.MunicipioDTO;
import com.example.javi.instawatch.modeloDTO.ProvinciaDTO;
import com.example.javi.instawatch.modeloDTO.UsuarioDTO;
//import com.google.common.hash.HashCode;
//import com.google.common.hash.Hashing;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

//import org.mindrot.jbcrypt.BCrypt;

import java.lang.reflect.Type;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.Collator;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import javax.net.ssl.HttpsURLConnection;

public class ControladorUsuarios {
    private static final String METODO_GET = "GET";
    private static final String METODO_POST = "POST";
    private List<ProvinciaDTO> provinciasDTO;
    private List<MunicipioDTO> municipiosDTO;
    private List<UsuarioDTO> usuariosDTO;
    public static final String TAG = ControladorUsuarios.class.getSimpleName();


    public String[] getProvincias() {
        URL url = null;
        try {
            url = new URL(Config.IP_SERVIDOR + "/instawatch/rest/usuarios/provincias");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        PeticionRest peticionRest = new PeticionRest();
        String provinciasJsonString = null;
        try {
            provinciasJsonString = peticionRest.execute(url, METODO_GET).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        Gson gson = new Gson();
        Type type = new TypeToken<List<ProvinciaDTO>>() {
        }.getType();
        provinciasDTO = new Gson().fromJson(provinciasJsonString, type);
        String[] nombresProvincias = new String[provinciasDTO.size() + 1];
        int i = 1;
        for (ProvinciaDTO provinciaDTO : provinciasDTO) {
            nombresProvincias[i] = provinciaDTO.getNombre();
            i++;
        }
        nombresProvincias[0] = "Provincia";
        return nombresProvincias;
    }

    public String[] getMunicipios(String provinciaSeleccionada) {
        URL url = null;
        String[] nombresMunicipios = new String[1];
        if (provinciaSeleccionada.equals("Provincia")) {
            nombresMunicipios[0] = "Municipio";
            return nombresMunicipios;
        }
        Log.d("1", "PROVINCIA***" + provinciaSeleccionada);
        try {
            int idProvincia = findByNombreProvincia(provinciaSeleccionada);
            url = new URL(Config.IP_SERVIDOR + "/instawatch/rest/usuarios/municipios/" + idProvincia);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        PeticionRest peticionRest = new PeticionRest();
        String municipiosJsonString = null;
        try {
            municipiosJsonString = peticionRest.execute(url, METODO_GET).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        Gson gson = new Gson();
        Type type = new TypeToken<List<MunicipioDTO>>() {
        }.getType();
        municipiosDTO = new Gson().fromJson(municipiosJsonString, type);
        nombresMunicipios = new String[municipiosDTO.size() + 1];
        nombresMunicipios[0] = "Municipio";
        int i = 1;
        Log.d("1", "getMunicipios:");
        for (MunicipioDTO municipioDTO : municipiosDTO) {
            nombresMunicipios[i] = municipioDTO.getNombre();
            Log.d("1", nombresMunicipios[i]);
            i++;
        }
        return nombresMunicipios;

    }

    private int findByNombreProvincia(String provinciaSeleccionada) {
        for (ProvinciaDTO provinciaDTO : provinciasDTO) {
            if (provinciaDTO.getNombre().equals(provinciaSeleccionada))
                return provinciaDTO.getId();
        }
        return 0;
    }

    private int findByNombreMunicipio(String municipioSeleccionado) {
        for (MunicipioDTO municipioDTO : municipiosDTO) {
            if (municipioDTO.getNombre().equals(municipioSeleccionado))
                return municipioDTO.getId();
        }
        return 0;
    }

    public int registrar(String nombre, String primerApellido, String segundoApellido,
                         String dni, String provincia, String municipio,
                         String tlf, String email, String edad, String nombreUsuario,
                         String password, String confirmPassword) {
        URL url = null;
        int codeResponse = HttpsURLConnection.HTTP_INTERNAL_ERROR;
        try {
            url = new URL(Config.IP_SERVIDOR + "/instawatch/rest/usuarios");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        PeticionRest peticionRest = new PeticionRest();
        Gson gson = new Gson();

        UsuarioDTO usuarioDTO = new UsuarioDTO(nombre, primerApellido, segundoApellido, dni, findByNombreProvincia(provincia),
                findByNombreMunicipio(municipio), tlf, email, edad, nombreUsuario, password);

        try {
            codeResponse = Integer.valueOf(peticionRest.execute(url, METODO_POST, gson.toJson(usuarioDTO)).get());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        return codeResponse;
    }


    public int login(String usuario, String password, LoginActivity activity, AcabadoAsynTaskListener loginActivity) {
        Log.d("1", "User:" + usuario + "password:" + password);
        int codeResponse = HttpsURLConnection.HTTP_FORBIDDEN;
        URL url = null;
        try {
            url = new URL(Config.IP_SERVIDOR + "/instawatch/rest/usuarios/login");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        PeticionRest peticionRest = new PeticionRest(activity, loginActivity);
        Gson gson = new Gson();
        AutenticacionDTO autenticacionDTO = new AutenticacionDTO(usuario, password);
        peticionRest.execute(url, METODO_POST, gson.toJson(autenticacionDTO));


        return codeResponse;
    }


    public List<String> getUsuarios(String usuario) {
        URL url = null;
        List<String> nombresUsuarios = new LinkedList<>();
        try {
            url = new URL(Config.IP_SERVIDOR + "/instawatch/rest/usuarios");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        PeticionRest peticionRest = new PeticionRest();
        String usuariosJsonString = null;
        try {
            usuariosJsonString = peticionRest.execute(url, METODO_GET).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        Type type = new TypeToken<List<UsuarioDTO>>() {
        }.getType();
        usuariosDTO = new Gson().fromJson(usuariosJsonString, type);
        for (UsuarioDTO usuarioDTO : usuariosDTO) {
            if (!usuarioDTO.getNombreUsuario().equals(usuario)) {
                nombresUsuarios.add(usuarioDTO.getNombreUsuario());
            }
        }
        Collections.sort(nombresUsuarios, new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                return Collator.getInstance().compare(o1, o2);
            }
        });
        return nombresUsuarios;
    }

    public UsuarioDTO getUsuario(String loginUsuario) {
        Log.d("1", "GET usuario de " + loginUsuario);
        URL url = null;
        try {
            url = new URL(Config.IP_SERVIDOR + "/instawatch/rest/usuarios/" + loginUsuario);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        PeticionRest peticionRest = new PeticionRest();
        String usuarioJSONString = null;
        try {
            usuarioJSONString = peticionRest.execute(url, METODO_GET).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        Gson gson = new Gson();
        UsuarioDTO usuarioDTO = new Gson().fromJson(usuarioJSONString, UsuarioDTO.class);
        return usuarioDTO;
    }

    public ProvinciaDTO getProvincia(int idProvincia) {
        URL url = null;
        try {
            url = new URL(Config.IP_SERVIDOR + "/instawatch/rest/usuarios/provincia/" + idProvincia);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        PeticionRest peticionRest = new PeticionRest();
        String provinciaJSONString = null;
        try {
            provinciaJSONString = peticionRest.execute(url, METODO_GET).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        Gson gson = new Gson();
        ProvinciaDTO provinciaDTO = new Gson().fromJson(provinciaJSONString, ProvinciaDTO.class);
        return provinciaDTO;
    }

    public MunicipioDTO getMunicipio(int idMunicipio) {
        URL url = null;
        try {
            url = new URL(Config.IP_SERVIDOR + "/instawatch/rest/usuarios/municipio/" + idMunicipio);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        PeticionRest peticionRest = new PeticionRest();
        String municipioJSONString = null;
        try {
            municipioJSONString = peticionRest.execute(url, METODO_GET).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        Gson gson = new Gson();
        MunicipioDTO municipioDTO = new Gson().fromJson(municipioJSONString, MunicipioDTO.class);
        return municipioDTO;

    }

    public int updateUsuario(String nombre, String primerApellido, String segundoApellido, String provinciaSeleccionada, String municipioSeleccionado, String dni, String tlf, String email, String edad, String nombreUsuario, String password) {
        URL url = null;
        int codeResponse = HttpsURLConnection.HTTP_INTERNAL_ERROR;
        try {
            url = new URL(Config.IP_SERVIDOR + "/instawatch/rest/usuarios");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        PeticionRest peticionRest = new PeticionRest();
        Gson gson = new Gson();

        UsuarioDTO usuarioDTO = new UsuarioDTO(nombre, primerApellido, segundoApellido, dni, findByNombreProvincia(provinciaSeleccionada),
                findByNombreMunicipio(municipioSeleccionado), tlf, email, edad, nombreUsuario, password);
        try {
            codeResponse = Integer.valueOf(peticionRest.execute(url, PeticionRest.METODO_PUT, gson.toJson(usuarioDTO)).get());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return codeResponse;
    }

    public boolean existeUsuario(String login) {
        UsuarioDTO usuario = getUsuario(login);
        return usuario != null;
    }

}
