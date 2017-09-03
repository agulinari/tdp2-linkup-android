package com.tddp2.grupo2.linkup.exception;


public class ServiceException extends Exception{

    private APIError error;

    public ServiceException(String messagge) {
        super(messagge);
    }

    public boolean isSessionExpired() {
        if (error != null) {
            return error.isSessionExpired();
        }
        return Boolean.FALSE;
    }

}
