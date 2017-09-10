package com.tddp2.grupo2.linkup.infrastructure;

import com.tddp2.grupo2.linkup.model.Image;
import com.tddp2.grupo2.linkup.model.Links;
import com.tddp2.grupo2.linkup.model.Profile;

import java.util.List;


public interface Database {

    Profile getProfile();

    void setProfile(Profile profile);

    Links getLinks();

    void setLinks(Links profiles);
}
