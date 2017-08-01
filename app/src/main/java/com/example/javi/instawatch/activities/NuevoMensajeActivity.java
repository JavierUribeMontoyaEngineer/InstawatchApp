package com.example.javi.instawatch.activities;

import android.app.IntentService;
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
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.javi.instawatch.R;
import com.example.javi.instawatch.controlador.ControladorUsuarios;
import com.example.javi.instawatch.fcm.EnviarMensajeVideoService;
import com.example.javi.instawatch.fcm.EnviarPeticionService;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class NuevoMensajeActivity extends AppCompatActivity {
    private String usuarioSeleccionado, usuario;
    private ListView listaUsuarios;
    private ArrayAdapter<String> adaptadorListaUsuarios, adaptadorListaVideos;
    private List<String> usuariosBD;
    private ControladorUsuarios controladorUsuarios;
    static final int SELECCIONAR_VIDEOS = 1;  // The request code
    ArrayList<String> videosSeleccionados;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        usuario = getIntent().getStringExtra("usuario");
        videosSeleccionados = new ArrayList<String>();
        setContentView(R.layout.activity_nuevo_mensaje);
        TextView txtDe = (TextView) findViewById(R.id.textView8);
        txtDe.setText(usuario);
        controladorUsuarios = new ControladorUsuarios();
        usuariosBD = controladorUsuarios.getUsuarios(usuario);
        final TextView txtPara = (TextView) findViewById(R.id.editTextPara);

        listaUsuarios = (ListView) findViewById(R.id.listaUsuariosPara);
        listaUsuarios.setTextFilterEnabled(true);
        adaptadorListaUsuarios = new
                ArrayAdapter<String>(NuevoMensajeActivity.this, android.R.layout.simple_list_item_1, usuariosBD);
        txtPara.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                listaUsuarios.setVisibility(View.VISIBLE);
            }
        });
        listaUsuarios.setAdapter(adaptadorListaUsuarios);

        //Actualizacion de la lista a medida que se escribe en usuarioTxtView
        txtPara.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
                listaUsuarios.setVisibility(View.VISIBLE);
                NuevoMensajeActivity.this.adaptadorListaUsuarios.getFilter().filter(arg0);
            }
            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
                                          int arg3) {}
            @Override
            public void afterTextChanged(Editable arg0) {}
        });

        listaUsuarios.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                usuarioSeleccionado = (String) parent.getItemAtPosition(position);
                txtPara.setText(usuarioSeleccionado);
                listaUsuarios.setVisibility(View.GONE);
            }
        });

        Button btnSeleccionarVideos = (Button) findViewById(R.id.btnSeleccionarVideos);
        btnSeleccionarVideos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), MultipleVideosChoiceActivity.class);
                intent.putExtra("usuario",usuario);
                startActivityForResult(intent, SELECCIONAR_VIDEOS);
            }
        });

        Button enviarButton = (Button) findViewById(R.id.btnEnviarMensaje);
        enviarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent enviarMensajeVideoService = new Intent(view.getContext(), EnviarMensajeVideoService.class);
                enviarMensajeVideoService.putExtra("usuarioSeleccionado", usuarioSeleccionado);
                enviarMensajeVideoService.putStringArrayListExtra("videosSeleccionados",videosSeleccionados);
                startService(enviarMensajeVideoService);

                Toast toast = Toast.makeText(getApplicationContext(), "Mensaje enviado correctamente", Toast.LENGTH_SHORT);
                toast.show();

            }
        });


    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == SELECCIONAR_VIDEOS) {
            if (resultCode == RESULT_OK) {
                //Recibimos los videos seleccionados en MultipleChoiceActivity
                videosSeleccionados = data.getStringArrayListExtra("videosSeleccionados");
                ListView listaVideosSeleccionados = (ListView) findViewById(R.id.listaVideosSeleccionados);
                listaVideosSeleccionados.setVisibility(View.VISIBLE);

                adaptadorListaVideos = new
                        ArrayAdapter<String>(NuevoMensajeActivity.this, android.R.layout.simple_list_item_1, videosSeleccionados);
                listaVideosSeleccionados.setAdapter(adaptadorListaVideos);
            }
        }
    }
}
