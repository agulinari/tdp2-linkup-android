package com.tddp2.grupo2.linkup.infrastructure.messaging;

import android.os.Parcel;
import android.os.Parcelable;


public class Notification implements Parcelable{

    public static String MATCH = "Match";
    public static String CHAT = "Chat";
    public static String FAKE = "Fake";
    public static String BAN = "Ban";

    public String fbid;
    public String fbidTo;
    public String messageTitle;
    public String messageBody;
    public String firstName;
    public String motive;

    public Notification(){
        this.fbid="";
        this.fbidTo="";
        this.messageTitle="";
        this.messageBody="";
        this.firstName="";
        this.motive= FAKE;
    }

    public Notification(String fbid, String fbidTo, String title, String message, String name, String motive) {
        this.fbid = fbid;
        this.fbidTo = fbidTo;
        this.messageTitle = title;
        this.messageBody = message;
        this.firstName = name;
        this.motive = motive;
    }

    protected Notification(Parcel in) {
        fbid = in.readString();
        fbidTo = in.readString();
        messageTitle = in.readString();
        messageBody = in.readString();
        firstName = in.readString();
        motive = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(fbid);
        dest.writeString(fbidTo);
        dest.writeString(messageTitle);
        dest.writeString(messageBody);
        dest.writeString(firstName);
        dest.writeString(motive);
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
