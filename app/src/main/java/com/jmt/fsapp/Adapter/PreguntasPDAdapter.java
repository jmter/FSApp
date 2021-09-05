package com.jmt.fsapp.Adapter;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.jmt.fsapp.POJO.Menus;
import com.jmt.fsapp.R;
import com.shitij.goyal.slidebutton.SwipeButton;

import java.util.ArrayList;
// Adaptador para recycler view de  menu

public class PreguntasPDAdapter extends RecyclerView.Adapter<PreguntasPDAdapter.AdapterViewHolder> {
    private ArrayList<String> preguntas;
    private Activity activity;

    public PreguntasPDAdapter(ArrayList<String> preguntas , Activity activity){
        this.preguntas = preguntas;
        this.activity = activity;

    }

    public void setPreguntas(ArrayList<String> preguntas) {
        this.preguntas = preguntas;
    }

    @NonNull
    @Override
    public AdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_pregunta_pd,parent,false);
        return new AdapterViewHolder (v);
    }

    @Override
    public void onBindViewHolder(@NonNull final AdapterViewHolder adapterViewHolder, int position) {
        final String pregunta = preguntas.get(position);
        adapterViewHolder.pregunta.setText(pregunta);

    }
    @Override
    public int getItemCount() {
        return preguntas.size();
    }

    public static class AdapterViewHolder extends RecyclerView.ViewHolder{

        private TextView pregunta;


        public AdapterViewHolder(@NonNull View itemView) {
            super(itemView);
            pregunta  = (TextView) itemView.findViewById(R.id.textpregunta);
        }
    }

}
