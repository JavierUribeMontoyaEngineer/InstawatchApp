package com.example.javi.instawatch.fcm;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.example.javi.instawatch.controlador.ControladorColaboraciones;
import com.example.javi.instawatch.modeloDTO.TokenDTO;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import android.provider.Settings.Secure;

import java.io.IOException;


/**
 * Created by Javi on 25/02/2017.
 */

//Servicio para refrescar los token
public class MyFirebaseInstanceIdService extends FirebaseInstanceIdService {
    public static final String TOKEN_BROADCAST = "firebasebroadcast";
    private ControladorColaboraciones controladorColaboraciones = new ControladorColaboraciones();

    @Override
    public void onTokenRefresh() {
        super.onTokenRefresh();
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        //  saveTokenToPrefs(refreshedToken);
        Log.d("myFireBase", "Refreshed token: " + refreshedToken);
        SharedPreferences sharedPreferences = getApplication().getSharedPreferences("My preferences", MODE_PRIVATE);
        String usuario = sharedPreferences.getString("usuario", null);
        if (usuario != null)
            sendRegistrationToServer(refreshedToken, usuario);
    }

    private void sendRegistrationToServer(String refreshedToken, String usuario) {
        Log.d("1", "registro en BD");
        String idDevice = Secure.getString(getContentResolver(),
                Secure.ANDROID_ID);
        //Se persiste el token en la BD
        controladorColaboraciones.registrarToken(refreshedToken, idDevice, usuario);
    }
}
