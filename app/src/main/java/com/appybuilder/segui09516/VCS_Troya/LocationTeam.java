package com.appybuilder.segui09516.VCS_Troya;

import java.io.Serializable;

public class LocationTeam implements Serializable {

    private double latitud;
    private double longitud;

    public LocationTeam(double latitud, double longitud) {
        this.latitud = latitud;
        this.longitud = longitud;
    }

    public double getLatitud() {
        return latitud;
    }

    public double getLongitud() {
        return longitud;
    }
}
