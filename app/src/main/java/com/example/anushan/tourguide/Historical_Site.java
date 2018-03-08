package com.example.anushan.tourguide;

/**
 * Created by Anushan on 3/8/2018.
 */

public class Historical_Site {
    private String Name, District,imageUrl;
    public Historical_Site() {
    }

    public Historical_Site(String name, String district, String imageUrl) {
        Name = name;
        District = district;
        this.imageUrl = imageUrl;
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

    public void setName(String name) {
        Name = name;
    }

    public void setDistrict(String district) {
        District = district;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
