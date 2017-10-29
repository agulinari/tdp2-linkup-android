package com.tddp2.grupo2.linkup.task;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;

import com.tddp2.grupo2.linkup.controller.LinkImageControllerInterface;
import com.tddp2.grupo2.linkup.exception.ServiceException;
import com.tddp2.grupo2.linkup.model.ImageBitmap;
import com.tddp2.grupo2.linkup.model.Images;
import com.tddp2.grupo2.linkup.service.api.LinksService;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;


public class LoadImagesTask extends AsyncTask<Object, Void, TaskResponse> {

    private LinksService linksService;
    private LinkImageControllerInterface controller;

    public LoadImagesTask(LinksService linksService, LinkImageControllerInterface controller) {
        this.linksService = linksService;
        this.controller = controller;
    }

    @Override
    protected void onPreExecute() {
        if (controller != null)
            controller.initLoadImageTask();
    }

    @Override
    protected TaskResponse doInBackground(Object... params) {

        List<ImageBitmap> images = new ArrayList<ImageBitmap>();
        Images imagesResponse = null;
        String fbidCandidate = (String) params[0];
        int count = (int) params[1];

        try {
            imagesResponse = linksService.loadImages(fbidCandidate, count);
            images = imagesResponse.getImages();
        } catch (ServiceException e) {
            TaskResponse response = new TaskResponse(fbidCandidate);
            return response;
        }

        List<Bundle> bundles = new ArrayList<Bundle>();

        for (ImageBitmap imageBitmap : images) {
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            imageBitmap.getBitmap().compress(Bitmap.CompressFormat.PNG, 100, stream);
            byte[] byteArray = stream.toByteArray();
            Bundle b = new Bundle();
            b.putByteArray("image", byteArray);
            bundles.add(b);
        }
        TaskResponse response = new LoadImagesTaskResponse(imagesResponse.isAlreadyUpdatedFromServer());
        response.setResponse(bundles);

        return response;
    }

    @Override
    protected void onPostExecute(TaskResponse response) {
        if (controller != null)
            controller.finishLoadImageTask();

        controller.onLoadImageResult(response);
    }

}