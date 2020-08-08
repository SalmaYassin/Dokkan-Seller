package com.example.dokkanseller.views.register;

import android.os.Parcel;
import android.os.Parcelable;

public class Step1Model implements Parcelable {
    private String location;
    private String phoneNum ;
    private String fbLink ;
    private String instaLink ;

    public Step1Model(String location, String phoneNum, String fbLink, String instaLink) {
        this.location = location;
        this.phoneNum = phoneNum;
        this.fbLink = fbLink;
        this.instaLink = instaLink;
    }

    public Step1Model(Parcel in) {
        location = in.readString();
        phoneNum = in.readString();
        fbLink = in.readString();
        instaLink = in.readString();
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    public String getFbLink() {
        return fbLink;
    }

    public void setFbLink(String fbLink) {
        this.fbLink = fbLink;
    }

    public String getInstaLink() {
        return instaLink;
    }

    public void setInstaLink(String instaLink) {
        this.instaLink = instaLink;
    }

    public static final Creator<Step1Model> CREATOR = new Creator<Step1Model>() {
        @Override
        public Step1Model createFromParcel(Parcel in) {
            return new Step1Model(in);
        }

        @Override
        public Step1Model[] newArray(int size) {
            return new Step1Model[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(location);
        parcel.writeString(phoneNum);
        parcel.writeString(fbLink);
        parcel.writeString(instaLink);
    }
}


