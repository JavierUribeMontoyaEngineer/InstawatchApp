package com.example.javi.instawatch.modelo;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;

import com.example.javi.instawatch.activities.LoginActivity;



import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Javi on 21/02/2017.
 */
public class PeticionRest extends AsyncTask<Object, Integer, String> {
    String metodo = "";
    public static final String METODO_GET = "GET";
    public static final String METODO_POST = "POST";
    public static final String METODO_PUT = "PUT";
    public static final String METODO_DELETE= "DELETE";

    private Activity activity;
    private AcabadoAsynTaskListener callback;

    public PeticionRest() {
    }

    public PeticionRest(Activity activity, AcabadoAsynTaskListener cb) {
        this.activity = activity;
        this.callback = cb;
    }

    private String readStream(InputStream is) {
        try {
            ByteArrayOutputStream bo = new ByteArrayOutputStream();
            int i = is.read();
            while (i != -1) {
                bo.write(i);
                i = is.read();
            }
            return bo.toString();
        } catch (IOException e) {
            return "";
        }
    }


    @Override
    protected String doInBackground(Object... parametros) {
        String result = "";
        try {
            //Thread.sleep(2000);
            URL url = (URL) parametros[0];
            metodo = (String) parametros[1];

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod(metodo);
            conn.setAllowUserInteraction(false);
            conn.setInstanceFollowRedirects(true);
            //Si es post se añaden unos flags distintos al get
            if (metodo == METODO_POST || metodo == METODO_PUT || metodo == METODO_DELETE) {
                conn.setDoInput(true);
                conn.setDoOutput(true);
                conn.setInstanceFollowRedirects(false);
                conn.setRequestProperty("Content-Type", "application/json");
                conn.setRequestProperty("charset", "utf-8");
                conn.setUseCaches(false);
            }
            conn.connect();
            //Si es post se mandan los parametros JSON
            if (metodo == METODO_POST || metodo == METODO_PUT || metodo == METODO_DELETE) {
                OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
                wr.write((String) parametros[2]);
                Log.d("5", (String) parametros[2]);
                wr.flush();
                result = String.valueOf(conn.getResponseCode());

            }//Si es un metodo GET se leen los datos que vienen en un String
            else if (metodo == METODO_GET) {
                InputStream in = new BufferedInputStream(conn.getInputStream());
                result = readStream(in);
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }

        //El resultado es enviado al metodo onPostExecute como parametro
        return result;
    }

    @Override
    protected void onPostExecute(String result) {
        if (activity != null) {
            Log.d("1","Result"+result);
            ((LoginActivity) activity).showProgress(false);
            callback.onTaskComplete(result);


        }

    }

    @Override
    protected void onCancelled() {
        if (activity != null) {
            Log.d("1","Fin login");
            ((LoginActivity) activity).showProgress(false);
        }
    }
}
