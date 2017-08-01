package com.example.javi.instawatch.adaptadores;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.example.javi.instawatch.R;
import com.example.javi.instawatch.controlador.ControladorColaboraciones;
import com.example.javi.instawatch.fcm.EnviarAceptacionPeticionService;
import com.example.javi.instawatch.modeloDTO.ColaboradorDTO;
import com.example.javi.instawatch.modeloDTO.PeticionDTO;

import java.util.List;

/**
 * Created by Javi on 21/02/2017.
 */

public class ListaColaboradores extends ArrayAdapter<ColaboradorDTO> {
    private final Activity context;
    private List<ColaboradorDTO> colaboradores;
    private ControladorColaboraciones controladorColaboraciones;

    public ListaColaboradores(Activity context, List<ColaboradorDTO> colaboradores) {
        super(context, R.layout.list_single_colaborador, colaboradores);
        this.context = context;
        this.colaboradores = colaboradores;
        controladorColaboraciones = new ControladorColaboraciones();
    }

    @Override
    public View getView(final int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.list_single_colaborador, null, true);
        TextView txtLogin = (TextView) rowView.findViewById(R.id.txtLogin);
        TextView txtNombre = (TextView) rowView.findViewById(R.id.txtNombre);

        final ColaboradorDTO colaboradorDTO = colaboradores.get(position);
        txtLogin.setText(colaboradorDTO.getLogin());
        txtNombre.setText(colaboradorDTO.getNombre());
        Button rechazarBtn = (Button) rowView.findViewById(R.id.btnRechazarCol);
        rechazarBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                controladorColaboraciones.borrarColaboracion(colaboradorDTO);
                colaboradores.remove(position);
                //Actualizacion en la grafica
                notifyDataSetChanged();
            }
        });

        return rowView;
    }
}
