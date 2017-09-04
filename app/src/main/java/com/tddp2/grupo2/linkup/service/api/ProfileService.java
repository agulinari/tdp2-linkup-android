package com.tddp2.grupo2.linkup.service.api;

import android.content.res.Configuration;
import android.util.Log;

import com.tddp2.grupo2.linkup.exception.ServiceException;
import com.tddp2.grupo2.linkup.model.Profile;
import java.io.IOException;



public abstract class ProfileService implements LinkupService{


    public abstract void createProfile(Profile profile) throws ServiceException;

    public abstract void updateProfile(Profile profile) throws ServiceException;

    public abstract void saveUser(Profile profile);

    public abstract void loadDataFromFacebook(Profile profile);

    public ServiceType getType() {
        return ServiceType.PROFILE;
    }
}
