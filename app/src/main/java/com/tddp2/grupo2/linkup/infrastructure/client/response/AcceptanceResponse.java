package com.tddp2.grupo2.linkup.infrastructure.client.response;


public class AcceptanceResponse {

    private int remainingSuperlinks;
    private boolean match;

    public AcceptanceResponse() {
        this.remainingSuperlinks = 0;
    }

    public int getRemainingSuperlinks() {
        return remainingSuperlinks;
    }

    public void setRemainingSuperlinks(int remainingSuperlinks) {
        this.remainingSuperlinks = remainingSuperlinks;
    }

    public boolean getMatch() {
        return match;
    }

    public void setMatch(boolean match) {
        this.match = match;
    }
}
