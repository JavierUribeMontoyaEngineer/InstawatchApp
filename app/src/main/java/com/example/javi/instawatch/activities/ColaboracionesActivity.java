package com.example.javi.instawatch.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.javi.instawatch.R;
import com.example.javi.instawatch.adaptadores.ListaColaboradores;
import com.example.javi.instawatch.adaptadores.ListaPeticiones;
import com.example.javi.instawatch.controlador.ControladorColaboraciones;
import com.example.javi.instawatch.fcm.EnviarPeticionService;
import com.example.javi.instawatch.controlador.ControladorUsuarios;
import com.example.javi.instawatch.modeloDTO.ColaboradorDTO;

import java.net.HttpURLConnection;
import java.util.List;

public class ColaboracionesActivity extends AppCompatActivity {
    private EditText usuarioTxtview, contraseniaTxtview;
    private ListView listaUsuarios;
    private ListView listaColaboradores;

    private ArrayAdapter<String> adaptadorListaUsuarios;
    private ListaColaboradores adaptadorListaColaboradores;
    private List<String> usuariosBD;
    private ControladorUsuarios controladorUsuarios;
    // private EnviarPeticionService controladorColaboraciones;
    private Button enviarButton;
    String usuarioSeleccionado, usuario;
    private CheckBox checkBoxPersonal, checkBoxDominios, checkBoxVideos;
    private List<ColaboradorDTO> colaboradores;
    private ControladorColaboraciones controladorColaboraciones;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_colaboraciones);
        usuario = getIntent().getStringExtra("usuario");
        controladorUsuarios = new ControladorUsuarios();
        controladorColaboraciones = new ControladorColaboraciones();
        usuarioTxtview = (EditText) findViewById(R.id.usuarioTxt);
        contraseniaTxtview = (EditText) findViewById(R.id.contraseniaTxt);
        usuariosBD = controladorUsuarios.getUsuarios(usuario);
        listaUsuarios = (ListView) findViewById(R.id.listaUsuarios);
        enviarButton = (Button) findViewById(R.id.enviarButton);
        checkBoxPersonal = (CheckBox) findViewById(R.id.checkBox);
        checkBoxDominios = (CheckBox) findViewById(R.id.checkBox2);
        checkBoxVideos = (CheckBox) findViewById(R.id.checkBox3);

        colaboradores = controladorColaboraciones.getColaboradores(usuario);
        adaptadorListaColaboradores = new ListaColaboradores(ColaboracionesActivity.this, colaboradores);
        listaColaboradores = (ListView) findViewById(R.id.listaColaboradores);
        listaColaboradores.setAdapter(adaptadorListaColaboradores);

        listaUsuarios.setTextFilterEnabled(true);
        adaptadorListaUsuarios = new
                ArrayAdapter<String>(ColaboracionesActivity.this, android.R.layout.simple_list_item_1, usuariosBD);
        listaUsuarios.setAdapter(adaptadorListaUsuarios);
        //solo cuando se pone el foco en el usuarioTxtView se muestra la lista de usuarios
        usuarioTxtview.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                listaUsuarios.setVisibility(View.VISIBLE);
            }
        });
        contraseniaTxtview.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                listaUsuarios.setVisibility(View.GONE);
            }
        });


        //Actualizacion de la lista a medida que se escribe en usuarioTxtView
        usuarioTxtview.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
                listaUsuarios.setVisibility(View.VISIBLE);
                ColaboracionesActivity.this.adaptadorListaUsuarios.getFilter().filter(arg0);
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
                                          int arg3) {
            }

            @Override
            public void afterTextChanged(Editable arg0) {
            }
        });

        listaUsuarios.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                usuarioSeleccionado = (String) parent.getItemAtPosition(position);
                usuarioTxtview.setText(usuarioSeleccionado);
                listaUsuarios.setVisibility(View.GONE);
            }
        });

        enviarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (controladorColaboraciones.isColaborador(usuario,usuarioSeleccionado)) {
                    Toast toast = Toast.makeText(getApplicationContext(), "Peticion no enviada. El usuario " + usuarioSeleccionado + " ya es tu colaborador", Toast.LENGTH_SHORT);
                    toast.show();
                } else {
                    Intent enviarPeticionService = new Intent(view.getContext(), EnviarPeticionService.class);
                    enviarPeticionService.putExtra("contrasenia", contraseniaTxtview.getText().toString());
                    enviarPeticionService.putExtra("usuarioSeleccionado", usuarioSeleccionado);
                    enviarPeticionService.putExtra("rolPersonal", checkBoxPersonal.isChecked());
                    enviarPeticionService.putExtra("rolDominios", checkBoxDominios.isChecked());
                    enviarPeticionService.putExtra("rolVideos", checkBoxVideos.isChecked());
                    startService(enviarPeticionService);

                    Toast toast = Toast.makeText(getApplicationContext(), "Peticion enviada correctamente", Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
        });

    }
}
