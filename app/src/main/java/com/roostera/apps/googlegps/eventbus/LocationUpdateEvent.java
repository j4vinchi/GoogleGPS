package com.roostera.apps.googlegps.eventbus;

import android.location.Location;

/**
 * Created by Javi3r on 31/01/2016.
 */
public class LocationUpdateEvent {
    private Location location;
    private String activity;

    public LocationUpdateEvent(Location location, String activity){
        this.location = location;
        this.activity = activity;
    }
    public Location getLocation(){
        return location;
    }
    public String getActivity(){
        return activity;
    }
}
