package com.tddp2.grupo2.linkup.service.api;

import com.tddp2.grupo2.linkup.exception.ServiceException;
import com.tddp2.grupo2.linkup.infrastructure.Database;
import com.tddp2.grupo2.linkup.model.Profile;

public abstract class FacebookService implements LinkupService {

    protected Database database;

    protected FacebookService(Database database) {
        this.database = database;
    }

    public ServiceType getType() {
        return ServiceType.FACEBOOK;
    }

    public abstract Profile loadNewUser();
    public abstract void updateUser(Profile profile) throws ServiceException;
}
