package com.krishiv.myislam.fragment;

import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.internal.LinkedTreeMap;
import com.google.gson.reflect.TypeToken;
import com.krishiv.myislam.API.Api;
import com.krishiv.myislam.ItemClickListener;
import com.krishiv.myislam.R;
import com.krishiv.myislam.activity.Event;
import com.krishiv.myislam.adapter.EventListAdapter;
import com.krishiv.myislam.dto.EventModel;
import com.krishiv.myislam.dto.ResultComman;
import com.krishiv.myislam.menu.BaseMenuActivity;
import com.krishiv.myislam.utils.GPSTracker;
import com.krishiv.myislam.utils.GlobalTask;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EventList extends Fragment implements ItemClickListener, EventListAdapter.layoutItemClicked {

    EventListAdapter adapter;
    ArrayList<EventModel> data;
    RecyclerView recyclerView;
    GPSTracker gpsTracker;
    Context context;
    String PAGE_SIZE = "50";
    int PAGE_INDEX = 1;
    Double latt,lng;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fg_event_list, container, false);

        context = getActivity();
        gpsTracker = new GPSTracker(context);
        recyclerView = view.findViewById(R.id.recyclerview);

        data = new ArrayList<>();

        adapter = new EventListAdapter(context, data, this);
        adapter.setClickListener(this);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);

        if (data.size() <= 0) {
            callApiGetEvents();
        }

        return view;
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
                        //GlobalTask.showToastMessage(context, resultObj.getContent().toString());
                    } else if (resultObj.getContent() instanceof LinkedTreeMap) {
                        try {
                            JsonObject jsonObject = new Gson().toJsonTree(resultObj.getContent()).getAsJsonObject();
                            data.clear();
                            if (jsonObject.has("response") && jsonObject.get("totalCount").getAsInt() != 0) {
                                String strJson = jsonObject.get("response").toString();
                                Type listType = new TypeToken<List<EventModel>>() {
                                }.getType();

                                data.addAll((ArrayList<EventModel>) new Gson().fromJson(strJson, listType));
                            }
                        } catch (Exception e) {
                        }
                        adapter.notifyDataSetChanged();
                    }
                } else {
                    adapter.notifyDataSetChanged();
                    GlobalTask.showTostErrorResponse(context, response.errorBody());
                }
                GlobalTask.hideProgressDialog();
            }

            @Override
            public void onFailure(Call<ResultComman> call, Throwable t) {
                GlobalTask.hideProgressDialog();
            }
        });
    }

    @Override
    public void itemClicked(View View, int position, int option) {

    }
//new code

    @Override
    public void onItemClick(int position, String lat, String longitude, String eventname, String address) {
        String map = "http://maps.google.co.in/maps?q=" + address;
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(map));
        context.startActivity(intent);
    }

}
