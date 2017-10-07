package com.tddp2.grupo2.linkup.controller;

import com.tddp2.grupo2.linkup.activity.view.LinkProfileView;
import com.tddp2.grupo2.linkup.model.AbuseReport;
import com.tddp2.grupo2.linkup.model.ImageBitmap;
import com.tddp2.grupo2.linkup.model.Location;
import com.tddp2.grupo2.linkup.model.Profile;
import com.tddp2.grupo2.linkup.task.TaskResponse;

public class LinkProfileController extends AbstractLinkProfileController {
    private int currentLinkIndex;

    public LinkProfileController(LinkProfileView view) {
        super(view);
    }

    @Override
    public void showLinkData(String currentLinkIndex) {
        this.currentLinkIndex = Integer.decode(currentLinkIndex);
        Profile profile = this.linksService.getDatabase().getLinks().getLinks().get(this.currentLinkIndex);
        view.showData(profile);

        loadImage(profile.getFbid());
    }

    public void onLoadImageResult(TaskResponse response) {
        String currentFbid = this.linksService.getDatabase().getLinks().getLinks().get(this.currentLinkIndex).getFbid();
        if (response.hasError()) {
            if (currentFbid.equals(response.getError())){
                view.showImage(null);
            }
        } else {
            ImageBitmap image = (ImageBitmap)response.getResponse();
            if (currentFbid.equals(image.getImageId())) {
                view.showImage(image.getBitmap());
            }
        }
    }

    @Override
    public void getCoordinatesAndUpdateDistance() {
        Location loggedUserLocation = this.linksService.getDatabase().getProfile().getLocation();
        Location linkLocation = this.linksService.getDatabase().getLinks().getLinks().get(this.currentLinkIndex).getLocation();
        view.updateDistance(loggedUserLocation, linkLocation);
    }

    @Override
    public void reportAbuse() {
        AbuseReport abuseReport = new AbuseReport();
        Profile reporter = this.linksService.getDatabase().getProfile();
        Profile reported = this.linksService.getDatabase().getLinks().getLinks().get(this.currentLinkIndex);
        abuseReport.setIdReporter(reporter.getFbid());
        abuseReport.setFullnameReporter(reporter.getFirstName() + " " + reporter.getLastName());
        abuseReport.setIdReported(reported.getFbid());
        abuseReport.setFullnameReported(reported.getFirstName() + " " + reported.getLastName());
        abuseReport.setIdCategory(1);//FIXME
        abuseReport.setComment("un comnetario");//FIXME
    }

    @Override
    public void blockUser() {

    }
}
