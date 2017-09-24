package com.tddp2.grupo2.linkup.infrastructure.client.response;


public class AcceptanceResponse {

    private int remainingSuperlinks;
    private boolean Match;

    public AcceptanceResponse() {}

    public int getRemainingSuperlinks() {
        return remainingSuperlinks;
    }

    public void setRemainingSuperlinks(int remainingSuperlinks) {
        this.remainingSuperlinks = remainingSuperlinks;
    }

    public boolean getMatch() {
        return Match;
    }

    public void setMatch(boolean Match) {
        this.Match = Match;
    }
}
