package com.tddp2.grupo2.linkup.task;

public class LoadImagesTaskResponse extends TaskResponse{

    public boolean alreadyUpdatedFromServer;

    public LoadImagesTaskResponse(boolean alreadyUpdatedFromServer) {
        this.alreadyUpdatedFromServer = alreadyUpdatedFromServer;
    }
}
