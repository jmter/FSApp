package com.jmt.fsapp.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageView;


import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jmt.fsapp.POJO.Menus;
import com.jmt.fsapp.R;

import java.util.ArrayList;

public class InformacionPersonal extends AppCompatActivity {
    private Toolbar toolbar;
    private EditText nombre;
    private EditText apellido;
    private EditText domicilio;
    private EditText fechanac;
    private EditText telefono;
    private EditText charlainduc;
    private EditText fechaingreso;
    private EditText seccion;
    private EditText categoria;
    private EditText credencial;
    private EditText numCI;
    private EditText vencCI;
    private EditText vencCS;
    private EditText vencLC;
    private EditText catLC;
    private ImageView ci;
    private ImageView lc;
    private ImageView cs;
    private DatabaseReference mdataBase;
    private Activity activity = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_informacion_personal);
        iniciarGraficos();
        mdataBase = FirebaseDatabase.getInstance().getReference().child("Usuarios");
        mdataBase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot ds : snapshot.getChildren()){
                    if(ds.child("Usuario").getValue().toString().equals(FirebaseAuth.getInstance().getCurrentUser().getEmail())) {
                        try {
                            nombre.setText(ds.child("Nombre").getValue().toString());
                            apellido.setText(ds.child("Apellido").getValue().toString());
                            domicilio.setText(ds.child("Domicilio").getValue().toString());
                            fechanac.setText(ds.child("Fecha de Nacimiento").getValue().toString());
                            telefono.setText(ds.child("Telefono").getValue().toString());
                            charlainduc.setText(ds.child("CInd").getValue().toString());
                            fechaingreso.setText(ds.child("Ingreso").getValue().toString());
                            seccion.setText(ds.child("Seccion").getValue().toString());
                            categoria.setText(ds.child("Categoria").getValue().toString());
                            credencial.setText(ds.child("Credencial").getValue().toString());
                            numCI.setText(ds.child("CI").child("Numero").getValue().toString());
                            vencCI.setText(ds.child("CI").child("Vencimiento").getValue().toString());
                            vencCS.setText(ds.child("CS").child("Vencimiento").getValue().toString());
                            vencLC.setText(ds.child("LC").child("Vencimiento").getValue().toString());
                            //catLC.setText(ds.child("LC").child("Categoria").getValue().toString());
                            Glide.with(activity).load(ds.child("CI").child("Imagen").getValue().toString()).into(ci);
                            Glide.with(activity).load(ds.child("LC").child("Imagen").getValue().toString()).into(lc);
                            Glide.with(activity).load(ds.child("CS").child("Imagen").getValue().toString()).into(cs);


                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }

                } //Chequeo usuario actual y cargo su info
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void iniciarGraficos(){
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        nombre = findViewById(R.id.eTnombre);
        apellido = findViewById(R.id.etapellidos);
        domicilio = findViewById(R.id.eTdomicilio);
        fechanac = findViewById(R.id.eTfechadenacimiento);
        telefono = findViewById(R.id.eTtelefono);
        charlainduc = findViewById(R.id.eTcharladeinduccion);
        fechaingreso = findViewById(R.id.eTfechadeingreso);
        seccion = findViewById(R.id.eTseccion);
        categoria = findViewById(R.id.eTCategoria);
        credencial = findViewById(R.id.eTcredencial);
        numCI = findViewById(R.id.eTnumerodecedula);
        vencCI = findViewById(R.id.eTvencimientocedula);
        vencCS = findViewById(R.id.eTvencimientocarnedesalud);
        vencLC = findViewById(R.id.eTvencimientolicenciadeconducir);
        catLC = findViewById(R.id.eTcategorialicenciadeconducir);
        ci = findViewById(R.id.iVcedula);
        cs = findViewById(R.id.iVcarnedesalud);
        lc = findViewById(R.id.iVlicenciadeconducir);
    }
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menuoverflow,menu);
        return true;
    }
    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();
        if(id == R.id.itemmenueditar){
            Log.i("MENU","El priemro");
        }
        if(id == R.id.itemmenurecibo){
            Log.i("MENU","El segundo");
        }
        if(id == R.id.menuitemSalir){
            Log.i("MENU","El tercero");
        }
        return super.onOptionsItemSelected(item);
    }
}
