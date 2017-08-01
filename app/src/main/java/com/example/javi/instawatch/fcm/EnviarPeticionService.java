package com.example.javi.instawatch.fcm;

import android.app.IntentService;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by Javi on 25/02/2017.
 */

public class EnviarPeticionService extends IntentService {
    private static final String METODO_POST = "POST";
    private static final String METODO_GET = "GET";

    private static String SENDER_ID = "1005137725735";
    //AtomicLong msgId = new AtomicLong(new Random().nextLong());

    public static final String TAG = EnviarPeticionService.class.getSimpleName();

    public EnviarPeticionService() {
        super(TAG);
    }


    @Override
    protected void onHandleIntent(Intent intent) {
        String usuarioSeleccionado = intent.getStringExtra("usuarioSeleccionado");
        String contrasenia = intent.getStringExtra("contrasenia");
        boolean rolPersonal = intent.getBooleanExtra("rolPersonal",false);
        boolean rolDominios = intent.getBooleanExtra("rolDominios",false);
        boolean rolVideos = intent.getBooleanExtra("rolVideos",false);

        Log.d(TAG, "Usuario destinatario:" + usuarioSeleccionado + ",contraseña:" + contrasenia);
        Log.d(TAG,"p:"+rolPersonal+" do:"+rolDominios+" vi:"+rolVideos);
        SharedPreferences sharedPreferences = getApplication().getSharedPreferences("My preferences", MODE_PRIVATE);
        String usuario = sharedPreferences.getString("usuario", null);
        FirebaseMessaging fm = FirebaseMessaging.getInstance();
        String id = "m-" + UUID.randomUUID().toString();
        Log.d("1","ID_MESSAGE:"+id);
        AtomicInteger msgId = new AtomicInteger();

        //Upstream Message->Enviar mensaje a FCM, que lo envia al servidor java y que tiene un listener escuchando mensajes upstream
        //cuando el servidor recibe el upstream, envia un upstream al usuario correspondiente
        fm.send(new RemoteMessage.Builder(SENDER_ID + "@gcm.googleapis.com")
                .setMessageId(Integer.toString(msgId.incrementAndGet()))
                .addData("action", "PETICION")
                //usuario es quien envia el mensaje
                .addData("remitente", usuario)
                .addData("destinatario", usuarioSeleccionado)
                .addData("contrasenia", contrasenia)
                .addData("rolPersonal",String.valueOf(rolPersonal))
                .addData("rolDominios",String.valueOf(rolDominios))
                .addData("rolVideos",String.valueOf(rolVideos))
                .build());
    }
}
