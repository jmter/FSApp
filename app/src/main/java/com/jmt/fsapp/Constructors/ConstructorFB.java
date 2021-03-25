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
import com.jmt.fsapp.POJO.Equipo;
import com.jmt.fsapp.POJO.Menus;
import com.jmt.fsapp.POJO.Personal;

import java.util.ArrayList;

public class ConstructorFB implements Constructor {
    private DatabaseReference mdataBase;
    private ArrayList<Menus> menus = new ArrayList();
    private RecyclerView menuRV;
    Activity activity;
    MenusAdapter adaptador;

    //Constructores
    public ConstructorFB() {
    }

    public ConstructorFB(RecyclerView menuRV, Activity activity) {
        this.menuRV = menuRV;
        this.activity = activity;
        adaptador = new MenusAdapter(menus,activity);
    }

    //Metodos interfaz
    public void loadMenus(){
        mdataBase = FirebaseDatabase.getInstance().getReference().child("Menus");
        mdataBase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()) {
                    menus.clear();
                    for (DataSnapshot ds : snapshot.getChildren()) {
                        menus.add(new Menus(ds.child("Name").getValue().toString(),ds.child("Imagen").getValue().toString(),ds.child("Intent").getValue().toString()));
                    }
                    adaptador.setMenus(menus);
                    menuRV.setAdapter(adaptador);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public void loadBv(TextView textView) {

    }

    public void loadUser(InformacionPersonal informacionPersonal){
    }
    public void modifieUser(Personal personal){    }

    public void modiefieEquipo(Equipo equipo){    }
}

