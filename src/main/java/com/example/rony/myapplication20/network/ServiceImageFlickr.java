package com.example.rony.myapplication20.network;


import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.rony.myapplication20.aplication.VolleySingleton;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONObject;

/**
 * Created by Rony on 25/04/2016.
 */
public class ServiceImageFlickr {

    String api_key = "88e05a10f2e86dcf0a57596908067aee";
    String latitude = "";
    String longitude = "";
    String id = "";
    String tag_json_obj = "json_obj_req";

    public ServiceImageFlickr(String id) {
        this.id = id;
    }


    public ServiceImageFlickr(String latitude, String longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public void callFlickrPhotosSearch(final DataCallback callback) {
        String url = String.format("https://api.flickr.com/services/rest/?method=flickr.photos.search&api_key=%s&lat=%s&lon=%s&format=json&nojsoncallback=1&extras=geo", api_key, latitude, longitude);
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST, url,
                null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Gson gson = new GsonBuilder().disableHtmlEscaping().create();
                try {
                    ObjectFlickr oFlickr = gson.fromJson(response.toString(), ObjectFlickr.class);
                    callback.onSuccess(oFlickr.photos.getPhoto());
                } catch (Exception e) {
                    callback.onError("El servicio de Flickr no esta disponible.");
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                callback.onError(error.getMessage());
            }
        });
        jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(5000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        int socketTimeout = 30000;//30 seconds
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        jsonObjReq.setRetryPolicy(policy);

        VolleySingleton.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);
    }

    public void callFlickrPhotoSise(final DataCallback callback) {
        String url = String.format("https://api.flickr.com/services/rest/?method=flickr.photos.getSizes&api_key=%s&photo_id=%s&format=json&nojsoncallback=1", api_key, id);

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET, url,
                null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Gson gson = new GsonBuilder().disableHtmlEscaping().create();
                try {
                    PhotoSizeFlickr oFlickr = gson.fromJson(response.toString(), PhotoSizeFlickr.class);
                    callback.onSuccessPhoto(oFlickr.getSizes());
                } catch (Exception e) {
                    callback.onError("El servicio de Flickr no esta disponible.");
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                callback.onError(error.getMessage());
            }
        });
        jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(5000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        int socketTimeout = 30000;//30 seconds
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        jsonObjReq.setRetryPolicy(policy);

        VolleySingleton.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);
    }

}
