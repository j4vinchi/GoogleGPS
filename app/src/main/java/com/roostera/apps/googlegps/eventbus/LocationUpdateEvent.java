package com.roostera.apps.googlegps.eventbus;

import android.location.Location;

/**
 * Created by Javi3r on 31/01/2016.
 */
public class LocationUpdateEvent {
    private Location location;

    public LocationUpdateEvent(Location location){
        this.location = location;
    }

    public Location getLocation(){
        return location;
    }
}
