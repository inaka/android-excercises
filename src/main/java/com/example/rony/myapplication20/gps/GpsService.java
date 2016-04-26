package com.example.rony.myapplication20.gps;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;

/**
 * Created by Rony on 24/04/2016.
 */
public class GpsService extends Service implements LocationListener {
    LocationManager lManager;
    Location location;
    public final static String KEYSERVICE = "gpsIntent";

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        lManager.removeUpdates(this);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        lManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        lManager.requestLocationUpdates(lManager.getBestProvider(new Criteria(), true), 4000, 0, this);

        return super.onStartCommand(intent, flags, startId);
    }


    //Location Methods
    @Override
    public void onLocationChanged(Location lo) {
        if (location == null) {
            location = lo;
            broadCastLocation();
        } else {
            float delta = location.distanceTo(lo);
            if (delta > 2) {
                broadCastLocation();
            }
        }
        location = lo;
    }

    private void broadCastLocation() {
        Intent intent = new Intent();
        intent.setAction(KEYSERVICE);
        intent.putExtra("latitude", location.getLatitude());
        intent.putExtra("longitude", location.getLongitude());
        sendBroadcast(intent);
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }


}