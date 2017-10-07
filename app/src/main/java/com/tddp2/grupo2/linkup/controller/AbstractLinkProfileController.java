package com.tddp2.grupo2.linkup.controller;

import com.tddp2.grupo2.linkup.activity.view.LinkProfileView;
import com.tddp2.grupo2.linkup.service.api.LinkUserService;
import com.tddp2.grupo2.linkup.service.api.LinksService;
import com.tddp2.grupo2.linkup.service.factory.ServiceFactory;
import com.tddp2.grupo2.linkup.task.LoadImageTask;
import com.tddp2.grupo2.linkup.task.TaskResponse;

public abstract class AbstractLinkProfileController implements LinkImageControllerInterface {
    protected LinkProfileView view;
    protected LinksService linksService;
    protected LinkUserService linkUserService;

    public abstract void getCoordinatesAndUpdateDistance();
    public abstract void showLinkData(String userId);
    public abstract void reportAbuse();
    public abstract void blockUser();

    public AbstractLinkProfileController(LinkProfileView view) {
        this.view = view;
        this.linksService = ServiceFactory.getLinksService();
        this.linkUserService = ServiceFactory.getUserService();
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

    public void onReportAbuseTaskResult(Object result) {
        TaskResponse response = (TaskResponse) result;
        if (response.hasError()) {
            view.onReportAbuseFailure();
        } else {
            view.onReportAbuseSuccess();
        }
    }
}

