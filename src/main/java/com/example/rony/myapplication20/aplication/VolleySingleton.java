package com.example.rony.myapplication20.aplication;

/**
 * Created by Rony on 25/04/2016.
 */

import android.app.Application;
import android.location.Location;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;
import com.example.rony.myapplication20.network.ObjectFlickr;
import com.example.rony.myapplication20.network.Photo;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
//import com.example.volley.tools.LruBitmapCache;

public class VolleySingleton extends Application {

    public static final String TAG = VolleySingleton.class.getSimpleName();

    public List<ObjectFlickr.Photo> photoList = new ArrayList<ObjectFlickr.Photo>();

    private RequestQueue mRequestQueue;
    private ImageLoader mImageLoader;
    public Location location;

    private static VolleySingleton mInstance;

    @Override
    public void onCreate() {

        super.onCreate();
        mInstance = this;
    }

    public static synchronized VolleySingleton getInstance() {
        return mInstance;
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        }
        return mRequestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req, String tag) {
        // set the default tag if tag is empty
        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        getRequestQueue().add(req);
    }

    public <T> void addToRequestQueue(Request<T> req) {
        req.setTag(TAG);
        getRequestQueue().add(req);
    }

    public void cancelPendingRequests(Object tag) {
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(tag);
        }
    }

    /*
    public ImageLoader getImageLoader() {
        getRequestQueue();
        if (mImageLoader == null) {
            mImageLoader = new ImageLoader(this.mRequestQueue,
                    new LruBitmapCache());
        }
        return this.mImageLoader;
    }
*/

}