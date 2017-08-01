package com.example.javi.instawatch.activities;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.javi.instawatch.R;
import com.example.javi.instawatch.adaptadores.ListaMensajes;
import com.example.javi.instawatch.adaptadores.ListaMisVideos;
import com.example.javi.instawatch.adaptadores.ListaPeticiones;
import com.example.javi.instawatch.controlador.ControladorColaboraciones;
import com.example.javi.instawatch.modeloDTO.MensajeVideoDTO;
import com.example.javi.instawatch.modeloDTO.PeticionDTO;

import java.util.List;


public class MensajesActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private List<PeticionDTO> peticiones;
    private List<MensajeVideoDTO> mensajes;
    private String usuario;
    private ListaPeticiones adapterPeticiones;
    private ListaMensajes adapterMensajes;
    private ControladorColaboraciones controladorColaboraciones;
    private PeticionDTO peticionSeleccionada;
    private ListView listaPeticiones, listaMensajes;
    private SwipeRefreshLayout swipeContainer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mensajes);

        setContentView(R.layout.activity_mensajes);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);

        usuario = getIntent().getStringExtra("usuario");
        controladorColaboraciones = new ControladorColaboraciones();
        peticiones = controladorColaboraciones.getPeticiones(usuario);
        mensajes = controladorColaboraciones.getMensajesVideo(usuario);
        adapterPeticiones = new ListaPeticiones(MensajesActivity.this, peticiones);
        listaPeticiones = (ListView) findViewById(R.id.listPeticiones);
        listaPeticiones.setAdapter(adapterPeticiones);
        listaPeticiones.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                peticionSeleccionada = peticiones.get(position);
                Log.d("1", peticionSeleccionada.toString());
            }
        });

        //Refrescar ventana al deslizar el dedo hacia arriba
        swipeContainer = (SwipeRefreshLayout) findViewById(R.id.ventanaMensajes);
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                peticiones = controladorColaboraciones.getPeticiones(usuario);
                mensajes = controladorColaboraciones.getMensajesVideo(usuario);
                adapterPeticiones = new ListaPeticiones(MensajesActivity.this, peticiones);
                listaPeticiones.setAdapter(adapterPeticiones);
                adapterMensajes = new ListaMensajes(MensajesActivity.this, mensajes);
                listaMensajes.setAdapter(adapterMensajes);
                swipeContainer.setRefreshing(false);
            }
        });


        listaMensajes = (ListView) findViewById(R.id.listaMensajesVideo);
        adapterMensajes = new ListaMensajes(MensajesActivity.this, mensajes);
        listaMensajes.setAdapter(adapterMensajes);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.navigation, menu);
        return true;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        return false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        //Opciones de los 3 puntitos arriba derecha
        switch (id) {
            case R.id.action_nuevo_mensaje:
                Intent intent = new Intent(this, NuevoMensajeActivity.class);
                intent.putExtra("usuario", usuario);
                startActivity(intent);
                break;
        }

        return super.onOptionsItemSelected(item);
    }
}
