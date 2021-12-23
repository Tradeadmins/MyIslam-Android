package com.krishiv.myislam.dto;

public class RegisterModel {
    String firstName, lastName, userName, email, phoneNumber="", password, languageCode, subscriptionType, provider="", externalUserId="";
    boolean subscriptionComplete = false;
    boolean fajrNotification, dhudrNotification, asarNotification, magribNotification, ishaNotification,
            fajrAlarm, dhudrAlarm, asarAlarm, magribAlarm, ishaAlarm;

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getLanguageCode() {
        return languageCode;
    }

    public void setLanguageCode(String languageCode) {
        this.languageCode = languageCode;
    }

    public String getSubscriptionType() {
        return subscriptionType;
    }

    public void setSubscriptionType(String subscriptionType) {
        this.subscriptionType = subscriptionType;
    }

    public boolean isFajrNotification() {
        return fajrNotification;
    }

    public void setFajrNotification(boolean fajrNotification) {
        this.fajrNotification = fajrNotification;
    }

    public boolean isDhudrNotification() {
        return dhudrNotification;
    }

    public void setDhudrNotification(boolean dhudrNotification) {
        this.dhudrNotification = dhudrNotification;
    }

    public boolean isAsarNotification() {
        return asarNotification;
    }

    public void setAsarNotification(boolean asarNotification) {
        this.asarNotification = asarNotification;
    }

    public boolean isMagribNotification() {
        return magribNotification;
    }

    public void setMagribNotification(boolean magribNotification) {
        this.magribNotification = magribNotification;
    }

    public boolean isIshaNotification() {
        return ishaNotification;
    }

    public void setIshaNotification(boolean ishaNotification) {
        this.ishaNotification = ishaNotification;
    }

    public boolean isFajrAlarm() {
        return fajrAlarm;
    }

    public void setFajrAlarm(boolean fajrAlarm) {
        this.fajrAlarm = fajrAlarm;
    }

    public boolean isDhudrAlarm() {
        return dhudrAlarm;
    }

    public void setDhudrAlarm(boolean dhudrAlarm) {
        this.dhudrAlarm = dhudrAlarm;
    }

    public boolean isAsarAlarm() {
        return asarAlarm;
    }

    public void setAsarAlarm(boolean asarAlarm) {
        this.asarAlarm = asarAlarm;
    }

    public boolean isMagribAlarm() {
        return magribAlarm;
    }

    public void setMagribAlarm(boolean magribAlarm) {
        this.magribAlarm = magribAlarm;
    }

    public boolean isIshaAlarm() {
        return ishaAlarm;
    }

    public void setIshaAlarm(boolean ishaAlarm) {
        this.ishaAlarm = ishaAlarm;
    }

    public String getProvider() {
        return provider;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }

    public String getExternalUserId() {
        return externalUserId;
    }

    public void setExternalUserId(String externalUserId) {
        this.externalUserId = externalUserId;
    }

    public boolean isSubscriptionComplete() {
        return subscriptionComplete;
    }

    public void setSubscriptionComplete(boolean subscriptionComplete) {
        this.subscriptionComplete = subscriptionComplete;
    }
}
