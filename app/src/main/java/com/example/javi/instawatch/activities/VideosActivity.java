package com.example.javi.instawatch.activities;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.javi.instawatch.Config;
import com.example.javi.instawatch.R;
import com.example.javi.instawatch.adaptadores.ListaMisVideos;
import com.example.javi.instawatch.adaptadores.ListaVideosYoutube;
import com.example.javi.instawatch.controlador.ControladorUsuarios;
import com.example.javi.instawatch.controlador.ControladorVideos;
import com.example.javi.instawatch.modeloDTO.VideoDTO;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;

import java.io.Serializable;
import java.util.List;

public class VideosActivity extends AppCompatActivity {
    static final int ADD_VIDEO = 1;  // The request code

    private ControladorVideos controladorVideos;
    private String usuario;
    private ListView listaVideosView;
    private VideoDTO videoSeleccionado;
    private List<VideoDTO> misVideos;
    private ListaMisVideos adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_videos);
        usuario = getIntent().getStringExtra("usuario");
        controladorVideos = new ControladorVideos();
        listaVideosView = (ListView) findViewById(R.id.listMisVideos);

        //Listar videos
        misVideos = controladorVideos.getVideos(usuario);
        adapter = new ListaMisVideos(VideosActivity.this, misVideos);
        listaVideosView.setAdapter(adapter);
        listaVideosView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Log.d("1","click");
                videoSeleccionado = misVideos.get(position);
                Intent myIntent = new Intent(view.getContext(), AddVideoActivity.class);
                myIntent.putExtra("usuario", usuario);
                myIntent.putExtra("urlVideo", videoSeleccionado.getUrl());
                myIntent.putExtra("titulo",videoSeleccionado.getTitulo());
                myIntent.putExtra("videoEditado",(VideoDTO) videoSeleccionado);
                startActivityForResult(myIntent,ADD_VIDEO);
            }
        });

        //Accion Añadir video
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(view.getContext(), AddVideoActivity.class);
                myIntent.putExtra("usuario", usuario);
                startActivityForResult(myIntent,ADD_VIDEO);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request we're responding to
        if (requestCode == ADD_VIDEO) {
            // Make sure the request was successful
            if (resultCode == RESULT_CANCELED) {
                Log.d("1","nuevo video");
               // VideoDTO nuevoVideo = (VideoDTO) getIntent().getSerializableExtra("nuevoVideo");
                Intent refresh = new Intent(this, VideosActivity.class);
                refresh.putExtra("usuario",usuario);
                startActivity(refresh);
                finish();
            }
        }
    }
}
