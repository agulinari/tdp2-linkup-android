package com.tddp2.grupo2.linkup;

import android.content.Context;
import android.location.Location;

public interface LocationView extends BaseView {
    void checkPermissionsAndLoadLocation();
    void loadLocation();
    void getLastLocation();
    void waitForLocation();
    void saveLocation(Location location);
    void onPermissionsDenied();
    void onLocationError();
    void onChangeSettingsDenied();
    void onLocationFetched(Location location);
    Context getContext();
}
