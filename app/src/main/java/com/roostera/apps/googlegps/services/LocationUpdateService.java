package com.roostera.apps.googlegps.services;

import android.app.Service;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.roostera.apps.googlegps.eventbus.LocationUpdateEvent;
import de.greenrobot.event.EventBus;

public class LocationUpdateService extends Service
        implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {
    public LocationUpdateService() {
    }

    private static final String TAG = LocationUpdateService.class.getSimpleName();
    private static String ACTIVITY = "";
    private GoogleApiClient mGoogleApiClient;
    private Location mLastLocation;
    private LocationRequest mLocationRequest;
    private LocationUpdateEvent event;
    private EventBus bus = EventBus.getDefault();

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "Service running");
        readData(intent);
        callConnection();
        return Service.START_STICKY;
    }

    private void readData(Intent intent){
        Bundle extras = intent.getExtras();
        if (extras != null){
            ACTIVITY = extras.getString("TAG");
        }
    }

    private void initLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(5000); //5 seconds
        mLocationRequest.setFastestInterval(2000); //2 seconds
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY); //most accurate, more power consume
    }

    private synchronized void callConnection() {
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }
        mGoogleApiClient.connect();
    }

    @Override
    public IBinder onBind(Intent intent) {return null;}

    private void startLocationUpdates() {
        initLocationRequest();
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, LocationUpdateService.this);
    }

    private void stopLocationUpdates() {
        LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, LocationUpdateService.this);
    }

    @Override
    public void onConnected(Bundle bundle) {
        Log.d(TAG, "onConnected" + bundle);
        if (mLastLocation != null){
            mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
            Log.d(TAG, "LastLocation " + "Lat:"+ mLastLocation.getLatitude()+", Lng:" + mLastLocation.getLongitude());
        }
        startLocationUpdates();
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.d(TAG, "onConnectionSuspended " + i);
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.d(TAG, "onConnectionFailed " + connectionResult);
    }

    @Override
    public void onDestroy() {
        Log.d(TAG, "onDestry");
        mGoogleApiClient.disconnect();
        super.onDestroy();
    }

    @Override
    public void onLocationChanged(Location location) {
        event = new LocationUpdateEvent(location, ACTIVITY);
        bus.post(event);
    }

    public Location getmLastLocation() {
        return mLastLocation;
    }
}