package com.tddp2.grupo2.linkup.service.impl;


import com.tddp2.grupo2.linkup.exception.APIError;
import com.tddp2.grupo2.linkup.exception.ServiceException;
import com.tddp2.grupo2.linkup.model.Profile;
import com.tddp2.grupo2.linkup.service.api.ProfileService;
import com.tddp2.grupo2.linkup.utils.ErrorUtils;

import java.io.IOException;


public class ProfileServiceImpl extends ProfileService {

    public ProfileServiceImpl() {
        super();
    }

    @Override
    public void createProfile(Profile profile) throws ServiceException {
        /*MatchClient matchClient = clientService.getClient();
        Call<MatchResponse> call = matchClient.users.createUser(new UserRequest(user));
        try {
            Response<MatchResponse> response = call.execute();
            if (response.isSuccessful()) {
                //Save User
                MatchResponse matchResponse = response.body();
                user.setId(matchResponse.getData());
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
