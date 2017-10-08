package com.tddp2.grupo2.linkup.service.impl;

import android.graphics.Bitmap;
import android.util.Log;
import com.google.firebase.database.FirebaseDatabase;
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
import com.tddp2.grupo2.linkup.task.AcceptLinkTaskResponse;
import com.tddp2.grupo2.linkup.utils.ErrorUtils;
import com.tddp2.grupo2.linkup.utils.ImageUtils;
import retrofit2.Call;
import retrofit2.Response;

import java.io.IOException;
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
                List<Profile> profiles = response.body().getCandidates();
                links = new Links();
                links.setLinks(profiles);
                saveLinks(links);
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
                Boolean isAMatch = response.body().getMatch();
                Log.i("MATCH", "Resultado de match: " + String.valueOf(isAMatch));
                AcceptLinkTaskResponse acceptLinkTaskResponse = new AcceptLinkTaskResponse();
                acceptLinkTaskResponse.setIsAMatch(isAMatch);
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
        for (Profile p : links.getLinks()){
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
    public ImageBitmap loadImage(String fbidCandidate) throws ServiceException {

        Bitmap bitmap = LinkupApplication.getImageCache().getBitmapFromMemCache(fbidCandidate);
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
                    getImageCache().addBitmapToMemoryCache(fbidCandidate, bitmap);
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
}
