package com.tddp2.grupo2.linkup.controller;

import com.tddp2.grupo2.linkup.activity.view.LinkProfileView;
import com.tddp2.grupo2.linkup.service.api.LinkUserService;
import com.tddp2.grupo2.linkup.service.api.LinksService;
import com.tddp2.grupo2.linkup.service.factory.ServiceFactory;
import com.tddp2.grupo2.linkup.task.LoadImageTask;

public abstract class AbstractLinkProfileController implements LinkImageControllerInterface {
    protected LinkProfileView view;
    protected LinksService linksService;
    protected LinkUserService linkUserService;

    public abstract void getCoordinatesAndUpdateDistance();
    public abstract void showLinkData(String userId);

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
}

