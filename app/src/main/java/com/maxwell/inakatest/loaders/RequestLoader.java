package com.maxwell.inakatest.loaders;

import com.android.volley.Request;

public abstract class RequestLoader {

    public Request getRequest(){
        Request request = generateRequest(generateUrl());
        return request;
    }

    protected abstract Request generateRequest(String url);
    protected abstract String generateUrl();

}
