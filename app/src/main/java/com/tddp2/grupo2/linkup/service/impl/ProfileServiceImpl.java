package com.tddp2.grupo2.linkup.service.impl;

import android.util.Log;

import com.tddp2.grupo2.linkup.exception.ServiceException;
import com.tddp2.grupo2.linkup.infrastructure.Database;
import com.tddp2.grupo2.linkup.infrastructure.LinkupClient;
import com.tddp2.grupo2.linkup.infrastructure.client.request.PostUserRequest;
import com.tddp2.grupo2.linkup.infrastructure.client.request.PutUserRequest;
import com.tddp2.grupo2.linkup.infrastructure.client.response.UserResponse;
import com.tddp2.grupo2.linkup.model.Profile;
import com.tddp2.grupo2.linkup.service.api.ClientService;
import com.tddp2.grupo2.linkup.service.api.FacebookService;
import com.tddp2.grupo2.linkup.service.api.ProfileService;
import com.tddp2.grupo2.linkup.service.factory.ServiceFactory;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Response;

public class ProfileServiceImpl extends ProfileService {

    private ClientService clientService;

    public ProfileServiceImpl(Database database, ClientService clientService) {
        super(database);
        this.clientService = clientService;
    }

    @Override
    public void createProfile(Profile profile) throws ServiceException {
        LinkupClient linkupClient = clientService.getClient();
        PostUserRequest request = new PostUserRequest(profile);
        Call<UserResponse> call = linkupClient.profiles.createProfile(request);
        try {
            Response<UserResponse> response = call.execute();
            if (response.isSuccessful()) {
                //Save User
                Profile profileResponse = response.body().getUser();
                String accountType = profileResponse.getSettings().getAccountType();
                profile.getSettings().setAccountType(accountType);
                saveUser(profile);
            } else {
                //APIError error = ErrorUtils.parseError(response);
                throw new ServiceException(response.message());
            }
        } catch (IOException e) {
            throw new ServiceException(e.getLocalizedMessage());
        }
    }

    @Override
    public void updateProfile(Profile profile) throws ServiceException {

        LinkupClient linkupClient = clientService.getClient();
        PutUserRequest request = new PutUserRequest(profile);
        Call<UserResponse> call = linkupClient.profiles.updateProfile(request);
        try {
            Response<UserResponse> response = call.execute();
            if (response.isSuccessful()) {
                //Save User
                Profile profileResponse = response.body().getUser();
                Log.i("ACCOUNT TYPE", profileResponse.getSettings().getAccountType());

                saveUser(profile);
            } else {
                //APIError error = ErrorUtils.parseError(response);
                throw new ServiceException(response.message());
            }
        } catch (IOException e) {
            throw new ServiceException(e.getLocalizedMessage());
        }
    }

    public void saveUser(Profile profile) {
           this.database.setProfile(profile);
    }

    @Override
    public void updateFromFacebook(Profile profile) throws ServiceException {
        FacebookService facebookService = ServiceFactory.getFacebookService();
        facebookService.updateUser(profile);
        database.setProfile(profile);
    }

    @Override
    public void saveLocation(com.tddp2.grupo2.linkup.model.Location location) {
        Profile profile = this.getLocalProfile();
        profile.setLocation(location);
        this.database.setProfile(profile);
    }
}
