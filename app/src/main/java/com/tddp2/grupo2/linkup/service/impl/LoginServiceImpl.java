package com.tddp2.grupo2.linkup.service.impl;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.tddp2.grupo2.linkup.exception.MissingAgeException;
import com.tddp2.grupo2.linkup.infrastructure.Database;
import com.tddp2.grupo2.linkup.model.Image;
import com.tddp2.grupo2.linkup.model.Profile;
import com.tddp2.grupo2.linkup.service.api.LoginService;
import com.tddp2.grupo2.linkup.task.LoadUserTaskResponse;
import com.tddp2.grupo2.linkup.utils.ImageUtils;

import org.joda.time.LocalDate;
import org.joda.time.Years;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static android.R.attr.bitmap;

public class LoginServiceImpl extends LoginService {

    public LoginServiceImpl(Database database) {
        super(database);
    }

    @Override
    public void loadUserData(LoadUserTaskResponse facebookData) {
       // if (!this.database.getProfile().getFbid().isEmpty()) {
            facebookData.isNewUser = true;
            loadDataFromFacebook(facebookData);
        /*} else {
            String loggedUserId = getLoggedUserId();
            String savedUserId = this.database.getProfile().getFbid();
            if (loggedUserId.equals(savedUserId)) {
                facebookData.isNewUser = false;
            } else {
                facebookData.isNewUser = true;
                loadDataFromFacebook(facebookData);
            }
        }*/
    }

    private String getLoggedUserId() {
        GraphRequest request = new GraphRequest(
                AccessToken.getCurrentAccessToken(),
                "/me"
        );
        Bundle parameters = new Bundle();
        parameters.putString("fields", "id");
        request.setParameters(parameters);
        GraphResponse response = request.executeAndWait();
        JSONObject jsonResponse = response.getJSONObject();
        return getStringParam(jsonResponse, "id");
    }


    @Override
    public void loadDataFromFacebook(LoadUserTaskResponse facebookData) {
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

        String birthday = getStringParam(jsonResponse, "birthday");
        try {
            int age = getAgeFromBirthday(birthday);
            facebookData.hasBirthday = true;
            facebookData.isAdult = (age >= 18);
        } catch (MissingAgeException e) {
            facebookData.hasBirthday = false;
        }

        JSONObject profilePicture = getJsonParam(jsonResponse, "picture");
        JSONObject profilePictureData = getJsonParam(profilePicture, "data");
        facebookData.hasProfilePicture = !getBooleanParam(profilePictureData, "is_silhouette");

        if (facebookData.hasProfilePicture && facebookData.hasBirthday && facebookData.isAdult) {
            Profile profile = new Profile();
            profile.setFbid(getStringParam(jsonResponse, "id"));
            profile.setFirstName(getStringParam(jsonResponse, "first_name"));
            profile.setLastName(getStringParam(jsonResponse, "last_name"));
            profile.setGender(getStringParam(jsonResponse, "gender"));
            profile.setBirthday(getStringParam(jsonResponse, "birthday"));
            profile.setComments(getStringParam(jsonResponse, "about"));
            profile.setOccupation(getWork(jsonResponse));
            profile.setEducation(getEducation(jsonResponse));

            Image image = new Image();
            Bitmap bitmap = loadProfilePicture(profile.getFbid());
            String profileImage = ImageUtils.bitmapToBase64(bitmap);
            image.setImage(profileImage);
            List<Image> images = new ArrayList<>();
            images.add(image);
            profile.setImages(images);

            database.setProfile(profile);
        }
    }

    private Bitmap loadProfilePicture(String fbid) {
        try {
            URL url = new URL("https://graph.facebook.com/" + fbid + "/picture?width=500");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            return BitmapFactory.decodeStream(input);
        } catch (IOException e) {
            Log.e("FacebookData", e.getMessage());
            return null;
        }
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

    private String getWork(JSONObject jsonResponse) {
        try {
            JSONArray workHistory = jsonResponse.getJSONArray("work");
            JSONObject lastWork = workHistory.getJSONObject(workHistory.length() - 1);
            JSONObject position = lastWork.getJSONObject("position");
            return position.getString("name");
        } catch (JSONException e) {
            Log.i("FacebookData", "Missing parameter work");
            return "";
        }
    }

    private String getEducation(JSONObject jsonResponse) {
        try {
            JSONArray educationHistory = jsonResponse.getJSONArray("education");
            JSONObject lastEducation = educationHistory.getJSONObject(educationHistory.length() - 1);
            JSONObject position = lastEducation.getJSONObject("school");
            return position.getString("name");
        } catch (JSONException e) {
            Log.i("FacebookData", "Missing parameter education");
            return "";
        }
    }

    private int getAgeFromBirthday(String birthday) throws MissingAgeException {
        if (birthday.equals("")) {
            throw new MissingAgeException();
        }
        try {
            DateTimeFormatter formatter = DateTimeFormat.forPattern("MM/dd/yyyy");
            LocalDate birthdayDate = formatter.parseLocalDate(birthday);
            LocalDate now = new LocalDate();
            Years age = Years.yearsBetween(birthdayDate, now);
            return age.getYears();
        } catch (IllegalArgumentException e) {
            throw new MissingAgeException();
        }
    }
}
