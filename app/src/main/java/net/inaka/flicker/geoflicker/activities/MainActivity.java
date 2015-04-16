package net.inaka.Flickr.geoFlickr.activities;


import android.app.ActivityManager;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Location;

import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;


import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import net.inaka.Flickr.geoFlickr.R;
import net.inaka.Flickr.geoFlickr.models.Photo;
import net.inaka.Flickr.geoFlickr.services.LocationService;


import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends ActionBarActivity {
    LoadPhotosTask mLoadPhotosTask;
    boolean mIsSearchingPhotos;

    Intent mLocationServiceIntent;
    LocationBroadcastReceiver mLocationBroadcastReceiver;

    GridView gridPhotos;
    ArrayList<Photo> mPhotos;
    ArrayAdapter<Photo> mArrayAdapter;

    DisplayImageOptions mImageOptions;

    Button buttonViewMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mImageOptions = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.mipmap.ic_launcher)
                .showImageForEmptyUri(R.mipmap.ic_launcher)
                .showImageOnFail(R.mipmap.ic_launcher)
                .cacheInMemory(false)
                .cacheOnDisk(false)
                .considerExifParams(true)
                .build();

        mLocationServiceIntent = new Intent(this, LocationService.class);
        gridPhotos = (GridView) findViewById(R.id.gridPhotos);

        mPhotos = new ArrayList<>();
        mArrayAdapter = buildAdapter();
        gridPhotos.setAdapter(mArrayAdapter);

        gridPhotos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Photo photo = mPhotos.get(position);

                String imageUrl = String.format("https://farm%s.staticflickr.com/%s/%s_%s_b.jpg",
                        photo.farm,
                        photo.server,
                        photo.id,
                        photo.secret);

                Intent intent = new Intent(MainActivity.this, PictureActivity.class);
                intent.putExtra("url", imageUrl);
                startActivity(intent);
            }
        });

        buttonViewMap = (Button)findViewById(R.id.buttonViewMap);
        buttonViewMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, MapsActivity.class);
                intent.putParcelableArrayListExtra("photos", mPhotos);
                startActivity(intent);
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_toggle_service) {
            if(isMyServiceRunning(LocationService.class)){
                stopService(mLocationServiceIntent);
                Toast.makeText(this, getText(R.string.text_service_stopped), Toast.LENGTH_LONG).show();
            } else {
                startService(mLocationServiceIntent);
                Toast.makeText(this, getText(R.string.text_service_started), Toast.LENGTH_LONG).show();
            }
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if(isMyServiceRunning(LocationService.class)){
            menu.getItem(0).setTitle(R.string.menu_service_stop);
        } else {
            menu.getItem(0).setTitle(R.string.menu_service_start);
        }

        return true;
    }

    @Override
    protected void onPause() {
        if (mLoadPhotosTask != null) {
            mLoadPhotosTask.cancel(true);
        }

        super.onPause();
    }

    @Override
    protected void onStart() {
        super.onStart();

        mLocationBroadcastReceiver = new LocationBroadcastReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(LocationService.GEO_Flickr_INTENT);
        registerReceiver(mLocationBroadcastReceiver, filter);

        startService(mLocationServiceIntent);
    }

    @Override
    protected void onStop() {
        unregisterReceiver(mLocationBroadcastReceiver);

        super.onStop();
    }

    private ArrayAdapter<Photo> buildAdapter() {
        return new ArrayAdapter<Photo>(this, R.layout.grid_item, mPhotos) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                ViewHolder holder = null;

                View view = convertView;
                if (view != null) {
                    holder = (ViewHolder) view.getTag();
                }

                if (holder == null) {
                    view = View.inflate(getContext(), R.layout.grid_item, null);
                    holder = new ViewHolder();
                    holder.image = (ImageView) view.findViewById(R.id.imagePhoto);
                    view.setTag(holder);
                }

                Photo photo = getItem(position);

                String imageUrl = String.format("https://farm%s.staticflickr.com/%s/%s_%s_t.jpg",
                        photo.farm,
                        photo.server,
                        photo.id,
                        photo.secret);

                ImageLoader.getInstance().displayImage(imageUrl, holder.image, mImageOptions);

                return view;
            }
        };
    }

    private class ViewHolder {
        ImageView image;
    }

    private class LoadPhotosTask extends AsyncTask<Location, Void, ResponseMessage> {
        ProgressDialog dialog = new ProgressDialog(MainActivity.this);

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            dialog.setMessage(getString(R.string.text_status));
            dialog.setCancelable(false);
            dialog.show();
        }

        @Override
        protected ResponseMessage doInBackground(Location... params) {
            ResponseMessage responseMessage = new ResponseMessage();

            Location location = params[0];
            String api_key = "e7ad43079913f57bc13df842caea48c5";
            String latitude = String.valueOf(location.getLatitude());
            String longitude = String.valueOf(location.getLongitude());

            try {
                String url = String.format("https://api.flickr.com/services/rest/?method=flickr.photos.search&api_key=%s&lat=%s&lon=%s&format=json&nojsoncallback=1&extras=geo", api_key, latitude, longitude);

                HttpClient client = new DefaultHttpClient();
                HttpGet request = new HttpGet(url);

                HttpResponse response = client.execute(request);
                HttpEntity entity = response.getEntity();

                String result = EntityUtils.toString(entity);
                entity.consumeContent();

                responseMessage.data = result;

            } catch (Exception ex) {
                responseMessage.message = ex.getMessage();
            }
            return responseMessage;
        }

        @Override
        protected void onPostExecute(ResponseMessage responseMessage) {
            super.onPostExecute(responseMessage);

            if (TextUtils.isEmpty(responseMessage.message)) {
                try {

                    mPhotos.clear();
                    gridPhotos.smoothScrollToPosition(0);

                    JSONObject jsonResponse = new JSONObject(responseMessage.data);
                    JSONObject jsonPhotoContent = jsonResponse.getJSONObject("photos");

                    JSONArray jsonPhotos = jsonPhotoContent.getJSONArray("photo");
                    for (int index = 0; index < jsonPhotos.length(); index++) {
                        JSONObject jsonPhoto = jsonPhotos.getJSONObject(index);

                        Photo photo = new Photo();
                        photo.farm = jsonPhoto.getString("farm");
                        photo.id = jsonPhoto.getString("id");
                        photo.isFamily = jsonPhoto.getString("isfamily");
                        photo.isFriend = jsonPhoto.getString("isfriend");
                        photo.isPublic = jsonPhoto.getString("ispublic");
                        photo.owner = jsonPhoto.getString("owner");
                        photo.secret = jsonPhoto.getString("secret");
                        photo.server = jsonPhoto.getString("server");
                        photo.title = jsonPhoto.getString("title");
                        photo.latitude = jsonPhoto.getDouble("latitude");
                        photo.longitude = jsonPhoto.getDouble("longitude");

                        mPhotos.add(photo);
                    }

                    mArrayAdapter.notifyDataSetChanged();

                } catch (Exception ex) {
                    Toast.makeText(MainActivity.this, ex.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                }
            } else {
                Toast.makeText(MainActivity.this, responseMessage.message, Toast.LENGTH_LONG).show();
            }

            mIsSearchingPhotos = false;
            dialog.dismiss();
        }
    }

    private class ResponseMessage {
        public String message;
        public String data;
    }

    private class LocationBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            double latitude = intent.getDoubleExtra("latitude", 0);
            double longitude = intent.getDoubleExtra("longitude", 0);

            if(!mIsSearchingPhotos) {
                mIsSearchingPhotos = true;

                Location location = new Location("GEO_Flickr");
                location.setLatitude(latitude);
                location.setLongitude(longitude);

                mLoadPhotosTask = new LoadPhotosTask();
                mLoadPhotosTask.execute(location);
            }
        }
    }

    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }
}
