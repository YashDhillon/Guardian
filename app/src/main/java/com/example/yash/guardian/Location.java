package com.example.yash.guardian;

public class Location {

    private long UID;
    private long PhoneNumber;
    private double Longitude;
    private double Latitude;

    public Location(){

    }

    public double getLongitude(){
        return this.Longitude;
    }

    public double getLatitude(){
        return this.Latitude;
    }

    public long getUID(){
        return this.UID;
    }

    public long getPhoneNumber(){
        return this.PhoneNumber;
    }

    public void setLongitude(double longitude){
        this.Longitude = longitude;
    }

    public void setLatitude(double latitude){
        this.Latitude = latitude;
    }

    public void setUID(long UID){
        this.UID = UID;
    }

    public void setPhoneNumber(long PhoneNumber){
        this.PhoneNumber = PhoneNumber;
    }
}
