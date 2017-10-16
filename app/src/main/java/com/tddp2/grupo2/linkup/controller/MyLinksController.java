package com.tddp2.grupo2.linkup.controller;

import com.tddp2.grupo2.linkup.activity.view.MyLinksView;
import com.tddp2.grupo2.linkup.model.MyLinks;
import com.tddp2.grupo2.linkup.service.api.MyLinksService;
import com.tddp2.grupo2.linkup.service.factory.ServiceFactory;
import com.tddp2.grupo2.linkup.task.GetMyLinksTask;
import com.tddp2.grupo2.linkup.task.TaskResponse;


public class MyLinksController implements MyLinksControllerInterface{
    private final MyLinksService myLinksService;
    private MyLinksView view;
    private MyLinks links;

    public MyLinksController(MyLinksView view) {
        this.myLinksService = ServiceFactory.getMyLinksService();
        this.view = view;
    }

    public void getMyLinks() {

        GetMyLinksTask task = new GetMyLinksTask(myLinksService, this);
        task.execute();
    }

    public void onGetMyLinksResult(TaskResponse response) {
        if (response.hasError()) {
            view.onError(response.getError());
        }

        links = (MyLinks) response.getResponse();
        view.showMyLinks(links);

    }

    public void finishGetMyLinksTask() {
    }

    public void initGetMyLinksTask() {
    }

    public void showInactiveAccountError() {
        view.hideChatAndNewLinksProgress();
        view.showInactiveAccountAlert();
    }
}
