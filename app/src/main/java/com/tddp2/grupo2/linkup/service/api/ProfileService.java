package com.tddp2.grupo2.linkup.service.api;

import com.tddp2.grupo2.linkup.exception.ServiceException;
import com.tddp2.grupo2.linkup.infrastructure.Database;
import com.tddp2.grupo2.linkup.model.Profile;

public abstract class ProfileService implements LinkupService{

    protected Database database;

    protected ProfileService(Database database) {
        this.database = database;
    }

    public abstract void createProfile(Profile profile) throws ServiceException;

    public abstract void updateProfile(Profile profile) throws ServiceException;

    public abstract void saveUser(Profile profile);

    public ServiceType getType() {
        return ServiceType.PROFILE;
    }

    public Profile getLocalProfile() {
        return database.getProfile();
    }

    public abstract void updateFromFacebook(Profile profile) throws ServiceException;

    public abstract void saveLocation(com.tddp2.grupo2.linkup.model.Location location);

    public abstract void upgradeAccountType() throws ServiceException;
}
