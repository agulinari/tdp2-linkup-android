package com.tddp2.grupo2.linkup.service.impl;

import android.os.Bundle;
import android.util.Log;
import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.tddp2.grupo2.linkup.infrastructure.Database;
import com.tddp2.grupo2.linkup.service.api.LoginService;
import org.json.JSONException;
import org.json.JSONObject;

public class LoginServiceImpl extends LoginService {

    public LoginServiceImpl(Database database) {
        super(database);
    }

    @Override
    public void loadDataFromFacebook() {
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

        //profile.setFbid(getStringParam(jsonResponse, "id"));

        //profile.setFirstName(getStringParam(jsonResponse, "first_name"));
        //profile.setLastName(getStringParam(jsonResponse, "last_name"));
        //profile.setGender(getStringParam(jsonResponse, "gender"));
        //profile.setBirthday(getStringParam(jsonResponse, "birthday"));
        //profile.setComments(getStringParam(jsonResponse, "about"));
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
