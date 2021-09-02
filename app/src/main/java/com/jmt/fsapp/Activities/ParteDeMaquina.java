package com.jmt.fsapp.Activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jmt.fsapp.Constructors.ConstructorPreguntasPD;
import com.jmt.fsapp.POJO.Equipo;
import com.jmt.fsapp.POJO.Personal;
import com.jmt.fsapp.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class ParteDeMaquina extends AppCompatActivity {
    private static final int ASK_QUESTION_PROFILE = 1;
    private Activity activity = this;
    private DatabaseReference mdataBase;
    private Equipo equipo = new Equipo();
    private Personal personal = new Personal();
    private TextView tvFecha,tvEquipo,tvUsuario;
    private FirebaseAuth mAuth;
    private RecyclerView partedeMaquinaRV;
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
        partedeMaquinaRV = findViewById(R.id.partedeMaquinaRV);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        partedeMaquinaRV.setLayoutManager(linearLayoutManager);
        new ConstructorPreguntasPD(partedeMaquinaRV, activity).loadPreguntas();
    }
    public void setTvFecha(){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm");
        String currentDateandTime = simpleDateFormat.format(new Date());
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
}
