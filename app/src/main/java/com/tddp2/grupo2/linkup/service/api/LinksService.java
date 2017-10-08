package com.tddp2.grupo2.linkup.service.api;

import com.tddp2.grupo2.linkup.exception.ServiceException;
import com.tddp2.grupo2.linkup.infrastructure.Database;
import com.tddp2.grupo2.linkup.model.ImageBitmap;
import com.tddp2.grupo2.linkup.model.Links;
import com.tddp2.grupo2.linkup.task.AcceptLinkTaskResponse;


public abstract class LinksService implements LinkupService{

    protected Database database;

    protected LinksService(Database database) {
        this.database = database;
    }

    public abstract Links getLinks() throws ServiceException;

    public abstract void saveLinks(Links links);

    public abstract Links rejectLink(String fbidCandidate) throws ServiceException;

    public abstract AcceptLinkTaskResponse acceptLink(String fbidCandidate, String tipoDeLink) throws ServiceException;

    public ServiceType getType() {
        return ServiceType.LINKS;
    }

    public abstract Database getDatabase();

    public abstract ImageBitmap loadImage(String fbidCandidate) throws ServiceException;

    public abstract Links removeLink(String fbidCandidate);
}
