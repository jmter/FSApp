package com.jmt.fsapp.Activities.User;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jmt.fsapp.Activities.User.InformacionPersonal;
import com.jmt.fsapp.Constructors.ConstructorMisRecibosDeSueldo;
import com.jmt.fsapp.POJO.Personal;
import com.jmt.fsapp.POJO.ReciboDeSueldo;
import com.jmt.fsapp.R;
import com.jmt.fsapp.datatype.Fecha;

import java.util.ArrayList;

public class MisRecibosDeSueldo extends AppCompatActivity {
    private Activity activity = this;
    private RecyclerView recibosdesueldoRV;
    private FirebaseUser user;
    private DatabaseReference mdataBase;
    private ArrayList<ReciboDeSueldo> recibos = new ArrayList();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        user = FirebaseAuth.getInstance().getCurrentUser();
        mdataBase = FirebaseDatabase.getInstance().getReference().child("Usuarios");
        setContentView(R.layout.activity_mis_recibos_de_sueldo);
        iniciarGraficos();
    }

    private void iniciarGraficos(){
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        currentUsercid();
    }

    private void loadRecibos(String cidActualUser){
        recibosdesueldoRV = findViewById(R.id.recibosdesueldoRV);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recibosdesueldoRV.setLayoutManager(linearLayoutManager);
        recibos = new ConstructorMisRecibosDeSueldo(recibosdesueldoRV, activity).loadRecibos(cidActualUser);
    }
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(activity, InformacionPersonal.class);
        startActivity(intent);
        finish();
    }

    public void currentUsercid(){
            mdataBase.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for(DataSnapshot ds : snapshot.getChildren()) {
                            for (DataSnapshot sd : ds.getChildren()) {
                                if (sd.child("usuario").getValue().toString().equals(user.getEmail())) {
                                    Personal personal = new Personal();
                                    try {
                                        personal.setReference(sd.getRef());
                                        personal.setUsuario(sd.child("usuario").getValue().toString());
                                        personal.setNombre(sd.child("nombre").getValue().toString());
                                        personal.setApellido(sd.child("apellido").getValue().toString());
                                        personal.setDomicilio(sd.child("domicilio").getValue().toString());
                                        personal.setNacimiento(new Fecha(Integer.parseInt(sd.child("nacimiento").child("dia").getValue().toString()), Integer.parseInt(sd.child("nacimiento").child("mes").getValue().toString()), Integer.parseInt(sd.child("nacimiento").child("ano").getValue().toString())));
                                        personal.setTelefono(sd.child("telefono").getValue().toString());
                                        //personal.setCind(new Fecha(Integer.parseInt(ds.child("CInd").child("Dia").getValue().toString()),Integer.parseInt(ds.child("CInd").child("Mes").getValue().toString()),Integer.parseInt(ds.child("CInd").child("Ano").getValue().toString())));
                                        personal.setIngreso(new Fecha(Integer.parseInt(sd.child("ingreso").child("dia").getValue().toString()), Integer.parseInt(sd.child("ingreso").child("mes").getValue().toString()), Integer.parseInt(sd.child("ingreso").child("ano").getValue().toString())));
                                        personal.setSeccion(sd.child("seccion").getValue().toString());
                                        personal.setCategoria(sd.child("categoria").getValue().toString());
                                        personal.setCredencial(sd.child("credencial").getValue().toString());
                                        personal.setCidnumero(sd.child("cid").child("numero").getValue().toString());
                                        personal.setCidvenc(new Fecha(Integer.parseInt(sd.child("cid").child("venc").child("dia").getValue().toString()), Integer.parseInt(sd.child("cid").child("venc").child("mes").getValue().toString()), Integer.parseInt(sd.child("cid").child("venc").child("ano").getValue().toString())));
                                        personal.setCSvenc(new Fecha(Integer.parseInt(sd.child("csalud").child("venc").child("dia").getValue().toString()), Integer.parseInt(sd.child("csalud").child("venc").child("mes").getValue().toString()), Integer.parseInt(sd.child("csalud").child("venc").child("ano").getValue().toString())));
                                        personal.setLCvenc(new Fecha(Integer.parseInt(sd.child("licenciaC").child("venc").child("dia").getValue().toString()), Integer.parseInt(sd.child("licenciaC").child("venc").child("mes").getValue().toString()), Integer.parseInt(sd.child("licenciaC").child("venc").child("ano").getValue().toString())));
                                        personal.setLCcategoria(sd.child("licenciaC").child("cat").getValue().toString());
                                        personal.setLCimagen(sd.child("licenciaC").child("imagen").getValue().toString());
                                        personal.setCidimagen(sd.child("cid").child("imagen").getValue().toString());

                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                    loadRecibos(personal.getCid().numero);
                                    Log.i("Recibo", personal.getCid().numero);
                                }
                            }
                        }
                    }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
    }
}
