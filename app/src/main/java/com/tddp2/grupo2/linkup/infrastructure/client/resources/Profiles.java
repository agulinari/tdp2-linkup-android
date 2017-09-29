package com.tddp2.grupo2.linkup.infrastructure.client.resources;

import com.tddp2.grupo2.linkup.infrastructure.client.request.PostUserRequest;
import com.tddp2.grupo2.linkup.infrastructure.client.request.PutUserRequest;
import com.tddp2.grupo2.linkup.infrastructure.client.request.TokenRequest;
import com.tddp2.grupo2.linkup.infrastructure.client.response.ImageResponse;
import com.tddp2.grupo2.linkup.infrastructure.client.response.UserResponse;
import com.tddp2.grupo2.linkup.model.Profile;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;


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
    Call<?> updateToken(@Body TokenRequest request);

}
