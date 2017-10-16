package com.tddp2.grupo2.linkup.controller;

import com.tddp2.grupo2.linkup.activity.view.LinkProfileView;
import com.tddp2.grupo2.linkup.model.MyLinks;
import com.tddp2.grupo2.linkup.service.api.LinkUserService;
import com.tddp2.grupo2.linkup.service.api.LinksService;
import com.tddp2.grupo2.linkup.service.api.MyLinksService;
import com.tddp2.grupo2.linkup.service.factory.ServiceFactory;
import com.tddp2.grupo2.linkup.task.GetMyLinksTask;
import com.tddp2.grupo2.linkup.task.LoadImageTask;
import com.tddp2.grupo2.linkup.task.TaskResponse;

public abstract class AbstractLinkProfileController implements LinkImageControllerInterface, MyLinksControllerInterface {
    protected LinkProfileView view;
    protected LinksService linksService;
    protected LinkUserService linkUserService;
    protected MyLinksService myLinksService;
    private MyLinks links;

    public abstract void getCoordinatesAndUpdateDistance();
    public abstract void showLinkData(String userId);
    public abstract void reportAbuse(int idCategory, String comment);
    public abstract void blockUser();

    public AbstractLinkProfileController(LinkProfileView view) {
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
}

