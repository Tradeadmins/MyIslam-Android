package com.krishiv.myislam.dto;

public class GoogleApiModel {
    String formatted_address, name, image;
    double lat, lng, distanceFar;

    public String getFormatted_address() {
        return formatted_address;
    }

    public void setFormatted_address(String formatted_address) {
        this.formatted_address = formatted_address;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public double getDistanceFar() {
        return distanceFar;
    }

    public void setDistanceFar(double distanceFar) {
        this.distanceFar = distanceFar;
    }
}
