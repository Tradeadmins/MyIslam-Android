package com.krishiv.myislam.dto;

import java.util.ArrayList;

public class ResultLanguage {

    String version;
    int statusCode;
    ArrayList<LanguageTranslationModel> content;
    String result ;
    String timestamp ;
    String size;
    String errorMessage;

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public ArrayList<LanguageTranslationModel> getContent() {
        return content;
    }

    public void setContent(ArrayList<LanguageTranslationModel> content) {
        this.content = content;
    }
}
