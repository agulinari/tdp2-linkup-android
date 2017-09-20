package com.tddp2.grupo2.linkup;

import com.tddp2.grupo2.linkup.model.Profile;


public interface LinksView  extends BaseView{

    void showLink(Profile profile);

    void showEmptyLinks();

    void disableActions();

    void enableActions();

    void showMatch(String matchName);
}
