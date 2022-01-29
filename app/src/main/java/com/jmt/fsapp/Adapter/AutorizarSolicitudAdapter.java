package com.jmt.fsapp.Adapter;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.jmt.fsapp.POJO.Estacion;
import com.jmt.fsapp.POJO.OrdenCompraEstacionServicio;
import com.jmt.fsapp.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import static android.app.Activity.RESULT_OK;
// Adaptador para recycler view de  menu

public class AutorizarSolicitudAdapter extends RecyclerView.Adapter<AutorizarSolicitudAdapter.AdapterViewHolder> {
    private ArrayList<OrdenCompraEstacionServicio> solicitudes;
    private Activity activity;
    private DatabaseReference mdataBase;
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();

    public AutorizarSolicitudAdapter(ArrayList<OrdenCompraEstacionServicio> solicitudes , Activity activity){
        this.solicitudes = solicitudes;
        this.activity = activity;

    }

    public void setSolicitudes(ArrayList<OrdenCompraEstacionServicio> solicitudes) {
        this.solicitudes = solicitudes;
    }

    @NonNull
    @Override
    public AdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_autorizar_solicitud,parent,false);

        return new AdapterViewHolder (v);
    }

    @Override
    public void onBindViewHolder(@NonNull final AdapterViewHolder adapterViewHolder, int position) {
        final OrdenCompraEstacionServicio solicitud = solicitudes.get(position);

        adapterViewHolder.usuarioTV.setText(solicitud.getOperario());
        String equipoEtiqueta = "("+solicitud.getEquipo().getId()+") "+solicitud.getEquipo().getMarca()+" "+ solicitud.getEquipo().getModelo();
        adapterViewHolder.equipoTV.setText(equipoEtiqueta);
        adapterViewHolder.productoTV.setText(solicitud.getProducto());
        String etiquetaTitulo = solicitud.getEstacion().getName() + " " +solicitud.getEstacion().getCity();
        adapterViewHolder.tituloTV.setText(etiquetaTitulo);
        adapterViewHolder.cantTI.setText("0");

        //region Funcionalideades cantidades sugeridas, aumentar, disminuir
        adapterViewHolder.cant1BT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                adapterViewHolder.cantTI.setText("10");
                }
            });
            adapterViewHolder.cant2BT.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    adapterViewHolder.cantTI.setText("50");
                }
            });
            adapterViewHolder.cant3BT.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    adapterViewHolder.cantTI.setText("100");
                }
            });
            adapterViewHolder.lessIV.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String Q = adapterViewHolder.cantTI.getText().toString();
                    if(!Q.equals("0")){
                        Q = String.valueOf(Integer.valueOf(Q) - 1);
                        adapterViewHolder.cantTI.setText(Q);
                    }

                }
            });
            adapterViewHolder.moreIV.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String Q = adapterViewHolder.cantTI.getText().toString();
                    Q = String.valueOf(Integer.valueOf(Q) + 1);
                    adapterViewHolder.cantTI.setText(Q);
                }
            });
        //endregion

        adapterViewHolder.autorizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                solicitud.setResponsable(mAuth.getCurrentUser().getEmail());
                Date currentTime = Calendar.getInstance().getTime();
                SimpleDateFormat sdf = new SimpleDateFormat("HH:mm dd/MM/yyyy");
                solicitud.setMaximoAutorizado(adapterViewHolder.cantTI.getText().toString());
                solicitud.setFechayhoraautorizacion(sdf.format(currentTime));
                solicitud.setEstado("Autorizado");
                String user = solicitud.getOperario().replace(".", "");
                mdataBase = FirebaseDatabase.getInstance().getReference().child("Ordenes Estaciones de Servicio").child("Autorizado").child(user);
                mdataBase = mdataBase.push();
                mdataBase.setValue(solicitud);
                mdataBase = FirebaseDatabase.getInstance().getReference().child("Ordenes Estaciones de Servicio").child("Pendiente autorizacion").child(user).child(solicitud.getKey());
                mdataBase.removeValue();
            }
        });

        adapterViewHolder.closeIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                builder.setTitle("¡Atencion!");
                builder.setMessage("¿Quieres eliminar la solicitud?");
                builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String user = mAuth.getCurrentUser().getEmail().replace(".", "");
                        mdataBase = FirebaseDatabase.getInstance().getReference().child("Ordenes Estaciones de Servicio").child(solicitud.getEstado()).child(user).child(solicitud.getKey());
                        mdataBase.removeValue();
                        activity.recreate();
                    }
                });
                builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();

            }
        });


    }
    @Override
    public int getItemCount() {
        return solicitudes.size();
    }

    public static class AdapterViewHolder extends RecyclerView.ViewHolder{

        private TextView usuarioTV,equipoTV,productoTV,tituloTV;
        private Button cant1BT,cant2BT,cant3BT,autorizar;
        private ImageView lessIV,moreIV,closeIV;
        private TextInputEditText cantTI;


        public AdapterViewHolder(@NonNull View itemView) {
            super(itemView);
            tituloTV  = (TextView) itemView.findViewById(R.id.tituloTV);
            usuarioTV  = (TextView) itemView.findViewById(R.id.usuarioTV);
            equipoTV  = (TextView) itemView.findViewById(R.id.equipoTV);
            productoTV = (TextView) itemView.findViewById(R.id.productoTV);
            cant1BT = (Button) itemView.findViewById(R.id.cant1BT);
            cant2BT = (Button) itemView.findViewById(R.id.cant2BT);
            cant3BT = (Button) itemView.findViewById(R.id.cant3BT);
            autorizar = (Button) itemView.findViewById(R.id.autorizarBT);
            lessIV = (ImageView) itemView.findViewById(R.id.lessIV);
            moreIV = (ImageView) itemView.findViewById(R.id.moreIV);
            cantTI = (TextInputEditText) itemView.findViewById(R.id.cantTI);
            closeIV = (ImageView) itemView.findViewById(R.id.closeIV);
        }
    }
}
