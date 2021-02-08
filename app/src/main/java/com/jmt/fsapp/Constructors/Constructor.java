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

public interface Constructor {
    DatabaseReference mdataBase = null;
    ArrayList<Menus> menus = null;
    RecyclerView menuRV = null;


    void loadMenus();
    Personal loadUser();
    void modifieUser(Personal personal);
    Equipo obtEquipo();
    void modiefieEquipo(Equipo equipo);

}
