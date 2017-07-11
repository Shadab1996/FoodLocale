package com.stinkinsweet.foodlocale;


public class Food {
    private String dish;
    private String place;
    private String loc;
    private String desc;
    private String name;
    private String cuisine;
    private String latitude;
    private String longitude;
    private String key;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getCuisine() {
        return cuisine;
    }

    public void setCuisine(String cuisine) {
        this.cuisine = cuisine;
    }


    public Food()
    {

    }

    public Food(String dish, String place, String loc, String desc,String name,String cuisine,String latitude,String longitude,String key) {
        this.key=key;
        this.dish = dish;
        this.place = place;
        this.loc = loc;
        this.desc = desc;
        this.name=name;
        this.cuisine=cuisine;
        this.latitude=latitude;
        this.longitude=longitude;
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
}
