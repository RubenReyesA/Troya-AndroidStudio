package com.appybuilder.segui09516.VCS_Troya;

import java.io.Serializable;

public class Team implements Serializable {

    private String NameTeam;
    private String FileNameTeam;
    private LocationTeam locationTeam;

    public Team(String nameTeam, String fileNameTeam,double latitud, double longitud) {
        NameTeam = nameTeam;
        FileNameTeam = fileNameTeam;
        locationTeam = new LocationTeam(latitud,longitud);
    }

    public String getNameTeam() {
        return NameTeam;
    }

    public String getFileNameTeam() {
        return FileNameTeam;
    }

    public double getLatitud(){
        return locationTeam.getLatitud();
    }

    public double getLongitud(){
        return locationTeam.getLongitud();
    }

}
