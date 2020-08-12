package com.example.dokkanseller.data_model;

import java.util.ArrayList;
import java.util.HashMap;

public class ProfileModel {
    private HashMap<String,String> Reviews ;
    private ArrayList<String> listOfcategIDs;
    private String key ;
    private String location;
    private String bio;
    private String coverImage;
    private String shopImage;
    private String shopName;
    private boolean fav  ;
    private float rate ;
    private  String phoneNum ;
    private  String about ;
    private  String policies;
    private  String fbLink;
    private  String instaLink;

    public ProfileModel() {
    }

    public ProfileModel(HashMap<String, String> reviews,
                        String key, String location, String bio, String coverImage,
                        String shopImage, ArrayList<String> listOfcategIDs, String shopName, boolean fav,
                        float rate, String phoneNum, String about, String policies, String fbLink, String instaLink) {
        Reviews = reviews;
        this.key = key;
        this.location = location;
        this.bio = bio;
        this.coverImage = coverImage;
        this.shopImage = shopImage;
        this.listOfcategIDs = listOfcategIDs;
        this.shopName = shopName;
        this.fav = fav;
        this.rate = rate;
        this.phoneNum = phoneNum;
        this.about = about;
        this.policies = policies;
        this.fbLink = fbLink;
        this.instaLink = instaLink;
    }

    public HashMap<String, String> getReviews() {
        return Reviews;
    }

    public void setReviews(HashMap<String, String> reviews) {
        Reviews = reviews;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getCoverImage() {
        return coverImage;
    }

    public void setCoverImage(String coverImage) {
        this.coverImage = coverImage;
    }

    public String getShopImage() {
        return shopImage;
    }

    public void setShopImage(String shopImage) {
        this.shopImage = shopImage;
    }

    public ArrayList<String> getListOfcategIDs() {
        return listOfcategIDs;
    }

    public void setListOfcategIDs(ArrayList<String> listOfcategIDs) {
        this.listOfcategIDs = listOfcategIDs;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public boolean isFav() {
        return fav;
    }

    public void setFav(boolean fav) {
        this.fav = fav;
    }

    public float getRate() {
        return rate;
    }

    public void setRate(float rate) {
        this.rate = rate;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    public String getPolicies() {
        return policies;
    }

    public void setPolicies(String policies) {
        this.policies = policies;
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
}
