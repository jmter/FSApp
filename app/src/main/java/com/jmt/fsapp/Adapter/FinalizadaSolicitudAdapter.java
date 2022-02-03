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
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.jmt.fsapp.Activities.Combustible.SolicitudEstacion;
import com.jmt.fsapp.POJO.OrdenCompraEstacionServicio;
import com.jmt.fsapp.R;

import java.util.ArrayList;
// Adaptador para recycler view de  menu

public class FinalizadaSolicitudAdapter extends RecyclerView.Adapter<FinalizadaSolicitudAdapter.AdapterViewHolder> {
    private ArrayList<OrdenCompraEstacionServicio> solicitudes;
    private Activity activity;
    private DatabaseReference mdataBase;
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();

    public FinalizadaSolicitudAdapter(ArrayList<OrdenCompraEstacionServicio> solicitudes , Activity activity){
        this.solicitudes = solicitudes;
        this.activity = activity;

    }

    public void setSolicitudes(ArrayList<OrdenCompraEstacionServicio> solicitudes) {
        this.solicitudes = solicitudes;
    }

    @NonNull
    @Override
    public AdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_solicitud_pendiente,parent,false);

        return new AdapterViewHolder (v);
    }

    @Override
    public void onBindViewHolder(@NonNull final AdapterViewHolder adapterViewHolder, int position) {
        final OrdenCompraEstacionServicio solicitud = solicitudes.get(position);

        adapterViewHolder.closeIV.setVisibility(View.GONE);
        String equipoEtiqueta = "("+solicitud.getEquipo().getId()+") "+solicitud.getEquipo().getMarca()+" "+ solicitud.getEquipo().getModelo();
        adapterViewHolder.equipoTV.setText(equipoEtiqueta);
        adapterViewHolder.productoTV.setText(solicitud.getProducto());
        String etiquetaTitulo = solicitud.getEquipo().getMarca()+" "+ solicitud.getEquipo().getModelo() + " - " +solicitud.getEstado();
        adapterViewHolder.tituloTV.setText(etiquetaTitulo);
        adapterViewHolder.fechayhora.setText((solicitud.getFechayhorasolicitud()));
        String etiequetaEstacion = solicitud.getEstacion().getName()+ "," + solicitud.getEstacion().getCity();
        adapterViewHolder.estacionTV.setText((etiequetaEstacion));
        adapterViewHolder.estadoIV.setImageResource(R.drawable.accepted_big_icon);
        adapterViewHolder.cardviewPendienteSolicitud.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                            Intent intent = new Intent(activity, SolicitudEstacion.class);
                            intent.putExtra("key",solicitud.getKey());
                            intent.putExtra("estado","CompletadasTrans");
                            activity.startActivity(intent);
                            activity.finish();
                }
        });


    }
    @Override
    public int getItemCount() {
        return solicitudes.size();
    }

    public static class AdapterViewHolder extends RecyclerView.ViewHolder{

        private TextView estacionTV,equipoTV,productoTV,tituloTV,fechayhora;
        private ImageView estadoIV,closeIV;
        private ConstraintLayout cardviewPendienteSolicitud;


        public AdapterViewHolder(@NonNull View itemView) {
            super(itemView);
            cardviewPendienteSolicitud = (ConstraintLayout) itemView.findViewById(R.id.cardviewPendienteSolicitud);
            tituloTV  = (TextView) itemView.findViewById(R.id.tituloTV);
            fechayhora  = (TextView) itemView.findViewById(R.id.fechayhoraTV);
            equipoTV  = (TextView) itemView.findViewById(R.id.equipoTV);
            productoTV = (TextView) itemView.findViewById(R.id.productoTV);
            estacionTV = (TextView) itemView.findViewById(R.id.estacionTV);
            estadoIV = (ImageView) itemView.findViewById(R.id.estadoIV);
            closeIV = (ImageView) itemView.findViewById(R.id.closeIV);
        }
    }
}
