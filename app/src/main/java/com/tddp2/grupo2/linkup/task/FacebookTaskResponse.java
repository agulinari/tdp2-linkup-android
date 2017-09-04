package com.tddp2.grupo2.linkup.task;

import com.tddp2.grupo2.linkup.model.Profile;

public class FacebookTaskResponse extends TaskResponse {
    private Profile profile;

    public void setProfileResponse(Profile profile) {
        this.profile = profile;
    }

    public Profile getProfile() {
        return this.profile;
    }
}
