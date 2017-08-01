package com.example.javi.instawatch.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.javi.instawatch.R;
import com.example.javi.instawatch.controlador.ControladorColaboraciones;
import com.example.javi.instawatch.controlador.ControladorUsuarios;
import com.example.javi.instawatch.modeloDTO.MunicipioDTO;
import com.example.javi.instawatch.modeloDTO.ProvinciaDTO;
import com.example.javi.instawatch.modeloDTO.UsuarioDTO;

import org.w3c.dom.Text;

import java.net.HttpURLConnection;

public class NavigationActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private String usuario;
    private ControladorColaboraciones controladorColaboraciones;
    private ControladorUsuarios controladorUsuarios;
    private String provinciaSeleccionada, municipioSeleccionado;

   @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        controladorColaboraciones = new ControladorColaboraciones();
        controladorUsuarios = new ControladorUsuarios();
        usuario = getIntent().getStringExtra("usuario");
        final UsuarioDTO usuarioDTO = controladorUsuarios.getUsuario(usuario);
        System.out.println("navigation->" + usuario);
        setContentView(R.layout.activity_navigation);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Home");


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        final EditText editTextNombre = (EditText) findViewById(R.id.nombreEdit);
        final EditText editTextAp1 = (EditText) findViewById(R.id.primerApellidoTxtview);
        final EditText editTextAp2 = (EditText) findViewById(R.id.segundoApellidoTxtview);
        final EditText editTextDNI = (EditText) findViewById(R.id.dniTxtview);
        final EditText editTextTlf = (EditText) findViewById(R.id.tlfTxtview);
        final EditText editTextEmail = (EditText) findViewById(R.id.emailTxtview);
        final EditText editTextEdad = (EditText) findViewById(R.id.edadTxtview);
        final TextView userNameTxt = (TextView) findViewById(R.id.nombreUsuarioTxtview);
        String provincias[] = controladorUsuarios.getProvincias();

        final Spinner spinnerProvincias = (Spinner) findViewById(R.id.lista_provincias);
        final Spinner spinnerMunicipios = (Spinner) findViewById(R.id.lista_municipios);

        ArrayAdapter<String> adapterProv = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, provincias);
        adapterProv.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerProvincias.setAdapter(adapterProv);
        final ProvinciaDTO provinciaDTO = controladorUsuarios.getProvincia(usuarioDTO.getIdProvincia());
        final int spinnerPosition = adapterProv.getPosition(provinciaDTO.getNombre());
        spinnerProvincias.setSelection(spinnerPosition);
        spinnerProvincias.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                provinciaSeleccionada = spinnerProvincias.getItemAtPosition(i).toString();
                if (i != spinnerPosition) {
                    String municipios[] = controladorUsuarios.getMunicipios(provinciaDTO.getNombre());
                    ArrayAdapter<String> adapterMunicipios = new ArrayAdapter<String>(view.getContext(), android.R.layout.simple_spinner_item, municipios);
                    adapterMunicipios.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinnerMunicipios.setAdapter(adapterMunicipios);
                    Log.d("1", "Provincia seleccionada");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        String municipios[] = controladorUsuarios.getMunicipios(provinciaDTO.getNombre());
        ArrayAdapter<String> adapterMunicipios = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, municipios);
        adapterMunicipios.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerMunicipios.setAdapter(adapterMunicipios);
        MunicipioDTO municipioDTO = controladorUsuarios.getMunicipio(usuarioDTO.getIdMunicipio());
        Log.d("1", "Nombre municipio" + municipioDTO.getNombre());
        int spinnerPosition2 = adapterMunicipios.getPosition(municipioDTO.getNombre());

        spinnerMunicipios.setSelection(spinnerPosition2);
        spinnerMunicipios.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                municipioSeleccionado = spinnerMunicipios.getItemAtPosition(i).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


       editTextNombre.setText(usuarioDTO.getNombre());
        editTextAp1.setText(usuarioDTO.getPrimerApellido());
        editTextAp2.setText(usuarioDTO.getSegundoApellido());
        editTextDNI.setText(usuarioDTO.getDni());
        editTextTlf.setText(usuarioDTO.getTlf());
        editTextEmail.setText(usuarioDTO.getEmail());
        editTextEdad.setText(usuarioDTO.getEdad());
        userNameTxt.setText(usuarioDTO.getNombreUsuario());

        Button editarPerfilBtn = (Button) findViewById(R.id.editarPerfilBtn);
        editarPerfilBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final EditText editTextNombre = (EditText) findViewById(R.id.nombreEdit);
                final EditText editTextAp1 = (EditText) findViewById(R.id.primerApellidoTxtview);
                final EditText editTextAp2 = (EditText) findViewById(R.id.segundoApellidoTxtview);
                final EditText editTextDNI = (EditText) findViewById(R.id.dniTxtview);
                final EditText editTextTlf = (EditText) findViewById(R.id.tlfTxtview);
                final EditText editTextEmail = (EditText) findViewById(R.id.emailTxtview);
                final EditText editTextEdad = (EditText) findViewById(R.id.edadTxtview);
                final TextView userNameTxt = (TextView) findViewById(R.id.nombreUsuarioTxtview);
                String nombre = editTextNombre.getText().toString();
                String primerApellido = editTextAp1.getText().toString();
                String segundoApellido = editTextAp2.getText().toString();
                String dni = editTextDNI.getText().toString();
                String tlf = editTextTlf.getText().toString();
                String email = editTextEmail.getText().toString();
                String edad = editTextEdad.getText().toString();
                String nombreUsuario = userNameTxt.getText().toString();
                int codeResponse = controladorUsuarios.updateUsuario(nombre, primerApellido, segundoApellido,
                        provinciaSeleccionada, municipioSeleccionado, dni, tlf, email, edad, nombreUsuario, usuarioDTO.getPassword());
                CharSequence text;
                if (codeResponse == HttpURLConnection.HTTP_ACCEPTED) {
                    text = "Usuario editado correctamente";
                } else {
                    text = "Error en la edición";
                }
                int duration = Toast.LENGTH_LONG;
                Toast toast = Toast.makeText(getApplicationContext(), text, duration);
                toast.show();
            }
        });
        View headerView = navigationView.getHeaderView(0); // 0-index header
        TextView txtNombreHeader = (TextView) headerView.findViewById(R.id.nombreHeader);
        txtNombreHeader.setText(usuarioDTO.getNombre());
        TextView emailHeader = (TextView) headerView.findViewById(R.id.emailHeader);
        emailHeader.setText(usuarioDTO.getEmail());
    }

    @Override
    public void onBackPressed() {
        Toast.makeText(getApplicationContext(), "Ya está en la ventana principal, si quiere salir cierre la aplicación", Toast.LENGTH_LONG).show();

    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        //Opciones de la barra lateral izquierda
        int id = item.getItemId();
        switch (id) {
            case R.id.nav_colaboraciones:
                Intent myIntent = new Intent(this, ColaboracionesActivity.class);
                myIntent.putExtra("usuario", usuario);
                startActivity(myIntent);
                break;
            case R.id.nav_mensajes:
                myIntent = new Intent(this, MensajesActivity.class);
                myIntent.putExtra("usuario", usuario);
                startActivity(myIntent);
                break;
            case R.id.nav_videos:
                myIntent = new Intent(this, VideosActivity.class);
                myIntent.putExtra("usuario", usuario);
                startActivity(myIntent);
                break;
            case R.id.nav_logout:
                SharedPreferences sharedPreferences = getApplication().getSharedPreferences("My preferences", MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean("logueado", false);
                editor.putString("usuario", null);
                editor.commit();
                String idDevice = Settings.Secure.getString(getContentResolver(),
                        Settings.Secure.ANDROID_ID);
                controladorColaboraciones.borrarToken(usuario, idDevice);
                myIntent = new Intent(this, LoginActivity.class);
                startActivity(myIntent);
                break;
            case R.id.nav_salir:
                Intent intent = new Intent(NavigationActivity.this, CloseActivity.class);
               //Clear all activities and start new task
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                break;
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


}
