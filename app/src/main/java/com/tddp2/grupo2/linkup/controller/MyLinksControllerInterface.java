package com.tddp2.grupo2.linkup.controller;


import com.tddp2.grupo2.linkup.task.TaskResponse;

public interface MyLinksControllerInterface {

    void initGetMyLinksTask();
    void finishGetMyLinksTask();
    void showInactiveAccountError();
    void onGetMyLinksResult(TaskResponse response);

}
