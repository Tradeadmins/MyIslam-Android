package com.krishiv.myislam.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.reflect.TypeToken;
import com.krishiv.myislam.ItemClickListener;
import com.krishiv.myislam.R;
import com.krishiv.myislam.adapter.CustomDuasAdapter;
import com.krishiv.myislam.dto.CustomDuaModel;
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

public class MyCustomDuas extends BaseMenuActivity implements ItemClickListener, View.OnClickListener{

    ArrayList<CustomDuaModel> data;
    CustomDuasAdapter adapter;
    RecyclerView recyclerView;
    int PAGE_INDEX = 1;

    FloatingActionButton fab_bead;
    LinearLayout lnr_bead;
    int beadCount = 0;
    TextView txt_count3,txt_count2,txt_count1;
    Vibrator vibe ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentViev(R.layout.activity_my_custom_duas);

        recyclerView = findViewById(R.id.recyclerview);
        fab_bead = findViewById(R.id.fab_bead);
        lnr_bead = findViewById(R.id.lnr_bead);
        txt_count3 = findViewById(R.id.txt_count3);
        txt_count2 = findViewById(R.id.txt_count2);
        txt_count1 = findViewById(R.id.txt_count1);

        data = new ArrayList<>();

        adapter = new CustomDuasAdapter(context, data);
        adapter.setClickListener(this);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);

        vibe = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
    }

    @Override
    protected void onResume() {
        super.onResume();
        callApi();
    }

    private void callApi() {
        GlobalTask.showProgressDialog(context);
        Api.getClient().GetAllCustomDuaByUserId(userProfile.getUserId(),"Bearer " + BaseMenuActivity.userProfile.getAccess_token()).enqueue(new Callback<ResultComman>() {
            @Override
            public void onResponse(Call<ResultComman> call, Response<ResultComman> response) {
                ResultComman resultObj = response.body();
                if (resultObj != null) {
                    if (resultObj.getContent() instanceof String) {
                        GlobalTask.showToastMessage(context, resultObj.getContent().toString());
                    }
                    else if (resultObj.getContent() instanceof ArrayList) {
                        try {
                            JsonArray jsonObject = new Gson().toJsonTree(resultObj.getContent()).getAsJsonArray();
                            data.clear();
                            Type listType = new TypeToken<List<CustomDuaModel>>() {}.getType();

                            data.addAll((ArrayList<CustomDuaModel>) new Gson().fromJson(jsonObject.toString(), listType));

                            if (data.size() == 0)
                                GlobalTask.showToastMessage(context, "Data not available");

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

    private void callDeleteApi(final int duaId) {
        GlobalTask.showProgressDialog(context);
        Api.getClient().DeleteByIdCustomDua(duaId + "","Bearer " + BaseMenuActivity.userProfile.getAccess_token()).enqueue(new Callback<ResultComman>() {
            @Override
            public void onResponse(Call<ResultComman> call, Response<ResultComman> response) {
                ResultComman resultObj = response.body();
                if (resultObj != null) {
                    if (resultObj.getContent() instanceof String) {
                        //GlobalTask.showToastMessage(context, resultObj.getContent().toString());
                        for (int i=0; i<data.size(); i++){
                            if (data.get(i).getCustomDuaId() == duaId){
                                data.remove(i);
                                break;
                            }
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
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.arrow_back:
                finish();
                break;
            case R.id.txt_custom_dua:
                startActivity(new Intent(context, CreateCustomDua.class));
                break;
            case R.id.fab_bead:
                beadCounting();
                break;
            case R.id.img_bead_close:
                lnr_bead.setVisibility(View.GONE);
                break;
            case R.id.img_bead_refresh:
                beadCount = -1;
                beadCounting();
                break;
        }
    }

    private void beadCounting() {
        vibe.vibrate(100);
        beadCount++;
        if (lnr_bead.getVisibility() != View.VISIBLE)
            lnr_bead.setVisibility(View.VISIBLE);

        if (beadCount <= 1) {
            txt_count1.setText(beadCount + "");
            txt_count2.setText("");
            txt_count3.setText("");
        }
        else if (beadCount == 2){
            txt_count1.setText(beadCount+"");
            txt_count2.setText((beadCount-1) + "");
            txt_count3.setText("");
        }else{
            txt_count1.setText(beadCount+"");
            txt_count2.setText((beadCount-1) + "");
            txt_count3.setText((beadCount-2) + "");
        }
    }

    @Override
    public void itemClicked(View View, int position, int clickedId) {
        if (clickedId == 0)
            data.get(position).setLike(!data.get(position).isLike());
        else if (clickedId == 1)
            startActivity(new Intent(context, CreateCustomDua.class).putExtra("data", new Gson().toJson(data.get(position))));
        else if (clickedId == 2)
            callDeleteApi(data.get(position).getCustomDuaId());
        else if (clickedId == 3)
            data.get(position).setExpanded(!data.get(position).isExpanded());

        /*for (int i=0; i<data.size(); i++){
            if (data.get(i).getSection().contentEquals(visibleData.get(position).getSection())) {
                data.get(i).setLike(!data.get(i).isLike());
                break;
            }
        }*/
        //pref.putString(pref.allahName, new Gson().toJson(data).toString());

        adapter.notifyDataSetChanged();
    }
}
