package com.example.javi.instawatch.fcm;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.example.javi.instawatch.R;
import com.example.javi.instawatch.activities.ColaboracionesActivity;
import com.example.javi.instawatch.activities.MensajesActivity;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import static android.content.Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT;

/**
 * Created by Javi on 26/02/2017.
 */

//Servicio para recibir los mensajes
public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "examplemessage";

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        String from = remoteMessage.getFrom();
        Log.d("1", "ON MESSAGE RECEIVED" + from);
        if (remoteMessage.getNotification() != null) {
            Log.d(TAG, "notificacion:" + remoteMessage.getNotification().getBody());
            mostrarNotificacion(remoteMessage.getNotification().getTitle(),
                    remoteMessage.getNotification().getBody());

        }


    }

    @Override
    public void onMessageSent(String s) {
        Log.d("1", "Enviado correcto");
    }

    @Override
    public void onSendError(String s, Exception e) {
        Log.d("1", "Error en el envio");
    }

    private void mostrarNotificacion(String title, String body) {
        Intent intent = new Intent(this, MensajesActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.setFlags(FLAG_ACTIVITY_BROUGHT_TO_FRONT);
        intent.setAction(ACTIVITY_SERVICE);
        int code = (int) System.currentTimeMillis();
        PendingIntent pendingIntent =
                PendingIntent.getActivity(this, code, intent, PendingIntent.FLAG_ONE_SHOT);
        Uri sonidoUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificacionBuilder = new NotificationCompat.Builder(this)
                .setContentTitle(title).setContentText(body).setAutoCancel(true).
                        setSound(sonidoUri).setContentIntent(pendingIntent);
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(0, notificacionBuilder.build());
    }

}
