package com.tddp2.grupo2.linkup.service.api;


import com.tddp2.grupo2.linkup.exception.ServiceException;
import com.tddp2.grupo2.linkup.infrastructure.Database;

public abstract class NotificationService implements LinkupService{

    protected Database database;

    protected NotificationService(Database database) {
        this.database = database;
    }

    public ServiceType getType(){
        return ServiceType.FCM;
    }

    public abstract void saveToken(String token);

    public abstract String getToken();

    public abstract void updateToken(String token) throws ServiceException;

}
