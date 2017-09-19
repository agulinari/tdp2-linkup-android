package com.tddp2.grupo2.linkup.infrastructure.client.resources;

import com.tddp2.grupo2.linkup.infrastructure.client.request.AcceptanceRequest;
import com.tddp2.grupo2.linkup.infrastructure.client.request.RejectionRequest;
import com.tddp2.grupo2.linkup.infrastructure.client.response.AcceptanceResponse;
import com.tddp2.grupo2.linkup.infrastructure.client.response.CandidatesResponse;
import com.tddp2.grupo2.linkup.infrastructure.client.response.RejectionResponse;
import com.tddp2.grupo2.linkup.model.Profile;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;


public interface Candidates {

    @GET("candidate/{id}")
    Call<CandidatesResponse> getCandidates(@Path("id") String fbid);

    @POST("rejection")
    Call<RejectionResponse> reject(@Body RejectionRequest rejectionRequest);

    @POST("link")
    Call<AcceptanceResponse> accept(@Body AcceptanceRequest acceptanceRequest);

}
