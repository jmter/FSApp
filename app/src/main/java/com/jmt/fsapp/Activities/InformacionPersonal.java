package com.jmt.fsapp.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;


import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jmt.fsapp.POJO.Personal;
import com.jmt.fsapp.R;
import com.jmt.fsapp.datatype.Fecha;

import java.util.ArrayList;

public class InformacionPersonal extends AppCompatActivity {
    private Toolbar toolbar;
    private EditText nombre,apellido,domicilio,fechanac,telefono,charlainduc,fechaingreso,seccion,categoria,credencial,numCI,vencCI,vencCS,vencLC,catLC;
    private Button agregarpicture,rotarpicture,agregarCI,rotarCI,agregarLC,rotarLC,agregarCS,rotarCS,guardar;
    private Spinner usuarios;
    private ArrayList<EditText> editTexts = new ArrayList<>();
    private ArrayList<Personal> personals = new ArrayList<>();
    private ImageView ci,lc,cs,pict;
    private DatabaseReference mdataBase;
    private FirebaseUser user;
    private Activity activity = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_informacion_personal);
        iniciarGraficos();
        user = FirebaseAuth.getInstance().getCurrentUser();
        loadDB();

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
        pict = findViewById(R.id.picture);
        agregarCI = findViewById(R.id.agregarCI);
        agregarCS = findViewById(R.id.agregarCS);
        agregarLC = findViewById(R.id.agregarLC);
        agregarpicture = findViewById(R.id.agregarpicture);
        rotarCI = findViewById(R.id.rotarCI);
        rotarCS = findViewById(R.id.rotarCS);
        rotarLC = findViewById(R.id.rotarLC);
        rotarpicture = findViewById(R.id.rotarpicture);
        guardar = findViewById(R.id.guardar);
        usuarios = findViewById(R.id.usuariosSP);
        cargarEditTexts();
        for(EditText et: editTexts){
            et.setFocusable(false);
            et.setEnabled(false);
        }

    }
    //Metodo que carga el menu a partiru de un objeto tipo Personal
    private void cargarInfo(Personal personal){
        nombre.setText(personal.getNombre());
        apellido.setText(personal.getApellido());
        domicilio.setText(personal.getDomicilio());
        fechanac.setText(personal.getNacimiento().toString());
        telefono.setText(personal.getTelefono());
       // charlainduc.setText(personal.getCind().toString());
        fechaingreso.setText(personal.getIngreso().toString());
       // seccion.setText(personal.getSeccion().toString());
        categoria.setText(personal.getCategoria());
        credencial.setText(personal.getCredencial());
        numCI.setText(personal.getCid().numero);
        vencCI.setText(personal.getCid().venc.toString());
        vencCS.setText(personal.getCsalud().venc.toString());
        vencLC.setText(personal.getLicienciaC().venc.toString());
        catLC.setText(personal.getLicienciaC().cat);
        Glide.with(activity).load(personal.getCid().imagen).into(ci);
        Glide.with(activity).load(personal.getLicienciaC().imagen).into(lc);
        Glide.with(activity).load(personal.getCsalud().imagen).into(cs);
        Glide.with(activity).load(personal.getPicture()).into(pict);
    }

    //Metodo que carga el array personals con la info de los usuarios
    private void loadDB(){
        mdataBase = FirebaseDatabase.getInstance().getReference().child("Usuarios");
        mdataBase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                personals.clear();
                personals = loadPersonal(snapshot);
                loadUsers(personals);
                for (Personal ps : personals) {
                    if (ps.getUsuario().equals(user.getEmail())) {
                        cargarInfo(ps);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
    //Metodo que a partir de un data snapshot devuelve listado de info de usarios
    private ArrayList<Personal> loadPersonal(DataSnapshot snapshot){
        ArrayList<Personal> users = new ArrayList<>();
        for(DataSnapshot ds : snapshot.getChildren()){
            Personal personal = new Personal();
            ;
            try {
                personal.setUsuario(ds.child("Usuario").getValue().toString());
                personal.setNombre(ds.child("Nombre").getValue().toString());
                personal.setApellido(ds.child("Apellido").getValue().toString());
                personal.setDomicilio(ds.child("Domicilio").getValue().toString());
                personal.setNacimiento(new Fecha(Integer.parseInt(ds.child("Fecha de Nacimiento").child("Dia").getValue().toString()),Integer.parseInt(ds.child("Fecha de Nacimiento").child("Mes").getValue().toString()),Integer.parseInt(ds.child("Fecha de Nacimiento").child("Ano").getValue().toString())));
                personal.setTelefono(ds.child("Telefono").getValue().toString());
                //personal.setCind(new Fecha(Integer.parseInt(ds.child("CInd").child("Dia").getValue().toString()),Integer.parseInt(ds.child("CInd").child("Mes").getValue().toString()),Integer.parseInt(ds.child("CInd").child("Ano").getValue().toString())));
                personal.setIngreso(new Fecha(Integer.parseInt(ds.child("Ingreso").child("Dia").getValue().toString()),Integer.parseInt(ds.child("Ingreso").child("Mes").getValue().toString()),Integer.parseInt(ds.child("Ingreso").child("Ano").getValue().toString())));
                //personal.setSeccion(ds.child("Seccion").getValue().toString());
                personal.setCategoria(ds.child("Categoria").getValue().toString());
                personal.setCredencial(ds.child("Credencial").getValue().toString());
                personal.setCidnumero(ds.child("CI").child("Numero").getValue().toString());
                personal.setCidvenc(new Fecha(Integer.parseInt(ds.child("CI").child("Vencimiento").child("Dia").getValue().toString()),Integer.parseInt(ds.child("CI").child("Vencimiento").child("Mes").getValue().toString()),Integer.parseInt(ds.child("CI").child("Vencimiento").child("Ano").getValue().toString())));
                personal.setCSvenc(new Fecha(Integer.parseInt(ds.child("CS").child("Vencimiento").child("Dia").getValue().toString()),Integer.parseInt(ds.child("CS").child("Vencimiento").child("Mes").getValue().toString()),Integer.parseInt(ds.child("CS").child("Vencimiento").child("Ano").getValue().toString())));
                personal.setLCvenc(new Fecha(Integer.parseInt(ds.child("LC").child("Vencimiento").child("Dia").getValue().toString()),Integer.parseInt(ds.child("LC").child("Vencimiento").child("Mes").getValue().toString()),Integer.parseInt(ds.child("LC").child("Vencimiento").child("Ano").getValue().toString())));
                personal.setLCcategoria(ds.child("LC").child("Categoria").getValue().toString());
                personal.setLCimagen(ds.child("LC").child("Imagen").getValue().toString());
                personal.setCidimagen(ds.child("CI").child("Imagen").getValue().toString());
                personal.setCSimagen(ds.child("CS").child("Imagen").getValue().toString());
                personal.setPicture(ds.child("Picture").getValue().toString());
                users.add(personal);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return users;
    }

    //Metodo que carga el listado de usuarios
    private void loadUsers(ArrayList<Personal> snapshot){
        ArrayList<String> users = new ArrayList<>();
        for(Personal ds : snapshot){
            users.add(ds.getUsuario());
        }

        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, users);
        usuarios.setAdapter(spinnerArrayAdapter);
        usuarios.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                for (Personal ps : personals) {
                    if (ps.getUsuario().equals(usuarios.getItemAtPosition(position))) {
                        cargarInfo(ps);
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    //Implementaciones del menu overflow
    private void editarInfo(){
        for(EditText et: editTexts){
            et.setEnabled(true);
            et.setTextIsSelectable(true);
            et.setFocusable(true);
            et.setFocusableInTouchMode(true);

        }
        agregarCI.setVisibility(View.VISIBLE);
        agregarCS.setVisibility(View.VISIBLE);
        agregarLC.setVisibility(View.VISIBLE);
        agregarpicture.setVisibility(View.VISIBLE);
        rotarCI.setVisibility(View.VISIBLE);
        rotarCS.setVisibility(View.VISIBLE);
        rotarLC.setVisibility(View.VISIBLE);
        rotarpicture.setVisibility(View.VISIBLE);
        guardar.setVisibility(View.VISIBLE);

        fechanac.setTextIsSelectable(false);
        fechaingreso.setTextIsSelectable(false);
        vencCI.setTextIsSelectable(false);
        vencLC.setTextIsSelectable(false);
        vencCS.setTextIsSelectable(false);
        iniciarDatePicker();
    }
    private void cargarEditTexts(){
        editTexts.add(nombre);
        editTexts.add(apellido);
        editTexts.add(domicilio);
        editTexts.add(fechanac);
        editTexts.add(telefono);
        editTexts.add(charlainduc);
        editTexts.add(fechaingreso);
        editTexts.add(seccion);
        editTexts.add(categoria);
        editTexts.add(credencial);
        editTexts.add(numCI);
        editTexts.add(vencCI);
        editTexts.add(vencCS);
        editTexts.add(catLC);
        editTexts.add(vencLC);

    }



    //Metodos del menu overflow
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menuoverflow,menu);
        return true;
    }
    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();
        if(id == R.id.itemmenueditar){
            editarInfo();
            Log.i("MENU","Editar Info");
        }
        if(id == R.id.itemmenurecibo){
            Log.i("MENU","El segundo");
        }
        if(id == R.id.menuitemSalir){
            Log.i("MENU","El tercero");
        }
        return super.onOptionsItemSelected(item);
    }

    //Inicializar DatePciker
    public void iniciarDatePicker(){
        fechanac.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog1 = new DatePickerDialog(activity, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        fechanac.setText(dayOfMonth+"/"+month+"/"+year);
                    }
                },1980,1,1);
                datePickerDialog1.show();
            }
        });
        fechaingreso.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog2 = new DatePickerDialog(activity, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        fechanac.setText(dayOfMonth+"/"+month+"/"+year);
                    }
                },1980,1,1);
                datePickerDialog2.show();
            }
        });

        vencCI.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog3 = new DatePickerDialog(activity, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        fechanac.setText(dayOfMonth+"/"+month+"/"+year);
                    }
                },1980,1,1);
                datePickerDialog3.show();
            }
        });
        vencLC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog4
                        = new DatePickerDialog(activity, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        fechanac.setText(dayOfMonth+"/"+month+"/"+year);
                    }
                },1980,1,1);
                datePickerDialog4.show();
            }
        });
        vencCS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog5 = new DatePickerDialog(activity, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        fechanac.setText(dayOfMonth+"/"+month+"/"+year);
                    }
                },1980,1,1);
                datePickerDialog5.show();
            }
        });

    }
}
