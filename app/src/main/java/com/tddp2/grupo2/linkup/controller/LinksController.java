package com.tddp2.grupo2.linkup.controller;

import com.tddp2.grupo2.linkup.activity.view.LinksView;
import com.tddp2.grupo2.linkup.model.ImageBitmap;
import com.tddp2.grupo2.linkup.model.Links;
import com.tddp2.grupo2.linkup.model.Profile;
import com.tddp2.grupo2.linkup.service.api.LinksService;
import com.tddp2.grupo2.linkup.service.factory.ServiceFactory;
import com.tddp2.grupo2.linkup.task.AcceptLinkTask;
import com.tddp2.grupo2.linkup.task.AcceptLinkTaskResponse;
import com.tddp2.grupo2.linkup.task.GetLinksTask;
import com.tddp2.grupo2.linkup.task.LoadImageTask;
import com.tddp2.grupo2.linkup.task.RejectLinkTask;
import com.tddp2.grupo2.linkup.task.TaskResponse;

public class LinksController implements LinkImageControllerInterface {

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

    public void loadImage(){
        LoadImageTask task = new LoadImageTask(linksService, this);
        Profile p = links.getLinks().get(currentLink);
        task.execute(p.getFbid());
    }

    public void acceptCurrentLink(String tipoDeLink) {
        AcceptLinkTask task = new AcceptLinkTask(linksService, this);
        Profile p = links.getLinks().get(currentLink);
        task.execute(p.getFbid(), p.getFirstName(), tipoDeLink);
    }

    public void rejectCurrentLink(){
        RejectLinkTask task = new RejectLinkTask(linksService, this);
        Profile p = links.getLinks().get(currentLink);
        task.execute(p.getFbid());
    }

    public void previousLink(){
        if (links==null || links.getLinks().isEmpty()){
            return;
        }
        if (currentLink == 0){
            currentLink = links.getLinks().size()-1;
        }else{
            currentLink--;
        }
        Profile profile = links.getLinks().get(currentLink);
        view.showLink(profile, currentLink);
    }

    public void nextLink(){
        if (links==null || links.getLinks().isEmpty()){
            return;
        }
        if (currentLink == (links.getLinks().size()-1)){
            currentLink = 0;
        }else{
            currentLink++;
        }
        Profile profile = links.getLinks().get(currentLink);
        view.showLink(profile, currentLink);
    }

    public void initGetLinksTask() {
        view.showProgress();
    }

    public void finishGetLinksTask() {
        view.hideProgress();
    }

    public void initRejectTask() {
        view.disableActions();
    }

    public void finishRejectTask() {
        view.enableActions();
    }

    public void initAcceptTask() {
        view.disableActions();
    }

    public void finishAcceptTask() {
        view.enableActions();
    }

    public void onGetLinksResult(TaskResponse response) {

        if (response.hasError()) {
            view.onError(response.getError());
        }
        //view.goToNext();
        links = (Links) response.getResponse();
        if (!links.getLinks().isEmpty()){
            Profile profile = links.getLinks().get(0);
            this.currentLink = 0;
            view.showLink(profile, currentLink);
        }else{
            view.showEmptyLinks();
        }

    }

    public void onRejectResult(TaskResponse response) {

        if (response.hasError()) {
            view.onError(response.getError());
        } else {
            links = (Links) response.getResponse();
            if (links.getLinks().isEmpty()){
                //si no hay mas candidatos
                view.showEmptyLinks();
            }
            else if (links.getLinks().size()<=currentLink){
                //si el current era el ultimo
                Profile profile = links.getLinks().get(links.getLinks().size()-1);
                this.currentLink = links.getLinks().size()-1;
                view.showLink(profile, currentLink);
            }else{
                //si el current no era el ultimo
                Profile profile = links.getLinks().get(currentLink);
                view.showLink(profile, currentLink);
            }
        }
    }

    public void onAcceptResult(TaskResponse response) {
        AcceptLinkTaskResponse acceptLinkTaskResponse = (AcceptLinkTaskResponse) response;

        if (response.hasError()) {
            view.onError(response.getError());
        } else {
            if (acceptLinkTaskResponse.isAMatch()) {
                view.showMatch(acceptLinkTaskResponse.getMatchName());
            } else {
                links = acceptLinkTaskResponse.getLinks();
                gotoNextLink();
            }
        }
    }

    public void gotoNextLink() {
        if (links.getLinks().isEmpty()){
            //si no hay mas candidatos
            view.showEmptyLinks();
        }
        else if (links.getLinks().size()<=currentLink){
            //si el current era el ultimo
            Profile profile = links.getLinks().get(links.getLinks().size()-1);
            this.currentLink = links.getLinks().size()-1;
            view.showLink(profile, currentLink);
        }else{
            //si el current no era el ultimo
            Profile profile = links.getLinks().get(currentLink);
            view.showLink(profile, currentLink);
        }
    }

    public void initLoadImageTask() {
        //view.blockCandidatesNavigation();
        view.showLoadingImage();
    }

    public void finishLoadImageTask() {
        //view.registerNextAndPreviousListeners();
        //view.hideLoadingImage();
    }

    public void onLoadImageResult(TaskResponse response) {
        String currentFbid = links.getLinks().get(currentLink).getFbid();
        if (response.hasError()) {
            if (currentFbid.equals(response.getError())){
                view.showImage(null);
            }

        } else {
            ImageBitmap image = (ImageBitmap)response.getResponse();
            if (currentFbid.equals(image.getImageId())) {
                view.hideLoadingImage();
                view.showImage(image.getBitmap());
            }
        }
    }

    public void updateDistance() {
        view.updateDistance(
                this.linksService.getDatabase().getProfile().getLocation(),
                this.links.getLinks().get(currentLink).getLocation()
        );
    }
}
