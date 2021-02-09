package com.jmt.fsapp.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.jmt.fsapp.Constructors.Constructor;
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






    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth = FirebaseAuth.getInstance();

        iniciarGrafico();

        //Carga los menus
        new ConstructorFB(menuRV, this).loadMenus();



    }
    private void iniciarGrafico(){
        Log.i("OnCReate","LLegue a IniciarGraf");
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



}
