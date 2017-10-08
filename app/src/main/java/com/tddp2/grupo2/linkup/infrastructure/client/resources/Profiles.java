package com.tddp2.grupo2.linkup.infrastructure.client.resources;

import com.tddp2.grupo2.linkup.infrastructure.client.request.*;
import com.tddp2.grupo2.linkup.infrastructure.client.response.AbuseReportResponse;
import com.tddp2.grupo2.linkup.infrastructure.client.response.BlockResponse;
import com.tddp2.grupo2.linkup.infrastructure.client.response.ImageResponse;
import com.tddp2.grupo2.linkup.infrastructure.client.response.UserResponse;
import com.tddp2.grupo2.linkup.model.Profile;
import retrofit2.Call;
import retrofit2.http.*;

import java.util.List;


public interface Profiles {

    @POST("user")
    Call<UserResponse> createProfile(@Body PostUserRequest request);

    @PUT("user")
    Call<UserResponse> updateProfile(@Body PutUserRequest request);

    @GET("user/{id}")
    Call<UserResponse> getProfile(@Path("id") String fbid);

    @GET("usersProfile")
    Call<List<Profile>> getProfiles();

    @GET("image/{id}")
    Call<ImageResponse> getImage(@Path("id") String fbid);

    @PUT("token")
    Call<Void> updateToken(@Body TokenRequest request);

    @POST("abuseReport")
    Call<AbuseReportResponse> reportAbuse(@Body AbuseReportRequest request);

    @POST("block")
    Call<BlockResponse> blockUser(@Body BlockRequest request);
}
