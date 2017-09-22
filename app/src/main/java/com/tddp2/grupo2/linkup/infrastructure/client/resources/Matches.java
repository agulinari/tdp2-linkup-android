package com.tddp2.grupo2.linkup.infrastructure.client.resources;

import com.tddp2.grupo2.linkup.infrastructure.client.response.CandidatesResponse;
import com.tddp2.grupo2.linkup.model.MyLinks;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;


public interface Matches {

    @GET("match/{id}")
    Call<MyLinks> getMatches(@Path("id") String fbid);

}
