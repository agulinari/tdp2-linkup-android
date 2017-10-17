package com.tddp2.grupo2.linkup.task;

import android.os.AsyncTask;
import com.tddp2.grupo2.linkup.controller.LinkProfileController;
import com.tddp2.grupo2.linkup.exception.ServiceException;
import com.tddp2.grupo2.linkup.model.Block;
import com.tddp2.grupo2.linkup.service.api.LinkUserService;


public class BlockUserTask extends AsyncTask<Object, Void, TaskResponse>{

    private LinkUserService linkUserService;
    private LinkProfileController controller;

    public BlockUserTask(LinkUserService linksService, LinkProfileController controller) {
        this.linkUserService = linksService;
        this.controller = controller;
    }

    @Override
    protected void onPreExecute() {
        if (controller != null) {
            controller.initBlockUserTask();
        }
    }

    @Override
    protected TaskResponse doInBackground(Object... params) {
        TaskResponse response = new TaskResponse();
        try {
            linkUserService.blockUser((Block) params[0]);
        } catch (ServiceException e) {
            response.setError(e.getMessage());
        }
        return response;
    }

    @Override
    protected void onPostExecute(TaskResponse response) {
        if (controller != null) {
            controller.finishBlockUserTask();
            controller.onBlockUserTaskResult(response);
        }
    }
}
