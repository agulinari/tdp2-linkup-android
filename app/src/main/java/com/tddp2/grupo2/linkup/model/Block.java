package com.tddp2.grupo2.linkup.model;

import java.io.Serializable;

public class Block implements Serializable{
    private String idBlockerUser;
    private String idBlockedUser;

    public String getIdBlockerUser() {
        return idBlockerUser;
    }

    public void setIdBlockerUser(String idBlockerUser) {
        this.idBlockerUser = idBlockerUser;
    }

    public String getIdBlockedUser() {
        return idBlockedUser;
    }

    public void setIdBlockedUser(String idBlockedUser) {
        this.idBlockedUser = idBlockedUser;
    }
}
