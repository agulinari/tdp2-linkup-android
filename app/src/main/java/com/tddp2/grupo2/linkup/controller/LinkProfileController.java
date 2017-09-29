package com.tddp2.grupo2.linkup.controller;

import com.tddp2.grupo2.linkup.LinkProfileView;
import com.tddp2.grupo2.linkup.service.api.LinksService;
import com.tddp2.grupo2.linkup.service.factory.ServiceFactory;

public class LinkProfileController {

    private LinksService linksService;
    private LinkProfileView view;

    public LinkProfileController(LinkProfileView view) {
        this.linksService = ServiceFactory.getLinksService();
        this.view = view;
    }
}
