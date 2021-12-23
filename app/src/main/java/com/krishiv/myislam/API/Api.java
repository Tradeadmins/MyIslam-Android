package com.krishiv.myislam.API;

import com.krishiv.myislam.API.ApiInterface;
import com.krishiv.myislam.utils.ApiConstant;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class Api {
    private static Retrofit retrofit = null;
    public static ApiInterface getClient() {
        if (retrofit==null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(ApiConstant.API_HOST)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        ApiInterface api = retrofit.create(ApiInterface.class);
        return api;
    }
}
