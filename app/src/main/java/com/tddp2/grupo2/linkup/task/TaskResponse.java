package com.tddp2.grupo2.linkup.task;


public class TaskResponse {

    private String error;
    private Boolean sessionExpired = Boolean.FALSE;
    private Object response;

    public TaskResponse(String error) {
        this.error = error;
    }

    public TaskResponse(Object response) {
        this.response = response;
        this.error = "";
    }

    public TaskResponse() {
        this.error = "";
    }

    public boolean hasError() {
        return !error.isEmpty();
    }

    public void setSessionExpired(Boolean sessionExpired) {
        this.sessionExpired = sessionExpired;
    }

    public boolean sessionExpired() {
        return sessionExpired;
    }

    public String getError() {
        return error;
    }

    public Object getResponse() {
        return response;
    }

    public void setError(String error) {
        this.error = error;
    }

    public void setResponse(Object response) {
        this.response = response;
    }
}