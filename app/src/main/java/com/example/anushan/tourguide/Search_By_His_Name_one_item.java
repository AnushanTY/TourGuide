package com.example.anushan.tourguide;

/**
 * Created by Anushan on 5/6/2018.
 */

public class Search_By_His_Name_one_item {
    private  String Name, District,imageUrl;

    public Search_By_His_Name_one_item() {
    }

    public Search_By_His_Name_one_item(String name, String district, String imageUrl) {
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
