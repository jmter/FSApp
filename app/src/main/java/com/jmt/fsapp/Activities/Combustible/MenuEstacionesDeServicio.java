package com.jmt.fsapp.Activities.Combustible;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jmt.fsapp.Activities.MainActivity;
import com.jmt.fsapp.Constructors.ConstructorFB;
import com.jmt.fsapp.R;

public class MenuEstacionesDeServicio extends AppCompatActivity {
    private Activity activity = this;
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    private LinearLayout nuevaSolicitud,pendienteSolicictud,autorizarSolicictud, finalizadaSolicitud;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Usuarios");
        setContentView(R.layout.activity_menu_estaciones_de_servicio);
        iniciarGraficos();
        chequeoAuth();
    }
    public void onBackPressed() {
        Intent intent = new Intent(activity, MainActivity.class);
        startActivity(intent);
        finish();
    }

    private void iniciarGraficos(){
        nuevaSolicitud = findViewById(R.id.linearLayoutNuevaSolcicitud);
        nuevaSolicitud.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity,NuevaSolicitud.class);
                startActivity(intent);
                finish();
            }
        });
        pendienteSolicictud = findViewById(R.id.linearLayoutSolicitudPendiente);
        pendienteSolicictud.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity,SolicitudesPendientes.class);
                startActivity(intent);
                finish();
            }
        });
        autorizarSolicictud = findViewById(R.id.linearLayoutAutorizarSolicitudes);
        autorizarSolicictud.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity,AutorizarSolicitudes.class);
                startActivity(intent);
                finish();
            }
        });
        finalizadaSolicitud = findViewById(R.id.linearLayoutSolicitudFinalizada);
        finalizadaSolicitud.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity,SolicitudesFianlizadas.class);
                startActivity(intent);
                finish();
            }
        });

    }

    private void chequeoAuth(){
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot ds: dataSnapshot.getChildren()){
                    for(DataSnapshot sd: ds.getChildren()){
                        if (sd.child("usuario").getValue().equals(mAuth.getCurrentUser().getEmail())){
                            if (Integer.valueOf(ds.getKey()) >= 3){
                                autorizarSolicictud.setVisibility(View.VISIBLE);
                            }
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
