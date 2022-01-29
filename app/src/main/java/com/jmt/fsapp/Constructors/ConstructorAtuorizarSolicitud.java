package com.jmt.fsapp.Constructors;

import android.app.Activity;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jmt.fsapp.Adapter.AutorizarSolicitudAdapter;
import com.jmt.fsapp.Adapter.RecibosDeSueldoAdapter;
import com.jmt.fsapp.POJO.Equipo;
import com.jmt.fsapp.POJO.Estacion;
import com.jmt.fsapp.POJO.OrdenCompraEstacionServicio;
import com.jmt.fsapp.POJO.ReciboDeSueldo;

import java.util.ArrayList;
import java.util.Collections;

public class ConstructorAtuorizarSolicitud {
    private DatabaseReference mdataBase;
    private ArrayList<OrdenCompraEstacionServicio> solicitudes = new ArrayList();
    private RecyclerView autorizarSolicitudesRV;
    Activity activity;
    AutorizarSolicitudAdapter adaptador;
    private FirebaseUser user;

    //Constructores
    public ConstructorAtuorizarSolicitud() {
    }

    public ConstructorAtuorizarSolicitud(RecyclerView autorizarSolicitudesRV, Activity activity) {
        this.autorizarSolicitudesRV = autorizarSolicitudesRV;
        this.activity = activity;
        adaptador = new AutorizarSolicitudAdapter(solicitudes,activity);
    }

    //Metodos interfaz
    public ArrayList<OrdenCompraEstacionServicio> loadSolicitudes(final String userEmailDot){

        mdataBase = FirebaseDatabase.getInstance().getReference().child("Ordenes Estaciones de Servicio");
        mdataBase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()) {
                    solicitudes.clear();
                     DataSnapshot snap = snapshot.child("Pendiente autorizacion");
                     for(DataSnapshot ds : snap.getChildren()){
                        for (DataSnapshot sd : ds.getChildren()) {
                                OrdenCompraEstacionServicio solicitud = new OrdenCompraEstacionServicio();
                                solicitud.setProducto(sd.child("producto").getValue().toString());
                                solicitud.setOperario(sd.child("operario").getValue().toString());
                                Equipo equipo = new Equipo();
                                equipo.setId(sd.child("equipo").child("id").getValue().toString());
                                equipo.setMarca(sd.child("equipo").child("marca").getValue().toString());
                                equipo.setModelo(sd.child("equipo").child("modelo").getValue().toString());
                                equipo.setFoto(sd.child("equipo").child("foto").getValue().toString());
                                Estacion estacion = new Estacion();
                                estacion.setName(sd.child("estacion").child("name").getValue().toString());
                                estacion.setCity(sd.child("estacion").child("city").getValue().toString());
                                estacion.setId(sd.child("estacion").child("id").getValue().toString());
                                estacion.setPicture(sd.child("estacion").child("picture").getValue().toString());
                                solicitud.setFechayhorasolicitud(sd.child(("fechayhorasolicitud")).getValue().toString());
                                solicitud.setEstado(sd.child(("estado")).getValue().toString());
                                solicitud.setKey(sd.getKey());
                                solicitud.setEstacion(estacion);
                                solicitud.setEquipo(equipo);
                                solicitudes.add(solicitud);
                        }
                     }
                    adaptador.setSolicitudes(solicitudes);
                    autorizarSolicitudesRV.setAdapter(adaptador);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        return solicitudes;
    }

}

