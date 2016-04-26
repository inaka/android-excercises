package com.example.rony.myapplication20.network;

import java.util.ArrayList;

/**
 * Created by Rony on 25/04/2016.
 */
public class Sizes {
    private String canblog;
    private String canprint;
    private String candownload;

    private ArrayList<PhotoSize> size;

    public String getCanblog() {
        return canblog;
    }

    public void setCanblog(String canblog) {
        this.canblog = canblog;
    }

    public String getCanprint() {
        return canprint;
    }

    public void setCanprint(String canprint) {
        this.canprint = canprint;
    }

    public String getCandownload() {
        return candownload;
    }

    public void setCandownload(String candownload) {
        this.candownload = candownload;
    }

    public ArrayList<PhotoSize> getSize() {
        return size;
    }

    public void setSize(ArrayList<PhotoSize> size) {
        this.size = size;
    }
}