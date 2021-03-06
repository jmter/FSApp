package com.jmt.fsapp.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
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
import android.widget.LinearLayout;
import android.widget.Spinner;


import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
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
import java.util.HashMap;
import java.util.Map;

public class InformacionPersonal extends AppCompatActivity {
    private Toolbar toolbar;
    private EditText nombre,apellido,domicilio,fechanac,telefono,charlainduc,fechaingreso,seccion,categoria,credencial,numCI,vencCI,vencCS,vencLC,catLC,usuario,contrasena;
    private Button agregarpicture,rotarpicture,agregarCI,rotarCI,agregarLC,rotarLC,agregarCS,rotarCS,guardar;
    private LinearLayout etUsuario,etContrasena,spinerUsuarios;
    private Spinner usuarios;
    private ArrayList<EditText> editTexts = new ArrayList<>();
    private ArrayList<Personal> personals = new ArrayList<>();
    private Personal personal;
    private ImageView ci,lc,cs,pict;
    private DatabaseReference mdataBase;
    private FirebaseUser user;
    private Activity activity = this;
    private ArrayList<String> users = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_informacion_personal);
        mdataBase = FirebaseDatabase.getInstance().getReference().child("Usuarios");
        user = FirebaseAuth.getInstance().getCurrentUser();
        iniciarGraficos();
        Log.i("Usuario", "Inicio"+user.getEmail());
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
        usuario = findViewById(R.id.eTusuario);
        etUsuario = findViewById(R.id.lletusuario);
        etContrasena = findViewById(R.id.lletcontrasena);
        contrasena = findViewById(R.id.eTcontrasena);
        spinerUsuarios = findViewById(R.id.llspinerusuarios);
        cargarEditTexts();
        guardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for(Personal personal: personals){
                    if(personal.getUsuario().equals(usuarios.getSelectedItem().toString())){
                        subirInfo(gurdarInfo(personal));
                    }
                }

            }
        });
        for(EditText et: editTexts){
            et.setFocusable(false);
            et.setEnabled(false);
        }

    }
    //Metodo que carga el menu a partir de un objeto tipo Personal
    private void cargarInfo(Personal personal){
        //usuarios.setSelection(users.indexOf(user.getEmail()));
        nombre.setText(personal.getNombre());
        Log.i("Usuario", personal.getNombre());
        apellido.setText(personal.getApellido());
        domicilio.setText(personal.getDomicilio());
        fechanac.setText(personal.getNacimiento().toString());
        telefono.setText(personal.getTelefono());
       // charlainduc.setText(personal.getCind().toString());
        fechaingreso.setText(personal.getIngreso().toString());
        seccion.setText(personal.getSeccion());
        categoria.setText(personal.getCategoria());
        credencial.setText(personal.getCredencial());
        numCI.setText(personal.getCid().numero);
        vencCI.setText(personal.getCid().venc.toString());
        vencCS.setText(personal.getCsalud().venc.toString());
        vencLC.setText(personal.getLicenciaC().venc.toString());
        catLC.setText(personal.getLicenciaC().cat);
        Glide.with(getApplicationContext()).load(personal.getCid().imagen).into(ci);
        Glide.with(getApplicationContext()).load(personal.getLicenciaC().imagen).into(lc);
        Glide.with(getApplicationContext()).load(personal.getCsalud().imagen).into(cs);
        Glide.with(getApplicationContext()).load(personal.getPicture()).into(pict);
    }
    //Metodo que carga el array personals con la info de los usuarios
    private void loadDB(){
        mdataBase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                personals.clear();
                personals = loadPersonal(snapshot);
                loadUsers(personals);
                for (Personal ps : personals) {
                    if (ps.getUsuario().equals(user.getEmail())) {
                        Log.i("Usuario","Cargo"+user.getEmail());
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
                personal.setReference(ds.getRef());
                personal.setUsuario(ds.child("usuario").getValue().toString());
                personal.setNombre(ds.child("nombre").getValue().toString());
                personal.setApellido(ds.child("apellido").getValue().toString());
                personal.setDomicilio(ds.child("domicilio").getValue().toString());
                personal.setNacimiento(new Fecha(Integer.parseInt(ds.child("nacimiento").child("dia").getValue().toString()),Integer.parseInt(ds.child("nacimiento").child("mes").getValue().toString()),Integer.parseInt(ds.child("nacimiento").child("ano").getValue().toString())));
                personal.setTelefono(ds.child("telefono").getValue().toString());
                //personal.setCind(new Fecha(Integer.parseInt(ds.child("CInd").child("Dia").getValue().toString()),Integer.parseInt(ds.child("CInd").child("Mes").getValue().toString()),Integer.parseInt(ds.child("CInd").child("Ano").getValue().toString())));
                personal.setIngreso(new Fecha(Integer.parseInt(ds.child("ingreso").child("dia").getValue().toString()),Integer.parseInt(ds.child("ingreso").child("mes").getValue().toString()),Integer.parseInt(ds.child("ingreso").child("ano").getValue().toString())));
                personal.setSeccion(ds.child("seccion").getValue().toString());
                personal.setCategoria(ds.child("categoria").getValue().toString());
                personal.setCredencial(ds.child("credencial").getValue().toString());
                personal.setCidnumero(ds.child("cid").child("numero").getValue().toString());
                personal.setCidvenc(new Fecha(Integer.parseInt(ds.child("cid").child("venc").child("dia").getValue().toString()),Integer.parseInt(ds.child("cid").child("venc").child("mes").getValue().toString()),Integer.parseInt(ds.child("cid").child("venc").child("ano").getValue().toString())));
                personal.setCSvenc(new Fecha(Integer.parseInt(ds.child("csalud").child("venc").child("dia").getValue().toString()),Integer.parseInt(ds.child("csalud").child("venc").child("mes").getValue().toString()),Integer.parseInt(ds.child("csalud").child("venc").child("ano").getValue().toString())));
                personal.setLCvenc(new Fecha(Integer.parseInt(ds.child("licenciaC").child("venc").child("dia").getValue().toString()),Integer.parseInt(ds.child("licenciaC").child("venc").child("mes").getValue().toString()),Integer.parseInt(ds.child("licenciaC").child("venc").child("ano").getValue().toString())));
                personal.setLCcategoria(ds.child("licenciaC").child("cat").getValue().toString());
                personal.setLCimagen(ds.child("licenciaC").child("imagen").getValue().toString());
                personal.setCidimagen(ds.child("cid").child("imagen").getValue().toString());
                personal.setCSimagen(ds.child("csalud").child("imagen").getValue().toString());
                personal.setPicture(ds.child("picture").getValue().toString());
                users.add(personal);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return users;
    }
    //Metodo que carga el listado de usuarios
    private void loadUsers(ArrayList<Personal> snapshot){
        users.clear();
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
                        Log.i("Usuario","Selecciono"+ps.getNombre());
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
        agregarpicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, CameraScanDoc.class);
                startActivity(intent);
                finish();
            }
        });
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
    //Metodo usado para agregar un usuario nuevo
    private void usuarioNuevo(){
        personal = new Personal();
        editarInfo();
        spinerUsuarios.setVisibility(View.GONE);
        etUsuario.setVisibility(View.VISIBLE);
        etContrasena.setVisibility(View.VISIBLE);
        cargarInfo(personal);
        guardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                personal = gurdarInfo(personal);
                personal.setUsuario(usuario.getText().toString());
                final FirebaseAuth newUser = FirebaseAuth.getInstance();
                newUser.createUserWithEmailAndPassword(usuario.getText().toString(), contrasena.getText().toString())
                        .addOnCompleteListener(activity, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    mdataBase.push().setValue(personal);
                                    activity.recreate();
                                } else {
                                    // If sign in fails, display a message to the user.
                                }
                            }
                        });
            }
        });
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
        }
        if(id == R.id.itemmenurecibo){
            Log.i("MENU","El segundo");
        }
        if(id == R.id.menuAgregarUsuario){
            usuarioNuevo();
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
                        fechanac.setText(dayOfMonth+"/"+(month+1)+"/"+year);
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
                        fechaingreso.setText(dayOfMonth+"/"+(month+1)+"/"+year);
                    }
                },2021,1,1);
                datePickerDialog2.show();
            }
        });

        vencCI.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog3 = new DatePickerDialog(activity, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        vencCI.setText(dayOfMonth+"/"+(month+1)+"/"+year);
                    }
                },2021,1,1);
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
                        vencLC.setText(dayOfMonth+"/"+(month+1)+"/"+year);
                    }
                },2021,1,1);
                datePickerDialog4.show();
            }
        });
        vencCS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog5 = new DatePickerDialog(activity, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        vencCS.setText(dayOfMonth+"/"+(month+1)+"/"+year);
                    }
                },1980,1,1);
                datePickerDialog5.show();
            }
        });

    }
    //Obtiene los datos de los cuadros y los modifica de un personal existente
    public Personal gurdarInfo(Personal personal){
        personal.setNombre(nombre.getText().toString());
        personal.setApellido(apellido.getText().toString());
        personal.setDomicilio(domicilio.getText().toString());
        personal.setNacimiento(fechanac.getText().toString());
        personal.setTelefono(telefono.getText().toString());
        personal.setIngreso(fechaingreso.getText().toString());
        personal.setSeccion(seccion.getText().toString());
        personal.setCategoria(categoria.getText().toString());
        personal.setCredencial(credencial.getText().toString());
        personal.setCid(vencCI.getText().toString(),numCI.getText().toString(),personal.getCid().imagen);//Falta  implementar la imagen
        personal.setLicienciaC(vencLC.getText().toString(),catLC.getText().toString(),personal.getLicenciaC().imagen);//Falta  implementar la imagen
        personal.setCsalud(vencCS.getText().toString(),personal.getCsalud().imagen);
        return personal;
    }
    //A partir de un objeto tipor Personal lo actualiza en la nube
    public void subirInfo(Personal personal){
        Map<String, Object> personalReload = new HashMap<>();
        personalReload.put("nombre",personal.getNombre());
        personalReload.put("apellido",personal.getApellido());
        personalReload.put("domicilio",personal.getDomicilio());
        personalReload.put("nacimiento",personal.getNacimiento());
        personalReload.put("telefono",personal.getTelefono());
        personalReload.put("categoria",personal.getCategoria());
        personalReload.put("credencial",personal.getCredencial());
        personalReload.put("picture",personal.getPicture());
        personalReload.put("seccion",personal.getSeccion());
        personalReload.put("cid/numero",personal.getCid().numero);
        personalReload.put("cid/venc/dia",personal.getCid().venc.dia);
        personalReload.put("cid/venc/mes",personal.getCid().venc.mes);
        personalReload.put("cid/venc/ano",personal.getCid().venc.ano);
        personalReload.put("cid/imagen",personal.getCid().imagen);
        personalReload.put("csalud/venc/dia",personal.getCsalud().venc.dia);
        personalReload.put("csalud/venc/mes",personal.getCsalud().venc.mes);
        personalReload.put("csalud/venc/ano",personal.getCsalud().venc.ano);
        personalReload.put("ingreso/dia",personal.getIngreso().dia);
        personalReload.put("ingreso/mes",personal.getIngreso().mes);
        personalReload.put("ingreso/ano",personal.getIngreso().ano);
        personalReload.put("csalud/imagen",personal.getCsalud().imagen);
        personalReload.put("licenciaC/venc/dia",personal.getLicenciaC().venc.dia);
        personalReload.put("licenciaC/venc/mes",personal.getLicenciaC().venc.mes);
        personalReload.put("licenciaC/venc/ano",personal.getLicenciaC().venc.ano);
        personalReload.put("licenciaC/imagen",personal.getLicenciaC().imagen);
        personalReload.put("licenciaC/cat",personal.getLicenciaC().cat);
        personal.getReference().updateChildren(personalReload);

    }
}
