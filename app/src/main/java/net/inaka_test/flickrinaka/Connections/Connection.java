package net.inaka_test.flickrinaka.Connections;

import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import net.inaka_test.flickrinaka.Application.AppController;
import net.inaka_test.flickrinaka.Models.Photo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Jonathan Garcia on 11/7/2015.
 */
public class Connection {

    public static final String PHOTO_URL = "https://farm%d.staticflickr.com/%s/%s_%s_%s.jpg";
    private static final String FLICKR_URL = "https://api.flickr.com/services/rest/?";

    private static final String FLICKR_API_KEY = "5b88d07a8a4e624454e93d093e55704d";

    /**
     *
     * @param params
     * @param networkResponse
     */
    public static void getPhotos(Map<String, String> params, final NetworkResponse networkResponse) {

        String url = FLICKR_URL+"method=flickr.photos.search&format=json&nojsoncallback=1&per_page=30&extras=geo&" +
                "api_key=%s&lat=%s&lon=%s&page=%s";

        url = String.format(url, FLICKR_API_KEY, params.get("latitude"), params.get("longitude"), params.get("page"));
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,
                url, null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("onResponse", response.toString());
                        List<Photo> photoArrayList = null;
                        try {
                            Gson gson = new Gson();
                            JSONArray jsonArray = response.getJSONObject("photos").getJSONArray("photo");
                            Type listType = new TypeToken<List<Photo>>(){}.getType();
                            photoArrayList = gson.fromJson(jsonArray.toString(),listType);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        networkResponse.onNetworkResponse(photoArrayList, null);
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        VolleyLog.d("onErrorResponse", "Error: " + error.getMessage());
                        networkResponse.onNetworkResponse(null, error);
                    }
        });

        AppController.getInstance().addToRequestQueue(jsonObjectRequest, "");
    }

    public interface NetworkResponse<T> {
        public void onNetworkResponse(T response, VolleyError error);
    }
}