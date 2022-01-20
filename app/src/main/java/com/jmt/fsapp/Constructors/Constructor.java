package com.jmt.fsapp.Constructors;

import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;
import com.jmt.fsapp.Activities.User.InformacionPersonal;
import com.jmt.fsapp.POJO.Equipo;
import com.jmt.fsapp.POJO.Menus;
import com.jmt.fsapp.POJO.Personal;

import java.util.ArrayList;

public interface Constructor {
    DatabaseReference mdataBase = null;
    ArrayList<Menus> menus = null;
    RecyclerView menuRV = null;


    void loadMenus();
    void loadBv(TextView textView);
    void loadUser(InformacionPersonal informacionPersonal);
    void modifieUser(Personal personal);
    void modiefieEquipo(Equipo equipo);

}
