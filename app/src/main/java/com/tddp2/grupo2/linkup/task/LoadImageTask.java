package com.tddp2.grupo2.linkup.task;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;

import com.tddp2.grupo2.linkup.controller.LinkImageControllerInterface;
import com.tddp2.grupo2.linkup.exception.ServiceException;
import com.tddp2.grupo2.linkup.model.ImageBitmap;
import com.tddp2.grupo2.linkup.service.api.LinksService;

import java.io.ByteArrayOutputStream;


public class LoadImageTask extends AsyncTask<Object, Void, TaskResponse> {

    private LinksService linksService;
    private LinkImageControllerInterface controller;
    private boolean isBitmap;

    public LoadImageTask(LinksService linksService, LinkImageControllerInterface controller, boolean isBitmap) {
        this.linksService = linksService;
        this.controller = controller;
        this.isBitmap = isBitmap;
    }

    @Override
    protected void onPreExecute() {
        if (controller != null)
            controller.initLoadImageTask();
    }

    @Override
    protected TaskResponse doInBackground(Object... params) {

        ImageBitmap image;
        String fbidCandidate = (String) params[0];
        int number = (int) params[1];
        TaskResponse taskResponse = new LoadImageTaskResponse(number);

        try {
            image = linksService.loadImage(fbidCandidate, number);
        } catch (ServiceException e) {
            TaskResponse response = new TaskResponse(fbidCandidate);
            return response;
        }
        if (isBitmap) {
            taskResponse.setResponse(image);
        }else{
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            image.getBitmap().compress(Bitmap.CompressFormat.PNG, 100, stream);
            byte[] byteArray = stream.toByteArray();
            Bundle b = new Bundle();
            b.putByteArray("image", byteArray);
            taskResponse.setResponse(b);
        }
        return taskResponse;
    }

    @Override
    protected void onPostExecute(TaskResponse response) {
        if (controller != null)
            controller.finishLoadImageTask();

        controller.onLoadImageResult(response);
    }

}
