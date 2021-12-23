package com.krishiv.myislam.activity;

import android.content.Intent;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.google.gson.JsonObject;
import com.krishiv.myislam.ItemClickListener;
import com.krishiv.myislam.R;
import com.krishiv.myislam.adapter.QurbaniAdapter;
import com.krishiv.myislam.dto.GoogleApiModel;
import com.krishiv.myislam.menu.BaseMenuActivity;
import com.krishiv.myislam.API.ApiInterface;
import com.krishiv.myislam.utils.GPSTracker;
import com.krishiv.myislam.utils.GlobalTask;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Qurbani extends BaseMenuActivity implements ItemClickListener, View.OnClickListener,QurbaniAdapter.layoutItemClicked{

    QurbaniAdapter adapter;
    ArrayList<GoogleApiModel> data;
    RecyclerView recyclerView;
    GPSTracker gpsTracker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentViev(R.layout.activity_qurbani);

        recyclerView = findViewById(R.id.recyclerview);

        data = new ArrayList<>();
//        appendDefaultData();
        gpsTracker = new GPSTracker(context);

        adapter = new QurbaniAdapter(context, data,this);
        adapter.setClickListener(this);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);

        if (data.size() <= 1) {
            callApi();
        }
    }

    private void callApi() {
        GlobalTask.showProgressDialog(context);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://maps.googleapis.com")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiInterface api = retrofit.create(ApiInterface.class);

        String strSearch = "Halal+slaughter+house";
        api.GetSearchGoogle(strSearch, gpsTracker.getLatitude() + ","+ gpsTracker.getLongitude() + "")
                .enqueue(new Callback<JsonObject>() {
                    @Override
                    public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                        JsonObject resultObj = response.body();
                        if (resultObj != null) {
                            try {
                                JSONArray jaResult = new JSONObject(resultObj.toString()).getJSONArray("results");

                                JSONObject jsonObject;
                                GoogleApiModel dataObj;
                                data.clear();
                                for (int i=0; i<jaResult.length(); i++){
                                    jsonObject = jaResult.getJSONObject(i);
                                    dataObj = new GoogleApiModel();

                                    dataObj.setFormatted_address(jsonObject.getString("formatted_address"));
                                    dataObj.setName(jsonObject.getString("name"));
                                    dataObj.setImage(jsonObject.getString("icon"));

                                    dataObj.setLat(jsonObject.getJSONObject("geometry").getJSONObject("location").getDouble("lat"));
                                    dataObj.setLng(jsonObject.getJSONObject("geometry").getJSONObject("location").getDouble("lng"));

                                    Location locationA = new Location("point A");
                                    locationA.setLatitude(gpsTracker.getLatitude());
                                    locationA.setLongitude(gpsTracker.getLongitude());
                                    Location locationB = new Location("point B");
                                    locationB.setLatitude(dataObj.getLat());
                                    locationB.setLongitude(dataObj.getLng());

                                    dataObj.setDistanceFar(locationA.distanceTo(locationB) / 1000);

                                    data.add(dataObj);
                                }
//                                appendDefaultData();
                                adapter.notifyDataSetChanged();
                                if (data.size() == 0)
                                    GlobalTask.showToastMessage(context, getResources().getString(R.string.alert_qurbani_not_found));

                            } catch (Exception e) {
                            }
                        } else {
                            GlobalTask.showTostErrorResponse(context, response.errorBody());
                        }
                        GlobalTask.hideProgressDialog();
                    }

                    @Override
                    public void onFailure(Call<JsonObject> call, Throwable t) {
                        GlobalTask.hideProgressDialog();
                    }
                });
    }

    void appendDefaultData(){
        GoogleApiModel googleApiModel = new GoogleApiModel();
        googleApiModel.setName("Donate to My Islam Charity");
        googleApiModel.setFormatted_address("00000");
        data.add(googleApiModel);
    }

    @Override
    public void itemClicked(View View, int position, int option) {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.arrow_back:
                finish();
                break;
        }
    }
    @Override
    public void onItemClick(int position, Double lat, Double longitude, String eventname, String address) {
        String geoUri = "http://maps.google.co.in/maps?q=" + address;
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(geoUri));
        context.startActivity(intent);
    }
}
