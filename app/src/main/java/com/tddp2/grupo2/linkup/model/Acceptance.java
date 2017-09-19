package com.tddp2.grupo2.linkup.model;


import java.io.Serializable;

public class Acceptance implements Serializable {

    private String fbidUser;
    private String fbidCandidate;

    public Acceptance(String fbidUser, String fbidCandidate) {
        this.fbidUser = fbidUser;
        this.fbidCandidate = fbidCandidate;
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

}
