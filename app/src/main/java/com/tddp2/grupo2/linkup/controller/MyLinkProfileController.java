package com.tddp2.grupo2.linkup.controller;

import com.tddp2.grupo2.linkup.activity.view.LinkProfileView;
import com.tddp2.grupo2.linkup.model.Profile;
import com.tddp2.grupo2.linkup.service.api.LinkUserService;
import com.tddp2.grupo2.linkup.service.factory.ServiceFactory;
import com.tddp2.grupo2.linkup.task.LoadLinkUserTask;
import com.tddp2.grupo2.linkup.task.TaskResponse;

public class MyLinkProfileController implements LinkImageControllerInterface {

    private LinkUserService linkUserService;
    private LinkProfileView view;

    public MyLinkProfileController(LinkProfileView view) {
        this.linkUserService = ServiceFactory.getUserService();
        this.view = view;
    }

    public void loadUser(String linkUserId) {
        LoadLinkUserTask task = new LoadLinkUserTask(this.linkUserService, this);
        task.execute(linkUserId);
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
            view.showData(profile);
        }
    }

    public void initLoadImageTask() {}
    public void finishLoadImageTask() {}
    public void onLoadImageResult(TaskResponse response) {}
}
