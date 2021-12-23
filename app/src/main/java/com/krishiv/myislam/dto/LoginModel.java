package com.krishiv.myislam.dto;

public class LoginModel {
    int expires_in;
    boolean subscriptioncomplete;
    String userId, access_token, token_type, refresh_token, userName;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public int getExpires_in() {
        return expires_in;
    }

    public void setExpires_in(int expires_in) {
        this.expires_in = expires_in;
    }

    public String getAccess_token() {
        return access_token;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }

    public String getToken_type() {
        return token_type;
    }

    public void setToken_type(String token_type) {
        this.token_type = token_type;
    }

    public String getRefresh_token() {
        return refresh_token;
    }

    public void setRefresh_token(String refresh_token) {
        this.refresh_token = refresh_token;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public boolean isSubscriptioncomplete() {
        return subscriptioncomplete;
    }

    public void setSubscriptioncomplete(boolean subscriptioncomplete) {
        this.subscriptioncomplete = subscriptioncomplete;
    }
}
