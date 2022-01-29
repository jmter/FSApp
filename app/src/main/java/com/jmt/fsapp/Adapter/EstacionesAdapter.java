package com.jmt.fsapp.Adapter;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.jmt.fsapp.POJO.Equipo;
import com.jmt.fsapp.POJO.Estacion;
import com.jmt.fsapp.R;

import java.util.ArrayList;

import static android.app.Activity.RESULT_OK;
// Adaptador para recycler view de  menu

public class EstacionesAdapter extends RecyclerView.Adapter<EstacionesAdapter.AdapterViewHolder> {
    private ArrayList<Estacion> estaciones;
    private Activity activity;

    public EstacionesAdapter(ArrayList<Estacion> estaciones , Activity activity){
        this.estaciones = estaciones;
        this.activity = activity;

    }

    public void setEstaciones(ArrayList<Estacion> estaciones) {
        this.estaciones = estaciones;
    }

    @NonNull
    @Override
    public AdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_estacion,parent,false);
        return new AdapterViewHolder (v);
    }

    @Override
    public void onBindViewHolder(@NonNull final AdapterViewHolder adapterViewHolder, int position) {
        final Estacion estacion = estaciones.get(position);
        Glide.with(activity).load(estacion.getPicture()).into(adapterViewHolder.imagen);
        adapterViewHolder.marcamodelo.setText(estacion.getName());
        adapterViewHolder.identity.setText(estacion.getCity());
        adapterViewHolder.layoutCV.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View v) {
                  Intent intent = new Intent();
                  intent.putExtra("id",estacion.getId());
                  intent.putExtra("name",estacion.getName());
                  intent.putExtra("city",estacion.getCity());
                  intent.putExtra("mail",estacion.getMail());
                  intent.putExtra("picture",estacion.getPicture());
                  intent.putExtra("location",estacion.getLocation());
                  intent.putExtra("phone",estacion.getPhone());
                  intent.putExtra("adress",estacion.getAdress());
                  activity.setResult(RESULT_OK,intent);
                  activity.finish();
              }
          });

    }
    @Override
    public int getItemCount() {
        return estaciones.size();
    }

    public static class AdapterViewHolder extends RecyclerView.ViewHolder{

        private ImageView imagen;
        private TextView marcamodelo;
        private TextView identity;
        private ConstraintLayout layoutCV;


        public AdapterViewHolder(@NonNull View itemView) {
            super(itemView);
            imagen  = (ImageView) itemView.findViewById(R.id.imageEquipo);
            marcamodelo  = (TextView) itemView.findViewById(R.id.marcamodelo);
            identity = (TextView) itemView.findViewById(R.id.identity);
            layoutCV  = (ConstraintLayout) itemView.findViewById(R.id.layoutCV);

        }
    }
}
