package com.example.javi.instawatch.fcm;

import android.app.IntentService;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.RemoteMessage;

import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by Javi on 25/02/2017.
 */

public class EnviarAceptacionPeticionService extends IntentService {
    private static final String METODO_POST = "POST";
    private static final String METODO_GET = "GET";

    private static String SENDER_ID = "1005137725735";

    public static final String TAG = EnviarAceptacionPeticionService.class.getSimpleName();

    public EnviarAceptacionPeticionService() {
        super(TAG);
    }


    @Override
    protected void onHandleIntent(Intent intent) {
        String destinatario = intent.getStringExtra("destinatario");
        String contrasenia = intent.getStringExtra("contrasenia");
        boolean rolPersonal = intent.getBooleanExtra("rolPersonal",false);
        boolean rolDominios = intent.getBooleanExtra("rolDominios",false);
        boolean rolVideos = intent.getBooleanExtra("rolVideos",false);
        Log.d("1","roles:"+rolPersonal+","+rolDominios+","+rolVideos);
        String idPeticion = intent.getStringExtra("idPeticion");
        SharedPreferences sharedPreferences = getApplication().getSharedPreferences("My preferences", MODE_PRIVATE);
        String usuario = sharedPreferences.getString("usuario", null);
        FirebaseMessaging fm = FirebaseMessaging.getInstance();
       // String id = "m-" + UUID.randomUUID().toString();
       Log.d("1","Remitente:"+usuario+",Destinatario:"+destinatario);
        //Upstream Message->Enviar mensaje a FCM, que lo envia al servidor java y que tiene un listener escuchando mensajes upstream
        //cuando el servidor recibe el upstream, envia un upstream al usuario correspondiente
        AtomicInteger msgId = new AtomicInteger();
        fm.send(new RemoteMessage.Builder(SENDER_ID + "@gcm.googleapis.com")
                .setMessageId(Integer.toString(msgId.incrementAndGet()))
                .addData("action", "ACEPTACION_PETICION")
                //usuario es quien envia el mensaje
                .addData("remitente", usuario)
                .addData("destinatario", destinatario)
                .addData("idPeticion",String.valueOf(idPeticion))
                .addData("contrasenia", contrasenia)
                .addData("rolPersonal",String.valueOf(rolPersonal))
                .addData("rolDominios",String.valueOf(rolDominios))
                .addData("rolVideos",String.valueOf(rolVideos))
                .build());
    }
}
