package com.krishiv.myislam.dto;

public class DuasCategoryModel {
    int duaCategoryId;
    String duaCategoryName;

    public DuasCategoryModel() {
    }

    public DuasCategoryModel(int duaCategoryId, String duaCategoryName) {
        this.duaCategoryId = duaCategoryId;
        this.duaCategoryName = duaCategoryName;
    }

    public int getDuaCategoryId() {
        return duaCategoryId;
    }

    public void setDuaCategoryId(int duaCategoryId) {
        this.duaCategoryId = duaCategoryId;
    }

    public String getDuaCategoryName() {
        return duaCategoryName;
    }

    public void setDuaCategoryName(String duaCategoryName) {
        this.duaCategoryName = duaCategoryName;
    }

    @Override
    public String toString() {
        return this.duaCategoryName;            // What to display in the Spinner list.
    }
}
