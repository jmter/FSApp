package com.jmt.fsapp.Adapter;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.jmt.fsapp.Activities.MainActivity;
import com.jmt.fsapp.POJO.Menus;
import com.jmt.fsapp.R;

import java.util.ArrayList;
// Adaptador para recycler view de  menu

public class MenusAdapter extends RecyclerView.Adapter<MenusAdapter.AdapterViewHolder> {
    private ArrayList<Menus> menus;
    private Activity activity;

    public MenusAdapter(ArrayList<Menus> menus ,Activity activity){
        this.menus = menus;
        this.activity = activity;

    }

    public void setMenus(ArrayList<Menus> menus) {
        this.menus = menus;
    }

    @NonNull
    @Override
    public AdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_menu,parent,false);
        return new AdapterViewHolder (v);
    }

    @Override
    public void onBindViewHolder(@NonNull final AdapterViewHolder adapterViewHolder, int position) {
        Menus menu = menus.get(position);
        Glide.with(activity).load(menu.getImagen()).into(adapterViewHolder.imagen);
        adapterViewHolder.nombre.setText(menu.getNombre());
        adapterViewHolder.layoutCV.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View v) {
               //Aca va lo que hace cuando clickeas el menu
              }
          });

    }
    @Override
    public int getItemCount() {
        return menus.size();
    }

    public static class AdapterViewHolder extends RecyclerView.ViewHolder{

        private ImageView imagen;
        private TextView nombre;
        private ConstraintLayout layoutCV;


        public AdapterViewHolder(@NonNull View itemView) {
            super(itemView);
            imagen  = (ImageView) itemView.findViewById(R.id.imageMenu);
            nombre  = (TextView) itemView.findViewById(R.id.nombreMenu);
            layoutCV  = (ConstraintLayout) itemView.findViewById(R.id.layoutCV);

        }
    }
}
