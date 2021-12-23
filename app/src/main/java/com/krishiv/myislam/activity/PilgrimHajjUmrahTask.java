package com.krishiv.myislam.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.github.lzyzsd.circleprogress.DonutProgress;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.internal.LinkedTreeMap;
import com.google.gson.reflect.TypeToken;
import com.krishiv.myislam.API.Api;
import com.krishiv.myislam.App;
import com.krishiv.myislam.ItemClickListener;
import com.krishiv.myislam.R;
import com.krishiv.myislam.adapter.PilgrimHajjUmrahAdapter;
import com.krishiv.myislam.dto.HajjGuideTaskModel;
import com.krishiv.myislam.dto.HajjTaskModel;
import com.krishiv.myislam.dto.ResultComman;
import com.krishiv.myislam.dto.UmrahTaskModel;
import com.krishiv.myislam.menu.BaseMenuActivity;
import com.krishiv.myislam.utils.GlobalTask;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PilgrimHajjUmrahTask extends BaseMenuActivity implements ItemClickListener, View.OnClickListener {

    ArrayList<Object> data;
    PilgrimHajjUmrahAdapter adapter;
    RecyclerView recyclerView;
    DonutProgress donut_progress;
    TextView txt_journey_title;

    int PAGE_INDEX = 1;
    Pilgrim.ActionPilgrim currenActionFor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentViev(R.layout.activity_pilgrim_hajj_umrah_task);

        donut_progress = findViewById(R.id.donut_progress);
        txt_journey_title = findViewById(R.id.txt_journey_title);
        recyclerView = findViewById(R.id.recyclerview);
        data = new ArrayList<>();

        currenActionFor = Pilgrim.ActionPilgrim.values()[getIntent().getIntExtra("actionFor", 0)];
        Log.e("ActionFor", currenActionFor + "");

        adapter = new PilgrimHajjUmrahAdapter(context, data, currenActionFor);
        adapter.setClickListener(this);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);

        //findViewById(R.id.rlt_recitation).setVisibility(View.VISIBLE);

    }

    @Override
    protected void onResume() {
        super.onResume();
        callApi();
    }

    private void callApi() {
        if (currenActionFor == Pilgrim.ActionPilgrim.Hajj) {

            txt_journey_title.setText(getResources().getString(R.string.your_hajj_journey));
            callHajjTaskApi();
        } else if (currenActionFor == Pilgrim.ActionPilgrim.Umrah) {
            txt_journey_title.setText(getResources().getString(R.string.your_umrah_journey));
            callUmrahTaskApi();
        } else if (currenActionFor == Pilgrim.ActionPilgrim.HajjGuide) {
            txt_journey_title.setText(getResources().getString(R.string.your_hajj_umrah_journey));

            callHajjGauideTaskApi();
        }
    }

    boolean isRecitationAdded = false;

    private void calculatePercentage() {

        int totalVal = data.size();

        if (isRecitationAdded)
            totalVal = totalVal - 1;

        int checkedVal = 0;
        for (int i = 0; i < data.size(); i++) {
            if (currenActionFor == Pilgrim.ActionPilgrim.Hajj) {
                if (((HajjTaskModel) data.get(i)).isHajjTaskIsCompleted())
                    checkedVal++;
            } else if (currenActionFor == Pilgrim.ActionPilgrim.Umrah) {
                if (((UmrahTaskModel) data.get(i)).isUmrahTaskIsCompleted())
                    checkedVal++;
            } else if (currenActionFor == Pilgrim.ActionPilgrim.HajjGuide) {
                if (((HajjGuideTaskModel) data.get(i)).isHajjGuideIsCompleted())
                    checkedVal++;
            }
        }
        if (!isRecitationAdded) {
            isRecitationAdded = true;
            if (currenActionFor == Pilgrim.ActionPilgrim.Hajj) {
                HajjTaskModel obb = new HajjTaskModel();
                obb.setHajjTaskName("Recitation");
                obb.setRecitation(true);
                data.add(obb);
            } else if (currenActionFor == Pilgrim.ActionPilgrim.Umrah) {
                UmrahTaskModel obb = new UmrahTaskModel();
                obb.setUmrahTaskName("Recitation");
                obb.setRecitation(true);
                data.add(obb);
            }else if (currenActionFor == Pilgrim.ActionPilgrim.HajjGuide) {
                HajjGuideTaskModel obb = new HajjGuideTaskModel();
                obb.setHajjGuideName("Recitation");
                obb.setRecitation(true);
                data.add(obb);
            }
        }


        float progress = ((float) checkedVal / totalVal) * 100;

        donut_progress.setText((int) progress + "%");
        donut_progress.setProgress(progress);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.lnr_complete_all:
                startActivity(new Intent(context, PilgrimHajjUmrahCompletedTask.class).putExtra("actionFor", currenActionFor.ordinal()));
                break;

            case R.id.txt_recitation_click:
                if (currenActionFor == Pilgrim.ActionPilgrim.Hajj) {
                    startActivity(new Intent(context, ActivityDuas.class).putExtra("category", 1));
                } else if (currenActionFor == Pilgrim.ActionPilgrim.Umrah) {
                    startActivity(new Intent(context, ActivityDuas.class).putExtra("category", 2));
                }
                break;

            case R.id.arrow_back:
                finish();
                break;
        }
    }

    @Override
    public void itemClicked(View View, int position, int clickedId) {
        if (clickedId == 2) {
            if (currenActionFor == Pilgrim.ActionPilgrim.Hajj) {
                startActivity(new Intent(context, ActivityDuas.class).putExtra("category", 1));
            } else if (currenActionFor == Pilgrim.ActionPilgrim.Umrah) {
                startActivity(new Intent(context, ActivityDuas.class).putExtra("category", 2));
            } else if (currenActionFor == Pilgrim.ActionPilgrim.HajjGuide) {
                startActivity(new Intent(context, ActivityDuas.class).putExtra("category", 1));
            }
        } else if (currenActionFor == Pilgrim.ActionPilgrim.Hajj) {
            HajjTaskModel obj = (HajjTaskModel) data.get(position);
            if (!obj.isHajjTaskIsCompleted())

                callHajjTaskCompletedApi(obj);
        } else if (currenActionFor == Pilgrim.ActionPilgrim.Umrah) {
            UmrahTaskModel obj = (UmrahTaskModel) data.get(position);
            if (!obj.isUmrahTaskIsCompleted())
                callUmrahTaskCompletedApi(obj);
        } else if (currenActionFor == Pilgrim.ActionPilgrim.HajjGuide) {
            HajjGuideTaskModel obj = (HajjGuideTaskModel) data.get(position);
            if (!obj.isHajjGuideIsCompleted())
                callHajjGuideTaskCompletedApi(obj);
        }


        /* data.get(position).setLike(!data.get(position).isLike());

         *//*for (int i=0; i<data.size(); i++){
            if (data.get(i).getSection().contentEquals(visibleData.get(position).getSection())) {
                data.get(i).setLike(!data.get(i).isLike());
                break;
            }
        }*//*

        pref.putString(pref.allahName, new Gson().toJson(data).toString());
        adapter.notifyDataSetChanged();*/
    }

    private void callHajjTaskApi() {
        final ProgressDialog progressDialog = GlobalTask.getProgressDialog(context);
        progressDialog.show();
        Api.getClient().GetAllHajjTask(PAGE_INDEX + "", App.PAGE_SIZE + "", "Bearer " + BaseMenuActivity.userProfile.getAccess_token()).enqueue(new Callback<ResultComman>() {
            @Override
            public void onResponse(Call<ResultComman> call, Response<ResultComman> response) {
                Type listType = new TypeToken<List<HajjTaskModel>>() {
                }.getType();
                getDataHandler(response, listType);
                progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<ResultComman> call, Throwable t) {
                progressDialog.dismiss();
            }
        });
    }

    private void callUmrahTaskApi() {
        final ProgressDialog progressDialog = GlobalTask.getProgressDialog(context);
        progressDialog.show();
        Api.getClient().GetAllUmrahTask(PAGE_INDEX + "", App.PAGE_SIZE + "", "Bearer " + BaseMenuActivity.userProfile.getAccess_token()).enqueue(new Callback<ResultComman>() {
            @Override
            public void onResponse(Call<ResultComman> call, Response<ResultComman> response) {
                Type listType = new TypeToken<List<UmrahTaskModel>>() {
                }.getType();
                getDataHandler(response, listType);
                progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<ResultComman> call, Throwable t) {
                progressDialog.dismiss();
            }
        });
    }

    private void callHajjGauideTaskApi() {
        final ProgressDialog progressDialog = GlobalTask.getProgressDialog(context);
        progressDialog.show();
        Api.getClient().GetAllHajjGuide(PAGE_INDEX + "", App.PAGE_SIZE + "", "Bearer " + BaseMenuActivity.userProfile.getAccess_token()).enqueue(new Callback<ResultComman>() {
            @Override
            public void onResponse(Call<ResultComman> call, Response<ResultComman> response) {
                Type listType = new TypeToken<List<HajjGuideTaskModel>>() {
                }.getType();
                getDataHandler(response, listType);
                progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<ResultComman> call, Throwable t) {
                progressDialog.dismiss();
            }
        });
    }

    private void getDataHandler(Response<ResultComman> response, Type listType) {
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

                        data.addAll((ArrayList<Object>) new Gson().fromJson(strJson, listType));
                        isRecitationAdded = false;
                        calculatePercentage();
                    }
                } catch (Exception e) {
                }
                adapter.notifyDataSetChanged();
            }
        } else {
            adapter.notifyDataSetChanged();
            //GlobalTask.showTostErrorResponse(context, response.errorBody());
        }
    }


    private void callHajjTaskCompletedApi(final HajjTaskModel obj) {
        final ProgressDialog progressDialog = GlobalTask.getProgressDialog(context);
        progressDialog.show();

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("hajjTaskId", obj.getHajjTaskId());
        jsonObject.addProperty("hajjTaskCompleteByUserId", userProfile.getUserId());

        Api.getClient().AddHajjTaskComplete(jsonObject, "Bearer " + BaseMenuActivity.userProfile.getAccess_token()).enqueue(new Callback<ResultComman>() {
            @Override
            public void onResponse(Call<ResultComman> call, Response<ResultComman> response) {
                ResultComman resultObj = response.body();
                if (resultObj != null) {
                    if (resultObj.getContent() instanceof String) {
                        obj.setHajjTaskIsCompleted(true);
                        adapter.notifyDataSetChanged();
                        calculatePercentage();
//                        callApi();
                    }
                } else {
                    adapter.notifyDataSetChanged();
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

    private void callUmrahTaskCompletedApi(final UmrahTaskModel obj) {
        final ProgressDialog progressDialog = GlobalTask.getProgressDialog(context);
        progressDialog.show();

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("umrahTaskId", obj.getUmrahTaskId());
        jsonObject.addProperty("umrahTaskCompleteByUserId", userProfile.getUserId());

        Api.getClient().AddUmrahTaskComplete(jsonObject, "Bearer " + BaseMenuActivity.userProfile.getAccess_token()).enqueue(new Callback<ResultComman>() {
            @Override
            public void onResponse(Call<ResultComman> call, Response<ResultComman> response) {
                ResultComman resultObj = response.body();
                if (resultObj != null) {
                    if (resultObj.getContent() instanceof String) {
                        obj.setUmrahTaskIsCompleted(true);
                        adapter.notifyDataSetChanged();
                        calculatePercentage();
//                        callApi();
                    }
                } else {
                    adapter.notifyDataSetChanged();
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

    private void callHajjGuideTaskCompletedApi(final HajjGuideTaskModel obj) {
        final ProgressDialog progressDialog = GlobalTask.getProgressDialog(context);
        progressDialog.show();

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("hajjGuideId", obj.getHajjGuideId());
        jsonObject.addProperty("hajjGuideCompleteByUserId", userProfile.getUserId());

        Api.getClient().AddHajjGuideTaskComplete(jsonObject, "Bearer " + BaseMenuActivity.userProfile.getAccess_token()).enqueue(new Callback<ResultComman>() {
            @Override
            public void onResponse(Call<ResultComman> call, Response<ResultComman> response) {
                ResultComman resultObj = response.body();
                if (resultObj != null) {
                    if (resultObj.getContent() instanceof String) {
                        obj.setHajjGuideIsCompleted(true);
                        adapter.notifyDataSetChanged();
                        calculatePercentage();
//                        callApi();
                    }
                } else {
                    adapter.notifyDataSetChanged();
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
}
