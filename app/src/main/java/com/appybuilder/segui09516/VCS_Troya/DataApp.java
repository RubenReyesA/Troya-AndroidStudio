package com.appybuilder.segui09516.VCS_Troya;

import java.io.Serializable;
import java.util.ArrayList;

public class DataApp implements Serializable {

    private ArrayList<Team> teams;
    private ArrayList<Match> matches;
    private ArrayList<Player> players;

    public DataApp() {
        this.teams=new ArrayList<>();
        this.matches=new ArrayList<>();
        this.players=new ArrayList<>();
    }

    public void addTeam(Team t){
        teams.add(t);
    }
    public void addMatch(Match m){
        matches.add(m);
    }
    public void addPlayer(Player p){ players.add(p); }

    public ArrayList<Team> getTeams() {
        return teams;
    }

    public ArrayList<Match> getMatches() {
        return matches;
    }

    public ArrayList<Player> getPlayers() {
        return players;
    }

    public void clearTeams(){
        this.teams.clear();
    }

    public void clearMatches(){
        this.matches.clear();
    }

    public void clearPlayers(){
        this.players.clear();
    }

    public ArrayList<String> getTeamsNames(){
        ArrayList<String> teamsString = new ArrayList<>();

        for (Team t : teams){
            teamsString.add(t.getNameTeam());
        }

        return teamsString;
    }

    public ArrayList<String> getPlayersNames(){
        ArrayList<String> playersStrings = new ArrayList<>();

        for (Player p : players){
            playersStrings.add(p.getName_player());
        }

        return playersStrings;
    }
}
