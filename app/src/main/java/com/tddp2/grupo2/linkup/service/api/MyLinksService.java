package com.tddp2.grupo2.linkup.service.api;

import com.tddp2.grupo2.linkup.exception.InactiveAccountException;
import com.tddp2.grupo2.linkup.exception.ServiceException;
import com.tddp2.grupo2.linkup.infrastructure.Database;
import com.tddp2.grupo2.linkup.model.MyLinks;


public abstract class MyLinksService implements LinkupService{

    protected Database database;

    protected MyLinksService(Database database) {
        this.database = database;
    }

    public abstract MyLinks getMyLinks() throws ServiceException, InactiveAccountException;

    public abstract void saveMyLinks(MyLinks mylinks);

    public ServiceType getType() {
        return ServiceType.MY_LINKS;
    }

    public abstract Database getDatabase();

}
