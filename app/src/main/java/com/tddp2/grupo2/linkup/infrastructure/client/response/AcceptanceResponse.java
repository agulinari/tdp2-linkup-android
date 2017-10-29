package com.tddp2.grupo2.linkup.infrastructure.client.response;


public class AcceptanceResponse {

    private int availableSuperlinks;
    private boolean match;

    public AcceptanceResponse() {
        this.availableSuperlinks = 0;
    }

    public int getAvailableSuperlinks() {
        return availableSuperlinks;
    }

    public void setAvailableSuperlinks(int availableSuperlinks) {
        this.availableSuperlinks = availableSuperlinks;
    }

    public boolean getMatch() {
        return match;
    }

    public void setMatch(boolean match) {
        this.match = match;
    }
}
