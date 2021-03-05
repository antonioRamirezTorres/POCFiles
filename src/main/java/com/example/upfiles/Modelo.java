package com.example.upfiles;

import com.opencsv.bean.CsvBindByPosition;

public class Modelo {

    @CsvBindByPosition(position = 0)
    private String campo1;

    @CsvBindByPosition(position = 1)
    private String campo2;

    public String getCampo1() {
        return campo1;
    }

    public void setCampo1(String campo1) {
        this.campo1 = campo1;
    }

    public String getCampo2() {
        return campo2;
    }

    public void setCampo2(String campo2) {
        this.campo2 = campo2;
    }

    @Override
    public String toString() {
        return "Modelo{" +
                "campo1='" + campo1 + '\'' +
                ", campo2='" + campo2 + '\'' +
                '}';
    }
}
