package net.inaka.Flickr.geoFlickr.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by griveroa on 4/15/15.
 */
public class Photo implements Parcelable {
    public String farm;
    public String id;
    public String isFamily;
    public String isFriend;
    public String isPublic;
    public String owner;
    public String secret;
    public String server;
    public String title;
    public double latitude;
    public double longitude;

    public Photo(){

    }

    public Photo(Parcel source){
        this.farm = source.readString();
        this.id = source.readString();
        this.isFamily = source.readString();
        this.isFriend = source.readString();
        this.isPublic = source.readString();
        this.owner = source.readString();
        this.secret = source.readString();
        this.server = source.readString();
        this.title = source.readString();
        this.latitude = source.readDouble();
        this.longitude = source.readDouble();
    }

    @Override
    public int describeContents() {
        return this.hashCode();
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeString(this.farm);
        parcel.writeString(this.id);
        parcel.writeString(this.isFamily);
        parcel.writeString(this.isFriend);
        parcel.writeString(this.isPublic);
        parcel.writeString(this.owner);
        parcel.writeString(this.secret);
        parcel.writeString(this.server);
        parcel.writeString(this.title);
        parcel.writeDouble(this.latitude);
        parcel.writeDouble(this.longitude);

    }

    public static final Creator<Photo> CREATOR = new Creator<Photo>() {
        @Override
        public Photo createFromParcel(Parcel parcel) {
            return new Photo(parcel);
        }

        public Photo[] newArray(int size){
            return new Photo[size];
        }
    };

}
