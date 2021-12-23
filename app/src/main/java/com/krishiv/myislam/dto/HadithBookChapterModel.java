package com.krishiv.myislam.dto;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public  class HadithBookChapterModel {


    @SerializedName("books")
    private List<Books> books;
    @SerializedName("Id")
    private String Id;
    @SerializedName("collection")
    private String collection;

    public List<Books> getBooks() {
        return books;
    }

    public void setBooks(List<Books> books) {
        this.books = books;
    }

    public String getId() {
        return Id;
    }

    public void setId(String Id) {
        this.Id = Id;
    }

    public String getCollection() {
        return collection;
    }

    public void setCollection(String collection) {
        this.collection = collection;
    }

    public static class Books {
        @SerializedName("Id")
        private String Id;
        @SerializedName("Name")
        private String Name;

        public String getId() {
            return Id;
        }

        public void setId(String Id) {
            this.Id = Id;
        }

        public String getName() {
            return Name;
        }

        public void setName(String Name) {
            this.Name = Name;
        }
    }
}
