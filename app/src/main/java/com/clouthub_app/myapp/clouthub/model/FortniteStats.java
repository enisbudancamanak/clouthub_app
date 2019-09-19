package com.clouthub_app.myapp.clouthub.model;

public class FortniteStats {



    private String kills;
    private String wins;
    private String kd;


    public FortniteStats(){

    }

    public FortniteStats(String kills, String wins, String kd) {
        this.kills = kills;
        this.wins = wins;
        this.kd = kd;
    }



    public void setKills(String kills) {
        this.kills = kills;
    }

    public void setWins(String wins) {
        this.wins = wins;
    }

    public void setKd(String kd) {
        this.kd = kd;
    }

    public String getKills() {
        return kills;
    }

    public String getWins() {
        return wins;
    }

    public String getKd() {
        return kd;
    }

}
