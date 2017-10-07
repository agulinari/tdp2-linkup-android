package com.tddp2.grupo2.linkup.controller;

import com.tddp2.grupo2.linkup.activity.view.LinkProfileView;
import com.tddp2.grupo2.linkup.model.ImageBitmap;
import com.tddp2.grupo2.linkup.model.Location;
import com.tddp2.grupo2.linkup.model.Profile;
import com.tddp2.grupo2.linkup.service.api.LinkUserService;
import com.tddp2.grupo2.linkup.service.factory.ServiceFactory;
import com.tddp2.grupo2.linkup.task.LoadLinkUserTask;
import com.tddp2.grupo2.linkup.task.TaskResponse;

public class MyLinkProfileController extends AbstractLinkProfileController {
    private LinkUserService linkUserService;
    private String fbId;
    private Profile profile;

    public MyLinkProfileController(LinkProfileView view) {
        super(view);
        this.linkUserService = ServiceFactory.getUserService();
    }

    public void loadUser(String linkUserId) {
        this.fbId = linkUserId;
        LoadLinkUserTask task = new LoadLinkUserTask(this.linkUserService, this);
        task.execute(this.fbId);
        loadImage(this.fbId);
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

    public void getCoordinatesAndUpdateDistance() {
        Location loggedUserLocation = this.linksService.getDatabase().getProfile().getLocation();
        Location linkLocation = this.profile.getLocation();
        view.updateDistance(loggedUserLocation, linkLocation);
    }
}
