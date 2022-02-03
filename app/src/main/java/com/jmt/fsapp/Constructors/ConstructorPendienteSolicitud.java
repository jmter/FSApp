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
import com.jmt.fsapp.Adapter.PendienteSolicitudAdapter;
import com.jmt.fsapp.POJO.Equipo;
import com.jmt.fsapp.POJO.Estacion;
import com.jmt.fsapp.POJO.OrdenCompraEstacionServicio;

import java.util.ArrayList;

public class ConstructorPendienteSolicitud {
    private DatabaseReference mdataBase;
    private ArrayList<OrdenCompraEstacionServicio> solicitudes = new ArrayList();
    private RecyclerView listadoSolicitudesRV;
    Activity activity;
    PendienteSolicitudAdapter adaptador;
    private FirebaseUser user;

    //Constructores
    public ConstructorPendienteSolicitud() {
    }

    public ConstructorPendienteSolicitud(RecyclerView listadoSolicitudesRV, Activity activity) {
        this.listadoSolicitudesRV = listadoSolicitudesRV;
        this.activity = activity;
        adaptador = new PendienteSolicitudAdapter(solicitudes,activity);
    }

    //Metodos interfaz
    public ArrayList<OrdenCompraEstacionServicio> loadSolicitudes(final String userEmailDot){

        mdataBase = FirebaseDatabase.getInstance().getReference().child("Ordenes Estaciones de Servicio");
        mdataBase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()) {
                    solicitudes.clear();
                    DataSnapshot snap = snapshot.child("Pendiente autorizacion").child(userEmailDot);
                    for (DataSnapshot sd : snap.getChildren()) {
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
                            solicitud.setEstado(sd.child("estado").getValue().toString());
                            solicitud.setFechayhorasolicitud(sd.child(("fechayhorasolicitud")).getValue().toString());
                            solicitud.setKey(sd.getKey());
                            solicitud.setEstacion(estacion);
                            solicitud.setEquipo(equipo);
                            solicitudes.add(solicitud);

                    }
                    snap = snapshot.child("Autorizado").child(userEmailDot);
                    for (DataSnapshot sd : snap.getChildren()) {
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
                        solicitud.setEstado(sd.child("estado").getValue().toString());
                        estacion.setMail(sd.child("estacion").child("mail").getValue().toString());
                        solicitud.setOperarioName(sd.child("operarioName").getValue().toString());
                        solicitud.setFechayhorasolicitud(sd.child(("fechayhorasolicitud")).getValue().toString());
                        solicitud.setFechayhoraautorizacion(sd.child(("fechayhoraautorizacion")).getValue().toString());
                        solicitud.setKey(sd.getKey());
                        solicitud.setEstacion(estacion);
                        solicitud.setEquipo(equipo);
                        solicitudes.add(solicitud);

                    }
                    adaptador.setSolicitudes(solicitudes);
                    listadoSolicitudesRV.setAdapter(adaptador);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        return solicitudes;
    }

}

