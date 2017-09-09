package com.tddp2.grupo2.linkup.infrastructure;

import com.tddp2.grupo2.linkup.model.Image;
import com.tddp2.grupo2.linkup.model.Profile;



public interface Database {

    Profile getProfile();

    void setProfile(Profile profile);

}
