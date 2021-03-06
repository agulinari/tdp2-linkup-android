package com.tddp2.grupo2.linkup.service.impl;

import android.util.Log;

import com.facebook.AccessToken;
import com.tddp2.grupo2.linkup.exception.MissingAgeException;
import com.tddp2.grupo2.linkup.exception.ServiceException;
import com.tddp2.grupo2.linkup.infrastructure.Database;
import com.tddp2.grupo2.linkup.infrastructure.LinkupClient;
import com.tddp2.grupo2.linkup.infrastructure.client.response.UserResponse;
import com.tddp2.grupo2.linkup.model.Profile;
import com.tddp2.grupo2.linkup.service.api.ClientService;
import com.tddp2.grupo2.linkup.service.api.FacebookService;
import com.tddp2.grupo2.linkup.service.api.LoginService;
import com.tddp2.grupo2.linkup.service.api.NotificationService;
import com.tddp2.grupo2.linkup.service.factory.ServiceFactory;
import com.tddp2.grupo2.linkup.task.LoadUserTaskResponse;
import com.tddp2.grupo2.linkup.utils.DateUtils;

import retrofit2.Call;
import retrofit2.Response;

public class LoginServiceImpl extends LoginService {

    private ClientService clientService;

    public LoginServiceImpl(Database database, ClientService clientService) {
        super(database);
        this.clientService = clientService;
    }

    @Override
    public void loadUserData(LoadUserTaskResponse facebookData)  {

            if (!isUserRegistered()){
                Log.i("LOGIN", "no esta registrado en la base local,  pregunto si esta registrado en el server");
                try {
                    boolean success = loadDataFromServer(facebookData);
                    if (success) {
                        facebookData.isNewUser = false;
                        Log.i("LOGIN", "no en la base lo traigo de server y da ok");
                        updateToken();
                    }else{
                        //El servidor responde con codigo diferente a 200
                        facebookData.isNewUser = true;
                        Log.i("LOGIN", "no en la base lo busco en el server y no devuelve success, voy a facebook");
                        loadDataFromFacebook(facebookData);
                    }
                }catch (ServiceException e){
                    Log.e("LOGIN", e.getLocalizedMessage());
                    Log.i("LOGIN", "no en la base lo traigo de server y da error, no hay conectividad");
                    facebookData.setError("Falló la conexión con el servidor");
                }
            }else{
                //TODO: Seria mas seguro verificar tambien si esta registrado en el server
                Log.i("LOGIN", "existe en la base y fue registrado, lo devuelvo");
                facebookData.isNewUser = false;
            }
    }

    private void updateToken() {
        NotificationService notificationService = ServiceFactory.getNotificationService();
        String token = notificationService.getToken();
        try {
            Log.d("FirebaseIDService", token);
            notificationService.updateToken(token);
        } catch (ServiceException e) {
            Log.e("FirebaseIDService" , e.getMessage(), e);
        }
    }

    @Override
    public void loadDataFromFacebook(LoadUserTaskResponse facebookData) {
        FacebookService facebookService = ServiceFactory.getFacebookService();
        Profile newUser = facebookService.loadNewUser();

        String birthday = newUser.getBirthday();
        try {
            int age = DateUtils.getAgeFromBirthday(birthday);
            facebookData.hasBirthday = true;
            facebookData.isAdult = (age >= 18);
        } catch (MissingAgeException e) {
            facebookData.hasBirthday = false;
        }
        facebookData.hasProfilePicture = !newUser.getImages().isEmpty();

        if (facebookData.hasProfilePicture && facebookData.hasBirthday && facebookData.isAdult) {
            database.setProfile(newUser);
        }
    }

    @Override
    public boolean loadDataFromServer(LoadUserTaskResponse serverData) throws ServiceException {
        String fbid = AccessToken.getCurrentAccessToken().getUserId();
        LinkupClient linkupClient = clientService.getClient();
        Call<UserResponse> call = linkupClient.profiles.getProfile(fbid);
        try {
            Response<UserResponse> response = call.execute();
            if (response.isSuccessful()) {
                //Save User
                Profile profileResponse = response.body().getUser();
                serverData.hasBirthday = true;
                serverData.hasProfilePicture = true;
                serverData.isAdult = true;
                database.setProfile(profileResponse);
                return true;
            } else {
                return false;
            }
        } catch (Exception e){
            throw new ServiceException(e.getLocalizedMessage());
        }
    }

    @Override
    public boolean isUserRegistered() {
        String userId = AccessToken.getCurrentAccessToken().getUserId();
        Profile profile = this.database.getProfile();
        if (profile == null){
            Log.i("LOGIN SERVICE","PROFILE NULL");
            return false;
        }
        if (!profile.getFbid().equals(userId)){
            Log.i("LOGIN SERVICE","OTRO USUARIO");
            return false;
        }
        Boolean isPremium = profile.getControl().getIsPremium();
        if (isPremium == null) {
            Log.i("LOGIN SERVICE","ACCOUNT NULL");
            return false;
        }else{
            Log.i("LOGIN SERVICE","REGISTRADO");
            return true;
        }
    }
}
