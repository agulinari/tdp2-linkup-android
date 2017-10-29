package com.tddp2.grupo2.linkup.infrastructure;

import com.tddp2.grupo2.linkup.model.Links;
import com.tddp2.grupo2.linkup.model.MyLinks;
import com.tddp2.grupo2.linkup.model.Profile;


public interface Database {

    Profile getProfile();

    void setProfile(Profile profile);

    Links getLinks();

    void setLinks(Links profiles);

    MyLinks getMyLinks();

    void setMyLinks(MyLinks myLinks);

    String getToken();

    void setToken(String token);

    Boolean isActive();

    void setActive(Boolean active);
}
