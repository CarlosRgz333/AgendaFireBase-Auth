package com.example.agendafirebase;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.nfc.Tag;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.regex.Pattern;

public class LoginActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private EditText txtUsuario, txtPass;
    private TextView lblRegistrar;
    private Button btnLogin;
    private boolean validacion = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        txtUsuario = (EditText) findViewById(R.id.txtUsuario);
        txtPass =(EditText) findViewById(R.id.txtPass);
        btnLogin = (Button) findViewById(R.id.btnIngresar);
        lblRegistrar = (TextView) findViewById(R.id.lblRegistrar);
        mAuth = FirebaseAuth.getInstance();

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validacion = true;
                if(txtUsuario.getText().toString().equals("") || txtPass.getText().toString().equals("")){
                    Toast.makeText(LoginActivity.this, "Capture todos los datos.", Toast.LENGTH_SHORT).show();
                    if(txtUsuario.getText().toString().equals("")){
                        txtUsuario.setError("Introduzca el correo");
                        validacion = false;
                    }
                    if(txtPass.getText().toString().equals("")){
                        txtPass.setError("Introduzca la contraseña");
                        validacion = false;
                    }
                }
                if(!validarEmail(txtUsuario.getText().toString())){
                    txtUsuario.setError("Introduzca un correo valido");
                    validacion = false;
                }

                if(validacion){
                    iniciarSesion(txtUsuario.getText().toString(), txtPass.getText().toString());
                }



            }
        });

        lblRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(LoginActivity.this, RegistroActivity.class);
                startActivity(i);
                limpiar();
            }
        });
    }

    public void limpiar(){
        txtUsuario.setError(null);
        txtPass.setError(null);
        txtUsuario.setText("");
        txtPass.setText("");
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            Intent i = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(i);
        }

    }

    private boolean validarEmail(String email) {
        boolean emailValid;
        if(Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            emailValid = true;
            //Toast.makeText(LoginActivity.this, "Email Valido", Toast.LENGTH_SHORT).show();
        }else{
            //Toast.makeText(LoginActivity.this, "Email Invalido", Toast.LENGTH_SHORT).show();
            emailValid = false;
        }
        return emailValid;
    }

    public void iniciarSesion(String email, String password) {


        if(isNetworkAvailable()){
            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                Log.d(TAG, "signInWithEmail:success");
                                FirebaseUser user = mAuth.getCurrentUser();
                                //updateUI(user);
                                Intent i = new Intent(LoginActivity.this, MainActivity.class);
                                startActivity(i);
                                finish();
                            } else {
                                Log.d(TAG, "signInWithEmail:failure", task.getException());
                                Toast.makeText(LoginActivity.this, "Usuario y/o contraseña incorrectos.", Toast.LENGTH_SHORT).show();
                                //updateUI(null);
                            }
                        }
                    });
        }else{
            Toast.makeText(LoginActivity.this, "Necesita Conexion a Internet", Toast.LENGTH_SHORT).show();
        }

    }




    public boolean isNetworkAvailable() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ni = cm.getActiveNetworkInfo();
        return ni != null && ni.isConnected();
    }
}