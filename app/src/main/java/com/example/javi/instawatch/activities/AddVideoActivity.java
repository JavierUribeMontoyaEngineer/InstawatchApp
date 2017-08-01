package com.example.javi.instawatch.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.javi.instawatch.Config;
import com.example.javi.instawatch.R;
import com.example.javi.instawatch.controlador.ControladorVideos;
import com.example.javi.instawatch.adaptadores.ListaVideosYoutube;
import com.example.javi.instawatch.modelo.VideoYoutube;
import com.example.javi.instawatch.modeloDTO.VideoDTO;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;

import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.ResourceId;
import com.google.api.services.youtube.model.SearchListResponse;
import com.google.api.services.youtube.model.SearchResult;
import com.google.api.services.youtube.model.Thumbnail;

import org.w3c.dom.Text;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class AddVideoActivity extends YouTubeBaseActivity implements YouTubePlayer.OnInitializedListener {
    private YouTubePlayerView youTubeView;
    private EditText urlTxtView, tituloTxtView;
    private Button playButton;
    private Button borrarButton;
    private Button guardarButton;
    private ControladorVideos controladorVideos;
    private YouTubePlayer.OnInitializedListener initializedListener;
    private static final long NUMBER_OF_VIDEOS_RETURNED = 10;
    boolean esBusqueda = false;
    boolean esYoutubeURL = false;
    private ListView listaVideosView;
    private VideoYoutube videoSeleccionado;
    private String url;
    private String usuario;
    private String titulo;
    private String urlVideo;
    private boolean esEdicion = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_video);
        usuario = getIntent().getStringExtra("usuario");
        youTubeView = (YouTubePlayerView) findViewById(R.id.youtube_player_add);
        listaVideosView = (ListView) findViewById(R.id.listVideos);
        tituloTxtView = (EditText) findViewById(R.id.tituloVideo);
        urlTxtView = (EditText) findViewById(R.id.urlYoutube);
        playButton = (Button) findViewById(R.id.buttonPlay);
        borrarButton = (Button) findViewById(R.id.buttonBorrrar);
        guardarButton = (Button) findViewById(R.id.buttonGuardar);
        controladorVideos = new ControladorVideos();
        titulo = getIntent().getStringExtra("titulo");
        if (titulo != null) {
            TextView encabezadoView = (TextView) findViewById(R.id.textView3);
            encabezadoView.setText("Editar Vídeo");
            urlVideo = getIntent().getStringExtra("urlVideo");
            urlTxtView.setText(urlVideo);
            tituloTxtView.setText(titulo);
            playButton.setText("Reproducir");
            playButton.setEnabled(true);
            guardarButton.setEnabled(true);
            esYoutubeURL = true;
            esEdicion = true;
        }
        //Cambiar el texto del boton buscar/reproducir segun la Regex
        urlTxtView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                guardarButton.setEnabled(false);
                url = urlTxtView.getText().toString();
                esBusqueda = url.matches("@youtube .*");
                esYoutubeURL = url.matches("https://www.youtube.com/watch\\?v=.*");
                if (esBusqueda) {
                    playButton.setText("Buscar");
                    playButton.setEnabled(true);
                } else if (esYoutubeURL) {
                    playButton.setText("Reproducir");
                    playButton.setEnabled(true);
                } else {
                    playButton.setEnabled(false);
                }
            }
        });

        //ACCION Boton Borrar
        borrarButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                int codeResponse=-1;
                if (!esEdicion) {
                    urlTxtView.setText("");
                    tituloTxtView.setText("");
                }else{
                    VideoDTO videoDTO = (VideoDTO) getIntent().getSerializableExtra("videoEditado");
                    codeResponse = controladorVideos.borrarVideo(videoDTO);
                    if (codeResponse == HttpURLConnection.HTTP_ACCEPTED) {
                        Toast.makeText(AddVideoActivity.this, "Video borrado correctamente", Toast.LENGTH_LONG).show();
                        Intent resultadoIntent = new Intent();
                        setResult(Activity.RESULT_CANCELED, resultadoIntent);
                        finish();
                    } else if (codeResponse == HttpURLConnection.HTTP_BAD_REQUEST) {
                        Toast.makeText(AddVideoActivity.this, "Fallo al borrar el video", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });

        //ACCION Boton Reproducir
        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                guardarButton.setEnabled(true);
                if (youTubePlayer != null) {
                    youTubePlayer.release();
                }

                YoutubeSearchAPI searchAPI = new YoutubeSearchAPI();
                if (esBusqueda) {
                    try {
                        //Ocultar teclado al pulsar buscar
                        InputMethodManager inputManager = (InputMethodManager)
                                getSystemService(Context.INPUT_METHOD_SERVICE);
                        inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                                InputMethodManager.HIDE_NOT_ALWAYS);

                        final List<VideoYoutube> videosYoutube = searchAPI.execute().get();
                        ListaVideosYoutube adapter = new
                                ListaVideosYoutube(AddVideoActivity.this, videosYoutube);
                        listaVideosView.setAdapter(adapter);
                        listaVideosView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                            @Override
                            public void onItemClick(AdapterView<?> parent, View view,
                                                    int position, long id) {
                                videoSeleccionado = videosYoutube.get(position);
                                urlTxtView.setText(videosYoutube.get(position).getUrl());
                                if (youTubePlayer != null) {
                                    youTubePlayer.release();
                                }
                                youTubeView.initialize(Config.YOUTUBE_API_KEY, (YouTubePlayer.OnInitializedListener) view.getContext());
                            }
                        });

                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    }

                } else if (esYoutubeURL) {
                    youTubeView.initialize(Config.YOUTUBE_API_KEY, (YouTubePlayer.OnInitializedListener) view.getContext());
                }
            }
        });

        //ACCION Boton guardar
        guardarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int codeResponse=-1;
                if (esEdicion) {
                    VideoDTO videoDTO = (VideoDTO) getIntent().getSerializableExtra("videoEditado");
                    videoDTO.setTitulo(tituloTxtView.getText().toString());
                    videoDTO.setUrl(urlTxtView.getText().toString());
                    if (youTubePlayer != null)
                        videoDTO.setDuracion(youTubePlayer.getDurationMillis() / 1000);
                    codeResponse = controladorVideos.editarVideo(videoDTO);
                } else {
                    VideoDTO videoDTO = new VideoDTO(tituloTxtView.getText().toString(), videoSeleccionado.getUrl(), youTubePlayer.getDurationMillis() / 1000, usuario);
                    codeResponse = controladorVideos.guardarVideo(videoDTO);
                }
                System.out.println("Codigo:" + codeResponse);
                if (codeResponse == HttpURLConnection.HTTP_ACCEPTED) {
                    Toast.makeText(AddVideoActivity.this, "Video guardado correctamente", Toast.LENGTH_LONG).show();
                    Intent resultadoIntent = new Intent();
                    setResult(Activity.RESULT_CANCELED, resultadoIntent);
                    finish();
                } else if (codeResponse == HttpURLConnection.HTTP_BAD_REQUEST) {
                    Toast.makeText(AddVideoActivity.this, "Fallo al guardar el video", Toast.LENGTH_LONG).show();
                }

            }
        });
    }

    private YouTubePlayer youTubePlayer;

    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean wasRestored) {
        guardarButton.setEnabled(true);
        this.youTubePlayer = youTubePlayer;
        String url = urlTxtView.getText().toString();
        Log.d("1", url);
        if (!wasRestored) {
            String id = url.split("\\?v=")[1];
            youTubePlayer.loadVideo(id);
        }
    }

    @Override
    public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult result) {
        Toast.makeText(this, "ERROR:" + result.toString(), Toast.LENGTH_LONG).show();
    }


    private class YoutubeSearchAPI extends AsyncTask<Void, Void, List<VideoYoutube>> {

        @Override
        protected List<VideoYoutube> doInBackground(Void... voids) {
            try {
                // This object is used to make YouTube Data API requests. The last
                // argument is required, but since we don't need anything
                // initialized when the HttpRequest is initialized, we override
                // the interface and provide a no-op function.
                YouTube youtube = new YouTube.Builder(new NetHttpTransport(), new JacksonFactory(), new HttpRequestInitializer() {
                    public void initialize(HttpRequest request) throws IOException {
                    }
                }).setApplicationName("youtube-cmdline-search-sample").build();

                // Prompt the user to enter a query term.

                String queryTerm = url.split("@youtube")[1];

                // Define the API request for retrieving search results.
                YouTube.Search.List search = youtube.search().list("id,snippet");

                // Set your developer key from the Google Developers Console for
                // non-authenticated requests. See:
                // https://console.developers.google.com/
                search.setKey(Config.YOUTUBE_API_KEY);
                search.setQ(queryTerm);

                // Restrict the search results to only include videosYoutube. See:
                // https://developers.google.com/youtube/v3/docs/search/list#type
                search.setType("video");

                // To increase efficiency, only retrieve the fields that the
                // application uses.
                search.setFields("items(id/kind,id/videoId,snippet/title,snippet/thumbnails/default/url)");
                search.setMaxResults(NUMBER_OF_VIDEOS_RETURNED);

                // Call the API and print results.
                SearchListResponse searchResponse = search.execute();
                List<SearchResult> searchResultList = searchResponse.getItems();
                List<VideoYoutube> videosYoutube = new LinkedList<VideoYoutube>();
                if (searchResultList != null) {
                    for (SearchResult busqueda : searchResultList) {
                        ResourceId rId = busqueda.getId();
                        if (rId.getKind().equals("youtube#video")) {
                            Thumbnail thumbnail = busqueda.getSnippet().getThumbnails().getDefault();
                            VideoYoutube videoYoutube = new VideoYoutube(rId.getVideoId(), busqueda.getSnippet().getTitle(), thumbnail.getUrl());
                            videosYoutube.add(videoYoutube);
                        }
                    }
                    return videosYoutube;
                    //prettyPrint(searchResultList.iterator(), queryTerm);
                }
            } catch (GoogleJsonResponseException e) {
                System.err.println("There was a service error: " + e.getDetails().getCode() + " : "
                        + e.getDetails().getMessage());
            } catch (IOException e) {
                System.err.println("There was an IO error: " + e.getCause() + " : " + e.getMessage());
            } catch (Throwable t) {
                t.printStackTrace();
            }
            return null;
        }
    }

}






