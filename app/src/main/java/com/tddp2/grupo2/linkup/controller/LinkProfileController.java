package com.tddp2.grupo2.linkup.controller;

import android.os.Bundle;

import com.tddp2.grupo2.linkup.activity.view.LinkProfileView;
import com.tddp2.grupo2.linkup.model.*;
import com.tddp2.grupo2.linkup.model.Recommendation;
import com.tddp2.grupo2.linkup.service.api.LinkUserService;
import com.tddp2.grupo2.linkup.service.api.LinksService;
import com.tddp2.grupo2.linkup.service.api.MyLinksService;
import com.tddp2.grupo2.linkup.service.factory.ServiceFactory;
import com.tddp2.grupo2.linkup.task.BlockUserTask;
import com.tddp2.grupo2.linkup.task.GetMyLinksTask;
import com.tddp2.grupo2.linkup.task.LoadImagesTask;
import com.tddp2.grupo2.linkup.task.LoadImagesTaskResponse;
import com.tddp2.grupo2.linkup.task.LoadLinkUserTask;
import com.tddp2.grupo2.linkup.task.RecommendLinkTask;
import com.tddp2.grupo2.linkup.task.ReportAbuseTask;
import com.tddp2.grupo2.linkup.task.TaskResponse;
import com.tddp2.grupo2.linkup.task.UpdateImageCacheTask;

import java.util.ArrayList;
import java.util.List;

public class LinkProfileController implements LinkImageControllerInterface, MyLinksControllerInterface {

    private LinkProfileView view;
    private LinksService linksService;
    private LinkUserService linkUserService;
    private MyLinksService myLinksService;
    private MyLinks links;
    private String fbId;
    private Profile profile;

    public void getCoordinatesAndUpdateDistance(){
        Location loggedUserLocation = this.linksService.getDatabase().getProfile().getLocation();
        Location linkLocation = this.profile.getLocation();
        view.updateDistance(loggedUserLocation, linkLocation);
    }

    public void showLinkData(String userId){
        this.fbId = userId;
        LoadLinkUserTask task = new LoadLinkUserTask(this.linkUserService, this);
        task.execute(this.fbId);
    }

    public void initTask() {
        view.showProgress();
    }

    public void finishTask() {
        view.hideProgress();
    }

    public void onResult(Object result) {
        TaskResponse response = (TaskResponse) result;
        if (response.hasError()) {
            view.onError(response.getError());
        } else {
            Profile profile = (Profile) response.getResponse();
            this.profile = profile;
            this.loadImage(profile.getFbid());
            view.showData(profile);
        }
    }

    public void reportAbuse(int idCategory, String comment){
        AbuseReport abuseReport = new AbuseReport();
        Profile reporter = this.linksService.getDatabase().getProfile();
        Profile reported = this.profile;
        abuseReport.setIdReporter(reporter.getFbid());
        abuseReport.setFullnameReporter(reporter.getFirstName() + " " + reporter.getLastName());
        abuseReport.setIdReported(reported.getFbid());
        abuseReport.setFullnameReported(reported.getFirstName() + " " + reported.getLastName());
        abuseReport.setIdCategory(idCategory);
        abuseReport.setComment(comment);
        ReportAbuseTask task = new ReportAbuseTask(linkUserService, this);
        task.execute(abuseReport);
    }

    public void blockUser(){
        Profile bloquer = this.linksService.getDatabase().getProfile();
        Profile bloqued = this.profile;
        Block block = new Block();
        block.setIdBlockerUser(bloquer.getFbid());
        block.setIdBlockedUser(bloqued.getFbid());
        BlockUserTask task = new BlockUserTask(linkUserService, this);
        task.execute(block);
    }

    public String getFbid(){
        return this.fbId;
    }

    public LinkProfileController(LinkProfileView view) {
        this.view = view;
        this.linksService = ServiceFactory.getLinksService();
        this.linkUserService = ServiceFactory.getUserService();
        this.myLinksService = ServiceFactory.getMyLinksService();
    }

    public void loadImage(String fbidCandidate) {
        LoadImagesTask task = new LoadImagesTask(linksService, this);
        task.execute(fbidCandidate, profile.getImages().size());
    }

    @Override
    public void updateImagesFromServer(String fbId) {
        UpdateImageCacheTask task = new UpdateImageCacheTask(linksService, this);
        task.execute(fbId);
    }

    public void initLoadImageTask() {
        view.showLoadingImage();
    }

    public void finishLoadImageTask() {
        view.hideLoadingImage();
    }

    @Override
    public void onLoadImageResult(TaskResponse response) {
        if (response.hasError()) {
                view.showImage(new ArrayList<Bundle>());
        } else {
            List<Bundle> bundles = (List<Bundle>)response.getResponse();
            view.showImage(bundles);
            if (!((LoadImagesTaskResponse)response).alreadyUpdatedFromServer) {
                this.updateImagesFromServer(this.fbId);
            }
        }
    }

    @Override
    public void reloadImages() {
        view.loadUserPictures();
    }

    public void initReportAbuseTask() {
        view.showReportAbuseProgress();
    }

    public void initBlockUserTask() {
        view.showBlockUserProgress();
    }

    public void finishReportAbuseTask() {
        view.hideProgress();
    }

    public void finishBlockUserTask() {
        view.hideProgress();
    }

    public void onReportAbuseTaskResult(Object result) {
        TaskResponse response = (TaskResponse) result;
        if (response.hasError()) {
            view.onReportAbuseFailure();
        } else {
            view.onReportAbuseSuccess();
        }
    }

    public void onBlockUserTaskResult(Object result) {
        TaskResponse response = (TaskResponse) result;
        if (response.hasError()) {
            view.onBlockUserFailure();
        } else {
            view.onBlockUserSuccess();
        }
    }

    @Override
    public void initGetMyLinksTask() {

    }

    @Override
    public void finishGetMyLinksTask() {

    }

    @Override
    public void showInactiveAccountError() {

    }

    @Override
    public void onGetMyLinksResult(TaskResponse response) {
        if (!response.hasError()) {
            links = (MyLinks) response.getResponse();
            view.onFinishLoadMyLinks(links);
        }
    }

    public void loadMyLinks() {
        GetMyLinksTask task = new GetMyLinksTask(myLinksService, this);
        task.execute();
    }

    public void recommendLink(MyLink myLink) {
        RecommendLinkTask task = new RecommendLinkTask(linkUserService, this);
        Recommendation recommendation = new Recommendation();
        recommendation.setIdFromUser(ServiceFactory.getProfileService().getLocalProfile().getFbid());
        recommendation.setIdToUser(myLink.getFbid());
        recommendation.setIdRecommendedUser(this.getFbid());
        task.execute(recommendation);
    }

    public void initRecommendLinkTask() {
    }


    public void finishRecommendLinkTask() {
    }

    public void onRecommendLinkTaskResult(TaskResponse response) {
        if (response.hasError()) {
            view.onRecommendLinkFailure();
        } else {
            view.onRecommendLinkSuccess();
        }
    }
}

