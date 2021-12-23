package com.krishiv.myislam.dto;

public class QuranTranslationModel {

    String firstLanguage, secondLanguage;
    int resource;

    public QuranTranslationModel(String firstLanguage, String secondLanguage) {
        this.firstLanguage = firstLanguage;
        this.secondLanguage = secondLanguage;
    }

    public String getFirstLanguage() {
        return firstLanguage;
    }

    public void setFirstLanguage(String firstLanguage) {
        this.firstLanguage = firstLanguage;
    }

    public String getSecondLanguage() {
        return secondLanguage;
    }

    public void setSecondLanguage(String secondLanguage) {
        this.secondLanguage = secondLanguage;
    }

    public int getResource() {
        return resource;
    }

    public void setResource(int resource) {
        this.resource = resource;
    }
}
