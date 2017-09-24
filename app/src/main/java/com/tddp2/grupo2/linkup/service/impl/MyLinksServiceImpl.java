package com.tddp2.grupo2.linkup.service.impl;

import com.tddp2.grupo2.linkup.exception.APIError;
import com.tddp2.grupo2.linkup.exception.ServiceException;
import com.tddp2.grupo2.linkup.infrastructure.Database;
import com.tddp2.grupo2.linkup.infrastructure.LinkupClient;
import com.tddp2.grupo2.linkup.model.MyLinks;
import com.tddp2.grupo2.linkup.model.Profile;
import com.tddp2.grupo2.linkup.service.api.ClientService;
import com.tddp2.grupo2.linkup.service.api.MyLinksService;
import com.tddp2.grupo2.linkup.utils.ErrorUtils;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Response;


public class MyLinksServiceImpl extends MyLinksService {

    private ClientService clientService;

    public MyLinksServiceImpl(Database database, ClientService clientService) {
        super(database);
        this.clientService = clientService;
    }

    /*@Override
    public MyLinks getMyLinks() throws ServiceException {
        Profile profile = this.database.getProfile();
        String fbid = profile.getFbid();
        Links links = this.database.getLinks();
        MyLinks myLinks = new MyLinks();
        if (links == null) return myLinks;

        List<MyLink> mylinks = new ArrayList<MyLink>();
        List<Profile> profiles = links.getLinks();
        for (Profile p : profiles){
            mylinks.add(new MyLink(p.getFbid(), p.getFirstName(), p.getBirthday(), p.getGender(), R.drawable.user));
        }
        myLinks.setLinks(mylinks);

        return myLinks;
    }*/



    @Override
    public MyLinks getMyLinks() throws ServiceException {
        Profile profile = this.database.getProfile();
        String fbid = profile.getFbid();

        LinkupClient linkupClient = clientService.getClient();
        Call<MyLinks> call = linkupClient.matches.getMatches(fbid);
        try {
            Response<MyLinks> response = call.execute();
            if (response.isSuccessful()) {
                //Save Links
                MyLinks mylinks = response.body();

                saveMyLinks(mylinks);
                return mylinks;
            } else {
                APIError error = ErrorUtils.parseError(response);
                throw new ServiceException(error);
            }
        } catch (IOException e) {
            throw new ServiceException(e.getLocalizedMessage());
        }
    }

    public void saveMyLinks(MyLinks mylinks) {

        this.database.setMyLinks(mylinks);
    }

    public Database getDatabase(){
        return this.database;
    }

}
