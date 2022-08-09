package com.example.agendafirebase;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
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

public class RegistroActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private EditText txtUsuario, txtPass;
    private Button btnLogin;
    private boolean validacion = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        txtUsuario = (EditText) findViewById(R.id.txtUsuario);
        txtPass = (EditText) findViewById(R.id.txtPass);
        btnLogin = (Button) findViewById(R.id.btnIngresar);
        mAuth = FirebaseAuth.getInstance();

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validacion = true;
                if(txtUsuario.getText().toString().equals("") || txtPass.getText().toString().equals("")){
                    validacion = false;
                    Toast.makeText(RegistroActivity.this, "Capture todos los datos.", Toast.LENGTH_SHORT).show();
                    if(txtUsuario.getText().toString().equals("")){
                        txtUsuario.setError("Introduzca el correo");
                        validacion = false;
                    }
                    if(!validarEmail(txtUsuario.getText().toString())){
                        txtUsuario.setError("Introduzca un correo valido");
                        validacion = false;
                    }
                    if(txtPass.getText().toString().length()<6){
                        txtPass.setError("La contraseña debe tener mas de 6 caracteres");
                        validacion = false;
                    }
                    if(txtPass.getText().toString().equals("")){
                        txtPass.setError("Introduzca la contraseña");
                        validacion = false;
                    }
                }else{
                    crearUsuario(txtUsuario.getText().toString(), txtPass.getText().toString());
                }

            }
        });

    }

    private boolean validarEmail(String email) {
        Pattern pattern = Patterns.EMAIL_ADDRESS;
        return pattern.matcher(email).matches();
    }
    private void crearUsuario(String email, String password){
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                            Toast.makeText(RegistroActivity.this, "Authentication successful.",
                                    Toast.LENGTH_SHORT).show();
                            FirebaseAuth.getInstance().signOut();
                            finish();
                            Intent i = new Intent(RegistroActivity.this, LoginActivity.class);
                            startActivity(i);

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(RegistroActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }
                    }
                });
    }
    private void reload() {
    }
    private void updateUI(FirebaseUser user) {

    }

}