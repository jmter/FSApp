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
import com.jmt.fsapp.POJO.Menus;
import com.jmt.fsapp.R;

import java.util.ArrayList;
// Adaptador para recycler view de  menu

public class EquiposAdapter extends RecyclerView.Adapter<EquiposAdapter.AdapterViewHolder> {
    private ArrayList<Equipo> equipos;
    private Activity activity;

    public EquiposAdapter(ArrayList<Equipo> equipos , Activity activity){
        this.equipos = equipos;
        this.activity = activity;

    }

    public void setMenus(ArrayList<Equipo> equipos) {
        this.equipos = equipos;
    }

    @NonNull
    @Override
    public AdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_equipos,parent,false);
        return new AdapterViewHolder (v);
    }

    @Override
    public void onBindViewHolder(@NonNull final AdapterViewHolder adapterViewHolder, int position) {
        final Equipo equipo = equipos.get(position);
        Glide.with(activity).load(equipo.getFoto()).into(adapterViewHolder.imagen);
        adapterViewHolder.marcamodelo.setText(equipo.getMarca()+equipo.getModelo());
        adapterViewHolder.identity.setText(equipo.getId());
//        adapterViewHolder.layoutCV.setOnClickListener(new View.OnClickListener() {
//              @Override
//              public void onClick(View v) {
//                  Intent intent = null;
//                  try {
//                      intent = new Intent(activity, Class.forName(menu.getIntent()));
//
//                  } catch (ClassNotFoundException e) {
//                  }
//                  if (intent != null){
//                      activity.finish();
//                      activity.startActivity(intent);
//                  }
//              }
//          });

    }
    @Override
    public int getItemCount() {
        return equipos.size();
    }

    public static class AdapterViewHolder extends RecyclerView.ViewHolder{

        private ImageView imagen;
        private TextView marcamodelo;
        private TextView identity;
        private ConstraintLayout layoutCV;


        public AdapterViewHolder(@NonNull View itemView) {
            super(itemView);
            imagen  = (ImageView) itemView.findViewById(R.id.imageMenu);
            marcamodelo  = (TextView) itemView.findViewById(R.id.marcamodelo);
            identity = (TextView) itemView.findViewById(R.id.identity);
            layoutCV  = (ConstraintLayout) itemView.findViewById(R.id.layoutCV);

        }
    }
}
