package com.tddp2.grupo2.linkup.infrastructure.client.response;


import com.tddp2.grupo2.linkup.model.Profile;

import java.io.Serializable;

public class UserResponse implements Serializable {

    private Profile user;

    public Profile getUser() {
        return user;
    }

    public void setUser(Profile user) {
        this.user = user;
    }
}
