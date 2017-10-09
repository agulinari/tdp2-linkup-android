package com.tddp2.grupo2.linkup.utils;

import android.graphics.Color;
import android.util.Log;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.maps.android.SphericalUtil;
import com.tddp2.grupo2.linkup.model.Location;

import java.text.DecimalFormat;

public class MapUtils {

    public static final int MAP_PADDING = 0;
    public static final int MIN_RADIUS_IN_METERS = 100;
    public static final int CIRCLE_PADDING_PERCENTAGE = 10;
    public static final int CIRCLE_RADIUS_PADDING_PERCENTAGE = 5;

    public static LatLngBounds getLocationBounds(LatLng centerPoint, double radiusInMeters) {
        radiusInMeters = (radiusInMeters < MIN_RADIUS_IN_METERS) ? MIN_RADIUS_IN_METERS : radiusInMeters;
        double radiusWithPadding = radiusInMeters / 100 * (100 + CIRCLE_PADDING_PERCENTAGE);

        LatLng northPoint = SphericalUtil.computeOffset(centerPoint, radiusWithPadding, 0);
        LatLng eastPoint = SphericalUtil.computeOffset(centerPoint, radiusWithPadding, 90);
        LatLng southPoint = SphericalUtil.computeOffset(centerPoint, radiusWithPadding, 180);
        LatLng westPoint = SphericalUtil.computeOffset(centerPoint, radiusWithPadding, 270);

        LatLng northeast = new LatLng(northPoint.latitude, eastPoint.longitude);
        LatLng southwest = new LatLng(southPoint.latitude, westPoint.longitude);
        return new LatLngBounds(southwest, northeast);
    }

    public static LatLng getCenterPoint(Location loggedUserLocation, Location linkLocation) {
        float distanceInMeters = getDistanceBetweenLocationsInMeters(loggedUserLocation, linkLocation);
        LatLng from = new LatLng(loggedUserLocation.getLatitude(), loggedUserLocation.getLongitude());
        LatLng to = new LatLng(linkLocation.getLatitude(), linkLocation.getLongitude());
        double heading = SphericalUtil.computeHeading(from, to);
        LatLng loggedUserLocationLatLng = new LatLng(loggedUserLocation.getLatitude(), loggedUserLocation.getLongitude());
        return SphericalUtil.computeOffset(loggedUserLocationLatLng, distanceInMeters / 2, heading);
    }

    public static String getDistanceTextBetweenLocations(Location linkUpLocation1, Location linkUpLocation2) {
        float distanceInMeters = getDistanceBetweenLocationsInMeters(linkUpLocation1, linkUpLocation2);
        return getDistanceTextFromMeters(distanceInMeters);
    }

    public static float getDistanceBetweenLocationsInMeters(Location linkUpLocation1, Location linkUpLocation2) {
        android.location.Location loc1 = new android.location.Location("");
        loc1.setLatitude(linkUpLocation1.getLatitude());
        loc1.setLongitude(linkUpLocation1.getLongitude());

        android.location.Location loc2 = new android.location.Location("");
        loc2.setLatitude(linkUpLocation2.getLatitude());
        loc2.setLongitude(linkUpLocation2.getLongitude());

        float distanceInMeters = loc1.distanceTo(loc2);
        Log.i("DISTANCE", String.valueOf(distanceInMeters) + " meters");

        return distanceInMeters;
    }

    public static String getDistanceTextFromMeters(float distanceInMeters) {
        if (distanceInMeters <= 100) {
            return "¡A menos de 100 metros!";
        } else {
            float distanceInKilometers = distanceInMeters / 1000;
            DecimalFormat decimalFormat = new DecimalFormat();
            decimalFormat.setMaximumFractionDigits(1);
            if (distanceInKilometers == 1.0) {
                return "A " + decimalFormat.format(distanceInKilometers) + " kilómetro";
            } else {
                return "A " + decimalFormat.format(distanceInKilometers) + " kilómetros";
            }
        }
    }

    public static CircleOptions getCircleOptions(LatLng centerPoint, double radiusInMeters) {
        radiusInMeters = (radiusInMeters < MIN_RADIUS_IN_METERS) ? MIN_RADIUS_IN_METERS : radiusInMeters;
        CircleOptions circleOptions = new CircleOptions();
        circleOptions.center(centerPoint);
        double radiusWithPadding = radiusInMeters / 100 * (100 + CIRCLE_RADIUS_PADDING_PERCENTAGE);
        circleOptions.radius(radiusWithPadding);
        circleOptions.strokeColor(Color.BLACK);
        circleOptions.fillColor(0x30ff0000);
        circleOptions.strokeWidth(2);
        return circleOptions;
    }
}
