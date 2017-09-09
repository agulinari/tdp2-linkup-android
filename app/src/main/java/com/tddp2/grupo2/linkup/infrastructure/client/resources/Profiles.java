package com.tddp2.grupo2.linkup.infrastructure.client.resources;

import com.tddp2.grupo2.linkup.model.Profile;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;


public interface Profiles {

    @POST("profile")
    Call<Profile> createProfile(@Body Profile profile);

    @PUT("profile")
    Call<Profile> updateProfile(@Body Profile profile);

    @GET("profile/{id}")
    Call<Profile> getProfile(@Path("id") String fbid);

}
