package com.tddp2.grupo2.linkup.controller;

import com.tddp2.grupo2.linkup.task.TaskResponse;

public interface ImageLinkController {
    void initLoadImageTask();
    void finishLoadImageTask();
    void onLoadImageResult(TaskResponse response);
}

