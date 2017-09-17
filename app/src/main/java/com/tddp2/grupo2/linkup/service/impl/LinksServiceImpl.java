package com.tddp2.grupo2.linkup.service.impl;

import com.tddp2.grupo2.linkup.exception.APIError;
import com.tddp2.grupo2.linkup.exception.ServiceException;
import com.tddp2.grupo2.linkup.infrastructure.Database;
import com.tddp2.grupo2.linkup.infrastructure.LinkupClient;
import com.tddp2.grupo2.linkup.infrastructure.client.response.CandidatesResponse;
import com.tddp2.grupo2.linkup.model.Links;
import com.tddp2.grupo2.linkup.model.Profile;
import com.tddp2.grupo2.linkup.service.api.ClientService;
import com.tddp2.grupo2.linkup.service.api.LinksService;
import com.tddp2.grupo2.linkup.utils.ErrorUtils;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

import static com.tddp2.grupo2.linkup.R.drawable.profile;


public class LinksServiceImpl extends LinksService{

    private ClientService clientService;

    public LinksServiceImpl(Database database, ClientService clientService) {
        super(database);
        this.clientService = clientService;
    }

    @Override
    public Links getLinks() throws ServiceException {
        Profile profile = this.database.getProfile();
        String fbid = profile.getFbid();

        Links links = this.database.getLinks();
        /*if (links !=null){
            return links;
        }*/
        LinkupClient linkupClient = clientService.getClient();
        Call<CandidatesResponse> call = linkupClient.candidates.getCandidates(fbid);
        try {
            Response<CandidatesResponse> response = call.execute();
            if (response.isSuccessful()) {
                //Save Links
                List<Profile> profiles = response.body().getCandidates();
                links = new Links();
                links.setLinks(profiles);
                saveLinks(links);
                return links;
            } else {
                APIError error = ErrorUtils.parseError(response);
                throw new ServiceException(error);
            }
        } catch (IOException e) {
            throw new ServiceException(e.getLocalizedMessage());
        }
    }

    public void saveLinks(Links links) {
        this.database.setLinks(links);
    }

    public Database getDatabase(){
        return this.database;
    }
}
