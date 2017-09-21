package com.tddp2.grupo2.linkup.service.impl;

import com.tddp2.grupo2.linkup.exception.APIError;
import com.tddp2.grupo2.linkup.exception.ServiceException;
import com.tddp2.grupo2.linkup.infrastructure.Database;
import com.tddp2.grupo2.linkup.infrastructure.LinkupClient;
import com.tddp2.grupo2.linkup.infrastructure.client.request.AcceptanceRequest;
import com.tddp2.grupo2.linkup.infrastructure.client.request.RejectionRequest;
import com.tddp2.grupo2.linkup.infrastructure.client.response.AcceptanceResponse;
import com.tddp2.grupo2.linkup.infrastructure.client.response.CandidatesResponse;
import com.tddp2.grupo2.linkup.infrastructure.client.response.RejectionResponse;
import com.tddp2.grupo2.linkup.model.Acceptance;
import com.tddp2.grupo2.linkup.model.Links;
import com.tddp2.grupo2.linkup.model.Profile;
import com.tddp2.grupo2.linkup.model.Rejection;
import com.tddp2.grupo2.linkup.service.api.ClientService;
import com.tddp2.grupo2.linkup.service.api.LinksService;
import com.tddp2.grupo2.linkup.task.AcceptLinkTaskResponse;
import com.tddp2.grupo2.linkup.utils.ErrorUtils;
import retrofit2.Call;
import retrofit2.Response;

import java.io.IOException;
import java.util.List;


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

    @Override
    public Links rejectLink(String fbidCandidate) throws ServiceException {
        Profile profile = this.database.getProfile();
        String fbid = profile.getFbid();

        LinkupClient linkupClient = clientService.getClient();
        Rejection rej = new Rejection(fbid, fbidCandidate);
        RejectionRequest request = new RejectionRequest(rej);
        Call<RejectionResponse> call = linkupClient.candidates.reject(request);
        try {
            Response<RejectionResponse> response = call.execute();
            if (response.isSuccessful()) {
                //Elimino el link localmente
                return removeLink(fbidCandidate);
            } else {
                APIError error = ErrorUtils.parseError(response);
                throw new ServiceException(error);
            }
        } catch (IOException e) {
            throw new ServiceException(e.getLocalizedMessage());
        }
    }

    @Override
    public AcceptLinkTaskResponse acceptLink(String fbidCandidate) throws ServiceException {
        Profile profile = this.database.getProfile();
        String fbid = profile.getFbid();

        LinkupClient linkupClient = clientService.getClient();
        Acceptance acceptance = new Acceptance(fbid, fbidCandidate);
        AcceptanceRequest request = new AcceptanceRequest(acceptance);
        Call<AcceptanceResponse> call = linkupClient.candidates.accept(request);
        try {
            Response<AcceptanceResponse> response = call.execute();
            if (response.isSuccessful()) {
                //Elimino el link localmente
                Links filteredLinks = removeLink(fbidCandidate);
                Boolean isAMatch = response.body().getMatch();
                AcceptLinkTaskResponse acceptLinkTaskResponse = new AcceptLinkTaskResponse();
                acceptLinkTaskResponse.setIsAMatch(isAMatch);
                acceptLinkTaskResponse.setLinks(filteredLinks);
                return acceptLinkTaskResponse;
            } else {
                APIError error = ErrorUtils.parseError(response);
                throw new ServiceException(error);
            }
        } catch (IOException e) {
            throw new ServiceException(e.getLocalizedMessage());
        }
    }

    private Links removeLink(String fbidCandidate) {
        Links links = database.getLinks();
        for (Profile p : links.getLinks()){
            if (p.getFbid().equals(fbidCandidate)){
                links.getLinks().remove(p);
                break;
            }
        }
        database.setLinks(links);
        return links;
    }

    public Database getDatabase(){
        return this.database;
    }
}
