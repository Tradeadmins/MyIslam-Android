package com.krishiv.myislam.dto;

import com.google.gson.annotations.SerializedName;

public  class ChapterModel {


    @SerializedName("book")
    private int book;

    public int getBook() {
        return book;
    }

    public void setBook(int book) {
        this.book = book;
    }
}
