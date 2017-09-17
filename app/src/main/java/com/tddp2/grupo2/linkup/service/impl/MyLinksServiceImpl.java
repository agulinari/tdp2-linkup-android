package com.tddp2.grupo2.linkup.service.impl;

import com.tddp2.grupo2.linkup.R;
import com.tddp2.grupo2.linkup.exception.APIError;
import com.tddp2.grupo2.linkup.exception.ServiceException;
import com.tddp2.grupo2.linkup.infrastructure.Database;
import com.tddp2.grupo2.linkup.infrastructure.LinkupClient;
import com.tddp2.grupo2.linkup.model.Links;
import com.tddp2.grupo2.linkup.model.MyLink;
import com.tddp2.grupo2.linkup.model.Profile;
import com.tddp2.grupo2.linkup.service.api.ClientService;
import com.tddp2.grupo2.linkup.service.api.MyLinksService;
import com.tddp2.grupo2.linkup.utils.ErrorUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;


public class MyLinksServiceImpl extends MyLinksService {

    private ClientService clientService;

    public MyLinksServiceImpl(Database database, ClientService clientService) {
        super(database);
        this.clientService = clientService;
    }

    @Override
    public List<MyLink> getMyLinks() throws ServiceException {
        Links links = this.database.getLinks();
        if (links == null) return new ArrayList<MyLink>();

        List<MyLink> l = new ArrayList<MyLink>();
        List<Profile> mylinks = links.getLinks();
        for (Profile p : mylinks){
            l.add(new MyLink(p.getFbid(),p.getFirstName(), p.getBirthday(), R.drawable.user));
        }
        return l;
    }

    /*@Override
    public List<MyLink> getMyLinks() throws ServiceException {
        Profile profile = this.database.getProfile();
        String fbid = profile.getFbid();

        LinkupClient linkupClient = clientService.getClient();
        Call<List<MyLink>> call = linkupClient.profiles.getMyLinks(fbid);
        try {
            Response<List<MyLink>> response = call.execute();
            if (response.isSuccessful()) {
                //Save Links
                List<MyLink> mylinks = response.body();

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
*/
    public void saveMyLinks(List<MyLink> mylinks) {

        //this.database.setMyLinks(mylinks);
    }

    public Database getDatabase(){
        return this.database;
    }

}
