package com.tddp2.grupo2.linkup.exception;


public class ServiceException extends Exception{

    private APIError error;

    public ServiceException(String messagge) {
        super(messagge);
    }

    public ServiceException(APIError error) {
        super(error.getData());
        this.error = error;
    }

    public boolean isSessionExpired() {
        if (error != null) {
            return error.isSessionExpired();
        }
        return Boolean.FALSE;
    }

    public boolean hasError(){
        return (error!=null);
    }

}
