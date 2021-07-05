package com.example.car.SendLocationUtility;

public class LocationData {

    private double longitude;
    private double latitude;
    private String date;


    public LocationData(double longitude, double latitude , String date) {
        this.longitude = longitude;
        this.latitude = latitude;
        this.date = date;

    }

    public double getLongitude() {
        return longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public String getDate(){
        return date;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public void setDate(String date){
        this.date = date;
    }
}
