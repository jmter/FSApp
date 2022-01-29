package com.jmt.fsapp.Constructors;

import android.app.Activity;
import android.util.Log;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jmt.fsapp.Activities.User.InformacionPersonal;
import com.jmt.fsapp.Adapter.EstacionesAdapter;
import com.jmt.fsapp.Adapter.MenusAdapter;
import com.jmt.fsapp.POJO.Equipo;
import com.jmt.fsapp.POJO.Estacion;
import com.jmt.fsapp.POJO.Menus;
import com.jmt.fsapp.POJO.Personal;

import java.util.ArrayList;

public class ConstructorEstaciones {
    private DatabaseReference mdataBase;
    private ArrayList<Estacion> estaciones = new ArrayList();
    private RecyclerView menuRV;
    private Integer auth;
    Activity activity;
    EstacionesAdapter adaptador;

    //Constructores
    public ConstructorEstaciones() {
    }

    public ConstructorEstaciones(RecyclerView menuRV, Activity activity) {
        this.menuRV = menuRV;
        this.activity = activity;

        adaptador = new EstacionesAdapter(estaciones,activity);
    }

    //Metodos interfaz
    public void loadEstaciones(){
        mdataBase = FirebaseDatabase.getInstance().getReference().child("Provedores").child("Estaciones de servicio");
        mdataBase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()) {
                    estaciones.clear();
                    for (DataSnapshot ds : snapshot.getChildren()) {
                        Log.i("Estaciones",ds.getValue().toString());
                        if(ds.child("state").getValue().toString().equals("activa") ) {
                            Estacion estacion = new Estacion();
                            estacion.setCity(ds.child("city").getValue().toString());
                            estacion.setId(ds.child("id").getValue().toString());
                            estacion.setName(ds.child("name").getValue().toString());
                            estacion.setPicture(ds.child("picture").getValue().toString());
                            Log.i("estation",estacion.getName());
                            estaciones.add(estacion);
                        }
                        }
                    adaptador.setEstaciones(estaciones);
                    menuRV.setAdapter(adaptador);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}

