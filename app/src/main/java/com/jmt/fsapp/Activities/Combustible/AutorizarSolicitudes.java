package com.jmt.fsapp.Activities.Combustible;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.jmt.fsapp.Constructors.ConstructorAtuorizarSolicitud;
import com.jmt.fsapp.POJO.OrdenCompraEstacionServicio;
import com.jmt.fsapp.R;

import java.util.ArrayList;


public class AutorizarSolicitudes extends AppCompatActivity {
    private RecyclerView autorizarSolicitudesRV;
    private ArrayList<OrdenCompraEstacionServicio> solicitudes = new ArrayList();
    private Activity activity = this;
    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_autorizar_solicitudes);
        autorizarSolicitudesRV = findViewById(R.id.autorizarSolicitudesRV);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        autorizarSolicitudesRV.setLayoutManager(linearLayoutManager);
        String userE = user.getEmail().replace(".","");
        solicitudes = new ConstructorAtuorizarSolicitud(autorizarSolicitudesRV, activity).loadSolicitudes(userE);
    }
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(activity, MenuEstacionesDeServicio.class);
        startActivity(intent);
        finish();
    }
}
