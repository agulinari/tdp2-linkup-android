package com.tddp2.grupo2.linkup.service.impl;

import android.graphics.Bitmap;
import android.util.Log;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.tddp2.grupo2.linkup.LinkupApplication;
import com.tddp2.grupo2.linkup.exception.APIError;
import com.tddp2.grupo2.linkup.exception.InactiveAccountException;
import com.tddp2.grupo2.linkup.exception.ServiceException;
import com.tddp2.grupo2.linkup.infrastructure.Database;
import com.tddp2.grupo2.linkup.infrastructure.LinkupClient;
import com.tddp2.grupo2.linkup.infrastructure.client.request.AcceptanceRequest;
import com.tddp2.grupo2.linkup.infrastructure.client.request.RejectionRequest;
import com.tddp2.grupo2.linkup.infrastructure.client.response.AcceptanceResponse;
import com.tddp2.grupo2.linkup.infrastructure.client.response.CandidatesResponse;
import com.tddp2.grupo2.linkup.infrastructure.client.response.ImageResponse;
import com.tddp2.grupo2.linkup.infrastructure.client.response.RejectionResponse;
import com.tddp2.grupo2.linkup.infrastructure.messaging.Notification;
import com.tddp2.grupo2.linkup.model.*;
import com.tddp2.grupo2.linkup.service.api.ClientService;
import com.tddp2.grupo2.linkup.service.api.LinksService;
import com.tddp2.grupo2.linkup.service.api.NotificationService;
import com.tddp2.grupo2.linkup.service.factory.ServiceFactory;
import com.tddp2.grupo2.linkup.task.AcceptLinkTaskResponse;
import com.tddp2.grupo2.linkup.utils.ErrorUtils;
import com.tddp2.grupo2.linkup.utils.ImageUtils;
import retrofit2.Call;
import retrofit2.Response;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.tddp2.grupo2.linkup.LinkupApplication.getImageCache;


public class LinksServiceImpl extends LinksService{

    private ClientService clientService;

    public LinksServiceImpl(Database database, ClientService clientService) {
        super(database);
        this.clientService = clientService;
    }

    @Override
    public Links getLinks() throws ServiceException, InactiveAccountException {
        Profile profile = this.database.getProfile();
        String fbid = profile.getFbid();

        Links links = this.database.getLinks();
        /*if (links !=null){
            return links;
        }*/
        LinkupClient linkupClient = clientService.getClient();
        Call<CandidatesResponse> call = linkupClient.candidates.getCandidates(fbid);
        try {
            Response<CandidatesResponse> response = call.execute();
            if (response.isSuccessful()) {
                //Save Links
                links = new Links();
                CandidatesResponse candidatesResponse = response.body();
                links.setLinks(this.adaptResponse(candidatesResponse.getCandidates()));
                int availableSuperlinks = candidatesResponse.getAvailableSuperlinks();
                Log.i("SUPERLINKS", "Recibidos en candidates: " + String.valueOf(availableSuperlinks));
                links.setAvailableSuperlinks(availableSuperlinks);
                saveLinks(links);
                updateToken();
                return links;
            } else if (response.code() == 401) {
                throw new InactiveAccountException();
            } else {
                APIError error = ErrorUtils.parseError(response);
                throw new ServiceException(error);
            }
        } catch (IOException e) {
            throw new ServiceException(e.getLocalizedMessage());
        }
    }

    private void updateToken() {
        NotificationService notificationService = ServiceFactory.getNotificationService();
        String token = notificationService.getToken();
        try {
            Log.d("FirebaseIDService", token);
            notificationService.updateToken(token);
        } catch (ServiceException e) {
            Log.e("FirebaseIDService" , e.getMessage(), e);
        }
    }

    public void saveLinks(Links links) {
        this.database.setLinks(links);
    }

    @Override
    public Links rejectLink(String fbidCandidate) throws ServiceException {
        Profile profile = this.database.getProfile();
        String fbid = profile.getFbid();

        LinkupClient linkupClient = clientService.getClient();
        Rejection rej = new Rejection(fbid, fbidCandidate);
        RejectionRequest request = new RejectionRequest(rej);
        Call<RejectionResponse> call = linkupClient.candidates.reject(request);
        try {
            Response<RejectionResponse> response = call.execute();
            if (response.isSuccessful()) {
                //Elimino el link localmente
                return removeLink(fbidCandidate);
            } else {
                APIError error = ErrorUtils.parseError(response);
                throw new ServiceException(error);
            }
        } catch (IOException e) {
            throw new ServiceException(e.getLocalizedMessage());
        }
    }

    @Override
    public AcceptLinkTaskResponse acceptLink(String fbidCandidate, String tipoDeLink) throws ServiceException {
        Profile profile = this.database.getProfile();
        String fbid = profile.getFbid();

        LinkupClient linkupClient = clientService.getClient();
        Acceptance acceptance = new Acceptance(fbid, fbidCandidate, tipoDeLink);
        AcceptanceRequest request = new AcceptanceRequest(acceptance);
        Call<AcceptanceResponse> call = linkupClient.candidates.accept(request);
        try {
            Response<AcceptanceResponse> response = call.execute();
            if (response.isSuccessful()) {
                //Elimino el link localmente
                Links filteredLinks = removeLink(fbidCandidate);

                AcceptanceResponse acceptanceResponse = response.body();

                Boolean isAMatch = acceptanceResponse.getMatch();
                Log.i("MATCH", "Resultado de match: " + String.valueOf(isAMatch));
                int availableSuperlinks = acceptanceResponse.getRemainingSuperlinks();
                Log.i("MATCH", "Superlinks restantes: " + String.valueOf(availableSuperlinks));

                AcceptLinkTaskResponse acceptLinkTaskResponse = new AcceptLinkTaskResponse();
                acceptLinkTaskResponse.setIsAMatch(isAMatch);
                filteredLinks.setAvailableSuperlinks(availableSuperlinks);
                acceptLinkTaskResponse.setLinks(filteredLinks);
                if (isAMatch){
                    sendNotification(fbid, fbidCandidate);
                }
                return acceptLinkTaskResponse;
            } else {
                APIError error = ErrorUtils.parseError(response);
                throw new ServiceException(error);
            }
        } catch (IOException e) {
            throw new ServiceException(e.getLocalizedMessage());
        }
    }

    private void sendNotification(String fbid, String fbidCandidate) {
        Notification n = new Notification(fbid, fbidCandidate, "Felicidades", "¡Has linkeado con alguien!", "", Notification.MATCH);
        FirebaseDatabase.getInstance().getReference().child("notifications")
                .push()
                .setValue(n);
    }

    public Links removeLink(String fbidCandidate) {
        Links links = database.getLinks();
        for (Link p : links.getLinks()){
            if (p.getFbid().equals(fbidCandidate)){
                links.getLinks().remove(p);
                break;
            }
        }
        database.setLinks(links);
        return links;
    }

    public Database getDatabase(){
        return this.database;
    }

    @Override
    public Images loadImages(String fbidCandidate, int count) throws ServiceException {

        boolean getFromServer = false;
        List<ImageBitmap> images = new ArrayList<ImageBitmap>();

        if (count >= 1) {
            for (int i = 1; i <= count; i++) {
                Bitmap bitmap = getUserPictureFromCache(fbidCandidate, i);
                if (bitmap != null) {
                    ImageBitmap imageBitmap = new ImageBitmap();
                    imageBitmap.setImageId(fbidCandidate);
                    imageBitmap.setBitmap(bitmap);
                    images.add(imageBitmap);
                } else {
                    getFromServer = true;
                    break;
                }
            }
        }else{
            getFromServer = true;
        }

        if (!getFromServer){
            return new Images(images, false);
        }

        images.clear();
        LinkupClient linkupClient = clientService.getClient();

        Call<ImageResponse> call = linkupClient.profiles.getImage(fbidCandidate);
        try {
            Response<ImageResponse> response = call.execute();
            if (response.isSuccessful()) {
                ImageResponse imageResponse = response.body();
                if (imageResponse.getImages().isEmpty()){
                    throw new ServiceException(fbidCandidate);
                }else{
                    for (Image image : imageResponse.getImages()) {
                        if (image.getIdImage().equals("0")){
                            continue;
                        }
                        Bitmap bitmap = ImageUtils.base64ToBitmap(image.getData());
                        ImageBitmap imageBitmap = new ImageBitmap();
                        imageBitmap.setImageId(fbidCandidate);
                        imageBitmap.setBitmap(bitmap);
                        saveUserPictureToCache(fbidCandidate, Integer.valueOf(image.getIdImage()), bitmap);
                        images.add(imageBitmap);
                    }
                    return new Images(images, true);
                }
            } else {
                APIError error = ErrorUtils.parseError(response);
                throw new ServiceException(error);
            }
        } catch (IOException e) {
            throw new ServiceException(e.getLocalizedMessage());
        }
    }

    @Override
    public Boolean updateImagesCache(String fbidCandidate) throws ServiceException {
        boolean hasChanges = false;
        LinkupClient linkupClient = clientService.getClient();

        Call<ImageResponse> call = linkupClient.profiles.getImage(fbidCandidate);
        try {
            Response<ImageResponse> response = call.execute();
            if (response.isSuccessful()) {
                ImageResponse imageResponse = response.body();
                if (imageResponse.getImages().isEmpty()){
                    throw new ServiceException(fbidCandidate);
                }else{
                    for (Image image : imageResponse.getImages()) {
                        if (image.getIdImage().equals("0")){
                            continue;
                        }
                        Bitmap bitmap = ImageUtils.base64ToBitmap(image.getData());
                        ImageBitmap imageBitmap = new ImageBitmap();
                        imageBitmap.setImageId(fbidCandidate);
                        imageBitmap.setBitmap(bitmap);
                        Bitmap oldBitmap = getUserPictureFromCache(fbidCandidate, Integer.valueOf(image.getIdImage()));
                        if (oldBitmap == null){
                            hasChanges = true;
                            saveUserPictureToCache(fbidCandidate, Integer.valueOf(image.getIdImage()), bitmap);
                        }
                        else if (!ImageUtils.compareBitmapsFast(bitmap, oldBitmap)){
                            hasChanges = true;
                            saveUserPictureToCache(fbidCandidate, Integer.valueOf(image.getIdImage()), bitmap);
                        }
                    }
                    return hasChanges;
                }
            } else {
                APIError error = ErrorUtils.parseError(response);
                throw new ServiceException(error);
            }
        } catch (IOException e) {
            throw new ServiceException(e.getLocalizedMessage());
        }
    }

    @Override
    public ImageBitmap loadImage(String fbidCandidate, int number) throws ServiceException {

        Bitmap bitmap = getUserPictureFromCache(fbidCandidate, number);
        if (bitmap != null){
            ImageBitmap imageBitmap = new ImageBitmap();
            imageBitmap.setImageId(fbidCandidate);
            imageBitmap.setBitmap(bitmap);
            return imageBitmap;
        }

        LinkupClient linkupClient = clientService.getClient();

        Call<ImageResponse> call = linkupClient.profiles.getImage(fbidCandidate);
        try {
            Response<ImageResponse> response = call.execute();
            if (response.isSuccessful()) {
                ImageResponse imageResponse = response.body();
                if (imageResponse.getImages().isEmpty()){
                    throw new ServiceException(fbidCandidate);
                }else{
                    Image image = imageResponse.getImages().get(1);

                    bitmap = ImageUtils.base64ToBitmap(image.getData());
                    ImageBitmap imageBitmap = new ImageBitmap();
                    imageBitmap.setImageId(fbidCandidate);
                    imageBitmap.setBitmap(bitmap);
                    saveUserPictureToCache(fbidCandidate, 1, bitmap);
                    return imageBitmap;
                }
            } else {
                APIError error = ErrorUtils.parseError(response);
                throw new ServiceException(error);
            }
        } catch (IOException e) {
            throw new ServiceException(e.getLocalizedMessage());
        }
    }

    private Bitmap getUserPictureFromCache(String fbid, int imageIndex) {
        String key = getImageKey(fbid, imageIndex);
        return LinkupApplication.getImageCache().getBitmapFromMemCache(key);
    }

    private void saveUserPictureToCache(String fbid, int imageIndex, Bitmap picture) {
        String key = getImageKey(fbid, imageIndex);
        getImageCache().addBitmapToMemoryCache(key, picture);
    }

    private String getImageKey(String fbid, int imageIndex) {
        return fbid + ":" + String.valueOf(imageIndex);
    }

    private List<Link> adaptResponse(List<JsonObject> elements) {
        List<Link> links = new ArrayList<>();
        Gson gson = new Gson();
        for (JsonObject jsonObject : elements) {
            if (jsonObject.has("advertiser")) {
                Advertisement advertisement = gson.fromJson(jsonObject.toString(), Advertisement.class);
                links.add(advertisement);
            } else {
                Profile profile = gson.fromJson(jsonObject.toString(), Profile.class);
                links.add(profile);
            }
        }
        return links;
    }
}
