package com.tddp2.grupo2.linkup;

public interface LocationView extends ProfileView {
    void checkPermissionsAndLoadLocation();
    void onPermissionsDenied();
    void onLocationError();
    void onChangeSettingsDenied();
    void showFetchingLocationMessage();
    void hideFetchingLocationMessage();
}
