package com.krishiv.myislam.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.internal.LinkedTreeMap;
import com.google.gson.reflect.TypeToken;
import com.krishiv.myislam.ItemClickListener;
import com.krishiv.myislam.R;
import com.krishiv.myislam.activity.EventCreate;
import com.krishiv.myislam.adapter.EventMyAdapter;
import com.krishiv.myislam.dto.EventModel;
import com.krishiv.myislam.dto.ResultComman;
import com.krishiv.myislam.menu.BaseMenuActivity;
import com.krishiv.myislam.API.Api;
import com.krishiv.myislam.utils.GlobalTask;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EventMy extends Fragment implements ItemClickListener {

    EventMyAdapter adapter;
    ArrayList<EventModel> data;
    RecyclerView recyclerView;
    Context context;

    String PAGE_SIZE = "50";
    int PAGE_INDEX = 1;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fg_event_mylist, container, false);

        context = getActivity();

        recyclerView = view.findViewById(R.id.recyclerview);

        data = new ArrayList<>();

        adapter = new EventMyAdapter(context, data);
        adapter.setClickListener(this);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);

        /*if (data.size()<=0) {
            callApiGetEvents();
        }*/

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        callApiGetEvents();
    }

    private void callApiGetEvents() {
        GlobalTask.showProgressDialog(context);
        Api.getClient().GetAllEventsByUserId(PAGE_INDEX+"", PAGE_SIZE,
                BaseMenuActivity.userProfile.getUserId(), "Bearer " + BaseMenuActivity.userProfile.getAccess_token()).enqueue(new Callback<ResultComman>() {
            @Override
            public void onResponse(Call<ResultComman> call, Response<ResultComman> response) {
                ResultComman resultObj = response.body();

                if (resultObj != null) {
                    if (resultObj.getContent() instanceof String) {
                        //GlobalTask.showToastMessage(context, resultObj.getContent().toString());
                    } else if (resultObj.getContent() instanceof LinkedTreeMap) {
                        JsonObject jsonObject = new Gson().toJsonTree(resultObj.getContent()).getAsJsonObject();
                        data.clear();
                        if (jsonObject.has("response") && jsonObject.get("totalCount").getAsInt() != 0) {
                            String strJson = jsonObject.get("response").toString();
                            Type listType = new TypeToken<List<EventModel>>() {
                            }.getType();
                            data.addAll((ArrayList<EventModel>) new Gson().fromJson(strJson, listType));
                        }else{
                            //GlobalTask.showToastMessage(context, "Data not available");
                        }
                        adapter.notifyDataSetChanged();
                    }
                } else {
                    adapter.notifyDataSetChanged();
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

    @Override
    public void itemClicked(View View, int position, int clickedId) {
        if (clickedId == 1){
            startActivity(new Intent(context, EventCreate.class).putExtra("data", new Gson().toJson(data.get(position))));
        }else{
            callDeleteApi(data.get(position).getMyEventId());
        }
    }

    private void callDeleteApi(final int myEventId) {
        GlobalTask.showProgressDialog(context);
        Api.getClient().DeleteEvent(myEventId+"", "Bearer " + BaseMenuActivity.userProfile.getAccess_token()).enqueue(new Callback<ResultComman>() {
            @Override
            public void onResponse(Call<ResultComman> call, Response<ResultComman> response) {
                ResultComman resultObj = response.body();
                if (resultObj != null) {
                    if (resultObj.getContent() instanceof String) {
//                        GlobalTask.showToastMessage(context, resultObj.getContent().toString());
                        for (int i=0; i<data.size(); i++){
                            if (data.get(i).getMyEventId() == myEventId){
                                data.remove(i);
                                break;
                            }
                        }
                        adapter.notifyDataSetChanged();
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
}