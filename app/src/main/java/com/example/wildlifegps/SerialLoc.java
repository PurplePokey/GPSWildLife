package com.example.wildlifegps;

import android.location.Location;

import java.io.Serializable;

public class SerialLoc implements Serializable {
    private double latitude;
    private double longitude;

    public SerialLoc(double lat, double lon){
        latitude = lat;
        longitude = lon;
    }

    public Location toRealLoc(){
        Location result = new Location("");
        result.setLatitude(latitude);
        result.setLongitude(longitude);
        return result;
    }
}
