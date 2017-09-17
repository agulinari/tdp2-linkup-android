package com.tddp2.grupo2.linkup.infrastructure.client.resources;

import com.tddp2.grupo2.linkup.infrastructure.client.response.CandidatesResponse;
import com.tddp2.grupo2.linkup.model.Profile;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;


public interface Candidates {

    @GET("candidate/{id}")
    Call<CandidatesResponse> getCandidates(@Path("id") String fbid);

}
