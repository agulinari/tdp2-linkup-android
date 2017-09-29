package com.tddp2.grupo2.linkup.controller;

import android.graphics.Bitmap;
import com.tddp2.grupo2.linkup.LinkProfileView;
import com.tddp2.grupo2.linkup.LinkupApplication;
import com.tddp2.grupo2.linkup.model.Profile;
import com.tddp2.grupo2.linkup.service.api.LinksService;
import com.tddp2.grupo2.linkup.service.factory.ServiceFactory;

public class LinkProfileController {

    private LinksService linksService;
    private LinkProfileView view;

    public LinkProfileController(LinkProfileView view) {
        this.linksService = ServiceFactory.getLinksService();
        this.view = view;
    }

    public void showLinkData(int currentLink) {
        Profile profile = this.linksService.getDatabase().getLinks().getLinks().get(currentLink);
        view.showData(profile);
    }
}
