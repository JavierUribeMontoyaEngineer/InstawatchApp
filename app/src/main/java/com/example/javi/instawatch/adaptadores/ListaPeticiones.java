package com.example.javi.instawatch.adaptadores;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.javi.instawatch.R;
import com.example.javi.instawatch.controlador.ControladorColaboraciones;
import com.example.javi.instawatch.fcm.EnviarAceptacionPeticionService;
import com.example.javi.instawatch.fcm.EnviarPeticionService;
import com.example.javi.instawatch.modeloDTO.PeticionDTO;
import com.example.javi.instawatch.modeloDTO.VideoDTO;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Javi on 21/02/2017.
 */

public class ListaPeticiones extends ArrayAdapter<PeticionDTO> {
    private final Activity context;
    private List<PeticionDTO> peticiones;
    private ControladorColaboraciones controladorColaboraciones;

    public ListaPeticiones(Activity context, List<PeticionDTO> peticiones) {
        super(context, R.layout.list_single_peticion, peticiones);
        this.context = context;
        this.peticiones = peticiones;
        controladorColaboraciones = new ControladorColaboraciones();

    }

    @Override
    public View getView(final int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        final View rowView = inflater.inflate(R.layout.list_single_peticion, null, true);
        TextView txtRemitente = (TextView) rowView.findViewById(R.id.remitente);
        final PeticionDTO peticionDTO = peticiones.get(position);
        txtRemitente.setText(peticionDTO.getRemitente());
        Button aceptBtn = (Button) rowView.findViewById(R.id.btnAceptar);
        aceptBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent enviarAceptacionService = new Intent(view.getContext(), EnviarAceptacionPeticionService.class);
                enviarAceptacionService.putExtra("destinatario", peticionDTO.getRemitente());
                enviarAceptacionService.putExtra("idPeticion", String.valueOf(peticionDTO.getId()));
                enviarAceptacionService.putExtra("contrasenia", peticionDTO.getContrasenia());
                Log.d("1", "Peticion original a mandar:" + peticionDTO.toString());
                enviarAceptacionService.putExtra("rolPersonal", peticionDTO.isRolPersonal());
                enviarAceptacionService.putExtra("rolDominios", peticionDTO.isRolDominios());
                enviarAceptacionService.putExtra("rolVideos", peticionDTO.isRolVideos());

                view.getContext().startService(enviarAceptacionService);
                peticiones.remove(position);
                notifyDataSetChanged();
            }


        });

        Button rechazBtn = (Button) rowView.findViewById(R.id.btnRechazar);
        rechazBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Borrado de la BD
                controladorColaboraciones.borrarPeticion(peticionDTO);
                //Borrado de la lista logica de la grafica
                peticiones.remove(position);
                //Actualizacion en la grafica
                notifyDataSetChanged();
            }
        });


        ImageButton info = (ImageButton) rowView.findViewById(R.id.infoPeticion);
        info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Dialog dialog = new Dialog(view.getContext());
                dialog.setContentView(R.layout.info_peticion_dialog);
                dialog.show();
                TextView txtCont = (TextView) dialog.findViewById(R.id.txtContrasenia);
                txtCont.setText(peticionDTO.getContrasenia());
                CheckBox chPersonal = (CheckBox) dialog.findViewById(R.id.checkBoxPersonal);
                CheckBox chDominios = (CheckBox) dialog.findViewById(R.id.checkBoxDominios);
                CheckBox chVideos = (CheckBox) dialog.findViewById(R.id.checkBoxVideos);

                if (peticionDTO.isRolPersonal()) {
                    chPersonal.setChecked(true);
                    Log.d("1","is rol personal");
                }
                if (peticionDTO.isRolDominios())
                    chDominios.setChecked(true);
                if (peticionDTO.isRolVideos())
                    chVideos.setChecked(true);

            }
        });
        return rowView;
    }
}
