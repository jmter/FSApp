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

import com.jmt.fsapp.Activities.User.ReciboDeSueldoViewer;
import com.jmt.fsapp.POJO.ReciboDeSueldo;
import com.jmt.fsapp.R;

import java.util.ArrayList;
// Adaptador para recycler view de  menu

public class RecibosDeSueldoAdapter extends RecyclerView.Adapter<RecibosDeSueldoAdapter.AdapterViewHolder> {
    private ArrayList<ReciboDeSueldo> recibos;
    private Activity activity;

    public RecibosDeSueldoAdapter(ArrayList<ReciboDeSueldo> recibos , Activity activity){
        this.recibos = recibos;
        this.activity = activity;

    }

    public void setRecibos(ArrayList<ReciboDeSueldo> recibos) {
        this.recibos = recibos;
    }

    @NonNull
    @Override
    public AdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_recibo_de_sueldo,parent,false);
        return new AdapterViewHolder (v);
    }

    @Override
    public void onBindViewHolder(@NonNull final AdapterViewHolder adapterViewHolder, int position) {
        final ReciboDeSueldo recibo = recibos.get(position);
        String textRecibo = recibo.getTipo() + " - " + recibo.getMes()+ " - " + recibo.getAno();
        adapterViewHolder.recibotext.setText(textRecibo);
        if(recibo.getFirma().equals("true")){
            adapterViewHolder.estadofirma.setImageResource(R.mipmap.firmadoico);
            adapterViewHolder.estadofirmatext.setText("Firmado");
        }else{
            adapterViewHolder.estadofirma.setImageResource(R.mipmap.sinfirmarico);
            adapterViewHolder.estadofirmatext.setText("Sin Firmar");
        };
        adapterViewHolder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(activity, ReciboDeSueldoViewer.class);
                i.putExtra("recibo", recibo);
                activity.startActivity(i);
                activity.finish();
            }
        });

    }
    @Override
    public int getItemCount() {
        return recibos.size();
    }

    public static class AdapterViewHolder extends RecyclerView.ViewHolder{

        private TextView recibotext;
        private ImageView estadofirma;
        private TextView estadofirmatext;
        private ConstraintLayout layout;


        public AdapterViewHolder(@NonNull View itemView) {
            super(itemView);
            recibotext  = (TextView) itemView.findViewById(R.id.recibosueldotipo);
            estadofirma = (ImageView) itemView.findViewById(R.id.signedicon);
            estadofirmatext = (TextView) itemView.findViewById(R.id.signedicontext);
            layout = (ConstraintLayout) itemView.findViewById(R.id.layoutCVRS);
        }
    }

}
