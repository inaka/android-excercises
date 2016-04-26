package com.example.rony.myapplication20.network;

import java.util.ArrayList;

/**
 * Created by Rony on 25/04/2016.
 */
public class ObjectFlickr {

    Photos photos;
    String stat;

    public ObjectFlickr() {


    }

    public class Photos {
        private String page;
        private String pages;
        private String perpage;
        private String total;
        private ArrayList<Photo> photo;

        public Photos() {

        }

        public String getPage() {
            return page;
        }

        public void setPage(String page) {
            this.page = page;
        }

        public String getPages() {
            return pages;
        }

        public void setPages(String pages) {
            this.pages = pages;
        }

        public String getPerpage() {
            return perpage;
        }

        public void setPerpage(String perpage) {
            this.perpage = perpage;
        }

        public String getTotal() {
            return total;
        }

        public void setTotal(String total) {
            this.total = total;
        }

        public ArrayList<Photo> getPhoto() {
            return photo;
        }

        public void setPhoto(ArrayList<Photo> photo) {
            this.photo = photo;
        }
    }

    public class Photo {

        private String id;
        private String owner;
        private String secret;
        private String server;
        private String farm;
        private String title;
        private String ispublic;
        private String isfriend;
        private String isfamily;
        private String latitude;
        private String longitude;
        private String accuracy;
        private String context;
        private String place_id;
        private String woeid;
        private String geo_is_family;
        private String geo_is_friend;
        private String geo_is_contact;
        private String geo_is_public;

        public Photo() {
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

        public String getFarm() {
            return farm;
        }

        public void setFarm(String farm) {
            this.farm = farm;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getIspublic() {
            return ispublic;
        }

        public void setIspublic(String ispublic) {
            this.ispublic = ispublic;
        }

        public String getIsfriend() {
            return isfriend;
        }

        public void setIsfriend(String isfriend) {
            this.isfriend = isfriend;
        }

        public String getIsfamily() {
            return isfamily;
        }

        public void setIsfamily(String isfamily) {
            this.isfamily = isfamily;
        }

        public String getLatitude() {
            return latitude;
        }

        public void setLatitude(String latitude) {
            this.latitude = latitude;
        }

        public String getLongitude() {
            return longitude;
        }

        public void setLongitude(String longitude) {
            this.longitude = longitude;
        }

        public String getAccuracy() {
            return accuracy;
        }

        public void setAccuracy(String accuracy) {
            this.accuracy = accuracy;
        }

        public String getContext() {
            return context;
        }

        public void setContext(String context) {
            this.context = context;
        }

        public String getPlace_id() {
            return place_id;
        }

        public void setPlace_id(String place_id) {
            this.place_id = place_id;
        }

        public String getWoeid() {
            return woeid;
        }

        public void setWoeid(String woeid) {
            this.woeid = woeid;
        }

        public String getGeo_is_family() {
            return geo_is_family;
        }

        public void setGeo_is_family(String geo_is_family) {
            this.geo_is_family = geo_is_family;
        }

        public String getGeo_is_friend() {
            return geo_is_friend;
        }

        public void setGeo_is_friend(String geo_is_friend) {
            this.geo_is_friend = geo_is_friend;
        }

        public String getGeo_is_contact() {
            return geo_is_contact;
        }

        public void setGeo_is_contact(String geo_is_contact) {
            this.geo_is_contact = geo_is_contact;
        }

        public String getGeo_is_public() {
            return geo_is_public;
        }

        public void setGeo_is_public(String geo_is_public) {
            this.geo_is_public = geo_is_public;
        }
    }


}


