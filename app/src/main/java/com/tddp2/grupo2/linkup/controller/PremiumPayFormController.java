package com.tddp2.grupo2.linkup.controller;

import com.tddp2.grupo2.linkup.activity.view.BaseView;
import com.tddp2.grupo2.linkup.service.api.ProfileService;
import com.tddp2.grupo2.linkup.service.factory.ServiceFactory;
import com.tddp2.grupo2.linkup.task.TaskResponse;
import com.tddp2.grupo2.linkup.task.UpgradeAccountTypeTask;

public class PremiumPayFormController {

    private ProfileService profileService;
    private BaseView view;

    public PremiumPayFormController(BaseView view) {
        this.profileService = ServiceFactory.getProfileService();
        this.view = view;
    }

    public void upgradeAccount() {
        UpgradeAccountTypeTask task = new UpgradeAccountTypeTask(this.profileService, this);
        task.execute();
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
            view.goToNext();
        }
    }
}
