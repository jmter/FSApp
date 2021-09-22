package com.jmt.fsapp.Activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.jmt.fsapp.POJO.Equipo;
import com.jmt.fsapp.R;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

public class AgregarEquipo extends AppCompatActivity {
    private Activity activity = this;
    private EditText identity,marca,modelo;
    private Spinner categoria,subcategoria;
    private ImageView imageView;
    private Button guardar;
    private MenuItem item;
    private DatabaseReference databaseReference;
    private Equipo equipo = new Equipo();
    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageRef = storage.getReference();
    final String TAG = "AgregarEquipo";
    private Uri path ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Equipos");
        setContentView(R.layout.activity_agregar_equipo);
        iniciarGraficos();
        readData(new firebaseCallback() {
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
                                        equipo.setCategoria(categoria.getSelectedItem().toString());
                                        equipo.setSubcategoria(subcategoria.getSelectedItem().toString());
                                        equipo.setPreid(obtenerProxID(equipos,categoria.getSelectedItem().toString(),subcategoria.getSelectedItem().toString()));
                                        identity.setText(equipo.getId());
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
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menuoverflow_maquinas,menu);
        menu.findItem(R.id.menuEditarMaquina).setVisible(false);
        return true;
    }
    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();
        if(id == R.id.menuEditarMaquina){

        }
        if(id == R.id.menuSalir){
            Intent intent = new Intent(activity,Equipos.class);
            startActivity(intent);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
    private void iniciarGraficos(){
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        imageView = findViewById(R.id.fotoEquipo);
        identity = findViewById(R.id.etId);
        marca = findViewById(R.id.etMarca);
        modelo = findViewById(R.id.etModelo);
        categoria = findViewById(R.id.etCategoria);
        subcategoria = findViewById(R.id.etSubcategoria);
        guardar = findViewById(R.id.guardarEquipo);
        guardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                guardar();
            }
        });
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent (Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                intent.setType("image/");
                startActivityForResult(intent.createChooser(intent,"Seleccione la aplicacion"),10);
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode == RESULT_OK){
            path = data.getData();
            imageView.setImageURI(path);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
    private void guardar(){

        equipo.setMarca(this.marca.getText().toString());
        equipo.setModelo(this.modelo.getText().toString());
        equipo.setCategoria(this.categoria.getSelectedItem().toString());
        equipo.setSubcategoria(this.subcategoria.getSelectedItem().toString());
        imageView.setDrawingCacheEnabled(true);
        imageView.buildDrawingCache();
        Bitmap bitmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 10, baos);
        byte[] data = baos.toByteArray();
        storageRef = storageRef.child("Equipos/" + equipo.getId() + "/" + "Picture");
        UploadTask uploadTask = storageRef.putBytes(data);
        final Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                if (!task.isSuccessful()) {
                    throw task.getException();
                }

                // Continue with the task to get the download URL
                return storageRef.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful()) {
                    Uri downloadUri = task.getResult();
                    equipo.setFoto(downloadUri.toString());
                    databaseReference.child(equipo.getCategoria()).child(equipo.getSubcategoria()).child(equipo.getId()).setValue(equipo);
                    Toast.makeText(activity,"El equipo fue agregado correctamente",Toast.LENGTH_LONG).show();
                } else {
                    // Handle failures
                    // ...
                    Toast.makeText(activity,"No fue posible agregar el equipo",Toast.LENGTH_LONG).show();
                }
            }
        });
        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
        finish();

    }
    private void readData(final firebaseCallback firebaseCallback){
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                final ArrayList<ArrayList<String>> subcategories = new ArrayList<>();
                ArrayList<Equipo> equipos = new ArrayList<>();
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
    private interface firebaseCallback{
        void onCallback(ArrayList<Equipo> equipos, ArrayList<ArrayList<String>> subcategories);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(activity,Equipos.class);
        startActivity(intent);
        finish();
    }

    private int obtenerProxID(ArrayList<Equipo> equipos, String categorie, String subcategorie){
        int max = 0;
        for(Equipo equipo:equipos){
            if(equipo.getCategoria().equals(categorie)  & equipo.getSubcategoria().equals(subcategorie) & equipo.getPreid() > max){
                max = equipo.getPreid();
            }
        }
        max = max + 1;
        return max;
    }

}
