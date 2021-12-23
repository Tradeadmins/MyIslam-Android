package com.krishiv.myislam.dto;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public  class HadithItemModel {


    @SerializedName("textArabic")
    private String textArabic;
    @SerializedName("fastIdentifier2")
    private String fastIdentifier2;
    @SerializedName("lastUpdated")
    private String lastUpdated;

    public HadithItemModel(String textArabic, String text, int hadith, int book, int collectionId) {
        this.textArabic = textArabic;
        this.text = text;
        this.hadith = hadith;
        this.book = book;
        this.collectionId = collectionId;
    }

    @SerializedName("links")
    private List<Links> links;
    @SerializedName("gradeFlag")
    private int gradeFlag;
    @SerializedName("text")
    private String text;
    @SerializedName("hadith")
    private int hadith;
    @SerializedName("book")
    private int book;
    @SerializedName("volume")
    private int volume;
    @SerializedName("collectionId")
    private int collectionId;
    @SerializedName("fastIdentifier")
    private String fastIdentifier;

    public String getTextArabic() {
        return textArabic;
    }

    public void setTextArabic(String textArabic) {
        this.textArabic = textArabic;
    }

    public String getFastIdentifier2() {
        return fastIdentifier2;
    }

    public void setFastIdentifier2(String fastIdentifier2) {
        this.fastIdentifier2 = fastIdentifier2;
    }

    public String getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(String lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public List<Links> getLinks() {
        return links;
    }

    public void setLinks(List<Links> links) {
        this.links = links;
    }

    public int getGradeFlag() {
        return gradeFlag;
    }

    public void setGradeFlag(int gradeFlag) {
        this.gradeFlag = gradeFlag;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getHadith() {
        return hadith;
    }

    public void setHadith(int hadith) {
        this.hadith = hadith;
    }

    public int getBook() {
        return book;
    }

    public void setBook(int book) {
        this.book = book;
    }

    public int getVolume() {
        return volume;
    }

    public void setVolume(int volume) {
        this.volume = volume;
    }

    public int getCollectionId() {
        return collectionId;
    }

    public void setCollectionId(int collectionId) {
        this.collectionId = collectionId;
    }

    public String getFastIdentifier() {
        return fastIdentifier;
    }

    public void setFastIdentifier(String fastIdentifier) {
        this.fastIdentifier = fastIdentifier;
    }

    public static class Links {
        @SerializedName("t")
        private String t;
        @SerializedName("l")
        private String l;

        public String getT() {
            return t;
        }

        public void setT(String t) {
            this.t = t;
        }

        public String getL() {
            return l;
        }

        public void setL(String l) {
            this.l = l;
        }
    }
}


