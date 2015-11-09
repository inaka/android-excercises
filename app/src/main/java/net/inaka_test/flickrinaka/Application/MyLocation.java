package net.inaka_test.flickrinaka.Application;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;

/**
 * Created by Jonathan Garcia on 11/7/2015.
 */
public class MyLocation implements LocationListener {

    private LocationManager lm;
    private LocationListener ll;
    private Context context;

    public MyLocation(Context context) {

        this.context = context;
        lm = (LocationManager) this.context.getSystemService(Context.LOCATION_SERVICE);
        ll = this;
        lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, ll); // si se quiere utilizar gps se usaria GPS_PROVIDER
    }

    @Override
    public void onLocationChanged(Location location) {
        // TODO Auto-generated method stub

        if (location != null) {

            Log.i("LOCATION CHANGED", location.getLatitude() + "");
            Log.i("LOCATION CHANGED", location.getLongitude() + "");
        }
        lm.removeUpdates(ll); //se le hace remove a la actualización de coordenadas
    }

    @Override
    public void onProviderDisabled(String arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onProviderEnabled(String arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onStatusChanged(String arg0, int arg1, Bundle arg2) {
        // TODO Auto-generated method stub

    }
}