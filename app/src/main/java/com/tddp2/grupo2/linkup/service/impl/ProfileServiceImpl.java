package com.tddp2.grupo2.linkup.service.impl;

import android.os.Bundle;
import android.util.Log;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;

import com.tddp2.grupo2.linkup.exception.ServiceException;
import com.tddp2.grupo2.linkup.infrastructure.Database;
import com.tddp2.grupo2.linkup.model.Profile;
import com.tddp2.grupo2.linkup.service.api.ProfileService;

import org.json.JSONException;
import org.json.JSONObject;


public class ProfileServiceImpl extends ProfileService {

    public ProfileServiceImpl(Database database) {
        super(database);
    }

    @Override
    public void createProfile(Profile profile) throws ServiceException {

    }

    @Override
    public void updateProfile(Profile profile) throws ServiceException {
        /*MatchClient matchClient = clientService.getAuthClient();
        Call<MatchResponse> call = matchClient.users.updateUser(new UserRequest(user));
        try {
            Response<MatchResponse> response = call.execute();
            if (response.isSuccessful()) {
                //Save User
                saveUser(user);
                //Save Token
                clientService.saveToken(response.headers());
            } else {
                APIError error = ErrorUtils.parseError(response);
                throw new ServiceException(error);
            }

        } catch (IOException e) {
            throw new ServiceException(e.getLocalizedMessage());
        }*/

        saveUser(profile);

        try {
            Thread.sleep(3000);
            // Do some stuff
        } catch (Exception e) {
            e.getLocalizedMessage();
        }
    }

    public void saveUser(Profile profile) {
           this.database.setProfile(profile);
    }
}
