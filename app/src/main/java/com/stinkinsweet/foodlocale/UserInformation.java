package com.stinkinsweet.foodlocale;

/**
 * Created by Funkies PC on 13-Nov-16.
 */
public class UserInformation {
    public String name;
    public String dish;
    public String place;
    public String loc;
    public String desc;
    public String cuisine;
    public String latitude;
    public String longitude;

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public UserInformation()
    {

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDish() {
        return dish;
    }

    public void setDish(String dish) {
        this.dish = dish;
    }
    public String getCuisine() {return cuisine;}

    public void setCuisine(String cuisine) {this.cuisine = cuisine;}

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public String getLoc() {
        return loc;
    }

    public void setLoc(String loc) {
        this.loc = loc;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }





    public UserInformation(String name, String dish, String place, String loc, String desc,String cuisine,String latitude,String longitude) {
        this.name = name;
        this.dish = dish;
        this.place = place;
        this.loc = loc;
        this.desc = desc;
        this.cuisine=cuisine;
        this.latitude=latitude;
        this.longitude=longitude;

    }
}
