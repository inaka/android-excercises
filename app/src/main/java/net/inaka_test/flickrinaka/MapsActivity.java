package net.inaka_test.flickrinaka;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import net.inaka_test.flickrinaka.Models.Photo;

import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private Context context = this;
    private GoogleMap mMap;
    List<Photo> photoList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        Bundle bundle = getIntent().getExtras();
        photoList = (List<Photo>) bundle.getSerializable("photoList");
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
        LatLng latLng = null;
        final ArrayList<Marker> markerArrayList = new ArrayList<>();

        for (Photo photo : photoList) {
            latLng = new LatLng(photo.getLatitude(), photo.getLongitude());
            Marker marker = mMap.addMarker(new MarkerOptions().position(latLng).title(photo.getTitle()));
            markerArrayList.add(marker);
        }
        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                int index = markerArrayList.indexOf(marker);
                final Photo photo = photoList.get(index);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent intent = new Intent(context, PhotoDetail.class);
                        intent.putExtra("photo", photo);
                        startActivity(intent);
                    }
                }, 1000);
                return false;
            }
        });

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 12));
    }
}
