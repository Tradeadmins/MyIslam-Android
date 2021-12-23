package com.krishiv.myislam.activity;

import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.JsonObject;
import com.krishiv.myislam.API.ApiInterface;
import com.krishiv.myislam.R;
import com.krishiv.myislam.dto.GoogleApiModel;
import com.krishiv.myislam.fragment.MosqueList;
import com.krishiv.myislam.fragment.MosqueMap;
import com.krishiv.myislam.menu.BaseMenuActivity;
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

public class Mosque extends BaseMenuActivity implements View.OnClickListener {

    public static ArrayList<GoogleApiModel> data;
    public static GetDataFromBase getDataFromBase;
    FrameLayout sub_container;
    MosqueList mosqueList;
    MosqueMap mosqueMap;
    GPSTracker gpsTracker;
    String activityFor = "mosque";
    TextView txt_title;
    RelativeLayout mosque;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentViev(R.layout.activity_mosque);

        sub_container = findViewById(R.id.sub_container);
        mosque = findViewById(R.id.frame);


        txt_title = findViewById(R.id.txt_title);

        gpsTracker = new GPSTracker(context);
        data = new ArrayList<>();

        mosqueList = new MosqueList();
        mosqueMap = new MosqueMap();

        setTitleOfScreen(1);

        if (savedInstanceState == null) {
            setFragment(mosqueList);
        }
        callApi();
    }

    //new code
    private void setTitleOfScreen(int isList) {
        try {
            activityFor = getIntent().getStringExtra("activity");
            if (activityFor.contentEquals("mosque")) {
                if (isList == 1) {
                    txt_title.setText(getResources().getString(R.string.near_mosque));
                } else {
                    txt_title.setText(getResources().getString(R.string.mosque));
                }
                mosque.setBackgroundResource(R.drawable.img_mosque);

            } else if (activityFor.contentEquals("restaurant")) {
                mosque.setBackgroundResource(R.drawable.img_restaurant);
                txt_title.setText(getResources().getString(R.string.halal_restaurant));
            }
        } catch (Exception e) {
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rd_list:
                setTitleOfScreen(1);
                setFragment(mosqueList);
                break;

            case R.id.rd_map:
                setTitleOfScreen(0);
                setFragment(mosqueMap);
                break;

            case R.id.arrow_back:
                finish();
                break;
        }
    }

    private void setFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(sub_container.getId(), fragment, "")
                .commit();
    }

    private void callApi() {
//https://maps.googleapis.com/maps/api/place/textsearch/json?query=Mosque&sensor=true&location=22.7196,75.8577&radius=20&key=AIzaSyAiLWZ4ofCimK3dzlZRTQ3oce5d7Pmkx-Q
        GlobalTask.showProgressDialog(context);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://maps.googleapis.com")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiInterface api = retrofit.create(ApiInterface.class);

        String strSearch = "";
        if (activityFor.contentEquals("mosque")) {
            strSearch = "Mosque";
        } else if (activityFor.contentEquals("restaurant")) {
            strSearch = "Halal+Restuarant";
        }

        api.GetSearchGoogle(strSearch, gpsTracker.getLatitude() + "," + gpsTracker.getLongitude() + "")
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
                                for (int i = 0; i < jaResult.length(); i++) {
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

                                    if (getDataFromBase != null)
                                        getDataFromBase.onDataGet();
                                }

                                if (data.size() == 0)
                                    GlobalTask.showToastMessage(context, "Data not available");

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

    public interface GetDataFromBase {
        void onDataGet();
    }
}
