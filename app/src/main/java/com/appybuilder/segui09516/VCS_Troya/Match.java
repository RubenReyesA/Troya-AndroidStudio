package com.appybuilder.segui09516.VCS_Troya;

import java.io.Serializable;

public class Match implements Serializable {

    private int n_Jornada;
    private String day;
    private String date;
    private Team localTeam;
    private Team visitanteTeam;
    private String result;

    public Match(int j, String day, String date, Team localTeam, Team visitanteTeam) {
        this.n_Jornada=j;
        this.day = day;
        this.date=date;
        this.localTeam = localTeam;
        this.visitanteTeam = visitanteTeam;
        this.result="X-X";
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getDay() {
        return day;
    }

    public String getDate() {
        return date;
    }

    public Team getLocalTeam() {
        return localTeam;
    }

    public Team getVisitanteTeam() {
        return visitanteTeam;
    }

    public String getResult() {
        return result;
    }

    public int getN_Jornada() {
        return n_Jornada;
    }
}
