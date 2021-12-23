package com.krishiv.myislam.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.internal.LinkedTreeMap;
import com.google.gson.reflect.TypeToken;
import com.krishiv.myislam.R;
import com.krishiv.myislam.adapter.InstructionAdapter;
import com.krishiv.myislam.dto.InstructionModel;
import com.krishiv.myislam.dto.ResultComman;
import com.krishiv.myislam.menu.BaseMenuActivity;
import com.krishiv.myislam.API.Api;
import com.krishiv.myislam.utils.GlobalTask;
import com.tmall.ultraviewpager.UltraViewPager;
import com.tmall.ultraviewpager.transformer.UltraScaleTransformer;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Instruction extends BaseMenuActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener{

    ArrayList<InstructionModel> data;
    InstructionAdapter adapter;
    UltraViewPager ultraViewPager;
    TextView txt_title;

    String PAGE_SIZE = "50";
    int PAGE_INDEX = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentViev(R.layout.activity_instruction);

        data = new ArrayList<>();
        ultraViewPager = (UltraViewPager) findViewById(R.id.ultra_viewpager);
        txt_title = findViewById(R.id.txt_title);

        setConfig();

        callApi();
    }

    private void setConfig() {
        ultraViewPager.setScrollMode(UltraViewPager.ScrollMode.HORIZONTAL);
        ultraViewPager.setAdapter(adapter);
        ultraViewPager.setMultiScreen(0.8f);
        ultraViewPager.setItemRatio(0.5f);
        ultraViewPager.setRatio(0.95f);
//        ultraViewPager.setMaxHeight(800);
        ultraViewPager.setAutoMeasureHeight(true);
        ultraViewPager.setPageTransformer(false, new UltraScaleTransformer());

        ultraViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
                txt_title.setText(data.get(i).getInstructionTitle());
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });


        /*ultraViewPager.initIndicator();
        ultraViewPager.getIndicator()
                .setOrientation(UltraViewPager.Orientation.HORIZONTAL)
                .setFocusColor(Color.GREEN)
                .setNormalColor(Color.WHITE)
                .setMargin(100,0,0,0)
                .setRadius((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 5, getResources().getDisplayMetrics()));
        ultraViewPager.getIndicator().setGravity(Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM);
        ultraViewPager.getIndicator().build();
        ultraViewPager.setItemMargin(0,0,0,50);*/

//        ultraViewPager.getIndicator().setFocusResId(R.drawable.ic_ins_round_fill)
//                .setNormalResId(R.drawable.ic_ins_round_unfill);
    }

    private void callApi() {
        GlobalTask.showProgressDialog(context);
        Api.getClient().GetAllInstruction(PAGE_INDEX + "", PAGE_SIZE + "","Bearer " + BaseMenuActivity.userProfile.getAccess_token()).enqueue(new Callback<ResultComman>() {
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
                            //if (jsonObject.has("response") && jsonObject.get("totalCount").getAsInt() != 0) {
                            if (jsonObject.has("response")) {
                                String strJson = jsonObject.get("response").toString();
                                Type listType = new TypeToken<List<InstructionModel>>() {}.getType();

                                data.addAll((ArrayList<InstructionModel>) new Gson().fromJson(strJson, listType));
                                if (data.size() == 0) {
                                    Log.e("", "Data not available");
                                    //GlobalTask.showToastMessage(context, "Data not available");
                                }
                                else
                                    txt_title.setText(data.get(0).getInstructionTitle());
                            }else{
                                Log.e("","Data not available");
                                //GlobalTask.showToastMessage(context, "Data not available");
                            }
                        } catch (Exception e) {
                        }
//                        adapter = new InstructionAdapter(context, data);
//                        ultraViewPager.setAdapter(adapter);
                        //adapter.notifyDataSetChanged();

                        adapter = new InstructionAdapter(context, data);
                        ultraViewPager.setAdapter(adapter);
                    }
                } else {
                    //adapter.notifyDataSetChanged();
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
        switch (view.getId()){
            case R.id.arrow_back:
                finish();
                break;
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        txt_title.setText(data.get(i).getInstructionTitle());
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
