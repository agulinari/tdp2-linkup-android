package com.tddp2.grupo2.linkup.service.impl;


import com.tddp2.grupo2.linkup.exception.APIError;
import android.os.Bundle;
import android.util.Log;
import com.tddp2.grupo2.linkup.exception.ServiceException;
import com.tddp2.grupo2.linkup.model.Profile;
import com.tddp2.grupo2.linkup.service.api.ProfileService;
import com.tddp2.grupo2.linkup.utils.ErrorUtils;

import java.io.IOException;
import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;

import org.json.JSONException;
import org.json.JSONObject;


public class ProfileServiceImpl extends ProfileService {

    public ProfileServiceImpl() {
        super();
    }

    @Override
    public void createProfile(Profile profile) throws ServiceException {
        /*MatchClient matchClient = clientService.getClient();
        Call<MatchResponse> call = matchClient.users.createUser(new UserRequest(user));
        try {
            Response<MatchResponse> response = call.execute();
            if (response.isSuccessful()) {
                //Save User
                MatchResponse matchResponse = response.body();
                user.setId(matchResponse.getData());
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
        try {
            Thread.sleep(3000);
            // Do some stuff
        } catch (Exception e) {
            e.getLocalizedMessage();
        }
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
        try {
            Thread.sleep(3000);
            // Do some stuff
        } catch (Exception e) {
            e.getLocalizedMessage();
        }
    }

    @Override
    public void loadDataFromFacebook(Profile profile) {
        GraphRequest request = new GraphRequest(
                AccessToken.getCurrentAccessToken(),
                "/me"

        );
        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,first_name");
        request.setParameters(parameters);
        GraphResponse response = request.executeAndWait();
        JSONObject jsonResponse = response.getJSONObject();
        profile.setFirstName(getStringParam(jsonResponse, "first_name"));
    }

    public void saveUser(Profile profile) {
        //   this.database.setUser(user);
    }

    private String getStringParam(JSONObject object, String key) {
        try {
            return object.getString(key);
        } catch (JSONException e) {
            Log.e("FacebookData", "Missing parameter " + key);
            return "";
        }
    }
}
