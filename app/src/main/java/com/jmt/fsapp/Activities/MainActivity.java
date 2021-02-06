package com.jmt.fsapp.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.menu.MenuAdapter;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jmt.fsapp.Adapter.MenusAdapter;
import com.jmt.fsapp.DataBase.DataBase;
import com.jmt.fsapp.POJO.Menus;
import com.jmt.fsapp.R;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private ImageView logoutBT;
    private TextView textActionBar;
    private FirebaseAuth mAuth;
    private RecyclerView notificacionesRV;
    private RecyclerView menuRV;
    private ArrayList<Menus> menus = new ArrayList();
    private DatabaseReference mDatabase;
    private DataBase db = new DataBase();




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                obtenerMenus();
                iniciarGrafico();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
    private void iniciarGrafico(){
        logoutBT = findViewById(R.id.logoutBT);
        textActionBar = findViewById(R.id.textView);
        menuRV = findViewById(R.id.menuRV);
        logoutBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logOut();
            }
        });
        LinearLayoutManager llm = new LinearLayoutManager(this);
        menuRV.setLayoutManager(llm);
        iniciarAdaptador();
    }
    private void logOut(){
        mAuth.signOut();
        Intent intent = new Intent(this, Login.class);
        startActivity(intent);
        finish();
    }
    private void loadUser(){

    }
    public void iniciarAdaptador(){
        MenusAdapter adaptador = new MenusAdapter(menus);
        menuRV.setAdapter(adaptador);
    }
    public void obtenerMenus(){
        menus = db.obtenerMenus();


        // Read from the database

    }
}
