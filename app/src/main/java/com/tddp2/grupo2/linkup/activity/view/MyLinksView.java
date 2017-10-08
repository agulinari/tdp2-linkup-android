package com.tddp2.grupo2.linkup.activity.view;


import com.tddp2.grupo2.linkup.model.MyLinks;

public interface MyLinksView {

    void showMyLinks(MyLinks links);

    void onError(String error);

    void showInactiveAccountAlert();

    void hideChatAndNewLinksProgress();
}
