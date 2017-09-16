package com.tddp2.grupo2.linkup.service.api;

import com.tddp2.grupo2.linkup.exception.ServiceException;
import com.tddp2.grupo2.linkup.infrastructure.Database;
import com.tddp2.grupo2.linkup.model.Links;
import com.tddp2.grupo2.linkup.model.MyLink;

import java.util.List;


public abstract class MyLinksService implements LinkupService{

    protected Database database;

    protected MyLinksService(Database database) {
        this.database = database;
    }

    public abstract List<MyLink> getMyLinks() throws ServiceException;

    public abstract void saveMyLinks(List<MyLink> mylinks);

    public ServiceType getType() {
        return ServiceType.MY_LINKS;
    }

    public abstract Database getDatabase();

}
