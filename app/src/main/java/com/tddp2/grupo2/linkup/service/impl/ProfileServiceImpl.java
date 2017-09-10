package com.tddp2.grupo2.linkup.service.impl;

import android.os.Bundle;
import android.util.Log;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;

import com.tddp2.grupo2.linkup.exception.APIError;
import com.tddp2.grupo2.linkup.exception.ServiceException;
import com.tddp2.grupo2.linkup.infrastructure.Database;
import com.tddp2.grupo2.linkup.infrastructure.LinkupClient;
import com.tddp2.grupo2.linkup.model.Profile;
import com.tddp2.grupo2.linkup.service.api.ClientService;
import com.tddp2.grupo2.linkup.service.api.ProfileService;
import com.tddp2.grupo2.linkup.utils.ErrorUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Response;

public class ProfileServiceImpl extends ProfileService {

    private ClientService clientService;

    public ProfileServiceImpl(Database database, ClientService clientService) {
        super(database);
        this.clientService = clientService;
    }

    @Override
    public void createProfile(Profile profile) throws ServiceException {
        LinkupClient linkupClient = clientService.getClient();
        Call<Profile> call = linkupClient.profiles.createProfile(profile);
        try {
            Response<Profile> response = call.execute();
            if (response.isSuccessful()) {
                //Save User
                Profile profileResponse = response.body();
                saveUser(profileResponse);
            } else {
                APIError error = ErrorUtils.parseError(response);
                throw new ServiceException(error);
            }
        } catch (IOException e) {
            throw new ServiceException(e.getLocalizedMessage());
        }
    }

    @Override
    public void updateProfile(Profile profile) throws ServiceException {

        LinkupClient linkupClient = clientService.getClient();
        Call<Profile> call = linkupClient.profiles.updateProfile(profile);
        try {
            Response<Profile> response = call.execute();
            if (response.isSuccessful()) {
                //Save User
                Profile profileResponse = response.body();
                saveUser(profileResponse);
            } else {
                APIError error = ErrorUtils.parseError(response);
                throw new ServiceException(error);
            }
        } catch (IOException e) {
            throw new ServiceException(e.getLocalizedMessage());
        }
    }

    public void saveUser(Profile profile) {
           this.database.setProfile(profile);
    }
}
