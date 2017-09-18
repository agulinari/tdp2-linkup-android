package com.tddp2.grupo2.linkup.infrastructure.client.response;

import com.tddp2.grupo2.linkup.model.Rejection;


public class RejectionResponse {

    private Rejection rejection;

    public RejectionResponse(Rejection rejection) {
        this.rejection = rejection;
    }

    public Rejection getRejection() {
        return rejection;
    }

    public void setRejection(Rejection rejection) {
        this.rejection = rejection;
    }

}
