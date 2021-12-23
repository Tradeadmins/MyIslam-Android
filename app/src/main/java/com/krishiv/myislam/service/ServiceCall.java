package com.krishiv.myislam.service;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.krishiv.myislam.utils.GlobalTask;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by Admin on 25-12-2017.
 */

public class ServiceCall extends AsyncTask<String, Integer, String>{

    private Context context;
    private METHOD requestMethod;
    private String URL;
    private ProgressDialog progressDialog;
    private boolean isProgressDialog;
    private boolean isInternetAlert;
    private int requestToken;
    private RequestBody formBody;
    private OnServiceCallBack serviceCallBack;
    private List<ServiceHeader> dataHeader;

    private String serviceStatus = "0";     //0 - process, 1 - no_net, 2 - fail

    public interface OnServiceCallBack{
        void onServiceCallBack(int requestToken, String responseData);
    }

    public enum METHOD{
        POST, GET
    }

    private ServiceCall(ServiceCallBuilder builder){
        this.context = builder.context;
        this.requestMethod = builder.requestMethod;
        this.URL = builder.URL;
        this.progressDialog = builder.progressDialog;
        this.isInternetAlert = builder.isInternetAlert;
        this.isProgressDialog = builder.isProgressDialog;
        this.requestToken = builder.requestToken;
        this.formBody = builder.formBody;
        this.dataHeader = builder.dataHeader;
        this.execute();
    }

    public void setCallBack(OnServiceCallBack serviceCallBack) {
        this.serviceCallBack = serviceCallBack;
    }

    public static class ServiceCallBuilder{

        private Context context;
        private METHOD requestMethod = METHOD.POST;
        private String URL;
        private ProgressDialog progressDialog;
        private boolean isProgressDialog = true;
        private boolean isInternetAlert = true;
        private int requestToken;
        private RequestBody formBody;
        private List<ServiceHeader> dataHeader;
//        private FormBody.Builder formBuilder;
        private MultipartBody.Builder formBuilder;

        public ServiceCallBuilder(Context context, String URL, int requestToken) {
            this.context = context;
            this.URL = URL;
            this.requestToken = requestToken;
        }

        public ServiceCallBuilder addParam(String key, String value){

            if (formBuilder == null)
                formBuilder = new MultipartBody.Builder();

            Log.e("Param", key + " --> " + value);
            //formBuilder.add(key, value);
            formBuilder.addFormDataPart(key, value);
            return this;
        }

        public ServiceCallBuilder addFile(String key, String value, String path){

            if (formBuilder == null)
                formBuilder = new MultipartBody.Builder();

            File f = new File(path);
            formBuilder.addFormDataPart(key, f.getName(), RequestBody.create(MediaType.parse("image/*"), f));
            /*formBuilder.addPart(
                    Headers.of("Content-Disposition", "form-data; name=\""+key+"\""),
                    RequestBody.create(MediaType.parse("image*//*"), new File(path)));*/

            return this;
        }

        public ServiceCallBuilder addFiles(String key, String[] path){

            if (formBuilder == null)
                formBuilder = new MultipartBody.Builder();

            for (int i=0; i<path.length; i++) {
                File f = new File(path[i]);
                formBuilder.addFormDataPart(key+"["+i+"]", f.getName(), RequestBody.create(MediaType.parse("image/*"), f));
            }
            return this;
        }

        public ServiceCallBuilder setInternetAlert(boolean internetAlert) {
            this.isInternetAlert = internetAlert;
            return this;
        }

        public ServiceCallBuilder setRequestMethod(METHOD requestMethod) {
            this.requestMethod = requestMethod;
            return this;
        }

        public ServiceCallBuilder setProgressDialog(ProgressDialog progressDialog) {
            this.progressDialog = progressDialog;
            return this;
        }

        public ServiceCallBuilder setProgressDialog(boolean progressDialog) {
            this.isProgressDialog = progressDialog;
            return this;
        }

        public ServiceCallBuilder setDefaultJsonParam(String jsonParam) {
            this.addParam("json", jsonParam);
            return this;
        }

        public ServiceCallBuilder setHeaderParam(String key, String value) {
            if (dataHeader == null)
                dataHeader = new ArrayList<>();
            dataHeader.add(new ServiceHeader(key, value));
            return this;
        }

        public ServiceCall build() {
            if (formBuilder != null)
                this.formBody = formBuilder.setType(MultipartBody.FORM).build();

            return new ServiceCall(this);
        }
    }

    @Override
    protected String doInBackground(String... strings) {

        if (!ServiceMethod.checkConn(context)){
            return "1";
        }


        OkHttpClient client = new OkHttpClient();

        Log.e("MultipzService", "------------------------------------------------");
        Log.e("ServiceRequestTo", URL);
        //Log.e("ServiceRequestParam", formBody.toString());
        Log.e("MultipzService", "------------------------------------------------");

        Request request;
        if (requestMethod == METHOD.POST) {
            Request.Builder requestBuilder = new Request.Builder();
            if (dataHeader != null && dataHeader.size()>0) {
                for (int i = 0; i < dataHeader.size(); i++) {
                    requestBuilder.addHeader(dataHeader.get(i).getKey(), dataHeader.get(i).getValue());
                }
            }
            request = requestBuilder.url(URL).post(formBody).build();
            //request = new Request.Builder().url(URL).post(formBody).build();
        }
        else {
            Request.Builder requestBuilder = new Request.Builder();
            if (dataHeader != null && dataHeader.size()>0) {
                for (int i = 0; i < dataHeader.size(); i++) {
                    requestBuilder.addHeader(dataHeader.get(i).getKey(), dataHeader.get(i).getValue());
                }
            }
            request = requestBuilder.url(URL).get().build();
            //request = new Request.Builder().url(URL).get().build();
        }
        try {
            Response response = client.newCall(request).execute();
            return response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("MultipzService",""+e);
            return ""+e;
        }
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        if (isProgressDialog){
            if (progressDialog == null)
                progressDialog = ProgressDialog.show(context, "Wait", "Loading");
        }
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);

        Log.e("MultipzService", "------------------------------------------------");
        Log.e("ServiceResponse", s);
        Log.e("MultipzService", "------------------------------------------------");

        try {
            if (isProgressDialog){
                progressDialog.dismiss();
            }
        }catch (Exception e){}

        if (s.contentEquals("1")) {
            if (isInternetAlert) {
                GlobalTask.showToastMessage(context, "Internet connection not detacted");
            }
            return;
        }
        serviceCallBack.onServiceCallBack(requestToken, s);
    }
}
