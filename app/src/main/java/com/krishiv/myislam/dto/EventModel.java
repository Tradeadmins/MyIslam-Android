package com.krishiv.myislam.dto;

public class EventModel {

    int myEventId, myEventCategory;
    String myEventName, address, city, country, mobileNumber, description, myEventLatitude, myEventLongitude, myEventStartDate,
            myEventEndDate, distance;
    boolean myEventMinor;

    public int getMyEventId() {
        return myEventId;
    }

    public void setMyEventId(int myEventId) {
        this.myEventId = myEventId;
    }

    public int getMyEventCategory() {
        return myEventCategory;
    }

    public void setMyEventCategory(int myEventCategory) {
        this.myEventCategory = myEventCategory;
    }

    public String getMyEventName() {
        return myEventName;
    }

    public void setMyEventName(String myEventName) {
        this.myEventName = myEventName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getMyEventLatitude() {
        return myEventLatitude;
    }

    public void setMyEventLatitude(String myEventLatitude) {
        this.myEventLatitude = myEventLatitude;
    }

    public String getMyEventLongitude() {
        return myEventLongitude;
    }

    public void setMyEventLongitude(String myEventLongitude) {
        this.myEventLongitude = myEventLongitude;
    }

    public String getMyEventStartDate() {
        return myEventStartDate;
    }

    public void setMyEventStartDate(String myEventStartDate) {
        this.myEventStartDate = myEventStartDate;
    }

    public String getMyEventEndDate() {
        return myEventEndDate;
    }

    public void setMyEventEndDate(String myEventEndDate) {
        this.myEventEndDate = myEventEndDate;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public boolean isMyEventMinor() {
        return myEventMinor;
    }

    public void setMyEventMinor(boolean myEventMinor) {
        this.myEventMinor = myEventMinor;
    }
}
