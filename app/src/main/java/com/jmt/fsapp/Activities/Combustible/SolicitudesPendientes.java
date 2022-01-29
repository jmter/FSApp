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
import com.jmt.fsapp.Constructors.ConstructorPendienteSolicitud;
import com.jmt.fsapp.POJO.OrdenCompraEstacionServicio;
import com.jmt.fsapp.R;

import java.util.ArrayList;

public class SolicitudesPendientes extends AppCompatActivity {
    private RecyclerView listadoSolicitudesRV;
    private ArrayList<OrdenCompraEstacionServicio> solicitudes = new ArrayList();
    private Activity activity = this;
    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_solicitudes_pendientes);
        listadoSolicitudesRV = findViewById(R.id.listadosolicitudesRV);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        listadoSolicitudesRV.setLayoutManager(linearLayoutManager);
        String userE = user.getEmail().replace(".","");
        solicitudes = new ConstructorPendienteSolicitud(listadoSolicitudesRV, activity).loadSolicitudes(userE);
    }
    public void onBackPressed() {
        Intent intent = new Intent(activity, MenuEstacionesDeServicio.class);
        startActivity(intent);
        finish();
    }
}
