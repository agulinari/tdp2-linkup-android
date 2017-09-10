package com.tddp2.grupo2.linkup.service.impl;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.tddp2.grupo2.linkup.exception.APIError;
import com.tddp2.grupo2.linkup.exception.MissingAgeException;
import com.tddp2.grupo2.linkup.exception.ServiceException;
import com.tddp2.grupo2.linkup.infrastructure.Database;
import com.tddp2.grupo2.linkup.infrastructure.LinkupClient;
import com.tddp2.grupo2.linkup.model.Image;
import com.tddp2.grupo2.linkup.model.Profile;
import com.tddp2.grupo2.linkup.service.api.ClientService;
import com.tddp2.grupo2.linkup.service.api.LoginService;
import com.tddp2.grupo2.linkup.task.LoadUserTaskResponse;
import com.tddp2.grupo2.linkup.utils.DateUtils;
import com.tddp2.grupo2.linkup.utils.ErrorUtils;
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

import retrofit2.Call;
import retrofit2.Response;

import static android.R.attr.accountType;
import static android.R.attr.bitmap;
import static com.facebook.AccessToken.getCurrentAccessToken;
import static com.facebook.internal.CallbackManagerImpl.RequestCodeOffset.Login;
import static com.tddp2.grupo2.linkup.R.drawable.profile;

public class LoginServiceImpl extends LoginService {

    private ClientService clientService;

    public LoginServiceImpl(Database database, ClientService clientService) {
        super(database);
        this.clientService = clientService;
    }

    @Override
    public void loadUserData(LoadUserTaskResponse facebookData)  {

        Profile profile = this.database.getProfile();
        if (profile != null){
            if (isUserRegistered()){
                Log.i("LOGIN", "existe en la base pero no fue registrado, voy a facebook");
                facebookData.isNewUser = true;
                loadDataFromFacebook(facebookData);
            }else{
                //TODO: Seria mas seguro verificar tambien si esta registrado en el server
                Log.i("LOGIN", "existe en la base y fue registrado, lo devuelvo");
                facebookData.isNewUser = false;
            }
        }else{
            try {
                loadDataFromServer(facebookData);
                facebookData.isNewUser = false;
                Log.i("LOGIN", "no en la base lo traigo de server y da ok");
            }catch (ServiceException e){
                Log.e("LOGIN", e.getLocalizedMessage());
                if (e.hasError()) {
                    //El servidor responde con codigo diferente a 200
                    facebookData.isNewUser = true;
                    Log.i("LOGIN", "no en la base lo busco en el server y no devuelve success, voy a facebook");
                    loadDataFromFacebook(facebookData);
                }else{
                    Log.i("LOGIN", "no en la base lo traigo de server y da error, no hay conectividad");
                    facebookData.isNewUser = true;
                    loadDataFromFacebook(facebookData);
                   // facebookData.setError("Falló la conexión con el servidor");
                }
            }
        }
    }

    private String getLoggedUserId() {
        GraphRequest request = new GraphRequest(
                getCurrentAccessToken(),
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
                getCurrentAccessToken(),
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
            int age = DateUtils.getAgeFromBirthday(birthday);
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

    @Override
    public void loadDataFromServer(LoadUserTaskResponse serverData) throws ServiceException {
        String fbid = AccessToken.getCurrentAccessToken().getUserId();
        LinkupClient linkupClient = clientService.getClient();
        Call<Profile> call = linkupClient.profiles.getProfile(fbid);
        try {
            Response<Profile> response = call.execute();
            if (response.isSuccessful()) {
                //Save User
                Profile profileResponse = response.body();
                serverData.hasBirthday = true;
                serverData.hasProfilePicture = true;
                serverData.isAdult = true;
                database.setProfile(profileResponse);
            } else {
                APIError error = ErrorUtils.parseError(response);
                throw new ServiceException(error);
            }
        } catch (Exception e) {
            throw new ServiceException(e.getLocalizedMessage());
        }
    }

    @Override
    public boolean isUserRegistered() {
        Profile profile = this.database.getProfile();
        if (profile == null){
            return false;
        }
        String accountType = profile.getSettings().getAccountType();
        if (accountType == null || accountType.isEmpty()){
            return false;
        }else{
            return true;
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
}
