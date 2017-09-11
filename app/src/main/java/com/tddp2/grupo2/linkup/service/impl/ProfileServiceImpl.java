package com.tddp2.grupo2.linkup.service.impl;

import android.util.Log;
import com.tddp2.grupo2.linkup.exception.APIError;
import com.tddp2.grupo2.linkup.exception.ServiceException;
import com.tddp2.grupo2.linkup.infrastructure.Database;
import com.tddp2.grupo2.linkup.infrastructure.LinkupClient;
import com.tddp2.grupo2.linkup.model.Profile;
import com.tddp2.grupo2.linkup.service.api.ClientService;
import com.tddp2.grupo2.linkup.service.api.FacebookService;
import com.tddp2.grupo2.linkup.service.api.ProfileService;
import com.tddp2.grupo2.linkup.service.factory.ServiceFactory;
import com.tddp2.grupo2.linkup.utils.ErrorUtils;
import retrofit2.Call;
import retrofit2.Response;

import java.io.IOException;

public class ProfileServiceImpl extends ProfileService {

    private ClientService clientService;

    public ProfileServiceImpl(Database database, ClientService clientService) {
        super(database);
        this.clientService = clientService;
    }

    @Override
    public void createProfile(Profile profile) throws ServiceException {
        LinkupClient linkupClient = clientService.getClient();
        Call<Profile> call = linkupClient.profiles.createProfile(profile);
        try {
            Response<Profile> response = call.execute();
            if (response.isSuccessful()) {
                //Save User
                Profile profileResponse = response.body();
                saveUser(profileResponse);
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
        Call<Profile> call = linkupClient.profiles.updateProfile(profile);
        try {
            Response<Profile> response = call.execute();
            if (response.isSuccessful()) {
                //Save User
                Profile profileResponse = response.body();
                Log.i("ACCOUNT TYPE",profileResponse.getSettings().getAccountType());
                saveUser(profileResponse);
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
    public void updateFromFacebook(Profile profile) {
        FacebookService facebookService = ServiceFactory.getFacebookService();
        facebookService.updateUser(profile);
        database.setProfile(profile);
    }
}
