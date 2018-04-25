package com.example.anushan.tourguide;

/**
 * Created by Anushan on 3/8/2018.
 */

public class Historical_Site {
    private String Name, District,imageUrl,information;
    private double Latitude,Longitude,rate;


    public Historical_Site() {
    }

    public Historical_Site(String name, String district, String imageUrl, String information, double latitude, double longitude, double rate) {
        Name = name;
        District = district;
        this.imageUrl = imageUrl;
        this.information = information;
        Latitude = latitude;
        Longitude = longitude;
        this.rate = rate;
    }

    public String getName() {
        return Name;
    }

    public String getDistrict() {
        return District;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getInformation() {
        return information;
    }

    public double getLatitude() {
        return Latitude;
    }

    public double getLongitude() {
        return Longitude;
    }

    public double getRate() {
        return rate;
    }

    public void setName(String name) {
        Name = name;
    }

    public void setDistrict(String district) {
        District = district;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public void setInformation(String information) {
        this.information = information;
    }

    public void setLatitude(double latitude) {
        Latitude = latitude;
    }

    public void setLongitude(double longitude) {
        Longitude = longitude;
    }

    public void setRate(double rate) {
        this.rate = rate;
    }
}
