package com.tddp2.grupo2.linkup.infrastructure.client.request;


import java.io.Serializable;

public class TokenRequest implements Serializable {

    private String fbid;
    private String token;

    public TokenRequest(String fbid, String token) {
        this.fbid = fbid;
        this.token = token;
    }

    public String getFbid() {
        return fbid;
    }

    public void setFbid(String fbid) {
        this.fbid = fbid;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

}
