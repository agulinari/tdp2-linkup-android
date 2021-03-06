package com.tddp2.grupo2.linkup.controller;

import com.tddp2.grupo2.linkup.task.TaskResponse;

public interface LinkImageControllerInterface {
    void initLoadImageTask();
    void finishLoadImageTask();
    void onLoadImageResult(TaskResponse response);
    void reloadImages();
    void updateImagesFromServer(String linkUserId);
}

