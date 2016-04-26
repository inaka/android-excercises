package com.example.rony.myapplication20.network;

import com.android.volley.VolleyError;

import org.json.JSONObject;

import java.util.List;

/**
 * Created by Rony on 25/04/2016.
 */
public interface DataCallback {

    void onSuccess(List<ObjectFlickr.Photo> photos);

    void onSuccessPhoto(Sizes sizes);

    void onError(String error);
}
