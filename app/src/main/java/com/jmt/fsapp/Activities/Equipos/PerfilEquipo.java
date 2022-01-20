package com.jmt.fsapp.Activities.Equipos;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jmt.fsapp.Activities.MainActivity;
import com.jmt.fsapp.POJO.Equipo;
import com.jmt.fsapp.R;

import java.util.ArrayList;

public class PerfilEquipo extends AppCompatActivity {
    private static final int ASK_QUESTION_PROFILE = 1;
    private Activity activity = this;
    private DatabaseReference mdataBase;
    private Equipo equipo = new Equipo();
    private ImageView fotoIV;
    private EditText idET,marcaET,modeloET,catET,subcatET;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        iniciarGraficos();
        Intent intent = new Intent(activity, Equipos.class);
        startActivityForResult(intent, ASK_QUESTION_PROFILE);


    }
    private void iniciarGraficos(){
        idET = findViewById(R.id.etId);
        marcaET = findViewById(R.id.etMarca);
        modeloET = findViewById(R.id.etModelo);
        catET = findViewById(R.id.etCategoria);
        subcatET = findViewById(R.id.etSubcategoria);
        fotoIV = findViewById(R.id.fotoEquipo);
    }

    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menuoverflow_perfil_maquinas,menu);
        return true;
    }
    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();
        if(id == R.id.menuEditarMaquina){
            Toast.makeText(activity, "Esta funcion aun no fue desarrollada", Toast.LENGTH_SHORT).show();
        }
        if(id == R.id.menuSalir){
            Intent intent = new Intent(activity, MainActivity.class);
            startActivity(intent);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(resultCode == RESULT_OK){
            setContentView(R.layout.activity_perfil_equipo);
            iniciarGraficos();
            Toolbar toolbar = findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);
            final String id = data.getStringExtra("id");
            equipo.reverseID(id);
            mdataBase = FirebaseDatabase.getInstance().getReference().child("Equipos").child(equipo.getCategoria()).child(equipo.getSubcategoria()).child(id);
            mdataBase.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot ds) {
                    equipo.setFoto(ds.child("foto").getValue().toString());
                    equipo.setModelo(ds.child("modelo").getValue().toString());
                    equipo.setMarca(ds.child("marca").getValue().toString());
                    equipo.setId(id);
                    idET.setText(equipo.getId());
                    marcaET.setText(equipo.getMarca());
                    modeloET.setText(equipo.getModelo());
                    catET.setText(equipo.getCategoria());
                    subcatET.setText(equipo.getSubcategoria());
                    Glide.with(getApplicationContext()).load(equipo.getFoto()).into(fotoIV);


                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }else {
            Intent intent = new Intent(activity,MainActivity.class);
            startActivity(intent);
            finish();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(activity,PerfilEquipo.class);
        startActivity(intent);
        finish();
    }
    private void readData(final FirebaseCallback firebaseCallback){

        mdataBase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<ArrayList<String>> subcategories = new ArrayList<>();
                ArrayList<Equipo> equipos = new ArrayList<>();
                ArrayList<String> todos = new ArrayList<>();
                todos.add("Todos");
                todos.add("Todos ");
                subcategories.add(todos);
                for (DataSnapshot ds:snapshot.getChildren()){
                    ArrayList<String> sub = new ArrayList<>();
                    sub.add(ds.getKey());
                    for(DataSnapshot sd: ds.getChildren()){
                        sub.add(sd.getKey());
                        for(DataSnapshot eq: sd.getChildren()){
                            if (!eq.getKey().equals("Vacio")) {
                                Equipo equipo = new Equipo(eq.child("marca").getValue().toString(), eq.child("modelo").getValue().toString(), eq.child("categoria").getValue().toString(), eq.child("subcategoria").getValue().toString());
                                equipo.setPreid(Integer.valueOf(eq.child("preid").getValue().toString()));
                                equipo.setFoto(eq.child("foto").getValue().toString());
                                equipos.add(equipo);

                            }
                        }
                    }
                    subcategories.add(sub);
                }
                firebaseCallback.onCallback(equipos,subcategories);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private interface FirebaseCallback{
        void onCallback(ArrayList<Equipo> equipos,ArrayList<ArrayList<String>> categorias);
    }

}
