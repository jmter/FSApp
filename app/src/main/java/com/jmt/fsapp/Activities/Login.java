package com.jmt.fsapp.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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
import com.jmt.fsapp.R;

public class Login extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private EditText etEmail;
    private EditText etPassword;
    private TextView textView1;
    private Button btLogin;
    private Button btChUser;

    private static final String TAG = "LoginActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mAuth = FirebaseAuth.getInstance();
        iniciarGrafico();




    }
    private void iniciarGrafico(){
        etEmail = findViewById(R.id.email);
        etPassword = findViewById(R.id.password);
        btLogin = findViewById(R.id.loginBT);
        textView1 = findViewById(R.id.texto1);
        btChUser = findViewById(R.id.chuserBT);
        if (VerificarLog()) {
            etEmail.setText(user.getEmail());
            etEmail.setInputType(0);
            etPassword.setVisibility(View.GONE);
            textView1.setText("Bienvenido!");
            textView1.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            btChUser.setVisibility(View.VISIBLE);

            btChUser.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FirebaseAuth.getInstance().signOut();
                    Log.i(TAG,String.valueOf(VerificarLog()));
                    if (!VerificarLog()) {
                        iniciarGrafico();
                    }
                }
            });
            btLogin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    iniciarMain();
                }
            });
        }else {
            etEmail.setText("");
            etEmail.setInputType(21);
            etPassword.setVisibility(View.VISIBLE);
            textView1.setText("Ingrese email y contrasena!");
            textView1.setTextAlignment(View.TEXT_ALIGNMENT_VIEW_START);
            btChUser.setVisibility(View.GONE);
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
