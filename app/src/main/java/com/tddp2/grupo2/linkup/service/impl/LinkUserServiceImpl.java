package com.tddp2.grupo2.linkup.service.impl;

import android.util.Log;

import com.tddp2.grupo2.linkup.exception.ServiceException;
import com.tddp2.grupo2.linkup.infrastructure.Database;
import com.tddp2.grupo2.linkup.infrastructure.LinkupClient;
import com.tddp2.grupo2.linkup.infrastructure.client.request.AbuseReportRequest;
import com.tddp2.grupo2.linkup.infrastructure.client.request.BlockRequest;
import com.tddp2.grupo2.linkup.infrastructure.client.request.RecommendRequest;
import com.tddp2.grupo2.linkup.infrastructure.client.response.AbuseReportResponse;
import com.tddp2.grupo2.linkup.infrastructure.client.response.BlockResponse;
import com.tddp2.grupo2.linkup.infrastructure.client.response.RecommendResponse;
import com.tddp2.grupo2.linkup.infrastructure.client.response.UserResponse;
import com.tddp2.grupo2.linkup.model.AbuseReport;
import com.tddp2.grupo2.linkup.model.Block;
import com.tddp2.grupo2.linkup.model.Profile;
import com.tddp2.grupo2.linkup.model.Recommend;
import com.tddp2.grupo2.linkup.service.api.ClientService;
import com.tddp2.grupo2.linkup.service.api.LinkUserService;
import com.tddp2.grupo2.linkup.service.api.LinksService;
import com.tddp2.grupo2.linkup.service.factory.ServiceFactory;

import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

public class LinkUserServiceImpl extends LinkUserService {
    private ClientService clientService;
    private Database database;
    private static String TAG = "LINK_USER_SERVICE";

    public LinkUserServiceImpl(Database database, ClientService clientService) {
        this.database = database;
        this.clientService = clientService;
    }

    @Override
    public Profile loadUser(String fbid) throws ServiceException {

        List<Profile> links = this.database.getLinks().getLinks();
        for (Profile profile : links){
            if (profile.getFbid().equals(fbid)){
                return profile;
            }
        }

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
            if (response.isSuccessful()) {
                LinksService linksService = ServiceFactory.getLinksService();
                linksService.removeLink(abuseReport.getIdReported());
            } else {
                throw new ServiceException("Abuse report error");
            }
        } catch (Exception e) {
            throw new ServiceException(e.getLocalizedMessage());
        }
    }

    @Override
    public void blockUser(Block block) throws ServiceException {
        LinkupClient linkupClient = clientService.getClient();
        BlockRequest blockRequest = new BlockRequest(block);
        Call<BlockResponse> call = linkupClient.profiles.blockUser(blockRequest);
        try {
            Response<BlockResponse> response = call.execute();
            if (response.isSuccessful()) {
                LinksService linksService = ServiceFactory.getLinksService();
                linksService.removeLink(block.getIdBlockedUser());
            } else {
                throw new ServiceException("Block user error");
            }
        } catch (Exception e) {
            throw new ServiceException(e.getLocalizedMessage());
        }
    }

    @Override
    public void recommendUser(Recommend recommend) throws ServiceException {
        LinkupClient linkupClient = clientService.getClient();
        RecommendRequest request = new RecommendRequest(recommend);
        Call<RecommendResponse> call = linkupClient.profiles.recommendUser(request);
        try {
            Response<RecommendResponse> response = call.execute();
            if (response.isSuccessful()) {
                return;
            } else {
                throw new ServiceException("Block user error");
            }
        } catch (Exception e) {
            throw new ServiceException(e.getLocalizedMessage());
        }
    }
}
