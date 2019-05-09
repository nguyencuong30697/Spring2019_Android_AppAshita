package com.example.firebaseloginauth.DTO;

import java.io.Serializable;

public class CoffeeDTO implements Serializable {
    private String ifCoffee;
    private double LatLng_long;
    private double LatLng_lat;
    private String information;
    private String photoCF;

    public CoffeeDTO(){

    }

    public CoffeeDTO(String idCoffee,double LatLng_long,double LatLng_lat,String info,String photoCF){
        this.ifCoffee = idCoffee;
        this.LatLng_lat = LatLng_lat;
        this.LatLng_long = LatLng_long;
        this.information = info;
        this.photoCF = photoCF;
    }

    public String getIfCoffee() {
        return ifCoffee;
    }

    public void setIfCoffee(String ifCoffee) {
        this.ifCoffee = ifCoffee;
    }

    public double getLatLng_long() {
        return LatLng_long;
    }

    public void setLatLng_long(double latLng_long) {
        LatLng_long = latLng_long;
    }

    public double getLatLng_lat() {
        return LatLng_lat;
    }

    public void setLatLng_lat(double latLng_lat) {
        LatLng_lat = latLng_lat;
    }

    public String getInformation() {
        return information;
    }

    public void setInformation(String information) {
        this.information = information;
    }

    public String getPhotoCF() {
        return photoCF;
    }

    public void setPhotoCF(String photoCF) {
        this.photoCF = photoCF;
    }
}
