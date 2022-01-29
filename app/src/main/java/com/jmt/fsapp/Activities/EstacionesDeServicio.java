package com.jmt.fsapp.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.os.Bundle;

import com.jmt.fsapp.Constructors.ConstructorEstaciones;
import com.jmt.fsapp.R;
public class EstacionesDeServicio extends AppCompatActivity {
    private RecyclerView listadestacionRV;
    private Activity activity=this;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_estaciones_de_servicio);
        listadestacionRV = findViewById(R.id.listadoestacionesRV);
        GridLayoutManager gridLayoutManagerm = new GridLayoutManager(this,2);
        listadestacionRV.setLayoutManager(gridLayoutManagerm);
        new ConstructorEstaciones(listadestacionRV,activity).loadEstaciones();
    }
}
