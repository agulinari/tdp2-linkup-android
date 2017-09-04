package com.tddp2.grupo2.linkup.service.impl;


import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.tddp2.grupo2.linkup.R;
import com.tddp2.grupo2.linkup.exception.APIError;
import com.tddp2.grupo2.linkup.exception.ServiceException;
import com.tddp2.grupo2.linkup.model.Profile;
import com.tddp2.grupo2.linkup.service.api.ProfileService;
import com.tddp2.grupo2.linkup.utils.ErrorUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;

import cz.msebera.android.httpclient.Header;



public class ProfileServiceImpl extends ProfileService {

    public ProfileServiceImpl() {
        super();
    }

    @Override
    public void createProfile(Profile profile) throws ServiceException {
        String url = "https://linkup.herokuapp.com/user";

        AsyncHttpClient client = new AsyncHttpClient();

        client.post(url,  new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String res, Throwable t) {
                // called when response HTTP status is "4XX" (eg. 401, 403, 404)
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable t, JSONObject res) {
                // called when response HTTP status is "4XX" (eg. 401, 403, 404)
                Log.e("ERROR", String.valueOf(statusCode));
            }
        });
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


    public void saveUser(Profile profile) {
        //   this.database.setUser(user);
    }
}
