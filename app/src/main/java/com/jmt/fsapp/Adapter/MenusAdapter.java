package com.jmt.fsapp.Adapter;

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

import com.jmt.fsapp.POJO.Menus;
import com.jmt.fsapp.R;

import java.util.ArrayList;
// Adaptador para recycler view de  menu

public class MenusAdapter extends RecyclerView.Adapter<MenusAdapter.AdapterViewHolder> {
    private ArrayList<Menus> menus;

    public MenusAdapter(ArrayList<Menus> menus){
        this.menus = menus;

    }
//    public Adapter(ArrayList<Menu> menus, Activity activity){
//        this.activity = activity;
//        this.menus = menus;
//    }

    @NonNull
    @Override
    public AdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_menu,parent,false);
        return new AdapterViewHolder (v);
    }

    @Override
    public void onBindViewHolder(@NonNull final AdapterViewHolder adapterViewHolder, int position) {
        Menus menu = menus.get(position);
//        adapterViewHolder.imagen.setImageResource(masc.getFoto()); Queda reservaod para cuando se implemente cargar imagen nueva
        adapterViewHolder.nombre.setText(menu.getNombre());
        Log.i("QueProblema",menu.getNombre());
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
        private LinearLayout layoutCV;


        public AdapterViewHolder(@NonNull View itemView) {
            super(itemView);
            imagen  = (ImageView) itemView.findViewById(R.id.imageMenu);
            nombre  = (TextView) itemView.findViewById(R.id.nombreMenu);
            layoutCV  = (LinearLayout) itemView.findViewById(R.id.layoutCV);

        }
    }
}
