package com.tddp2.grupo2.linkup.service.impl;


import com.tddp2.grupo2.linkup.exception.APIError;
import com.tddp2.grupo2.linkup.exception.ServiceException;
import com.tddp2.grupo2.linkup.infrastructure.Database;
import com.tddp2.grupo2.linkup.infrastructure.LinkupClient;
import com.tddp2.grupo2.linkup.infrastructure.client.request.TokenRequest;
import com.tddp2.grupo2.linkup.model.Profile;
import com.tddp2.grupo2.linkup.service.api.ClientService;
import com.tddp2.grupo2.linkup.service.api.NotificationService;
import com.tddp2.grupo2.linkup.utils.ErrorUtils;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Response;

public class NotificationServiceImpl  extends NotificationService{

    private ClientService clientService;

    public NotificationServiceImpl(Database database, ClientService clientService) {
        super(database);
        this.clientService = clientService;
    }

    @Override
    public void saveToken(String token) {
        this.database.setToken(token);
    }

    @Override
    public String getToken() {
        return this.database.getToken();
    }

    @Override
    public void updateToken(String token) throws ServiceException {
        Profile profile = this.database.getProfile();
        if (profile == null){
            return;
        }
        String fbid = profile.getFbid();

        LinkupClient linkupClient = clientService.getClient();
        TokenRequest request = new TokenRequest(fbid, token);
        Call<?> call = linkupClient.profiles.updateToken(request);
        try {
            Response<?> response = call.execute();
            if (response.isSuccessful()) {
                return;
            } else {
                APIError error = ErrorUtils.parseError(response);
                throw new ServiceException(error);
            }
        } catch (IOException e) {
            throw new ServiceException(e.getLocalizedMessage());
        }

    }
}
