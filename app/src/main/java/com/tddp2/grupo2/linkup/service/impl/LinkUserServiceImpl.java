package com.tddp2.grupo2.linkup.service.impl;

import android.util.Log;
import com.tddp2.grupo2.linkup.exception.ServiceException;
import com.tddp2.grupo2.linkup.infrastructure.LinkupClient;
import com.tddp2.grupo2.linkup.infrastructure.client.request.AbuseReportRequest;
import com.tddp2.grupo2.linkup.infrastructure.client.response.AbuseReportResponse;
import com.tddp2.grupo2.linkup.infrastructure.client.response.UserResponse;
import com.tddp2.grupo2.linkup.model.AbuseReport;
import com.tddp2.grupo2.linkup.model.Profile;
import com.tddp2.grupo2.linkup.service.api.ClientService;
import com.tddp2.grupo2.linkup.service.api.LinkUserService;
import retrofit2.Call;
import retrofit2.Response;

public class LinkUserServiceImpl extends LinkUserService {
    private ClientService clientService;
    private static String TAG = "LINK_USER_SERVICE";

    public LinkUserServiceImpl(ClientService clientService) {
        this.clientService = clientService;
    }

    @Override
    public Profile loadUser(String fbid) throws ServiceException {
        LinkupClient linkupClient = clientService.getClient();
        Call<UserResponse> call = linkupClient.profiles.getProfile(fbid);
        try {
            Response<UserResponse> response = call.execute();
            if (response.isSuccessful()) {
                Log.i(TAG, response.body().getUser().toString());
               return response.body().getUser();
            } else {
                throw new ServiceException("Error retrieving fbid " + fbid);
            }
        } catch (Exception e) {
            throw new ServiceException(e.getLocalizedMessage());
        }
    }

    @Override
    public void reportAbuse(AbuseReport abuseReport) throws ServiceException {
        LinkupClient linkupClient = clientService.getClient();
        AbuseReportRequest reportAbuseRequest = new AbuseReportRequest(abuseReport);
        Call<AbuseReportResponse> call = linkupClient.profiles.reportAbuse(reportAbuseRequest);
        try {
            Response<AbuseReportResponse> response = call.execute();
            if (!response.isSuccessful()) {
                throw new ServiceException("Abuse report error");
            }
        } catch (Exception e) {
            throw new ServiceException(e.getLocalizedMessage());
        }
    }
}
