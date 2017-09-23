package com.tddp2.grupo2.linkup.infrastructure.client.request;


import com.tddp2.grupo2.linkup.model.Profile;

import java.io.Serializable;

public class PostUserRequest implements Serializable {

    private Profile user;

    public PostUserRequest(Profile profile) {
        this.user = profile;
    }

    public Profile getUser() {
        return user;
    }

    public void setUser(Profile user) {
        this.user = user;
    }
}
