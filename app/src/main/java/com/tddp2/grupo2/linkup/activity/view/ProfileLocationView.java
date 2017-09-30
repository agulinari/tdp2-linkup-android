package com.tddp2.grupo2.linkup.activity.view;

public interface ProfileLocationView extends ProfileView {
    void checkPermissionsAndLoadLocation();
    void onPermissionsDenied();
    void onLocationError();
    void onChangeSettingsDenied();
    void showFetchingLocationMessage();
    void hideFetchingLocationMessage();
}
