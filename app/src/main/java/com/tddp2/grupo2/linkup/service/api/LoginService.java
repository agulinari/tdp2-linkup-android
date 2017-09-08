package com.tddp2.grupo2.linkup.service.api;

import com.tddp2.grupo2.linkup.infrastructure.Database;

public abstract class LoginService implements LinkupService {

    protected Database database;

    protected LoginService(Database database) {
        this.database = database;
    }

    public abstract void loadDataFromFacebook();

    public ServiceType getType() {
        return ServiceType.LOGIN;
    }
}
