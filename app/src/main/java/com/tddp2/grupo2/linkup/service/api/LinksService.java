package com.tddp2.grupo2.linkup.service.api;

import com.tddp2.grupo2.linkup.exception.ServiceException;
import com.tddp2.grupo2.linkup.infrastructure.Database;
import com.tddp2.grupo2.linkup.model.Links;
import com.tddp2.grupo2.linkup.model.Profile;

import java.util.List;


public abstract class LinksService implements LinkupService{

    protected Database database;

    protected LinksService(Database database) {
        this.database = database;
    }

    public abstract Links getLinks() throws ServiceException;

    public abstract void saveLinks(Links links);

    public ServiceType getType() {
        return ServiceType.LINKS;
    }

    public abstract Database getDatabase();

}
