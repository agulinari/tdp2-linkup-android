package com.tddp2.grupo2.linkup.controller;

import com.tddp2.grupo2.linkup.activity.view.LinkProfileView;
import com.tddp2.grupo2.linkup.model.AbuseReport;
import com.tddp2.grupo2.linkup.model.Block;
import com.tddp2.grupo2.linkup.model.ImageBitmap;
import com.tddp2.grupo2.linkup.model.Location;
import com.tddp2.grupo2.linkup.model.MyLink;
import com.tddp2.grupo2.linkup.model.MyLinks;
import com.tddp2.grupo2.linkup.model.Profile;
import com.tddp2.grupo2.linkup.model.Recommend;
import com.tddp2.grupo2.linkup.service.api.LinkUserService;
import com.tddp2.grupo2.linkup.service.api.LinksService;
import com.tddp2.grupo2.linkup.service.api.MyLinksService;
import com.tddp2.grupo2.linkup.service.factory.ServiceFactory;
import com.tddp2.grupo2.linkup.task.BlockUserTask;
import com.tddp2.grupo2.linkup.task.GetMyLinksTask;
import com.tddp2.grupo2.linkup.task.LoadImageTask;
import com.tddp2.grupo2.linkup.task.LoadLinkUserTask;
import com.tddp2.grupo2.linkup.task.RecommendLinkTask;
import com.tddp2.grupo2.linkup.task.ReportAbuseTask;
import com.tddp2.grupo2.linkup.task.TaskResponse;

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
        LoadImageTask task = new LoadImageTask(linksService, this);
        task.execute(fbidCandidate);
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
            if (this.fbId.equals(response.getError())){
                view.showImage(null);
            }
        } else {
            ImageBitmap image = (ImageBitmap)response.getResponse();
            if (this.fbId.equals(image.getImageId())) {
                view.showImage(image.getBitmap());
            }
        }
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
        Recommend recommend = new Recommend();
        recommend.setIdReceiverUser(myLink.getFbid());
        recommend.setIdRecommendedUser(this.getFbid());
        task.execute(recommend);
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

