package com.appybuilder.segui09516.VCS_Troya;

import java.io.Serializable;

public class Cuentas implements Serializable {

    private String concepto;
    private String precio;

    public Cuentas(){

    }

    public Cuentas(String concepto, String precio) {
        this.concepto = concepto;
        this.precio = precio;
    }

    public String getConcepto() {
        return concepto;
    }

    public void setConcepto(String concepto) {
        this.concepto = concepto;
    }

    public String getPrecio() {
        return precio;
    }

    public void setPrecio(String precio) {
        this.precio = precio;
    }
}
