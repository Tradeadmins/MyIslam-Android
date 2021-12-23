package com.krishiv.myislam.dto;

import com.google.gson.annotations.SerializedName;

public class HadithFavouriteModel {
    int collectionId, chapterId, hadithId;
    String bookname;

    public String getBookname() {
        return bookname;
    }

    public void setBookname(String bookname) {
        this.bookname = bookname;
    }

    Boolean isLike =false;

    @SerializedName("Version for translation ok")
    String Version;

    public HadithFavouriteModel(int collectionId, int chapterId, int hadithId, boolean isLike,String bookname) {
        this.collectionId = collectionId;
        this.chapterId = chapterId;
        this.hadithId = hadithId;
        this.isLike = isLike;
        this.bookname=bookname;
    }

    public int getCollectionId() {
        return collectionId;
    }

    public void setCollectionId(int collectionId) {
        this.collectionId = collectionId;
    }

    public int getChapterId() {
        return chapterId;
    }

    public void setChapterId(int chapterId) {
        this.chapterId = chapterId;
    }

    public int getHadithId() {
        return hadithId;
    }

    public void setHadithId(int hadithId) {
        this.hadithId = hadithId;
    }

    public String getVersion() {
        return Version;
    }

    public void setVersion(String version) {
        Version = version;
    }


    public Boolean getLike() {
        return isLike;
    }

    public void setLike(Boolean like) {
        isLike = like;
    }
}
