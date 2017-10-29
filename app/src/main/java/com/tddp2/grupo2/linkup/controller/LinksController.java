package com.tddp2.grupo2.linkup.controller;

import android.os.AsyncTask;
import com.tddp2.grupo2.linkup.activity.view.LinksView;
import com.tddp2.grupo2.linkup.model.*;
import com.tddp2.grupo2.linkup.service.api.LinksService;
import com.tddp2.grupo2.linkup.service.factory.ServiceFactory;
import com.tddp2.grupo2.linkup.task.*;
import com.tddp2.grupo2.linkup.utils.ImageUtils;

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

    public Link getCurrentLink(){
        return this.links.getLinks().get(currentLink);
    }

    public void loadImage(){
        LoadImageTask task = new LoadImageTask(linksService, this, true);
        Link p = links.getLinks().get(currentLink);
        task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, p.getFbid(), 1);
    }

    public void acceptCurrentLink(String tipoDeLink) {
        AcceptLinkTask task = new AcceptLinkTask(linksService, this);
        Link p = links.getLinks().get(currentLink);
        if (!p.getFbid().equals("")) {
            Profile profile = (Profile) p;
            task.execute(profile.getFbid(), profile.getFirstName(), tipoDeLink);
        }
    }

    public void rejectCurrentLink(){
        RejectLinkTask task = new RejectLinkTask(linksService, this);
        Link p = links.getLinks().get(currentLink);
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
        Link profile = links.getLinks().get(currentLink);
        this.showLink(profile, currentLink);
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
        Link profile = links.getLinks().get(currentLink);
        this.showLink(profile, currentLink);
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
            Link profile = links.getLinks().get(0);
            this.currentLink = 0;
            this.showLink(profile, currentLink);
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
                Link profile = links.getLinks().get(links.getLinks().size()-1);
                this.currentLink = links.getLinks().size()-1;
                this.showLink(profile, currentLink);
            }else{
                //si el current no era el ultimo
                Link profile = links.getLinks().get(currentLink);
                this.showLink(profile, currentLink);
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
            Link profile = links.getLinks().get(links.getLinks().size()-1);
            this.currentLink = links.getLinks().size()-1;
            this.showLink(profile, currentLink);
        }else{
            //si el current no era el ultimo
            Link profile = links.getLinks().get(currentLink);
            this.showLink(profile, currentLink);
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
            if ((currentFbid != null) && (currentFbid.equals(image.getImageId()))) {
                view.hideLoadingImage();
                view.showImage(image.getBitmap());
            }
        }
    }

    @Override
    public void reloadImages() {
        //no se utiliza aqui
    }

    @Override
    public void updateImagesFromServer(String linkUserId) {
        //no se utiliza aqui
    }

    public void updateDistance() {
        Link link = this.links.getLinks().get(currentLink);
        if (!link.getFbid().equals("")) {
            Profile profile = (Profile) link;
            view.updateDistance(
                    this.linksService.getDatabase().getProfile().getLocation(),
                    profile.getLocation()
            );
        }

    }

    public void showInactiveAccountError() {
        view.showInactiveAccountAlert();
    }

    private void showLink(Link link, int index) {
        if (link.getFbid().equals("")) {
            Advertisement advertisement = (Advertisement) link;
            view.showAdvertisement(advertisement.getAdvertiser(), advertisement.getUrl());
            view.hideLoadingImage();
            view.showImage(ImageUtils.base64ToBitmap(advertisement.getImage()));
        } else {
            Profile profile = (Profile) link;
            view.showLink(profile, index);
        }
    }
}
