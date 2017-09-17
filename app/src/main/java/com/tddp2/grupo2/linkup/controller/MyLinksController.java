package com.tddp2.grupo2.linkup.controller;

import android.util.Log;

import com.tddp2.grupo2.linkup.R;
import com.tddp2.grupo2.linkup.activity.MyLinksFragment;
import com.tddp2.grupo2.linkup.exception.ServiceException;
import com.tddp2.grupo2.linkup.model.MyLink;
import com.tddp2.grupo2.linkup.service.api.MyLinksService;
import com.tddp2.grupo2.linkup.service.factory.ServiceFactory;

import java.util.ArrayList;
import java.util.List;



public class MyLinksController {
    private final MyLinksService myLinksService;

    public MyLinksController(MyLinksFragment myLinksFragment) {
        this.myLinksService = ServiceFactory.getMyLinksService();
    }

    public List<MyLink> getMyLinks() {

        try {
            return myLinksService.getMyLinks();
        } catch (ServiceException e) {
            Log.e("MyLinksController",e.getLocalizedMessage());
            return new ArrayList<MyLink>();
        }
    }

}
