package com.tddp2.grupo2.linkup.infrastructure.client.response;


import com.tddp2.grupo2.linkup.model.Acceptance;

public class AcceptanceResponse {

    private Acceptance acceptance;

    public AcceptanceResponse(Acceptance acceptance) {
        this.acceptance = acceptance;
    }

    public Acceptance getAcceptance() {
        return acceptance;
    }

    public void setAcceptance(Acceptance acceptance) {
        this.acceptance = acceptance;
    }

}
