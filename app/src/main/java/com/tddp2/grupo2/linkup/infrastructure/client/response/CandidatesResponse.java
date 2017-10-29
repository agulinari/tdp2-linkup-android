package com.tddp2.grupo2.linkup.infrastructure.client.response;


import com.google.gson.JsonObject;

import java.io.Serializable;
import java.util.List;

public class CandidatesResponse implements Serializable {

    private List<JsonObject> candidates;
    private int availableSuperlinks;

    public CandidatesResponse() {
        this.availableSuperlinks = 0;
    }

    public List<JsonObject> getCandidates() {
        return candidates;
    }

    public void setCandidates(List<JsonObject> candidates) {
        this.candidates = candidates;
    }

    public int getAvailableSuperlinks() {
        return availableSuperlinks;
    }

    public void setAvailableSuperlinks(int availableSuperlinks) {
        this.availableSuperlinks = availableSuperlinks;
    }
}
