package com.jmt.fsapp.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jmt.fsapp.R;

public class InformacionPersonal extends AppCompatActivity {
    private EditText nombre;
    private EditText apellido;
    private EditText domicilio;
    private EditText fechanac;
    private EditText telefono;
    private EditText charlainduc;
    private EditText fechaingreso;
    private EditText seccion;
    private EditText categoria;
    private EditText credencial;
    private EditText numCI;
    private EditText vencCI;
    private EditText vencCS;
    private EditText vencLC;
    private EditText catLC;
    private DatabaseReference mdataBase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_informacion_personal);
        iniciarGraficos();
        mdataBase = FirebaseDatabase.getInstance().getReference().child("Usuarios");
        mdataBase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot ds : snapshot.getChildren()){
                    if(ds.child("Usuario").getValue().toString().equals(FirebaseAuth.getInstance().getCurrentUser().getEmail())) {
                        nombre.setText(ds.child("Nombre").getValue().toString());
                        apellido.setText(ds.child("Apellido").getValue().toString());
                        domicilio.setText(ds.child("Domicilio").getValue().toString());

                    }
                    else{

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void iniciarGraficos(){
        nombre = findViewById(R.id.eTnombre);
        apellido = findViewById(R.id.etapellidos);
        domicilio = findViewById(R.id.eTdomicilio);
        fechanac = findViewById(R.id.eTfechadenacimiento);
        telefono = findViewById(R.id.eTtelefono);
        charlainduc = findViewById(R.id.eTcharladeinduccion);
        fechaingreso = findViewById(R.id.eTfechadeingreso);
        seccion = findViewById(R.id.eTseccion);
        categoria = findViewById(R.id.eTCategoria);
        credencial = findViewById(R.id.eTcredencial);
        numCI = findViewById(R.id.eTnumerodecedula);
        vencCI = findViewById(R.id.eTvencimientocedula);
        vencCS = findViewById(R.id.eTvencimientocarnedesalud);
        vencLC = findViewById(R.id.eTvencimientolicenciadeconducir);
        catLC = findViewById(R.id.eTcategorialicenciadeconducir);
    }
}
