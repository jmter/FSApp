package com.jmt.fsapp.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.jmt.fsapp.R;

public class Login extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private EditText etEmail;
    private EditText etPassword;
    private Button btLogin;

    private static final String TAG = "LoginActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        iniciarGrafico();
        mAuth = FirebaseAuth.getInstance();



    }
    private void iniciarGrafico(){
        etEmail = findViewById(R.id.email);
        etPassword = findViewById(R.id.password);
        btLogin = findViewById(R.id.loginBT);
        if (VerificarLog()) {
            btLogin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    iniciarMain();
                }
            });
        }else {
            btLogin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    signIn(etEmail.getText().toString(), etPassword.getText().toString());
                }
            });
        }
    }

    private void iniciarMain(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    private void signIn(String email, String password){
        if (email.isEmpty() || password.isEmpty()){
            Toast.makeText(Login.this, "Ingrese email y contrasena",
                    Toast.LENGTH_LONG).show();
        } else {
                mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                Log.d(TAG, "signInWithEmail:success");
                                FirebaseUser user = mAuth.getCurrentUser();
                                iniciarMain();

                            } else {
                                // If sign in fails, display a message to the user.
                                Log.w(TAG, "signInWithEmail:failure", task.getException());
                                Toast.makeText(Login.this, "Authentication failed.",
                                        Toast.LENGTH_SHORT).show();

                                // ...
                            }

                            // ...
                        }
                    });
        }
    }

    private boolean VerificarLog() {
        boolean logd = false;
        user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            // User is signed in
            logd = true;
        }
    return logd;
    }
}
