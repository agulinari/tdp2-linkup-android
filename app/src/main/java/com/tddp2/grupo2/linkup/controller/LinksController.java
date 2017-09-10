package com.tddp2.grupo2.linkup.controller;

import com.tddp2.grupo2.linkup.BaseView;
import com.tddp2.grupo2.linkup.LinksView;
import com.tddp2.grupo2.linkup.model.Links;
import com.tddp2.grupo2.linkup.model.Profile;
import com.tddp2.grupo2.linkup.model.Settings;
import com.tddp2.grupo2.linkup.service.api.LinksService;
import com.tddp2.grupo2.linkup.service.api.ProfileService;
import com.tddp2.grupo2.linkup.service.factory.ServiceFactory;
import com.tddp2.grupo2.linkup.task.CreateProfileTask;
import com.tddp2.grupo2.linkup.task.GetLinksTask;
import com.tddp2.grupo2.linkup.task.TaskResponse;
import com.tddp2.grupo2.linkup.task.UpdateSettingsTask;

import static com.tddp2.grupo2.linkup.R.drawable.profile;

public class LinksController {

    private LinksService linksService;
    private LinksView view;
    private Links links;
    private int currentLink;

    public LinksController(LinksView view) {
        this.linksService = ServiceFactory.getLinksService();
        this.view = view;
        this.currentLink = -1;
    }


    public void getLinks() {
        GetLinksTask task = new GetLinksTask(linksService, this);
        task.execute();

    }

    public void previousLink(){
        if (links.getLinks().isEmpty()){
            return;
        }
        if (currentLink == 0){
            currentLink = links.getLinks().size()-1;
        }else{
            currentLink--;
        }
        Profile profile = links.getLinks().get(currentLink);
        view.showLink(profile);
    }

    public void nextLink(){
        if (links.getLinks().isEmpty()){
            return;
        }
        if (currentLink == (links.getLinks().size()-1)){
            currentLink = 0;
        }else{
            currentLink++;
        }
        Profile profile = links.getLinks().get(currentLink);
        view.showLink(profile);
    }

    public void initTask() {
        view.showProgress();
    }

    public void finishTask() {
        view.hideProgress();
    }

    public void onResult(Object result) {
        TaskResponse response = (TaskResponse) result;
        if (response.sessionExpired()) {
            view.sessionExpired();
        } else if (response.hasError()) {
            view.onError(response.getError());
        } else {
            //view.goToNext();
            links = (Links) response.getResponse();
            if (!links.getLinks().isEmpty()){
                Profile profile = links.getLinks().get(0);
                this.currentLink = 0;
                view.showLink(profile);
            }else{
                view.showEmptyLinks();
            }
        }
    }

}
