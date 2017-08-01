package com.example.javi.instawatch.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.javi.instawatch.R;
import com.example.javi.instawatch.controlador.ControladorUsuarios;
import com.example.javi.instawatch.modeloDTO.UsuarioDTO;

import java.net.HttpURLConnection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A login screen that offers login via email/password.
 */
public class RegisterActivity extends AppCompatActivity {

    private int MAX_LENGTH_PASSWORD = 5;
    // UI references.
    private AutoCompleteTextView mNombreView;
    private AutoCompleteTextView mPrimerApellidoView;
    private AutoCompleteTextView mSegundoApellidoView;
    private EditText mDNIView, mEmailiew, mTlfView, mEdadView, mNombreUsuarioView, mPasswordView, mConfirmPasswordView;
    private String provinciaSeleccionada = "Provincia";
    private String municipioSeleccionado;
    private EditText m;
    private View mProgressView;
    private View mRegisterView;
    private ControladorUsuarios controlador = new ControladorUsuarios();
    private Spinner spinnerMunicipios, spinnerProvincias;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        Button mRegisterButton = (Button) findViewById(R.id.registerComplete_button);
        mRegisterButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                register();
            }
        });
        mostrarProvincias();
        mostrarMunicipios(false);
        mRegisterView = findViewById(R.id.register_form);
        mProgressView = findViewById(R.id.register_progress);
    }

    private void mostrarProvincias() {
        String provincias[] = controlador.getProvincias();
        spinnerProvincias = (Spinner) findViewById(R.id.lista_provincias);
        ArrayAdapter<String> adapterProv = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, provincias);
        adapterProv.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerProvincias.setAdapter(adapterProv);
        spinnerProvincias.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                provinciaSeleccionada = spinnerProvincias.getItemAtPosition(i).toString();
                Log.d("1", "provinciaSeleccionada:" + provinciaSeleccionada);
                if (provinciaSeleccionada.equals("Provincia"))
                    mostrarMunicipios(false);
                else
                    mostrarMunicipios(true);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void mostrarMunicipios(boolean estanMunicipios) {
        Log.d("1", "mostrarMunicipios*->provinciaSeleccionada " + provinciaSeleccionada);
        String municipios[] = controlador.getMunicipios(provinciaSeleccionada);

        spinnerMunicipios = (Spinner) findViewById(R.id.lista_municipios);
        spinnerMunicipios.setEnabled(estanMunicipios);
        ArrayAdapter<String> adapterMun = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, municipios);
        adapterMun.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinnerMunicipios.setAdapter(adapterMun);
        spinnerMunicipios.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                municipioSeleccionado = spinnerMunicipios.getItemAtPosition(i).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }

    public void register() {
        CharSequence text;
        int duration = Toast.LENGTH_LONG;
        boolean cancel = false;
        View focusView = null;
        mNombreView = (AutoCompleteTextView) findViewById(R.id.nombreTxtview);
        mPrimerApellidoView = (AutoCompleteTextView) findViewById(R.id.primerApellidoTxtview);
        mSegundoApellidoView = (AutoCompleteTextView) findViewById(R.id.segundoApellidoTxtview);
        mDNIView = (EditText) findViewById(R.id.dniTxtview);
        mTlfView = (EditText) findViewById(R.id.tlfTxtview);
        mEmailiew = (AutoCompleteTextView) findViewById(R.id.emailTxtview);
        mEdadView = (EditText) findViewById(R.id.edadTxtview);
        mNombreUsuarioView = (EditText) findViewById(R.id.nombreUsuarioTxtview);
        mPasswordView = (EditText) findViewById(R.id.passwordTxtview);
        mConfirmPasswordView = (EditText) findViewById(R.id.confirmPasswordTxtview);

        String nombre = mNombreView.getText().toString();
        String primerApellido = mPrimerApellidoView.getText().toString();
        String segundoApellido = mSegundoApellidoView.getText().toString();
        String dni = mDNIView.getText().toString();

        if (!isDNIvalido(dni)) {
            mDNIView.setError(getString(R.string.error_invalid_dni));
            focusView = mDNIView;
            cancel = true;
        }
        String tlf = mTlfView.getText().toString();
        String email = mEmailiew.getText().toString();
        if (!email.equals("") && !isEmailValido(email)) {
            mEmailiew.setError(getString(R.string.error_invalid_email));
            focusView = mEmailiew;
            cancel = true;
        }
        String edad = mEdadView.getText().toString();
        String nombreUsuario = mNombreUsuarioView.getText().toString();
        String password = mPasswordView.getText().toString();
        if (!isPasswordValido(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }
        String confirmPassword = mConfirmPasswordView.getText().toString();

        if (!isPasswordConfirmed(password, confirmPassword)) {
            mConfirmPasswordView.setError(getString(R.string.error_password_noconfirmada));
            focusView = mConfirmPasswordView;
            cancel = true;
        }
        if (nombre.equals("")) {
            mNombreView.setError(getString(R.string.error_invalid_campo_vacio));
            focusView = mNombreView;
            cancel = true;
        }
        if (primerApellido.equals("")) {
            mPrimerApellidoView.setError(getString(R.string.error_invalid_campo_vacio));
            focusView = mPrimerApellidoView;
            cancel = true;

        }
        if (segundoApellido.equals("")) {
            mSegundoApellidoView.setError(getString(R.string.error_invalid_campo_vacio));
            focusView = mSegundoApellidoView;
            cancel = true;
        }

        if (nombreUsuario.equals("")) {
            mNombreUsuarioView.setError(getString(R.string.error_invalid_campo_vacio));
            focusView = mNombreUsuarioView;
            cancel = true;
        }

        if (provinciaSeleccionada.equals("Provincia")) {
            text = "Debe seleccionar una provincia";
            Toast toast = Toast.makeText(getApplicationContext(), text, duration);
            toast.show();
            focusView = spinnerProvincias;
            cancel = true;
        }
        Log.d("1","Municipio seleccionado:"+municipioSeleccionado);
        if (municipioSeleccionado.equals("") || municipioSeleccionado.equals("Municipio")) {
            text = "Debe seleccionar un municipio";
            Toast toast = Toast.makeText(getApplicationContext(), text, duration);
            toast.show();
            focusView = spinnerMunicipios;
            cancel = true;
        }

        if (!nombreUsuario.equals("")) {
            boolean existeUsuario = controlador.existeUsuario(nombreUsuario);
            if (existeUsuario) {
                mNombreUsuarioView.setError(getString(R.string.error_incorrect_user_yaexiste));
                focusView = mNombreUsuarioView;
                cancel = true;
            }
        }
        if (cancel) {
            focusView.requestFocus();
        } else {
            int codeResponse = controlador.registrar(nombre, primerApellido, segundoApellido, dni,
                    provinciaSeleccionada, municipioSeleccionado, tlf,
                    email, edad, nombreUsuario, password, confirmPassword);

            if (codeResponse == HttpURLConnection.HTTP_ACCEPTED) {
                text = "¡Registro realizado correctamente!";
            } else {
                text = "Error en el registro";
            }
            //Se cambia al loginActivity con el mensaje correspondiente
            Intent myIntent = new Intent(this, LoginActivity.class);
            startActivity(myIntent);
            Toast toast = Toast.makeText(getApplicationContext(), text, duration);
            toast.show();
        }

    }

    private boolean isDNIvalido(String dni) {
        Pattern p = Pattern.compile("[0-9]{8}[A-Za-z]");
        Matcher m = p.matcher(dni);
        return m.matches();
    }


    private boolean isEmailValido(String email) {
        return email.contains("@");
    }


    private boolean isPasswordConfirmed(String password, String confirmPassword) {
        return password.equals(confirmPassword);
    }


    private boolean isPasswordValido(String password) {
        return password.length() > MAX_LENGTH_PASSWORD;
    }


}

