package com.tddp2.grupo2.linkup.service.api;

import com.tddp2.grupo2.linkup.exception.ServiceException;
import com.tddp2.grupo2.linkup.model.AbuseReport;
import com.tddp2.grupo2.linkup.model.Profile;

public abstract class LinkUserService implements LinkupService {
    public ServiceType getType() {
        return ServiceType.LINK_USER;
    }

    public abstract Profile loadUser(String fbid) throws ServiceException;
    public abstract void reportAbuse(AbuseReport abuseReport) throws ServiceException;
}
