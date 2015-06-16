package net.inaka.Flickr.geoFlickr.services;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import net.inaka.Flickr.geoFlickr.R;

/**
 * Created by griveroa on 4/16/15.
 */
public class LocationService extends Service implements LocationListener {
    LocationManager mLocationManager;
    Location mLastLocation;
    public final static String GEO_Flickr_INTENT = "GEO_Flickr_INTENT";


    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        mLocationManager.removeUpdates(this);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        mLocationManager.requestLocationUpdates(mLocationManager.getBestProvider(new Criteria(), true), 4000, 0, this);

        return super.onStartCommand(intent, flags, startId);
    }


    //Location Methods
    @Override
    public void onLocationChanged(Location location) {
        if(mLastLocation == null) {
            mLastLocation = location;
            broadCastLocation();
        } else {
            //Check if the delta is larger than 2 meters...
            float delta = mLastLocation.distanceTo(location);
            if (delta > 2) {
                broadCastLocation();
            }
        }

        mLastLocation = location;
    }

    private void broadCastLocation(){
        Intent intent = new Intent();
        intent.setAction(GEO_Flickr_INTENT);
        intent.putExtra("latitude", mLastLocation.getLatitude());
        intent.putExtra("longitude", mLastLocation.getLongitude());

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
