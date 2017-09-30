package com.tddp2.grupo2.linkup.infrastructure.messaging;

import android.os.Parcel;
import android.os.Parcelable;


public class Notification implements Parcelable{

    public String fbid;
    public String title;
    public String message;

    public Notification(){
        this.fbid="";
        this.title="";
        this.message="";
    }

    public Notification(String fbid, String title, String message) {
        this.fbid = fbid;
        this.title = title;
        this.message = message;
    }

    protected Notification(Parcel in) {
        fbid = in.readString();
        title = in.readString();
        message = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(fbid);
        dest.writeString(title);
        dest.writeString(message);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Notification> CREATOR = new Creator<Notification>() {
        @Override
        public Notification createFromParcel(Parcel in) {
            return new Notification(in);
        }

        @Override
        public Notification[] newArray(int size) {
            return new Notification[size];
        }
    };
}
