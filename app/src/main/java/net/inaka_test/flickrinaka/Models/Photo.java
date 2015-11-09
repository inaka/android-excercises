package net.inaka_test.flickrinaka.Models;

import android.os.Parcel;

import java.io.Serializable;

/**
 * Created by Jonathan Garcia on 11/7/2015.
 *
 * POJO Photo
 */
public class Photo implements Serializable {

    private String id;
    private String owner;
    private String secret;
    private String server;
    private int farm;
    private String title;
    private int ispublic;
    private int isfriend;
    private int isfamily;
    private double latitude;
    private double longitude;

    public Photo() {}

    public Photo(String id, String owner, String secret, String server, int farm, String title, int ispublic, int isfriend, int isfamily, double latitude, double longitude) {
        this.id = id;
        this.owner = owner;
        this.secret = secret;
        this.server = server;
        this.farm = farm;
        this.title = title;
        this.ispublic = ispublic;
        this.isfriend = isfriend;
        this.isfamily = isfamily;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public String getServer() {
        return server;
    }

    public void setServer(String server) {
        this.server = server;
    }

    public int getFarm() {
        return farm;
    }

    public void setFarm(int farm) {
        this.farm = farm;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getIspublic() {
        return ispublic;
    }

    public void setIspublic(int ispublic) {
        this.ispublic = ispublic;
    }

    public int getIsfriend() {
        return isfriend;
    }

    public void setIsfriend(int isfriend) {
        this.isfriend = isfriend;
    }

    public int getIsfamily() {
        return isfamily;
    }

    public void setIsfamily(int isfamily) {
        this.isfamily = isfamily;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
}
//// Json example ////
/*
 id: "22864982881"
owner: "25220271@N07"
secret: "c369e1ea61"
server: "5721"
farm: 6
title: "#MARCHADELORGULO #orgullogay"
ispublic: 1
isfriend: 0
isfamily: 0
latitude: "-34.589994"
longitude: "-58.379943"
*/