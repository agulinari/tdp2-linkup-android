package com.tddp2.grupo2.linkup.infrastructure.client.response;


import com.google.gson.JsonObject;

import java.io.Serializable;
import java.util.List;

public class CandidatesResponse implements Serializable {

    private List<JsonObject> candidates;

    public CandidatesResponse(List<JsonObject> candidates) {
        this.candidates = candidates;
    }

    public List<JsonObject> getCandidates() {
        return candidates;
    }

    public void setCandidates(List<JsonObject> candidates) {
        this.candidates = candidates;
    }
}
