package com.tddp2.grupo2.linkup.service.api;

import com.tddp2.grupo2.linkup.infrastructure.Database;

public class FacebookService implements LinkupService {

    protected Database database;

    protected FacebookService(Database database) {
        this.database = database;
    }

    public ServiceType getType() {
        return ServiceType.FACEBOOK;
    }
}
