package com.tddp2.grupo2.linkup.infrastructure.client.response;


import com.tddp2.grupo2.linkup.model.Profile;

import java.io.Serializable;
import java.util.List;

import static com.tddp2.grupo2.linkup.R.drawable.profile;

public class CandidatesResponse implements Serializable {

    private List<Profile> candidates;

    public CandidatesResponse(List<Profile> candidates) {
        this.candidates = candidates;
    }

    public List<Profile> getCandidates() {
        return candidates;
    }

    public void setCandidates(List<Profile> candidates) {
        this.candidates = candidates;
    }
}
