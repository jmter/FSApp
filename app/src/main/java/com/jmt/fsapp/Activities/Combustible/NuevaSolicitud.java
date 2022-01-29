package com.jmt.fsapp.Activities.Combustible;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jmt.fsapp.Activities.Equipos.Equipos;
import com.jmt.fsapp.Activities.EstacionesDeServicio;
import com.jmt.fsapp.Activities.MainActivity;
import com.jmt.fsapp.POJO.Equipo;
import com.jmt.fsapp.POJO.Estacion;
import com.jmt.fsapp.POJO.OrdenCompraEstacionServicio;
import com.jmt.fsapp.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class NuevaSolicitud extends AppCompatActivity {
    private static final int ASK_QUESTION_EQUIPO = 1;
    private static final int ASK_QUESTION_ESTACION = 2;
    private TextInputEditText usuarioET, fechayhoraET, equipoET, estacionET;
    private AutoCompleteTextView productoET;
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    private Equipo equipo;
    private Estacion estacion;
    private Activity activity= this;
    private Button nuevaSolicitud;
    private OrdenCompraEstacionServicio ordenCompraEstacionServicio;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nueva_solicitud);
        mAuth = FirebaseAuth.getInstance();
        equipo = new Equipo();
        estacion = new Estacion();
        ordenCompraEstacionServicio = new OrdenCompraEstacionServicio();
        iniciarGraficos();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK) {
            if (requestCode == 1) {
                setEquipoET(data);

            }
            if (requestCode == 2) {
                setEstacionET(data);
            }
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(activity, MenuEstacionesDeServicio.class);
        startActivity(intent);
        finish();
        super.onBackPressed();
    }

    private void iniciarGraficos(){
        usuarioET = findViewById(R.id.textInputUsuario);
        fechayhoraET = findViewById(R.id.textInputFechayHora);
        equipoET = findViewById(R.id.textInputEquipo);
        estacionET = findViewById(R.id.texInputEstacion);
        equipoET.setInputType(InputType.TYPE_NULL);
        estacionET.setInputType(InputType.TYPE_NULL);
        productoET = findViewById(R.id.prducto_dropdown);
        nuevaSolicitud = findViewById(R.id.nuevaSolicitud);
        nuevaSolicitud.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveSolicitud();
            }
        });
        estacionET.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if(hasFocus) {
                    Intent intent = new Intent(activity, EstacionesDeServicio.class);
                    startActivityForResult(intent,ASK_QUESTION_ESTACION);
                } else {
                    // when it does not have focus
                }
            }
        });
        equipoET.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if(hasFocus) {
                    Intent intent = new Intent(activity, Equipos.class);
                    startActivityForResult(intent,ASK_QUESTION_EQUIPO);
                } else {
                    // when it does not have focus
                }
            }
        });
        setUsuario();
        setFechayHora();
        setProductos();
    }

    private void setUsuario(){
        usuarioET.setText(mAuth.getCurrentUser().getEmail());
    }
    private void setFechayHora(){
        Date currentTime = Calendar.getInstance().getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm dd/MM/yyyy");
        fechayhoraET.setText(sdf.format(currentTime));

    }

    private void setEquipoET(Intent data){
        equipo.setMarca(data.getStringExtra("marca"));
        equipo.setModelo(data.getStringExtra("modelo"));
        equipo.setId(data.getStringExtra("id"));
        equipo.setFoto(data.getStringExtra("foto"));
        equipoET.setText("("+equipo.getId()+") "+ equipo.getMarca() + " " + equipo.getModelo());
        equipoET.clearFocus();
    }
    private void setEstacionET(Intent data){
        estacion.setCity(data.getStringExtra("city"));
        estacion.setName(data.getStringExtra("name"));
        estacion.setId(data.getStringExtra("id"));
        estacion.setMail(data.getStringExtra("mail"));
        estacion.setPicture(data.getStringExtra("picture"));
        estacion.setPhone(data.getStringExtra("phone"));
        estacion.setLocation(data.getStringExtra("location"));
        estacion.setAdress(data.getStringExtra("adress"));
        estacionET.setText(estacion.getName() + ", " +estacion.getCity());
        estacionET.clearFocus();

    }
    private void setProductos(){

        mDatabase = FirebaseDatabase.getInstance().getReference().child("Listado de productos").child("Estaciones de servicio");
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<String> type = new ArrayList<>();
                for (DataSnapshot ds:snapshot.getChildren()){
                    type.add(ds.child("nombre").getValue().toString());
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<>(activity,R.layout.dropdown_menu_popup_item_producto_estacion,type);
                AutoCompleteTextView editTextFilledExposedDropdown = findViewById(R.id.prducto_dropdown);
                editTextFilledExposedDropdown.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void saveSolicitud(){
        if(checkEmpty()) {
            ordenCompraEstacionServicio.NuevaSolicitud(mAuth.getCurrentUser().getEmail(), fechayhoraET.getText().toString(), equipo, "Pendiente autorizacion", estacion, productoET.getText().toString());
            String user = mAuth.getCurrentUser().getEmail().replace(".", "");
            mDatabase = FirebaseDatabase.getInstance().getReference().child("Ordenes Estaciones de Servicio").child("Pendiente autorizacion").child(user);
            mDatabase = mDatabase.push();
            mDatabase.setValue(ordenCompraEstacionServicio);
            Intent intent = new Intent(activity, MainActivity.class);
            Toast.makeText(activity,"Solicitud Finalizada",Toast.LENGTH_LONG).show();
            Toast.makeText(activity,"Puede seguirla en Solicitudes Pendientes",Toast.LENGTH_LONG).show();
            startActivity(intent);
            finish();
        }else{
            Toast.makeText(activity,"Faltan completar datos",Toast.LENGTH_LONG).show();
        }
    }
    private boolean checkEmpty(){
        boolean empty = true;
        if(equipoET.getText().toString().isEmpty()){
            empty = false;
        }
        if(estacionET.getText().toString().isEmpty()){
            empty = false;
        }
        if(productoET.getText().toString().isEmpty()){
            empty = false;
        }
        return empty;
    }
}
