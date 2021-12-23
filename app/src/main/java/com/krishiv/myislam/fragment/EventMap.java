package com.krishiv.myislam.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.internal.LinkedTreeMap;
import com.google.gson.reflect.TypeToken;
import com.krishiv.myislam.API.Api;
import com.krishiv.myislam.R;
import com.krishiv.myislam.dto.EventModel;
import com.krishiv.myislam.dto.ResultComman;
import com.krishiv.myislam.menu.BaseMenuActivity;
import com.krishiv.myislam.utils.GPSTracker;
import com.krishiv.myislam.utils.GlobalTask;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EventMap extends Fragment implements OnMapReadyCallback {

    class MyInfoWindowAdapter implements GoogleMap.InfoWindowAdapter {

        private final View myContentsView;

        MyInfoWindowAdapter(){
            myContentsView = getLayoutInflater().inflate(R.layout.custom_map_info, null);
        }

        @Override
        public View getInfoContents(Marker marker) {

            TextView tvTitle = ((TextView)myContentsView.findViewById(R.id.title));
            tvTitle.setText(marker.getTitle());
            TextView tvSnippet = ((TextView)myContentsView.findViewById(R.id.snippet));
            tvSnippet.setText(marker.getSnippet());

            return myContentsView;
        }

        @Override
        public View getInfoWindow(Marker marker) {
            // TODO Auto-generated method stub
            return null;
        }

    }


    ArrayList<EventModel> data;
    Context context;
    GPSTracker gpsTracker;
    String PAGE_SIZE = "50";
    int PAGE_INDEX = 1;
    private GoogleMap mMap;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fg_event_map, container, false);

        SupportMapFragment mapFragment = (SupportMapFragment) this.getChildFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        gpsTracker = new GPSTracker(context);
        context = getActivity();
        data = new ArrayList<>();

        if (data.size() <= 0) {
            callApiGetEvents();
        }
        return view;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setInfoWindowAdapter(new MyInfoWindowAdapter());

        setMarkerOnMap();
    }

    private void callApiGetEvents() {
        GlobalTask.showProgressDialog(context);
        Api.getClient().GetAllEventsByLatLong(PAGE_INDEX + "", PAGE_SIZE + "",
                gpsTracker.getLatitude() + "", gpsTracker.getLongitude() + "",
                "Bearer " + BaseMenuActivity.userProfile.getAccess_token()).enqueue(new Callback<ResultComman>() {
            @Override
            public void onResponse(Call<ResultComman> call, Response<ResultComman> response) {
                ResultComman resultObj = response.body();
                if (resultObj != null) {
                    if (resultObj.getContent() instanceof String) {
                        // GlobalTask.showToastMessage(context, resultObj.getContent().toString());
                    } else if (resultObj.getContent() instanceof LinkedTreeMap) {
                        try {
                            JsonObject jsonObject = new Gson().toJsonTree(resultObj.getContent()).getAsJsonObject();
                            data.clear();
                            if (jsonObject.has("response") && jsonObject.get("totalCount").getAsInt() != 0) {
                                String strJson = jsonObject.get("response").toString();
                                Type listType = new TypeToken<List<EventModel>>() {
                                }.getType();

                                data.addAll((ArrayList<EventModel>) new Gson().fromJson(strJson, listType));
                                if (data.size() != 0)
                                    setMarkerOnMap();
                            } else {
                                //GlobalTask.showToastMessage(context, "Data not available");
                            }
                        } catch (Exception e) {
                        }
                    }
                } else {
                    //GlobalTask.showTostErrorResponse(context, response.errorBody());
                }
                GlobalTask.hideProgressDialog();
            }

            @Override
            public void onFailure(Call<ResultComman> call, Throwable t) {
                GlobalTask.hideProgressDialog();
            }
        });
    }

    private void setMarkerOnMap() {
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        if (data.size() > 0) {
            for (int i = 0; i < data.size(); i++) {
                LatLng sydney = new LatLng(Double.parseDouble(data.get(i).getMyEventLatitude()), Double.parseDouble(data.get(i).getMyEventLongitude()));
                Marker newMarker = mMap.addMarker(new MarkerOptions()
                        .position(sydney)
                        .title(data.get(i).getMyEventName())
                        .snippet(data.get(i).getAddress()));

                newMarker.setTitle(newMarker.getTitle());
//                mMap.addMarker(new MarkerOptions().position(sydney).title(data.get(i).getMyEventName() + " " + data.get(i).getAddress()));
                builder.include(sydney);
            }
            LatLngBounds bounds = builder.build();
            int padding = 0; // offset from edges of the map in pixels
            CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, padding);
            mMap.moveCamera(cu);
            mMap.animateCamera(cu);
        }
    }
}
