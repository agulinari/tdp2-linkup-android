package com.tddp2.grupo2.linkup.infrastructure;

import com.tddp2.grupo2.linkup.model.Profile;

import java.util.List;
import java.util.Map;



public interface Database {

    Profile getProfile();

    void setProfile(Profile profile);

}
