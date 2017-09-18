package com.tddp2.grupo2.linkup.infrastructure.client.request;


import com.tddp2.grupo2.linkup.model.Rejection;

import java.io.Serializable;

public class RejectionRequest implements Serializable {

    private Rejection rejection;

    public RejectionRequest(Rejection rejection) {
        this.rejection = rejection;
    }

    public Rejection getRejection() {
        return rejection;
    }

    public void setRejection(Rejection rejection) {
        this.rejection = rejection;
    }

}
