package com.tddp2.grupo2.linkup.service.impl;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.tddp2.grupo2.linkup.exception.ServiceException;
import com.tddp2.grupo2.linkup.infrastructure.Database;
import com.tddp2.grupo2.linkup.model.Image;
import com.tddp2.grupo2.linkup.model.ImageWrapper;
import com.tddp2.grupo2.linkup.model.Profile;
import com.tddp2.grupo2.linkup.service.api.ClientService;
import com.tddp2.grupo2.linkup.service.api.FacebookService;
import com.tddp2.grupo2.linkup.utils.DateUtils;
import com.tddp2.grupo2.linkup.utils.ImageUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class FacebookServiceImpl extends FacebookService {

    private ClientService clientService;

    public FacebookServiceImpl(Database database, ClientService clientService) {
        super(database);
        this.clientService = clientService;
    }

    public Profile loadNewUser() {
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

        boolean hasProfilePicture = false;
        JSONObject profilePicture = getJsonParam(jsonResponse, "picture");
        JSONObject profilePictureData = getJsonParam(profilePicture, "data");
        if (profilePictureData.has("is_silhouette")) {
            hasProfilePicture = !getBooleanParam(profilePictureData, "is_silhouette");
        }

        Profile profile = new Profile();
        profile.setFbid(getStringParam(jsonResponse, "id"));
        profile.setFirstName(getStringParam(jsonResponse, "first_name"));
        profile.setLastName(getStringParam(jsonResponse, "last_name"));
        profile.setGender(getStringParam(jsonResponse, "gender"));

        String facebookBirthday = getStringParam(jsonResponse, "birthday");
        String linkUpBirthday = DateUtils.facebookToLinkupFormat(facebookBirthday);
        profile.setBirthday(linkUpBirthday);

        profile.setComments(getStringParam(jsonResponse, "about"));
        profile.setOccupation(getWork(jsonResponse));
        profile.setEducation(getEducation(jsonResponse));

        if (hasProfilePicture) {
            getAndSaveProfilePicture(profile);
        }
        return profile;
    }

    public void updateUser(Profile profile) throws ServiceException {
        GraphRequest request = new GraphRequest(
                AccessToken.getCurrentAccessToken(),
                "/me"

        );
        Log.i("FACEBOOK_API_VERSION", com.facebook.internal.ServerProtocol.getDefaultAPIVersion());
        Log.i("FACEBOOK_BASE_GRAPH_URL", com.facebook.internal.ServerProtocol.getGraphUrlBase());
        Bundle parameters = new Bundle();
        parameters.putString("fields", "first_name,last_name,gender,work,education,about,picture");
        request.setParameters(parameters);
        GraphResponse response = request.executeAndWait();
        if (response.getError() != null) {
            throw new ServiceException("Cannot connect to Facebook");
        }
        JSONObject jsonResponse = response.getJSONObject();
        Log.i("FacebookData", jsonResponse.toString());

        boolean hasProfilePicture = false;
        JSONObject profilePicture = getJsonParam(jsonResponse, "picture");
        JSONObject profilePictureData = getJsonParam(profilePicture, "data");
        if (profilePictureData.has("is_silhouette")) {
            hasProfilePicture = !getBooleanParam(profilePictureData, "is_silhouette");
        }

        profile.setFirstName(getStringParam(jsonResponse, "first_name"));
        profile.setLastName(getStringParam(jsonResponse, "last_name"));
        profile.setGender(getStringParam(jsonResponse, "gender"));
        profile.setComments(getStringParam(jsonResponse, "about"));
        profile.setOccupation(getWork(jsonResponse));
        profile.setEducation(getEducation(jsonResponse));

        if (hasProfilePicture) {
            getAndSaveProfilePicture(profile);
        }

        loadUserPhotos(getStringParam(jsonResponse, "id"), profile);
    }

    public void loadUserPhotos(String fbid, Profile profile) throws ServiceException {
        GraphRequest request = new GraphRequest(
                AccessToken.getCurrentAccessToken(),
                "/" + fbid + "/photos?type=uploaded&fields=images"
        );
        GraphResponse response = request.executeAndWait();
        if (response.getError() != null) {
            Log.i("FacebookData", "ERROR");
        } else {
            JSONObject jsonResponse = response.getJSONObject();
            Log.i("FacebookData", jsonResponse.toString());
            JSONArray userPicturesData = getJsonArray(jsonResponse, "data");
            if (userPicturesData.length() != 0) {
                int numberOfPictures = (userPicturesData.length() < 5) ? userPicturesData.length() : 5;
                for (int i = 0; i < numberOfPictures; i++) {
                    JSONObject pictureData = getJsonArrayElement(userPicturesData, i);
                    JSONArray pictureSizes = getJsonArray(pictureData, "images");
                    String pictureUrl = getBestPictureSizeUrl(pictureSizes);
                    if (pictureUrl != "") {
                        Bitmap picture = loadPictureFormUrl(pictureUrl);
                        if (picture != null) {
                            int pictureId = i + 2;
                            addImageToProfile(profile, picture, pictureId);
                        }
                    }
                }
            }
        }
    }

    private String getBestPictureSizeUrl(JSONArray pictureSizes) {
        for (int j = 0; j < pictureSizes.length(); j++) {
            JSONObject sizeData = getJsonArrayElement(pictureSizes, j);
            int pictureWidth = getIntParam(sizeData, "width");
            if (pictureWidth < 600 && pictureWidth > 0) {
                return getStringParam(sizeData, "source");
            }
        }
        return "";
    }

    private void addImageToProfile(Profile profile, Bitmap picture, int pictureId) {
        String profileImage = ImageUtils.bitmapToBase64(picture);

        Image image = new Image();
        image.setData(profileImage);
        image.setIdImage(String.valueOf(pictureId));

        List<ImageWrapper> images = profile.getImages();
        images.add(new ImageWrapper(image));
        profile.setImages(images);
    }

    private void getAndSaveProfilePicture(Profile profile) {
        Image image = new Image();
        Image avatarImage  = new Image();
        Bitmap bitmap = loadProfilePicture(profile.getFbid(), "500");
        Bitmap minibitmap = loadProfilePicture(profile.getFbid(), "80");
        String profileImage = ImageUtils.bitmapToBase64(bitmap);
        String avatar = ImageUtils.bitmapToBase64(minibitmap);
        image.setData(profileImage);
        image.setIdImage("1");
        avatarImage.setIdImage("0");
        avatarImage.setData(avatar);
        List<ImageWrapper> images = new ArrayList<ImageWrapper>();
        images.add(new ImageWrapper(image));
        profile.setImages(images);
        profile.setAvatar(new ImageWrapper(avatarImage));
    }

    private Bitmap loadProfilePicture(String fbid, String size) {
        String pictureUrl = "https://graph.facebook.com/" + fbid + "/picture?width=" + size;
        return this.loadPictureFormUrl(pictureUrl);
    }

    private Bitmap loadPictureFormUrl(String pictureUrl) {
        try {
            URL url = new URL(pictureUrl);
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

    private int getIntParam(JSONObject object, String key) {
        try {
            return object.getInt(key);
        } catch (JSONException e) {
            Log.i("FacebookData", "Missing parameter " + key);
            return 0;
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

    private JSONArray getJsonArray(JSONObject object, String key) {
        try {
            return object.getJSONArray(key);
        } catch (JSONException e) {
            Log.i("FacebookData", "Missing parameter " + key);
            return new JSONArray();
        }
    }

    private JSONObject getJsonArrayElement(JSONArray object, int index) {
        try {
            return object.getJSONObject(index);
        } catch (JSONException e) {
            Log.i("FacebookData", "Missing index " + index);
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
}
