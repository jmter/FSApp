package com.jmt.fsapp.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jmt.fsapp.Adapter.EquiposAdapter;
import com.jmt.fsapp.Adapter.MenusAdapter;
import com.jmt.fsapp.POJO.Equipo;
import com.jmt.fsapp.R;

import java.util.ArrayList;

public class Equipos extends AppCompatActivity {
    private Spinner categoria, subcategoria;
    private Activity activity = this;
    private DatabaseReference mdataBase;
    private Equipo equipo = new Equipo();
    private RecyclerView equipoRV;

    private FirebaseUser user;
    private ArrayList<String> category,subcategory;
    private ArrayList<Equipo> equipos;
    @Override
    protected void onCreate(Bundle savedInstanceState) {


        user = FirebaseAuth.getInstance().getCurrentUser();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_equipos);

        iniciarGraficos();
        setUpSpinner();
        readData(new FirebaseCallback() {
            @Override
            public void onCallback(ArrayList<Equipo> equipos, ArrayList<ArrayList<String>> categorias) {
                Log.i("Equipo", equipos.toString());
                if(equipos.size()>0) {
                    EquiposAdapter adaptador = new EquiposAdapter(equipos, activity);
                    equipoRV.setAdapter(adaptador);
                }
            }
        });


    }
    private void iniciarGraficos(){
        equipoRV = findViewById(R.id.listadoequiposRV);
        GridLayoutManager gridLayoutManagerm = new GridLayoutManager(this,2);
        equipoRV.setLayoutManager(gridLayoutManagerm);
        categoria = findViewById(R.id.spCategoria);
        subcategoria = findViewById(R.id.spTipo);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menuoverflow_maquinas,menu);
        return true;
    }
    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();
        if(id == R.id.menuAgregarMaquina){
            Intent intent = new Intent(activity,AgregarEquipo.class);
            startActivity(intent);
            finish();
        }
        if(id == R.id.menuSalir){
            Intent intent = new Intent(activity,MainActivity.class);
            startActivity(intent);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private void readData(final FirebaseCallback firebaseCallback){
        mdataBase = FirebaseDatabase.getInstance().getReference().child("Equipos");
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

    private void setUpSpinner(){
        readData(new FirebaseCallback() {
            @Override
            public void onCallback(final ArrayList<Equipo> equipos, final ArrayList<ArrayList<String>> subcategories) {
                final ArrayList<String> categories = new ArrayList<>();
                for (ArrayList cat:subcategories){
                    categories.add(cat.get(0).toString());
                }
                ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(activity,android.R.layout.simple_spinner_item,categories);
                categoria.setAdapter(spinnerArrayAdapter);
                categoria.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        for(ArrayList cat:subcategories){
                            if (cat.get(0).toString().equals(categories.get(position))){
                                ArrayList<String> subcat = (ArrayList<String>) cat.clone();
                                subcat.remove(0);
                                for(int i=0;subcat.size() > i;++i){
                                    if(subcat.get(i).equals("Todos")){
                                        subcat.remove(i);

                                    }
                                }
                                ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(activity,android.R.layout.simple_spinner_item,subcat);
                                subcategoria.setAdapter(spinnerArrayAdapter);
                                subcategoria.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                    @Override
                                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                                    }

                                    @Override
                                    public void onNothingSelected(AdapterView<?> parent) {

                                    }
                                });
                            }
                        }


                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
            }
        });

    }
}
