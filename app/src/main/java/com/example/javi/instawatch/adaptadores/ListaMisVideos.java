package com.example.javi.instawatch.adaptadores;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.javi.instawatch.R;
import com.example.javi.instawatch.modeloDTO.VideoDTO;

import java.util.List;

/**
 * Created by Javi on 21/02/2017.
 */

public class ListaMisVideos extends ArrayAdapter<VideoDTO> {
    private final Activity context;
    private List<VideoDTO> videos;

    public ListaMisVideos(Activity context, List<VideoDTO> videos) {
        super(context,R.layout.list_single_video_mis_videos,videos);
        this.context = context;
        this.videos = videos;

    }


    public String formatearTiempo(int tiempo){
        String tiempoFormateado = "";
        if (tiempo == 0)
            tiempoFormateado = "00:";
        else {
            if (tiempo < 10)
                tiempoFormateado = "0"+String.valueOf(tiempo)+":";
            else
                tiempoFormateado = String.valueOf(tiempo)+":";
        }
        return tiempoFormateado;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.list_single_video_mis_videos, null, true);
        TextView txtTitle = (TextView) rowView.findViewById(R.id.txtTitulo);
        VideoDTO videoYoutubeActual = videos.get(position);
        txtTitle.setText(videoYoutubeActual.getTitulo());

        TextView txtpropietario= (TextView) rowView.findViewById(R.id.textUsuarioVideo);
        txtpropietario.setText(videoYoutubeActual.getUsuario());
        TextView txtDuracion = (TextView) rowView.findViewById(R.id.duracionTxt);
        int horas = videoYoutubeActual.getDuracion()/3600;
        int mins = videoYoutubeActual.getDuracion()/60;
        int secs = videoYoutubeActual.getDuracion()%60;
        String tiempo = formatearTiempo(horas) + formatearTiempo(mins) + formatearTiempo(secs);
        tiempo = tiempo.substring(0,tiempo.length()-1);
        txtDuracion.setText(tiempo);
        return rowView;
    }
}
