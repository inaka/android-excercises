package com.example.rony.myapplication20.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.rony.myapplication20.R;
import com.example.rony.myapplication20.aplication.VolleySingleton;
import com.example.rony.myapplication20.network.ObjectFlickr;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.clear();

        mMap.setOnMarkerClickListener(this);

        BitmapDescriptor bitmapMarker;
        bitmapMarker = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE);
        LatLng photoLocation = new LatLng(0, 0);

        for (ObjectFlickr.Photo p : VolleySingleton.getInstance().photoList) {
            photoLocation = new LatLng(Double.parseDouble(p.getLatitude()), Double.parseDouble(p.getLongitude()));

            MarkerOptions marker = new MarkerOptions().position(photoLocation).title(p.getOwner()).snippet(p.getTitle()).icon(bitmapMarker);


            mMap.addMarker(marker);
        }

        if (photoLocation.latitude == 0 && photoLocation.longitude == 0) {

            if (VolleySingleton.getInstance().location != null) {
                LatLng photoL = new LatLng(VolleySingleton.getInstance().location.getLatitude(), VolleySingleton.getInstance().location.getLongitude());
                CameraUpdate lastLocation = CameraUpdateFactory.newLatLngZoom(photoLocation, 12);
                mMap.moveCamera(lastLocation);
            }

        } else {
            CameraUpdate lastLocation = CameraUpdateFactory.newLatLngZoom(photoLocation, 12);
            mMap.moveCamera(lastLocation);
        }



        /*
        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        8*/
    }

    @Override
    public boolean onMarkerClick(final Marker marker) {

        for (ObjectFlickr.Photo photo : VolleySingleton.getInstance().photoList) {
            if (marker.getTitle().equalsIgnoreCase(photo.getOwner()) && marker.getSnippet().equalsIgnoreCase(photo.getTitle())) {

                /*

                String imageUrl = String.format("https://farm%s.staticflickr.com/%s/%s_%s_b.jpg",
                        photo.getFarm(),
                        photo.getSecret(),
                        photo.getId(),
                        photo.getSecret());

                Intent intent = new Intent(MapActivity.this, PhotoActivity.class);
                intent.putExtra("url", imageUrl);
                intent.putExtra("text", photo.getTitle());
                startActivity(intent);

                */


                Intent intent = new Intent(MapActivity.this, PhotoActivity.class);
                //intent.putExtra("url", imageUrl);
                intent.putExtra("text", photo.getTitle());
                intent.putExtra("id", photo.getId());
                startActivity(intent);

            }
        }

        return false;
    }
}
