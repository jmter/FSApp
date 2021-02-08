package com.jmt.fsapp.DataBase;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jmt.fsapp.POJO.Equipo;
import com.jmt.fsapp.POJO.Menus;
import com.jmt.fsapp.POJO.Personal;

import java.util.ArrayList;

public class DataBase {
    private DatabaseReference mdataBase;
    private ArrayList<Menus> menus = new ArrayList();

    public DataBase() {
    }


    //Devuelve un array con los objetos Menu de la base de datos
    public ArrayList<Menus> obtenerMenus(){
        Log.i("Prueba Db", "Ejecutando Obtener Menu");
        mdataBase = FirebaseDatabase.getInstance().getReference().child("Menus");
        mdataBase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()) {
                    Log.i("Prueba Db", "LLEgue a la base de datos");
                    for (DataSnapshot ds : snapshot.getChildren()) {
                        menus.add(new Menus(ds.child("Name").getValue().toString(), ds.child("Intent").getValue().toString()));
                        Log.i("Prueba", menus.toString());
                    }

                }
                else{
                    Log.i("Prueba Db", "No existe");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        return  menus;
    }
    public Personal ObtenerUsuario(){
        Personal personal = new Personal();

        return personal;
    }

    public void ModificarUsuario(Personal personal){

    }
    public Equipo ObtenerEquipo(){
        Equipo equipo = new Equipo();
        return equipo;
    }
    public void ModificarEquipo(Equipo equipo){}
}

