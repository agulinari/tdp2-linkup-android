package com.tddp2.grupo2.linkup.service.impl;

import android.os.Bundle;
import android.util.Log;
import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.tddp2.grupo2.linkup.infrastructure.Database;
import com.tddp2.grupo2.linkup.model.Profile;
import com.tddp2.grupo2.linkup.service.api.LoginService;
import com.tddp2.grupo2.linkup.task.FacebookTaskResponse;
import org.json.JSONException;
import org.json.JSONObject;

public class LoginServiceImpl extends LoginService {

    public LoginServiceImpl(Database database) {
        super(database);
    }

    @Override
    public void loadDataFromFacebook(FacebookTaskResponse facebookData) {
        GraphRequest request = new GraphRequest(
                AccessToken.getCurrentAccessToken(),
                "/me"

        );
        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,first_name,last_name,gender,birthday,work,education,about,picture");
        request.setParameters(parameters);
        GraphResponse response = request.executeAndWait();
        JSONObject jsonResponse = response.getJSONObject();

        Log.i("FacebookData", jsonResponse.toString());
        Profile profile = new Profile();
        profile.setFbid(getStringParam(jsonResponse, "id"));
        profile.setFirstName(getStringParam(jsonResponse, "first_name"));
        profile.setLastName(getStringParam(jsonResponse, "last_name"));
        profile.setGender(getStringParam(jsonResponse, "gender"));
        profile.setBirthday(getStringParam(jsonResponse, "birthday"));
        profile.setComments(getStringParam(jsonResponse, "about"));

        database.setProfile(profile);

        JSONObject profilePicture = getJsonParam(jsonResponse, "picture");
        JSONObject profilePictureData = getJsonParam(profilePicture, "data");
        boolean hasProfilePicture = !getBooleanParam(profilePictureData, "is_silhouette");

        facebookData.setBirthday(profile.getBirthday());
        facebookData.setHasProfilePicture(hasProfilePicture);
    }

    private String getStringParam(JSONObject object, String key) {
        try {
            return object.getString(key);
        } catch (JSONException e) {
            Log.i("FacebookData", "Missing parameter " + key);
            return "";
        }
    }

    private JSONObject getJsonParam(JSONObject object, String key) {
        try {
            return object.getJSONObject(key);
        } catch (JSONException e) {
            Log.i("FacebookData", "Missing parameter " + key);
            return new JSONObject();
        }
    }

    private boolean getBooleanParam(JSONObject object, String key) {
        try {
            return object.getBoolean(key);
        } catch (JSONException e) {
            Log.i("FacebookData", "Missing parameter " + key);
            return false;
        }
    }
}
