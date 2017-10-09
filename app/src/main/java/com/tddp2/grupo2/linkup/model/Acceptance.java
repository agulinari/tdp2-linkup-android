package com.tddp2.grupo2.linkup.model;


import java.io.Serializable;

public class Acceptance implements Serializable {

    private String fbidUser;
    private String fbidCandidate;
    private String typeOfLink;

    public Acceptance(String fbidUser, String fbidCandidate, String tipoDeLink) {
        this.fbidUser = fbidUser;
        this.fbidCandidate = fbidCandidate;
        this.typeOfLink = tipoDeLink;
    }

    public String getFbidUser() {
        return fbidUser;
    }

    public void setFbidUser(String fbidUser) {
        this.fbidUser = fbidUser;
    }

    public String getFbidCandidate() {
        return fbidCandidate;
    }

    public void setFbidCandidate(String fbidCandidate) {
        this.fbidCandidate = fbidCandidate;
    }

    public String getTypeOfLink() {
        return typeOfLink;
    }

    public void setTypeOfLink(String tipoDeLink) {
        this.typeOfLink = tipoDeLink;
    }
}
