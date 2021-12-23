package com.krishiv.myislam.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RadioButton;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.internal.LinkedTreeMap;
import com.google.gson.reflect.TypeToken;
import com.krishiv.myislam.ItemClickListener;
import com.krishiv.myislam.R;
import com.krishiv.myislam.adapter.RequestPrayerAdapter;
import com.krishiv.myislam.dto.MakeDuasModel;
import com.krishiv.myislam.dto.RequestPrayerModel;
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

public class RequestForPrayer extends BaseMenuActivity implements View.OnClickListener, ItemClickListener {

    RequestPrayerAdapter adapter;
    ArrayList<RequestPrayerModel> data;
    RecyclerView recyclerView;

    String PAGE_SIZE = "50";
    int PAGE_INDEX = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentViev(R.layout.activity_request_for_prayer);

        recyclerView = findViewById(R.id.recyclerview);

        data = new ArrayList<>();

        adapter = new RequestPrayerAdapter(context, data);
        adapter.setClickListener(this);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);

        findViewById(R.id.fab_create_dua).setVisibility(View.GONE);
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (((RadioButton)findViewById(R.id.rd_all)).isChecked()){
            callApiGetAllPrayerRequest();
        }else{
            callApiGetAllPrayerRequestByUserId();
        }
    }

    private void callApiMakeDua(final RequestPrayerModel requestPrayerModel) {
        GlobalTask.showProgressDialog(context);
        MakeDuasModel makeDuasModel = new MakeDuasModel();
        makeDuasModel.setMakeDuaByUserId(userProfile.getUserId());
        makeDuasModel.setMakeDuaPrayerRequestId(requestPrayerModel.getPrayerRequestId());

        Api.getClient().makeDua(makeDuasModel, "Bearer " + userProfile.getAccess_token()).enqueue(new Callback<ResultComman>() {
            @Override
            public void onResponse(Call<ResultComman> call, Response<ResultComman> response) {
                ResultComman resultObj = response.body();
                if (resultObj != null) {
                    if (resultObj.getContent() instanceof String) {
                        requestPrayerModel.setPrayerRequestIsLiked(true);
                        requestPrayerModel.setPrayerRequestTotalDuaCount((Integer.parseInt(requestPrayerModel.getPrayerRequestTotalDuaCount())+1)+"");
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

    private void callApiGetAllPrayerRequest() {
        GlobalTask.showProgressDialog(context);
        Api.getClient().GetAllPrayerRequest(PAGE_INDEX+"", PAGE_SIZE, "Bearer " + userProfile.getAccess_token()).enqueue(new Callback<ResultComman>() {
            @Override
            public void onResponse(Call<ResultComman> call, Response<ResultComman> response) {
                ResultComman resultObj = response.body();

                if (resultObj != null) {
                    if (resultObj.getContent() instanceof String) {
                        //GlobalTask.showToastMessage(context, resultObj.getContent().toString());
                    } else if (resultObj.getContent() instanceof LinkedTreeMap) {
                        JsonObject jsonObject = new Gson().toJsonTree(resultObj.getContent()).getAsJsonObject();
                        String strJson = jsonObject.get("response").toString();
                        Type listType = new TypeToken<List<RequestPrayerModel>>() {}.getType();
                        data.clear();
                        data.addAll((ArrayList<RequestPrayerModel>) new Gson().fromJson(strJson, listType));
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

    private void callApiGetAllPrayerRequestByUserId() {
        GlobalTask.showProgressDialog(context);
        Api.getClient().GetAllPrayerRequestByUserId(userProfile.getUserId(), "Bearer " + userProfile.getAccess_token()).enqueue(new Callback<ResultComman>() {
            @Override
            public void onResponse(Call<ResultComman> call, Response<ResultComman> response) {
                ResultComman resultObj = response.body();
                if (resultObj != null) {
                    if (resultObj.getContent() instanceof String) {
                        //GlobalTask.showToastMessage(context, resultObj.getContent().toString());
                    } else if (resultObj.getContent() instanceof LinkedTreeMap) {
                        JsonObject jsonObject = new Gson().toJsonTree(resultObj.getContent()).getAsJsonObject();
                        String strJson = jsonObject.get("response").toString();
                        Type listType = new TypeToken<List<RequestPrayerModel>>() {}.getType();

                        data.clear();
                        data.addAll((ArrayList<RequestPrayerModel>) new Gson().fromJson(strJson, listType));
                        adapter.notifyDataSetChanged();
                    }

                } else {
                    data.clear();
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
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.fab_create_dua:
                startActivity(new Intent(context, RequestForDua.class));
                break;

            case R.id.rd_myreq:
                findViewById(R.id.fab_create_dua).setVisibility(View.VISIBLE);
                callApiGetAllPrayerRequestByUserId();
                break;

            case R.id.rd_all:
                findViewById(R.id.fab_create_dua).setVisibility(View.GONE);
                callApiGetAllPrayerRequest();
                break;

            case R.id.arrow_back:
                finish();
                break;
        }
    }

    @Override
    public void itemClicked(View View, int position, int clickedId) {
        if (!data.get(position).isPrayerRequestIsLiked()) {
            callApiMakeDua(data.get(position));
        }
    }
}
