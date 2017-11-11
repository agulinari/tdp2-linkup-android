package com.tddp2.grupo2.linkup.infrastructure.client.resources;

import com.tddp2.grupo2.linkup.infrastructure.client.request.*;
import com.tddp2.grupo2.linkup.infrastructure.client.response.*;
import com.tddp2.grupo2.linkup.model.Profile;
import retrofit2.Call;
import retrofit2.http.*;

import java.util.List;


public interface Profiles {

    @POST("user")
    Call<UserResponse> createProfile(@Body PostUserRequest request);

    @PUT("user")
    Call<UserResponse> updateProfile(@Body PutUserRequest request);

    @PUT("user")
    Call<UserResponse> upgradeAccount(@Body UpgradeAccountRequest request);

    @GET("user/{id}")
    Call<UserResponse> getProfile(@Path("id") String fbid);

    @GET("usersProfile")
    Call<List<Profile>> getProfiles();

    @GET("image/{id}")
    Call<ImageResponse> getImage(@Path("id") String fbid);

    @PUT("user")
    Call<Void> updateToken(@Body TokenRequest request);

    @POST("abuseReport")
    Call<AbuseReportResponse> reportAbuse(@Body AbuseReportRequest request);

    @POST("block")
    Call<BlockResponse> blockUser(@Body BlockRequest request);

    @POST("recommendation")
    Call<RecommendResponse> recommendUser(@Body RecommendRequest request);
}
