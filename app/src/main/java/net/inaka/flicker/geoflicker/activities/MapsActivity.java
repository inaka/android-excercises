package net.inaka.Flickr.geoFlickr.activities;

import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import net.inaka.Flickr.geoFlickr.R;
import net.inaka.Flickr.geoFlickr.models.Photo;

import java.util.ArrayList;
import java.util.Dictionary;
import java.util.LinkedHashMap;

public class MapsActivity extends FragmentActivity {

    private GoogleMap mMap;
    ArrayList<Photo> mPhotos;
    LinkedHashMap<String, Photo> mMarkersPhotos;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        mPhotos = getIntent().getParcelableArrayListExtra("photos");

        mMarkersPhotos = new LinkedHashMap();

        setUpMapIfNeeded();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
    }

    private void setUpMapIfNeeded() {
        if (mMap == null) {
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map)).getMap();
            if (mMap != null) {
                setUpMap();

                mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
                    @Override
                    public void onInfoWindowClick(Marker marker) {
                        Photo photo = mMarkersPhotos.get(marker.getId());

                        String imageUrl = String.format("https://farm%s.staticflickr.com/%s/%s_%s_b.jpg",
                                photo.farm,
                                photo.server,
                                photo.id,
                                photo.secret);

                        Intent intent = new Intent(MapsActivity.this, PictureActivity.class);
                        intent.putExtra("url", imageUrl);
                        startActivity(intent);
                    }
                });
            }


        }
    }

    private void setUpMap() {
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        for(Photo photo : mPhotos){
            LatLng geoPoint = new LatLng(photo.latitude, photo.longitude);
            Marker marker = mMap.addMarker(new MarkerOptions().position(geoPoint).title(photo.title));
            mMarkersPhotos.put(marker.getId(), photo);
            builder.include(geoPoint);
        }

        if(mPhotos.size() > 0) {
            final LatLngBounds bounds = builder.build();

            mMap.setOnCameraChangeListener(new GoogleMap.OnCameraChangeListener() {
                @Override
                public void onCameraChange(CameraPosition arg0) {
                    mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 10));
                    mMap.setOnCameraChangeListener(null);
                }
            });
        }
    }
}
