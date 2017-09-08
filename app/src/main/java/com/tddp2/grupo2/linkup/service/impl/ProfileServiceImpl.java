package com.tddp2.grupo2.linkup.service.impl;


import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.tddp2.grupo2.linkup.R;
import com.tddp2.grupo2.linkup.exception.APIError;
import android.os.Bundle;
import android.util.Log;
import com.tddp2.grupo2.linkup.exception.ServiceException;
import com.tddp2.grupo2.linkup.infrastructure.Database;
import com.tddp2.grupo2.linkup.model.Profile;
import com.tddp2.grupo2.linkup.service.api.ProfileService;
import com.tddp2.grupo2.linkup.utils.ErrorUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;



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

    @Override
    public void loadDataFromFacebook(Profile profile) {
        GraphRequest request = new GraphRequest(
                AccessToken.getCurrentAccessToken(),
                "/me"

        );
        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,first_name,last_name,gender,birthday,work,education,about");
        request.setParameters(parameters);
        GraphResponse response = request.executeAndWait();
        JSONObject jsonResponse = response.getJSONObject();
        Log.i("FacebookData", jsonResponse.toString());
        profile.setFirstName(getStringParam(jsonResponse, "first_name"));
    }

    public void saveUser(Profile profile) {
           this.database.setProfile(profile);
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
