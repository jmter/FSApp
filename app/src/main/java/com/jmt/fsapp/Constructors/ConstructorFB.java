package com.jmt.fsapp.Constructors;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jmt.fsapp.Adapter.MenusAdapter;
import com.jmt.fsapp.POJO.Equipo;
import com.jmt.fsapp.POJO.Menus;
import com.jmt.fsapp.POJO.Personal;

import java.util.ArrayList;

public class ConstructorFB implements Constructor {
    private DatabaseReference mdataBase;
    private ArrayList<Menus> menus = new ArrayList();
    private RecyclerView menuRV;

    public ConstructorFB() {
    }

    public ConstructorFB(RecyclerView menuRV) {
        this.menuRV = menuRV;
    }

    //Devuelve un array con los objetos Menu de la base de datos
    public void loadMenus(){
        mdataBase = FirebaseDatabase.getInstance().getReference().child("Menus");
        mdataBase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()) {
                    menus.clear();
                    for (DataSnapshot ds : snapshot.getChildren()) {
                        menus.add(new Menus(ds.child("Name").getValue().toString(), ds.child("Intent").getValue().toString()));
                    }
                    MenusAdapter adaptador = new MenusAdapter(menus);
                    menuRV.setAdapter(adaptador);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    public Personal loadUser(){
        Personal personal = new Personal();

        return personal;
    }
    public void modifieUser(Personal personal){

    }
    public Equipo obtEquipo(){
        Equipo equipo = new Equipo();
        return equipo;
    }
    public void modiefieEquipo(Equipo equipo){}
}
