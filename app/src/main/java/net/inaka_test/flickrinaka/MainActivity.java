package net.inaka_test.flickrinaka;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.android.volley.VolleyError;

import net.inaka_test.flickrinaka.Adapters.PhotosAdapter;
import net.inaka_test.flickrinaka.Connections.Connection;
import net.inaka_test.flickrinaka.Models.Photo;
import net.inaka_test.flickrinaka.Utils.UtilsLocation;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Jonathan Garcia on 11/7/2015.
 * <p/>
 * List of pictures near the user's location
 */
public class MainActivity extends AppCompatActivity {

    private Context context = this;
    private ListView listView;
    private PhotosAdapter photosAdapter;
    private List<Photo> photoList;
    private ToggleButton toggleButton;
    private ProgressBar initialProgressBar;
    private ProgressBar reloadProgressBar;
    private int preLast;
    /**
     * current page
     */
    private int page = 1;
    private GridView gridView;

    private LocationManager locationManager;
    private LocationListener locationListener;
    private double currentLatitude;
    private double currentLongitude;
    private boolean locationRequestIsOn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        initialProgressBar = (ProgressBar) findViewById(R.id.initialProgressBar);
        reloadProgressBar = (ProgressBar) findViewById(R.id.reloadProgressBar);
        //initialProgressBar.setVisibility(View.GONE);
        toggleButton = (ToggleButton)findViewById(R.id.toggleButton);
        toggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    // The toggle is enabled
                    String provider = UtilsLocation.selectLocationProvider(context);
                    locationManager.requestLocationUpdates(provider, 40000, 10, locationListener);
                } else {
                    // The toggle is disabled
                    stopGeoData();
                }
            }
        });

        listView = (ListView) findViewById(R.id.listView);
        photoList = new ArrayList<>();
        photosAdapter = new PhotosAdapter(context, photoList);
        listView.setAdapter(photosAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(context, PhotoDetail.class);
                intent.putExtra("photo", ((Photo) parent.getAdapter().getItem(position)));
                startActivity(intent);

                /*Intent intent = new Intent(context, MapsActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("photoList", (Serializable) photoList);
                intent.putExtras(bundle);
                startActivity(intent);*/
            }
        });

        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                switch (view.getId()) {
                    case R.id.listView:

                        // Make your calculation stuff here. You have all your
                        // needed info from the parameters of this function.

                        // Sample calculation to determine if the last
                        // item is fully visible.
                        final int lastItem = firstVisibleItem + visibleItemCount;
                        if (lastItem == totalItemCount) {
                            if (preLast != lastItem) { //to avoid multiple calls for last item
                                Log.d("LAST", "Last");
                                //Toast.makeText(context, "last", Toast.LENGTH_SHORT).show();
                                preLast = lastItem;
                                reloadProgressBar.setVisibility(View.VISIBLE);
                                loadMorePhotos(currentLatitude, currentLongitude);
                            }
                        }
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (UtilsLocation.isLocationEnabled(context)) {
            if (!locationRequestIsOn) {
                locationRequestIsOn = true;
                //toggleButton.setChecked(true);
                initGeoData();
            }
        } else {
            locationRequestIsOn = false;
            if (locationManager != null) {
                locationManager.removeUpdates(locationListener);
            }
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            // Add the buttons
            builder.setPositiveButton(R.string.alert_dialog_config, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    // User clicked OK button
                    Intent myIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    startActivity(myIntent);
                }
            });
            builder.setNegativeButton(R.string.alert_dialog_cancel, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    // User cancelled the dialog
                    finish();
                }
            });
            builder.setTitle("Atencion");
            builder.setMessage(getString(R.string.active_location));
            AlertDialog dialog = builder.create();
            dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    finish();
                }
            });
            dialog.show();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (locationManager != null) {
            locationManager.removeUpdates(locationListener);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_map) {
            if (photoList.size() > 0) {
                Intent intent = new Intent(context, MapsActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("photoList", (Serializable) photoList);
                intent.putExtras(bundle);
                startActivity(intent);
            } else {
                Toast.makeText(context, getString(R.string.mapa_no_data), Toast.LENGTH_SHORT).show();
            }
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    /**
     * load more photos from API
     *
     * @param latitude
     * @param longitude
     */
    private void loadMorePhotos(double latitude, double longitude) {

        Map<String, String> params = new HashMap<>();
        params.put("latitude", String.valueOf(latitude));
        params.put("longitude", String.valueOf(longitude));
        params.put("page", String.valueOf(page));

        Connection.getPhotos(params, new Connection.NetworkResponse<List<Photo>>() {
            @Override
            public void onNetworkResponse(List<Photo> response, VolleyError error) {
                initialProgressBar.setVisibility(View.GONE);
                reloadProgressBar.setVisibility(View.GONE);

                if (response != null) {
                    int cursorPosition = photoList.size();
                    photoList.addAll(response);
                    photosAdapter.notifyDataSetChanged();
                    listView.smoothScrollToPosition(cursorPosition);
                    page++;
                } else {
                    Toast.makeText(context, getString(R.string.network_error), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    /**
     * load new photos when change user location
     *
     * @param latitude
     * @param longitude
     */
    private void loadNewGeoPhotos(double latitude, double longitude) {

        Map<String, String> params = new HashMap<>();
        params.put("latitude", String.valueOf(latitude));
        params.put("longitude", String.valueOf(longitude));
        params.put("page", String.valueOf(1));

        Connection.getPhotos(params, new Connection.NetworkResponse<List<Photo>>() {
            @Override
            public void onNetworkResponse(final List<Photo> response, VolleyError error) {
                initialProgressBar.setVisibility(View.GONE);
                //reloadProgressBar.setVisibility(View.GONE);

                if (response != null) {
                    //int cursorPosition = photoList.size();
                    photoList.clear();
                    photoList.addAll(response);
                    photosAdapter.notifyDataSetChanged();
                    listView.smoothScrollToPosition(0);
                    page = 2;
                    Toast.makeText(context, getString(R.string.gallery_updated), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    /**
     * init track user location
     */
    private void initGeoData() {

        locationManager = (LocationManager) this.context.getSystemService(Context.LOCATION_SERVICE);
        locationListener = new MyLocation();
        String provider = UtilsLocation.selectLocationProvider(context);
        locationManager.requestLocationUpdates(provider, 60000, 10, locationListener);

        List<String> matchingProviders = locationManager.getAllProviders();
        for (String providerName : matchingProviders) {
            Location location = locationManager.getLastKnownLocation(providerName);
            if (location != null) {
                currentLatitude = location.getLatitude();
                currentLongitude = location.getLongitude();
                loadNewGeoPhotos(location.getLatitude(), location.getLongitude());
                break;
            }
        }
    }

    /**
     * stop track user location
     */
    private void stopGeoData() {
        locationManager.removeUpdates(locationListener);
    }

    /**
     * Location Listener
     */
    private class MyLocation implements LocationListener {

        @Override
        public void onLocationChanged(Location location) {
            // TODO Auto-generated method stub

            if (location != null) {

                Log.i("LOCATION CHANGED", location.getLatitude() + "");
                Log.i("LOCATION CHANGED", location.getLongitude() + "");
                currentLatitude = location.getLatitude();
                currentLongitude = location.getLongitude();
                loadNewGeoPhotos(location.getLatitude(), location.getLongitude());
            }
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
}