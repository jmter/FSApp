package com.jmt.fsapp.Activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
    private Button agregarpicture,agregarCI,agregarLC,agregarCS,guardar;
    private LinearLayout etUsuario,etContrasena,spinerUsuarios;
    private Spinner usuarios;
    private ArrayList<EditText> editTexts = new ArrayList<>();
    private ArrayList<Personal> personals = new ArrayList<>();
    private Personal personal;
    private ImageView ci,lc,cs,pict;
    private DatabaseReference mdataBase;
    private FirebaseUser user;
    private Activity activity = this;
    private int access;
    private ArrayList<String> users = new ArrayList<>();
    static final int ASK_QUESTION_REQUEST_EDIT = 0;
    static final int ASK_QUESTION_REQUEST_NEW = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_informacion_personal);
        mdataBase = FirebaseDatabase.getInstance().getReference().child("Usuarios");
        user = FirebaseAuth.getInstance().getCurrentUser();
        iniciarGraficos();
        loadDB();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // check if the request code is same as what is passed  here it is 1
        if (requestCode == ASK_QUESTION_REQUEST_EDIT) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                usuarios.setSelection(getIndexSpinner(usuarios,data.getStringExtra("user")));
                for(Personal ps: personals){
                    if(data.getStringExtra("user").equals(ps.getUsuario())){
                        switch (data.getStringExtra("name")){
                            case "PIC":
                                ps.setPicture(data.getStringExtra("URL"));
                                break;
                            case "LC":
                                ps.setLCimagen(data.getStringExtra("URL"));
                                break;
                            case "CI":
                                ps.setCidimagen(data.getStringExtra("URL"));
                                break;
                            case "CS":
                                ps.setCSimagen(data.getStringExtra("URL"));
                                break;
                        }
                        cargarInfo(ps);
                    }

                }



            }
        }
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
                        activity.recreate();
                    }
                }

            }
        });
        for(EditText et: editTexts){
            et.setFocusable(false);
            et.setEnabled(false);
        }

    }
    public boolean onPrepareOptionsMenu (Menu menu){
        ajustarParamAcces(menu);
        return true;
    }
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

        }
        if(id == R.id.menuAgregarUsuario){
            usuarioNuevo();
        }
        if(id == R.id.menuitemSalir){
            Intent intent = new Intent(activity,MainActivity.class);
            startActivity(intent);
            finish();

        }
        return super.onOptionsItemSelected(item);
    }
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
    //Metodo que carga el array personals con la info de los usuarios
    private void loadDB(){
        mdataBase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                personals.clear();
                access = 0;
                for (DataSnapshot ds : snapshot.getChildren()){
                    for (DataSnapshot sd : ds.getChildren()){
                        if (sd.child("usuario").getValue().equals(user.getEmail())) {
                            Log.i("Usuario","El usuario es"+user.getEmail()+"Su acces es "+Integer.parseInt(sd.child("acceso").getValue().toString()));
                            access = Integer.parseInt(sd.child("acceso").getValue().toString());
                        }
                    }
                }
                for(DataSnapshot ds : snapshot.getChildren()){
                    for(DataSnapshot sd : ds.getChildren()){
                    Log.i("Accses",sd.child("usuario").getValue().toString()+" == "+user.getEmail());
                    if((access == 3) || (sd.child("usuario").getValue().toString().equals(user.getEmail()))) {

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
                            personal.setCSimagen(sd.child("csalud").child("imagen").getValue().toString());
                            personal.setPicture(sd.child("picture").getValue().toString());
                            personals.add(personal);

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
                loadUsers(personals);
                for (Personal ps : personals) {
                    if (ps.getUsuario().equals(user.getEmail())) {
                        usuarios.setSelection(users.indexOf(user.getEmail()));
                        cargarInfo(ps);
                    }
                }
            }}

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
    //Metodo que carga el menu a partir de un objeto tipo Personal
    private void cargarInfo(Personal personal){
        nombre.setText(personal.getNombre());
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
                        break;
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
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
                                    mdataBase.child(String.valueOf(personal.getAcceso())).push().setValue(personal);
                                    activity.recreate();
                                } else {
                                    // If sign in fails, display a message to the user.
                                }
                            }
                        });
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
        if (access !=3 ){
            fechaingreso.setEnabled(false);
            fechaingreso.setTextIsSelectable(false);
            fechaingreso.setFocusable(false);
            fechaingreso.setFocusableInTouchMode(false);

            seccion.setEnabled(false);
            seccion.setTextIsSelectable(false);
            seccion.setFocusable(false);
            seccion.setFocusableInTouchMode(false);

            categoria.setEnabled(false);
            categoria.setTextIsSelectable(false);
            categoria.setFocusable(false);
            categoria.setFocusableInTouchMode(false);

        }
        agregarCI.setVisibility(View.VISIBLE);
        agregarpicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, CameraScanDoc.class);
                intent.putExtra("name","PIC");
                intent.putExtra("user",usuarios.getSelectedItem().toString());
                startActivityForResult(intent, ASK_QUESTION_REQUEST_EDIT);
            }
        });
        agregarLC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, CameraScanDoc.class);
                intent.putExtra("name","LC");
                intent.putExtra("user",usuarios.getSelectedItem().toString());
                startActivityForResult(intent, ASK_QUESTION_REQUEST_EDIT);
            }
        });
        agregarCI.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, CameraScanDoc.class);
                intent.putExtra("name","CI");
                intent.putExtra("user",usuarios.getSelectedItem().toString());
                startActivityForResult(intent, ASK_QUESTION_REQUEST_EDIT);
            }
        });
        agregarCS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, CameraScanDoc.class);
                intent.putExtra("name","CS");
                intent.putExtra("user",usuarios.getSelectedItem().toString());
                startActivityForResult(intent, ASK_QUESTION_REQUEST_EDIT);
            }
        });
        agregarCS.setVisibility(View.VISIBLE);
        agregarLC.setVisibility(View.VISIBLE);
        agregarpicture.setVisibility(View.VISIBLE);
        guardar.setVisibility(View.VISIBLE);
        fechanac.setTextIsSelectable(false);
        fechaingreso.setTextIsSelectable(false);
        vencCI.setTextIsSelectable(false);
        vencLC.setTextIsSelectable(false);
        vencCS.setTextIsSelectable(false);
        iniciarDatePicker();
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
    //private method of your class
    private int getIndexSpinner(Spinner spinner, String myString){
        for (int i=0;i<spinner.getCount();i++){
            if (spinner.getItemAtPosition(i).toString().equalsIgnoreCase(myString)){
                return i;
            }
        }

        return 0;
    }
    private void ajustarParamAcces(Menu menu){
        switch (access){
            case 0 :
            case 1:
            case 2:

            break;
            case 3:
                menu.findItem(R.id.menuAgregarUsuario).setVisible(true);
                spinerUsuarios.setVisibility(View.VISIBLE);
            break;

        }

    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(activity,MainActivity.class);
        startActivity(intent);
        finish();
    }
}
