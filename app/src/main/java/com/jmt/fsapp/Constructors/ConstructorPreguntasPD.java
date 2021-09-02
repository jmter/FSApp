package com.jmt.fsapp.Constructors;

import android.app.Activity;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jmt.fsapp.Activities.InformacionPersonal;
import com.jmt.fsapp.Adapter.MenusAdapter;
import com.jmt.fsapp.Adapter.PreguntasPDAdapter;
import com.jmt.fsapp.POJO.Equipo;
import com.jmt.fsapp.POJO.Menus;
import com.jmt.fsapp.POJO.Personal;

import java.util.ArrayList;

public class ConstructorPreguntasPD  {
    private DatabaseReference mdataBase;
    private ArrayList<String> preguntas = new ArrayList();
    private RecyclerView partedeMaquinaRV;
    Activity activity;
    PreguntasPDAdapter adaptador;

    //Constructores
    public ConstructorPreguntasPD() {
    }

    public ConstructorPreguntasPD(RecyclerView partedeMaquinaRV, Activity activity) {
        this.partedeMaquinaRV = partedeMaquinaRV;
        this.activity = activity;
        adaptador = new PreguntasPDAdapter(preguntas,activity);
    }

    //Metodos interfaz
    public void loadPreguntas(){
        mdataBase = FirebaseDatabase.getInstance().getReference().child("PreguntasPD");
        mdataBase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()) {
                    preguntas.clear();
                    for (DataSnapshot ds : snapshot.getChildren()) {
                        preguntas.add(ds.child("Pregunta").getValue().toString());
                        }
                    adaptador.setPreguntas(preguntas);
                    partedeMaquinaRV.setAdapter(adaptador);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}

