package com.example.jaychou.finalproject;

/**
 * Created by jaychou on 4/29/17.
 */

import java.io.Serializable;
import java.util.ArrayList;

public class business implements Serializable {

    private String business_id;
    private String name;
    private String address;
    private String city;
    private String state;
    private String postCode;

    private String phone;

    private String image_url;


    private double lat;
    private double lng;

    private double stars;

    private int review_count;

    private boolean is_close;

    private ArrayList<String> categories;

    private ArrayList<String> hours;




    public String getBusiness_id() { return business_id; }
    public void setBusiness_id(String business_id) { this.business_id = business_id; }


    public String getName() { return name; }
    public void setName(String name) { this.name = name; }


    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }


    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }


    public String getState() { return state; }
    public void setState(String state) { this.state = state; }


    public String getPostCode() { return postCode; }
    public void setPostCode(String postCode) { this.postCode = postCode; }


    public double getlat() { return lat; }
    public void setlat(double lat) { this.lat = lat; }

    public double getLng() { return lng; }
    public void setLng(double lng) { this.lng = lng; }

    public double getStars() { return stars; }
    public void setStars(double stars) { this.stars = stars; }


    public int getReview_count() { return review_count; }
    public void setReview_count(int review_count) { this.review_count = review_count; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getImage_url() { return image_url; }
    public void setImage_url(String image_url) { this.image_url = image_url; }


    public boolean getIs_close() { return is_close; }
    public void setIs_close(boolean is_close) { this.is_close = is_close; }



    public ArrayList<String> getCategories(){return categories; }
    public void setCategories(ArrayList<String> categories){this.categories=categories; }


    public ArrayList<String> getHours(){return hours; }
    public void setHours(ArrayList<String> hours){this.hours=hours; }


    public ArrayList<business> elist = new ArrayList<business>();






}
