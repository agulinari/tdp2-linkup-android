package com.tddp2.grupo2.linkup.controller;

import com.tddp2.grupo2.linkup.activity.view.LinkProfileView;
import com.tddp2.grupo2.linkup.model.*;
import com.tddp2.grupo2.linkup.task.BlockUserTask;
import com.tddp2.grupo2.linkup.task.LoadLinkUserTask;
import com.tddp2.grupo2.linkup.task.ReportAbuseTask;
import com.tddp2.grupo2.linkup.task.TaskResponse;

public class MyLinkProfileController extends AbstractLinkProfileController {
    private String fbId;
    private Profile profile;

    public MyLinkProfileController(LinkProfileView view) {
        super(view);
    }

    @Override
    public void showLinkData(String linkUserId) {
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

    @Override
    public void getCoordinatesAndUpdateDistance() {
        Location loggedUserLocation = this.linksService.getDatabase().getProfile().getLocation();
        Location linkLocation = this.profile.getLocation();
        view.updateDistance(loggedUserLocation, linkLocation);
    }

    @Override
    public void reportAbuse(int idCategory, String comment) {
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

    @Override
    public void blockUser() {
        Profile bloquer = this.linksService.getDatabase().getProfile();
        Profile bloqued = this.profile;
        Block block = new Block();
        block.setIdBlockerUser(bloquer.getFbid());
        block.setIdBlockedUser(bloqued.getFbid());
        BlockUserTask task = new BlockUserTask(linkUserService, this);
        task.execute(block);
    }

    @Override
    public String getFbid() {
        return this.fbId;
    }
}
