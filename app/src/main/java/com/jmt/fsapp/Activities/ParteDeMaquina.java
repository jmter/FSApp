package com.jmt.fsapp.Activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jmt.fsapp.Activities.Equipos.Equipos;
import com.jmt.fsapp.Constructors.ConstructorPreguntasPD;
import com.jmt.fsapp.POJO.Equipo;
import com.jmt.fsapp.POJO.Personal;
import com.jmt.fsapp.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class ParteDeMaquina extends AppCompatActivity {
    private static final int ASK_QUESTION_PROFILE = 1;
    private Activity activity = this;
    private DatabaseReference mdataBase;
    private Equipo equipo = new Equipo();
    private Personal personal = new Personal();
    private TextView tvFecha,tvEquipo,tvUsuario;
    private FirebaseAuth mAuth;
    private ArrayList<String> preguntas = new ArrayList();
    private ArrayList<String> preguntasc;
    private HashMap<String,String> respuestas=  new HashMap<String, String>();
    private RecyclerView partedeMaquinaRV;
    private String date;
    private String currentDateandTime;
    private Button finalizar,restablecer,aceptar, cancelar;
    private EditText comentario;
    private AlertDialog.Builder dialogBuilder;
    private AlertDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = new Intent(activity, Equipos.class);
        startActivityForResult(intent, ASK_QUESTION_PROFILE);
    }
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menuoverflow_parte_de_maquina,menu);
        return true;
    }
    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();
        if(id == R.id.menuSalir){
            Intent intent = new Intent(activity,MainActivity.class);
            startActivity(intent);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(resultCode == RESULT_OK){
            setContentView(R.layout.activity_parte_de_maquina);
            Toolbar toolbar = findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);
            iniciarGraficos();
            setTvFecha();
            setTvEquipo(data);
            setTvUsuario();
        }else {
            Intent intent = new Intent(activity,MainActivity.class);
            startActivity(intent);
            finish();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
    public void iniciarGraficos(){
        tvEquipo = findViewById(R.id.tvEquipo);
        tvFecha = findViewById(R.id.tvFecha);
        tvUsuario = findViewById(R.id.tvUsuario);
        restablecer = findViewById(R.id.buttonRestablecer);
        restablecer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            restablecer();
            }
        });
        finalizar = findViewById(R.id.buttonFinalizar);
        finalizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finalizarParte();
            }
        });
        partedeMaquinaRV = findViewById(R.id.partedeMaquinaRV);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        partedeMaquinaRV.setLayoutManager(linearLayoutManager);
        preguntas = new ConstructorPreguntasPD(partedeMaquinaRV, activity).loadPreguntas();
        new ItemTouchHelper(itemtouchhelpercallback).attachToRecyclerView(partedeMaquinaRV);
    }
    public void setTvFecha(){
        SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("dd-MM-yyyy HH:mm");
        SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat("yyyyMMddHHmm");
        currentDateandTime = simpleDateFormat1.format(new Date());
        date = simpleDateFormat2.format(new Date());
        tvFecha.setText(currentDateandTime);
    }
    public void setTvEquipo(Intent data){
        String id = data.getStringExtra("id");
        equipo.reverseID(id);
        mdataBase = FirebaseDatabase.getInstance().getReference().child("Equipos").child(equipo.getCategoria()).child(equipo.getSubcategoria()).child(id);
        mdataBase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot ds) {
                equipo.setFoto(ds.child("foto").getValue().toString());
                equipo.setModelo(ds.child("modelo").getValue().toString());
                equipo.setMarca(ds.child("marca").getValue().toString());
                equipo.setId(ds.child("id").getValue().toString());
                tvEquipo.setText(equipo.getMarca()+" "+equipo.getModelo()+" ("+equipo.getId()+")");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
    public void setTvUsuario(){
        mAuth = FirebaseAuth.getInstance();
        personal.setUsuario(mAuth.getCurrentUser().getEmail());
        mdataBase = FirebaseDatabase.getInstance().getReference().child("Usuarios");
        mdataBase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot ds:dataSnapshot.getChildren()){
                    for(DataSnapshot sd:ds.getChildren()){
                        if(sd.child("usuario").getValue().equals(personal.getUsuario())){
                            personal.setNombre(sd.child("nombre").getValue().toString());
                            personal.setApellido(sd.child("apellido").getValue().toString());
                            String usuario = personal.getNombre() +" "+personal.getApellido()+" ("+personal.getUsuario()+")";
                            tvUsuario.setText(usuario);
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
    ItemTouchHelper.SimpleCallback itemtouchhelpercallback = new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.RIGHT|ItemTouchHelper.LEFT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            final int position = viewHolder.getAdapterPosition();
            partedeMaquinaRV.getAdapter().notifyItemRemoved(position);

            switch (direction){
                case ItemTouchHelper.RIGHT:
                    respuestas.put(preguntas.get(position),"Aceptable");
                    preguntas.remove(position);
                    break;
                case ItemTouchHelper.LEFT:
                    respuestas.put(preguntas.get(position),"Rechazado");
                    preguntas.remove(position);
                    break;

            }
        }

        @Override
        public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
            View itemView = viewHolder.itemView;
            Drawable d1 = ContextCompat.getDrawable(activity, R.drawable.aceptable);
            Drawable d2 = ContextCompat.getDrawable(activity, R.drawable.rechazado);
            d1.setBounds(itemView.getLeft(), itemView.getTop(), (int) dX, itemView.getBottom());
            d2.setBounds(itemView.getRight() + (int)dX, itemView.getTop(),itemView.getRight() , itemView.getBottom());
            d1.draw(c);
            d2.draw(c);
            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
        }
    };

    private void finalizarParte(){
        mdataBase = FirebaseDatabase.getInstance().getReference().child("Partes diarios").child(String.valueOf(equipo.getId())).child(dateCode(date));
        if(preguntas.size()>0){
            Toast.makeText(activity,"Debe completar todo el formualrio",Toast.LENGTH_LONG).show();
        } else {
            if(!evaluarParte(respuestas)){
                //Todo lo referente a parte rechazado
                crearPopupRechazo();
            } else{
                //Aca guardo el reporte
                respuestas.put("Fecha",currentDateandTime);
                respuestas.put("Usuario",personal.getUsuario());
                mdataBase.setValue(respuestas);
                Intent intent = new Intent(activity,MainActivity.class);
                Toast.makeText(activity,"Parte diario finalizado con exito",Toast.LENGTH_LONG).show();
                startActivity(intent);
                finish();
            }
        }
    }
    private boolean evaluarParte(HashMap<String,String> respuestas){
        boolean test = true;
        for(Map.Entry<String,String> entry : respuestas.entrySet()){
            if (entry.getValue().equals("Rechazado")){ test = false;}
        }
        return test;
    }
    private String dateCode(String date){
        String date1;
        String date2;
        String dateC;
        date1 = date.substring(0,4);
        date2 = date.substring(4,12);
        date1 = String.valueOf(2099 - Integer.valueOf(date1));
        date2 = String.valueOf(12312359 - Integer.valueOf(date2));
        dateC = date1 + date2;
        return dateC;
    }
    private void crearPopupRechazo(){
        dialogBuilder = new AlertDialog.Builder(activity);
        final View popupRechazo  = getLayoutInflater().inflate(R.layout.popup_parte_rechazado,null);
        dialogBuilder.setView(popupRechazo);
        dialog = dialogBuilder.create();
        comentario = popupRechazo.findViewById(R.id.comentario_pd);
        cancelar = popupRechazo.findViewById(R.id.button_cancelar);
        cancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        aceptar = popupRechazo.findViewById(R.id.button_aceptar);
        aceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!comentario.getText().toString().isEmpty()){
                    respuestas.put("Fecha",currentDateandTime);
                    respuestas.put("Usuario",personal.getUsuario());
                    respuestas.put("comentario",comentario.getText().toString());
                    Intent intent = new Intent(activity,MainActivity.class);
                    mdataBase.setValue(respuestas);
                    Toast.makeText(activity,"Parte diario finalizado con exito",Toast.LENGTH_LONG).show();
                    startActivity(intent);
                    finish();
                }else {
                    Toast.makeText(activity,"Debe escribir un comentario para finalizar",Toast.LENGTH_LONG).show();
                }

            }
        });
        dialog.show();
    }
    private void restablecer(){
        for(HashMap.Entry entry:respuestas.entrySet()){
            preguntas.add(entry.getKey().toString());
        }
        Collections.sort(preguntas,new Comparator<String>(){

            @Override
            public int compare(String o1, String o2) {
                return o1.compareTo(o2);
            }
        });
        partedeMaquinaRV.getAdapter().notifyDataSetChanged();
        respuestas.clear();
    }
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(activity,ParteDeMaquina.class);
        startActivity(intent);
        finish();
    }
}
