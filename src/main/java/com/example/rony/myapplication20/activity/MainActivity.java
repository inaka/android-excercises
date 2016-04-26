package com.example.rony.myapplication20.activity;

import android.Manifest;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.example.rony.myapplication20.R;
import com.example.rony.myapplication20.aplication.VolleySingleton;
import com.example.rony.myapplication20.gps.GpsService;
import com.example.rony.myapplication20.network.DataCallback;
import com.example.rony.myapplication20.network.ObjectFlickr;
import com.example.rony.myapplication20.network.PhotoAdapter;
import com.example.rony.myapplication20.network.PhotoSize;
import com.example.rony.myapplication20.network.ServiceImageFlickr;
import com.example.rony.myapplication20.network.Sizes;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    final Activity _activity = this;
    private boolean LocationAvailable;
    Intent locationIntent;
    LocationReceiver locationReceiver;
    boolean scanImages;

    private static final int PERMISSION_REQUEST_CODE = 1;

    Dialog dialog;


    private List<ObjectFlickr.Photo> photoList = new ArrayList<ObjectFlickr.Photo>();
    private RecyclerView recyclerView;
    private PhotoAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        if (!checkPermission()) {
            requestPermission();
        }

        locationIntent = new Intent(this, GpsService.class);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setImageResource(R.drawable.googlemaps);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Intent intent = new Intent(MainActivity.this, MapActivity.class);

                startActivity(intent);


                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });


        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        //recyclerView.setItemAnimator(new DefaultItemAnimator());

        mAdapter = new PhotoAdapter(this, photoList);
        recyclerView.setAdapter(mAdapter);

        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));

        /*
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);

        */

    }

    @Override
    protected void onStart() {
        super.onStart();

        locationReceiver = new LocationReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(GpsService.KEYSERVICE);
        registerReceiver(locationReceiver, filter);
        startService(locationIntent);

        dialog = ProgressDialog.show(this, "", "Loading. Please wait...", true);
    }


    @Override
    protected void onStop() {
        dialog.dismiss();
        unregisterReceiver(locationReceiver);
        super.onStop();
    }

    @Override
    protected void onPause() {
        VolleySingleton.getInstance().cancelPendingRequests(VolleySingleton.TAG);
        dialog.dismiss();
        super.onPause();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            if (isGPSRunning(GpsService.class)) {
                stopService(locationIntent);
                Toast.makeText(this,"Servicio detenido", Toast.LENGTH_LONG).show();
            } else {
                startService(locationIntent);
                Toast.makeText(this, "Servicio iniciado", Toast.LENGTH_LONG).show();
            }
        }

        return super.onOptionsItemSelected(item);
    }

    private class LocationReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(final Context context, Intent intent) {
            double latitude = intent.getDoubleExtra("latitude", 0);
            double longitude = intent.getDoubleExtra("longitude", 0);
            if (!scanImages) {
                scanImages = true;
                Location location = new Location(GpsService.KEYSERVICE);
                location.setLatitude(latitude);
                location.setLongitude(longitude);


                VolleySingleton.getInstance().location = location;

                ServiceImageFlickr service = new ServiceImageFlickr(String.valueOf(location.getLatitude()), String.valueOf(location.getLongitude()));

                service.callFlickrPhotosSearch(new DataCallback() {
                    @Override
                    public void onSuccess(List<ObjectFlickr.Photo> photos) {

                        dialog.dismiss();
                        scanImages = false;
                        photoList = photos;
                        VolleySingleton.getInstance().photoList = photos;
                        mAdapter = new PhotoAdapter(_activity, photoList);
                        recyclerView.setAdapter(mAdapter);

                    }

                    @Override
                    public void onError(String error) {
                        dialog.dismiss();
                        scanImages = false;
                        final ViewGroup viewGroup = (ViewGroup) ((ViewGroup) _activity.findViewById(android.R.id.content)).getChildAt(0);
                        Snackbar.make(viewGroup, error, Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();
                    }

                    @Override
                    public void onSuccessPhoto(Sizes sezes) {

                    }
                });

            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (checkPermission()) {
                        //lManager.requestLocationUpdates(lManager.GPS_PROVIDER, TIME_UPDATES, DISTANCE_UPDATES, this);
                    } else {
                        requestPermission();
                    }
                } else {
                    Toast.makeText(this, "Permission Not Granted.", Toast.LENGTH_LONG).show();
                }
                break;
        }
    }

    public void requestPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
            Toast.makeText(this, "This app relies on location data for it's main functionality. Please enable GPS data to access all features.", Toast.LENGTH_LONG).show();
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_REQUEST_CODE);
        }
    }

    private boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
        if (result == PackageManager.PERMISSION_GRANTED) {
            LocationAvailable = true;
            return true;
        } else {
            LocationAvailable = false;
            return false;
        }
    }

    public void goto_photo_detail(ObjectFlickr.Photo photo) {

        //https://farm{farm-id}.staticflickr.com/{server-id}/{id}_{secret}_[mstzb].jpg


        String imageUrl = String.format("https://farm%s.staticflickr.com/%s/%s_%s_b.jpg",
                photo.getFarm(),
                photo.getServer(),
                photo.getId(),
                photo.getSecret());

        Intent intent = new Intent(MainActivity.this, PhotoActivity.class);
        intent.putExtra("url", imageUrl);
        intent.putExtra("text", photo.getTitle());
        intent.putExtra("id", photo.getId());
        startActivity(intent);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if (isGPSRunning(GpsService.class)) {
            menu.getItem(0).setTitle("Iniciar busqueda");
        } else {
            menu.getItem(0).setTitle("Finalizar busqueda");
        }

        return true;
    }

    private boolean isGPSRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }
}
