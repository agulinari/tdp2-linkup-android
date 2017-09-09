package com.tddp2.grupo2.linkup.infrastructure;

import com.tddp2.grupo2.linkup.infrastructure.client.ServiceGenerator;
import com.tddp2.grupo2.linkup.infrastructure.client.resources.Profiles;

import retrofit2.Retrofit;

public class LinkupClient {

    public Profiles profiles;

    public LinkupClient() {
    }

    public void build() {
        Retrofit retrofit = ServiceGenerator.defaultRetrofit();
        profiles = retrofit.create(Profiles.class);
    }

}