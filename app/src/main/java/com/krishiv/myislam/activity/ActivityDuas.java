package com.krishiv.myislam.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.internal.LinkedTreeMap;
import com.google.gson.reflect.TypeToken;
import com.krishiv.myislam.API.Api;
import com.krishiv.myislam.App;
import com.krishiv.myislam.ItemClickListener;
import com.krishiv.myislam.R;
import com.krishiv.myislam.adapter.DuasAdapter;
import com.krishiv.myislam.dto.DuasCategoryModel;
import com.krishiv.myislam.dto.DuasModel;
import com.krishiv.myislam.dto.ResultComman;
import com.krishiv.myislam.menu.BaseMenuActivity;
import com.krishiv.myislam.utils.GlobalTask;
import com.krishiv.myislam.utils.SharedFavourite;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActivityDuas extends BaseMenuActivity implements ItemClickListener, View.OnClickListener{

    SharedFavourite pref;
    ArrayList<DuasCategoryModel> dataCategory;
    ArrayList<DuasModel> data, visibleData;
    DuasAdapter adapter;
    RecyclerView recyclerView;
    Spinner spn_category;
    ArrayAdapter<DuasCategoryModel> spnAdapter;
    int PAGE_INDEX = 1;
    FloatingActionButton fab_bead;
    LinearLayout lnr_bead;
    int beadCount = 0;
    TextView txt_count3,txt_count2,txt_count1;
    Vibrator vibe ;
    int directCategory=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentViev(R.layout.activity_duas);

        pref = new SharedFavourite(context);
        recyclerView = findViewById(R.id.recyclerview);
        spn_category = findViewById(R.id.spn_category);
        fab_bead = findViewById(R.id.fab_bead);
        lnr_bead = findViewById(R.id.lnr_bead);
        txt_count3 = findViewById(R.id.txt_count3);
        txt_count2 = findViewById(R.id.txt_count2);
        txt_count1 = findViewById(R.id.txt_count1);

        dataCategory = new ArrayList<>();
        dataCategory.add(new DuasCategoryModel(0, getResources().getString(R.string.duas_category_get_all)));
        data = new ArrayList<>();
        visibleData = new ArrayList<>();

        adapter = new DuasAdapter(context, visibleData);
        adapter.setClickListener(this);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);

        spnAdapter = new ArrayAdapter<DuasCategoryModel>(this, R.layout.item_spineer_dropdown, dataCategory);
        spnAdapter.setDropDownViewResource(R.layout.spinner_dropdown);
        spn_category.setAdapter(spnAdapter);

        directCategory = getIntent().getIntExtra("category",0);

        callCategoriesApi();
        //callDuasApi();
        spn_category.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    if (directCategory == 0)
                        callDuasApi();
                }
                else {
                    callCategoriesDuasApi(dataCategory.get(position).getDuaCategoryId());
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        vibe = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
    }

    void openRedirectData(){

        if (directCategory == 0)
            return;

        Log.e("Red", "Redirecting");

        for (int i=0; i<dataCategory.size(); i++){
            if (dataCategory.get(i).getDuaCategoryId() == directCategory){
                spn_category.setSelection(i);
                directCategory = 0;
                break;
            }
        }
    }

    private void callCategoriesDuasApi(int position) {
        final ProgressDialog progressDialog = GlobalTask.getProgressDialog(context);
        progressDialog.show();
        Api.getClient().GetAllDuaByCategoryId(position+"","Bearer " + BaseMenuActivity.userProfile.getAccess_token()).enqueue(new Callback<ResultComman>() {
            @Override
            public void onResponse(Call<ResultComman> call, Response<ResultComman> response) {
                ResultComman resultObj = response.body();
                if (resultObj != null) {
                    if (resultObj.getContent() instanceof String) {
                        //GlobalTask.showToastMessage(context, resultObj.getContent().toString());
                    }
                    else if (resultObj.getContent() instanceof ArrayList) {
                        try {
                            JsonArray jsonObject = new Gson().toJsonTree(resultObj.getContent()).getAsJsonArray();
                            data.clear();
                            visibleData.clear();

                            Type listType = new TypeToken<List<DuasModel>>() {}.getType();

                            data.addAll((ArrayList<DuasModel>) new Gson().fromJson(jsonObject.toString(), listType));
                            visibleData.addAll(data);
                            filterDataByFav();

                        } catch (Exception e) {
                        }
                        adapter.notifyDataSetChanged();
                    }
                } else {
                    spnAdapter.notifyDataSetChanged();
                    //GlobalTask.showTostErrorResponse(context, response.errorBody());
                }
                progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<ResultComman> call, Throwable t) {
                progressDialog.dismiss();
            }
        });
    }

    private void callCategoriesApi() {
        final ProgressDialog progressDialog = GlobalTask.getProgressDialog(context);
        progressDialog.show();
        Api.getClient().GetAllDuaCategory("Bearer " + BaseMenuActivity.userProfile.getAccess_token()).enqueue(new Callback<ResultComman>() {
            @Override
            public void onResponse(Call<ResultComman> call, Response<ResultComman> response) {
                ResultComman resultObj = response.body();
                if (resultObj != null) {
                    if (resultObj.getContent() instanceof String) {
                        //GlobalTask.showToastMessage(context, resultObj.getContent().toString());
                    }
                    else if (resultObj.getContent() instanceof ArrayList) {
                        try {
                            JsonArray jsonObject = new Gson().toJsonTree(resultObj.getContent()).getAsJsonArray();
                            dataCategory.clear();
                            dataCategory.add(new DuasCategoryModel(0, getResources().getString(R.string.duas_category_get_all)));

                            Type listType = new TypeToken<List<DuasCategoryModel>>() {}.getType();
                            dataCategory.addAll((ArrayList<DuasCategoryModel>) new Gson().fromJson(jsonObject.toString(), listType));
                            if (dataCategory.size() != 0)
                                openRedirectData();
                        } catch (Exception e) {
                        }
                        spnAdapter.notifyDataSetChanged();
                    }
                } else {
                    spnAdapter.notifyDataSetChanged();
                    //GlobalTask.showTostErrorResponse(context, response.errorBody());
                }
                progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<ResultComman> call, Throwable t) {
                progressDialog.dismiss();
            }
        });
    }

    private void callDuasApi() {
        final ProgressDialog progressDialog = GlobalTask.getProgressDialog(context);
        progressDialog.show();
        Api.getClient().GetAllDua(PAGE_INDEX + "", App.PAGE_SIZE + "","Bearer " + BaseMenuActivity.userProfile.getAccess_token()).enqueue(new Callback<ResultComman>() {
            @Override
            public void onResponse(Call<ResultComman> call, Response<ResultComman> response) {
                ResultComman resultObj = response.body();
                if (resultObj != null) {
                    if (resultObj.getContent() instanceof String) {
                        //GlobalTask.showToastMessage(context, resultObj.getContent().toString());
                    }
                    else if (resultObj.getContent() instanceof LinkedTreeMap) {
                        try {
                            JsonObject jsonObject = new Gson().toJsonTree(resultObj.getContent()).getAsJsonObject();
                            data.clear();
                            visibleData.clear();
                            if (jsonObject.has("response") && jsonObject.get("totalCount").getAsInt() != 0) {
                                String strJson = jsonObject.get("response").toString();
                                Type listType = new TypeToken<List<DuasModel>>() {}.getType();

                                data.addAll((ArrayList<DuasModel>) new Gson().fromJson(strJson, listType));
                                visibleData.addAll(data);
                                filterDataByFav();
                            }
                        } catch (Exception e) {
                        }
                        adapter.notifyDataSetChanged();
                    }
                } else {
                    spnAdapter.notifyDataSetChanged();
                    //GlobalTask.showTostErrorResponse(context, response.errorBody());
                }
                progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<ResultComman> call, Throwable t) {
                progressDialog.dismiss();
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rd_list:
                visibleData.clear();
                visibleData.addAll(data);
                adapter.notifyDataSetChanged();
                break;

            case R.id.rd_fav:
                visibleData.clear();
                for (int i=0; i<data.size(); i++) {
                    if (data.get(i).isLike())
                        visibleData.add(data.get(i));
                }
                if (visibleData.size()<=0)
                    GlobalTask.showToastMessage(context, "Favourite list is empty");

                adapter.notifyDataSetChanged();
                break;

            case R.id.arrow_back:
                finish();
                break;
            case R.id.txt_custom_dua:
                startActivity(new Intent(context, MyCustomDuas.class));
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
        if (lnr_bead.getVisibility() != View.VISIBLE) {
            lnr_bead.setVisibility(View.VISIBLE);
            return;
        }
        beadCount++;
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
        if (clickedId == 0) {
            visibleData.get(position).setLike(!visibleData.get(position).isLike());
            updatePref(visibleData.get(position), visibleData.get(position).isLike());
        }
        else {
            visibleData.get(position).setExpanded(!visibleData.get(position).isExpanded());
        }
        adapter.notifyDataSetChanged();
    }

    private void updatePref(DuasModel duasModel, boolean isRemove){
        ArrayList<DuasModel> dataFav = new ArrayList<>();
        Type listType = new TypeToken<List<DuasModel>>() {}.getType();
        dataFav = new Gson().fromJson(pref.getString("favDuas",""), listType);
        if (dataFav == null)
            dataFav = new ArrayList<>();
        if (!isRemove) {
            for (int i = 0; i < dataFav.size(); i++) {
                if (duasModel.getDuaId() == dataFav.get(i).getDuaId()) {
                    dataFav.remove(i);
                    break;
                }
            }
        }else {
            dataFav.add(duasModel);
        }
        pref.putString("favDuas", new Gson().toJson(dataFav));
    }

    private void filterDataByFav() {
        ArrayList<DuasModel> dataFav = new ArrayList<>();
        Type listType = new TypeToken<List<DuasModel>>() {}.getType();
        dataFav = new Gson().fromJson(pref.getString("favDuas",""), listType);

        for (int i=0; i<visibleData.size(); i++){
            for (int j=0; j<dataFav.size(); j++){
                if (visibleData.get(i).getDuaId() == dataFav.get(j).getDuaId()){
                    visibleData.get(i).setLike(true);
                }
            }
        }
    }
}
