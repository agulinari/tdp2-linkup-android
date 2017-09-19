package com.tddp2.grupo2.linkup.infrastructure.client.request;


import com.tddp2.grupo2.linkup.model.Acceptance;

import java.io.Serializable;

public class AcceptanceRequest implements Serializable {

    private Acceptance acceptance;

    public AcceptanceRequest(Acceptance acceptance) {
        this.acceptance = acceptance;
    }

    public Acceptance getAcceptance() {
        return acceptance;
    }

    public void setAcceptance(Acceptance acceptance) {
        this.acceptance = acceptance;
    }

}
