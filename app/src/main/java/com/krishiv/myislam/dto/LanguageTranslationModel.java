package com.krishiv.myislam.dto;

public class LanguageTranslationModel {
    String quranTranslateId ;
    String quranTranslateLanguage;
    String quranTranslateBy;
    String quranTranslateUrl;
    boolean language_selection_status = false;

    public boolean isLanguage_selection_status() {
        return language_selection_status;
    }

    public void setLanguage_selection_status(boolean language_selection_status) {
        this.language_selection_status = language_selection_status;
    }

    public String getQuranTranslateId() {
        return quranTranslateId;
    }

    public void setQuranTranslateId(String quranTranslateId) {
        this.quranTranslateId = quranTranslateId;
    }

    public String getQuranTranslateLanguage() {
        return quranTranslateLanguage;
    }

    public void setQuranTranslateLanguage(String quranTranslateLanguage) {
        this.quranTranslateLanguage = quranTranslateLanguage;
    }

    public String getQuranTranslateBy() {
        return quranTranslateBy;
    }

    public void setQuranTranslateBy(String quranTranslateBy) {
        this.quranTranslateBy = quranTranslateBy;
    }

    public String getQuranTranslateUrl() {
        return quranTranslateUrl;
    }

    public void setQuranTranslateUrl(String quranTranslateUrl) {
        this.quranTranslateUrl = quranTranslateUrl;
    }
}
