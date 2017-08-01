package com.example.javi.instawatch.adaptadores;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.javi.instawatch.R;
import com.example.javi.instawatch.activities.ColaboracionesActivity;
import com.example.javi.instawatch.controlador.ControladorColaboraciones;
import com.example.javi.instawatch.fcm.EnviarAceptacionPeticionService;
import com.example.javi.instawatch.modeloDTO.MensajeVideoDTO;
import com.example.javi.instawatch.modeloDTO.PeticionDTO;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Javi on 21/02/2017.
 */

public class ListaMensajes extends ArrayAdapter<MensajeVideoDTO> {
    private final Activity context;
    private List<MensajeVideoDTO> mensajes;
    private ControladorColaboraciones controladorColaboraciones;

    public ListaMensajes(Activity context, List<MensajeVideoDTO> peticiones) {
        super(context, R.layout.list_single_mensaje, peticiones);
        this.context = context;
        this.mensajes = peticiones;
        controladorColaboraciones = new ControladorColaboraciones();

    }

    @Override
    public View getView(final int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.list_single_mensaje, null, true);
        TextView txtRemitente = (TextView) rowView.findViewById(R.id.remitenteMensaje);
        final MensajeVideoDTO mensajeVideoDTO = mensajes.get(position);
        txtRemitente.setText(mensajeVideoDTO.getRemitente());
        final ArrayList<String> videos = mensajeVideoDTO.getVideos();
        String[] arrayVideos = videos.toArray(new String[videos.size()]);
        ImageButton info = (ImageButton) rowView.findViewById(R.id.infoMensaje);
        info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Dialog dialog = new Dialog(view.getContext());
                dialog.setContentView(R.layout.info_mensaje_dialog);

                dialog.show();
                ListView listaVideos = (ListView) dialog.findViewById(R.id.listaVideosDialog);
                ArrayAdapter<String> adapterVideos = new ArrayAdapter<String>(dialog.getContext(), android.R.layout.simple_spinner_item, videos);
                listaVideos.setAdapter(adapterVideos);

            }
        });

        // / Spinner spinnerVideos = (Spinner) rowView.findViewById(R.id.spinnerVideos);
       // ArrayAdapter<String> adapterVideos = new ArrayAdapter<String>(rowView.getContext(), android.R.layout.simple_spinner_item, arrayVideos);
        //adapterVideos.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
       // spinnerVideos.setAdapter(adapterVideos);
        Button borrarMensaje = (Button) rowView.findViewById(R.id.borrarMensaje);
        borrarMensaje.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Borrado de la BD
                controladorColaboraciones.borrarMensaje(mensajeVideoDTO);
                //Borrado de la lista logica de la grafica
                mensajes.remove(position);
                //Actualizacion en la grafica
                notifyDataSetChanged();
            }
        });
        return rowView;
    }
}
