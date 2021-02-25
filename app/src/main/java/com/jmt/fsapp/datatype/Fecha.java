package com.jmt.fsapp.datatype;

import androidx.annotation.NonNull;

public class Fecha {
    public int dia, mes, ano;

    public Fecha() {
        this.dia = 1;
        this.mes = 1;
        this.ano = 2021;
    }

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
