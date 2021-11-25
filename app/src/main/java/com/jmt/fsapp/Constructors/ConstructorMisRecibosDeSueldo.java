package com.jmt.fsapp.Constructors;

import android.app.Activity;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jmt.fsapp.Adapter.PreguntasPDAdapter;
import com.jmt.fsapp.Adapter.RecibosDeSueldoAdapter;
import com.jmt.fsapp.POJO.ReciboDeSueldo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class ConstructorMisRecibosDeSueldo {
    private DatabaseReference mdataBase;
    private ArrayList<ReciboDeSueldo> recibos = new ArrayList();
    private RecyclerView recibosDeSueldoRV;
    Activity activity;
    RecibosDeSueldoAdapter adaptador;
    private FirebaseUser user;

    //Constructores
    public ConstructorMisRecibosDeSueldo() {
    }

    public ConstructorMisRecibosDeSueldo(RecyclerView recibosDeSueldoRV, Activity activity) {
        this.recibosDeSueldoRV = recibosDeSueldoRV;
        this.activity = activity;
        adaptador = new RecibosDeSueldoAdapter(recibos,activity);
    }

    //Metodos interfaz
    public ArrayList<ReciboDeSueldo> loadRecibos(String cidActualUser){

        mdataBase = FirebaseDatabase.getInstance().getReference().child("RecibosDeSueldo").child(cidActualUser);
        mdataBase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()) {
                    recibos.clear();
                    for (DataSnapshot ds : snapshot.getChildren()) {
                        ReciboDeSueldo recibo = new ReciboDeSueldo(ds.child("fecha").getValue().toString(),ds.child("firma").getValue().toString(),ds.child("mes").getValue().toString(),ds.child("tipo").getValue().toString(),ds.child("url").getValue().toString());
                        recibos.add(recibo);
                        }
                    adaptador.setRecibos(recibos);
                    recibosDeSueldoRV.setAdapter(adaptador);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        return recibos;
    }

}

