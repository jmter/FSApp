package com.jmt.fsapp.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jmt.fsapp.Activities.Login.Login;
import com.jmt.fsapp.Constructors.ConstructorFB;
import com.jmt.fsapp.POJO.Menus;
import com.jmt.fsapp.R;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity  {
    private ImageView logoutBT;
    private TextView textActionBar;
    private FirebaseAuth mAuth;
    private RecyclerView notificacionesRV;
    private RecyclerView menuRV;
    private ArrayList<Menus> menus = new ArrayList();
    private DatabaseReference mDatabase;
    private Activity activity = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Usuarios");
        iniciarGrafico();
        chequeoAuth();
        //Carga los menus


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
        GridLayoutManager gridLayoutManagerm = new GridLayoutManager(this,2);
        textActionBar.setText("Bienvenido "+ mAuth.getCurrentUser().getEmail());
        menuRV.setLayoutManager(gridLayoutManagerm);
    }
    private void logOut(){
        mAuth.signOut();
        Intent intent = new Intent(this, Login.class);
        finish();
        startActivity(intent);

    }
    private void loadUser(){

    }
    private void chequeoAuth(){

        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot ds: dataSnapshot.getChildren()){
                    for(DataSnapshot sd: ds.getChildren()){
                        if (sd.child("usuario").getValue().equals(mAuth.getCurrentUser().getEmail())){
                            new ConstructorFB(menuRV, activity,Integer.valueOf(ds.getKey())).loadMenus();
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }



}
