package com.tddp2.grupo2.linkup.task;

import android.os.AsyncTask;
import com.tddp2.grupo2.linkup.controller.AbstractLinkProfileController;
import com.tddp2.grupo2.linkup.exception.ServiceException;
import com.tddp2.grupo2.linkup.model.AbuseReport;
import com.tddp2.grupo2.linkup.service.api.LinkUserService;


public class ReportAbuseTask extends AsyncTask<Object, Void, TaskResponse>{

    private LinkUserService linkUserService;
    private AbstractLinkProfileController controller;

    public ReportAbuseTask(LinkUserService linksService, AbstractLinkProfileController controller) {
        this.linkUserService = linksService;
        this.controller = controller;
    }

    @Override
    protected void onPreExecute() {
        if (controller != null) {
            controller.initReportAbuseTask();
        }
    }

    @Override
    protected TaskResponse doInBackground(Object... params) {
        TaskResponse response = new TaskResponse();
        try {
            linkUserService.reportAbuse((AbuseReport) params[0]);
        } catch (ServiceException e) {
            response.setError(e.getMessage());
        }
        return response;
    }

    @Override
    protected void onPostExecute(TaskResponse response) {
        if (controller != null) {
            controller.finishReportAbuseTask();
        }
        controller.onReportAbuseTaskResult(response);
    }

}
