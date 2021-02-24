package com.jmt.fsapp.datatype;

import androidx.annotation.NonNull;

public class Fecha {
    public int dia, mes, ano;

    public Fecha(int dia, int mes, int ano) {
        this.dia = dia;
        this.mes = mes;
        this.ano = ano;
    }

    @NonNull
    @Override
    public String toString() {

        return dia+"/"+mes+"/"+ano;
    }
}
