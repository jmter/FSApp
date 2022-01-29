package com.jmt.fsapp.Activities.Combustible;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jmt.fsapp.POJO.Equipo;
import com.jmt.fsapp.POJO.Estacion;
import com.jmt.fsapp.POJO.OrdenCompraEstacionServicio;
import com.jmt.fsapp.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

public class SolicitudEstacion extends AppCompatActivity {
    Activity activity = this;
    private DatabaseReference mDatabase;
    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private TextInputEditText horasolicitudTI,estacionTI,textInputUsuario,equipoTI,productoTI,cantidadTIET,horaskm;
    private TextView textViewCode;
    private LinearLayout llFinalizado,llNoFinalizado;
    private CheckBox horaskmCheck;
    private TextInputLayout cantidadTIL,horaskmTI;
    private Button finalizarBTT;
    private OrdenCompraEstacionServicio solicitud = new OrdenCompraEstacionServicio();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_solicitud_estacion);
        iniciarGraficos();
        Intent intent  = getIntent();
        getData(intent.getStringExtra("key"),intent.getStringExtra("estado"));
        Log.i("Estado",intent.getStringExtra("estado"));
        if(intent.getStringExtra("estado").equals("CompletadasTrans")){
            showSolicitudFin();
        }
        cantidadTIET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                Log.i("Cantidad", "Cambio");
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

    }

    public void onBackPressed() {
        Intent intent = new Intent(activity, MenuEstacionesDeServicio.class);
        startActivity(intent);
        finish();
    }

    public void iniciarGraficos(){
        horasolicitudTI = findViewById(R.id.horasolicitudTI);
        estacionTI = findViewById(R.id.estacionTI);
        textInputUsuario = findViewById(R.id.textInputUsuario);
        equipoTI = findViewById(R.id.equipoTI);
        estacionTI = findViewById(R.id.estacionTI);
        productoTI = findViewById(R.id.productoTI);
        cantidadTIET = findViewById(R.id.cantidadTIET);
        cantidadTIL = findViewById(R.id.cantidadTIL);
        finalizarBTT = findViewById(R.id.finalizarBTT);
        horaskm = findViewById(R.id.horaskm);
        horaskmCheck = findViewById(R.id.horaskmCheck);
        llFinalizado = findViewById(R.id.llFinalizado);
        horaskmTI = findViewById(R.id.horaskmTI);
        llNoFinalizado = findViewById(R.id.llNoFinalizado);
        textViewCode = findViewById(R.id.textViewCode);
        setHorasFunction();
        finalizarBTT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(checkForData()){
                    //sendEmail();
                    setHoraCompleto();
                    showSolicitudFin();
                    saveSolicitud(solicitud);
                    deleteSolicitudFromAut();
                }
            }
        });

    }

    public void getData(String key,String estado) {
        String userE = user.getEmail().replace(".","");
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Ordenes Estaciones de Servicio").child(estado).child(userE).child(key);
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot sd) {
                if (sd.exists()) {
                    solicitud.setProducto(sd.child("producto").getValue().toString());
                    solicitud.setOperario(sd.child("operario").getValue().toString());
                    Equipo equipo = new Equipo();
                    equipo.setId(sd.child("equipo").child("id").getValue().toString());
                    equipo.setMarca(sd.child("equipo").child("marca").getValue().toString());
                    equipo.setModelo(sd.child("equipo").child("modelo").getValue().toString());
                    equipo.setFoto(sd.child("equipo").child("foto").getValue().toString());
                    Estacion estacion = new Estacion();
                    estacion.setName(sd.child("estacion").child("name").getValue().toString());
                    estacion.setCity(sd.child("estacion").child("city").getValue().toString());
                    estacion.setId(sd.child("estacion").child("id").getValue().toString());
                    estacion.setPicture(sd.child("estacion").child("picture").getValue().toString());
                    solicitud.setFechayhorasolicitud(sd.child(("fechayhorasolicitud")).getValue().toString());
                    solicitud.setFechayhoraautorizacion(sd.child(("fechayhoraautorizacion")).getValue().toString());
                    solicitud.setMaximoAutorizado(sd.child(("maximoAutorizado")).getValue().toString());
                    solicitud.setKey(sd.getKey());
                    solicitud.setEstacion(estacion);
                    solicitud.setEquipo(equipo);
                    cantidadTIET.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                        }

                        @Override
                        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                            String cant = cantidadTIET.getText().toString();
                            if (!cant.equals("")) {
                                if (Float.parseFloat(cant) > Float.parseFloat(solicitud.getMaximoAutorizado())) {
                                    cantidadTIL.setError("La cantidad maxsima autorizada es: " + solicitud.getMaximoAutorizado());
                                } else {
                                    cantidadTIL.setHelperText("La cantidad maxima autorizada es: " + solicitud.getMaximoAutorizado());
                                }
                            }
                        }

                        @Override
                        public void afterTextChanged(Editable editable) {

                        }
                    });
                    setData(solicitud);
                    mDatabase.removeEventListener(this);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void setData(OrdenCompraEstacionServicio solicitud){
        horasolicitudTI.setText(solicitud.getFechayhorasolicitud());
        estacionTI.setText(solicitud.getEstacion().getName());
        textInputUsuario.setText(solicitud.getOperario());
        String etiquetaEquipo = solicitud.getEquipo().getMarca() + " " + solicitud.getEquipo().getModelo();
        equipoTI.setText(etiquetaEquipo);
        productoTI.setText(solicitud.getProducto());
        cantidadTIL.setHelperText("La cantidad maxima autorizada es: "+solicitud.getMaximoAutorizado());
    }

    public boolean checkForData(){
        boolean valid = true;
        String cant = cantidadTIET.getText().toString();
        if(cant.equals("")){
            cantidadTIL.setError("La cantidad no puede ser 0" );
            valid = false;
        }else{
            if (Float.parseFloat(cant) > Float.parseFloat(solicitud.getMaximoAutorizado())) {
                cantidadTIL.setError("La cantidad maxsima autorizada es: " + solicitud.getMaximoAutorizado());
                valid = false;
            }
        }
        String horas = horaskm.getText().toString();
        if(horas.equals("") & !horaskmCheck.isChecked()){
            horaskmTI.setError("La horas/km no puede ser 0" );
            valid = false;
        }else {
            horaskmTI.setError("");
        }
        return valid;
    }

    public void setHorasFunction(){
        horaskm.addTextChangedListener(new TextWatcher() {
                                           @Override
                                           public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                                           }

                                           @Override
                                           public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                                               if(charSequence.length() > 0){
                                                   Log.i("Check","desCheck" +charSequence);
                                                   horaskmCheck.setChecked(false);
                                               }
                                           }

                                           @Override
                                           public void afterTextChanged(Editable editable) {

                                           }
                                       });
        horaskmCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                        if (b) {
                            horaskm.setText("");
                        }
                    }
        });
    }
    public void sendEmail(){
    }
    public void showSolicitudFin(){
        llFinalizado.setVisibility(View.VISIBLE);
        llNoFinalizado.setVisibility(View.GONE);
        horaskmCheck.setEnabled(false);
        cantidadTIET.setFocusable(false);
        cantidadTIL.setEnabled(false);
        cantidadTIET.setInputType(0);
        horaskmTI.setEnabled(false);
        horaskm.setFocusable(false);
        horaskm.setInputType(0);
        finalizarBTT.setVisibility(View.GONE);
        String condeText = "Codigo: "+ calcCode();
        textViewCode.setText(condeText);

    }
    public void saveSolicitud(OrdenCompraEstacionServicio soli){
        String user = mAuth.getCurrentUser().getEmail().replace(".", "");
        soli.setEstado("Completada");
        if(!horaskmCheck.isChecked()){
            soli.setHoraskm(horaskm.getText().toString());
        }else{
            soli.setHoraskm("NTNA");
        }
        soli.setCantidadcargada(cantidadTIET.getText().toString());
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Ordenes Estaciones de Servicio").child("CompletadasTrans").child(user);
        mDatabase = mDatabase.push();
        mDatabase.setValue(soli);
    }
    public void deleteSolicitudFromAut(){
        String user = mAuth.getCurrentUser().getEmail().replace(".", "");
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Ordenes Estaciones de Servicio").child("Autorizado").child(user).child(solicitud.getKey());
        mDatabase.removeValue();
    }
    private void setHoraCompleto(){
        Date currentTime = Calendar.getInstance().getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm dd/MM/yyyy");
        solicitud.setFechayhoracompleto(sdf.format(currentTime));
    }
    public String calcCode(){
        String code = "";
        int acum = 0;
        Random random = new Random();
        int value = random.nextInt(90 - 65) + 65;
        acum = acum +value;
        char character = (char)value;
        code = code + character;
        value = random.nextInt(90 - 65) + 65;
        acum = acum +value;
        character = (char)value;
        code = code + character;
        value = random.nextInt(90 - 65) + 65;
        acum = acum +value;
        character = (char)value;
        code = code + character;
        acum = acum * 37;
        code = code + acum;
        Log.i("Code",code);
        return code;
    }
}
