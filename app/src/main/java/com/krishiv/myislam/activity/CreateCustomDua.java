package com.krishiv.myislam.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.google.gson.Gson;
import com.krishiv.myislam.R;
import com.krishiv.myislam.dto.CustomDuaModel;
import com.krishiv.myislam.dto.ResultComman;
import com.krishiv.myislam.menu.BaseMenuActivity;
import com.krishiv.myislam.API.Api;
import com.krishiv.myislam.utils.GlobalTask;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreateCustomDua extends BaseMenuActivity implements View.OnClickListener{

    EditText edt_txt, edt_title;
    CustomDuaModel customDuaModel;
    boolean isUpdate = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentViev(R.layout.activity_create_custom_dua);

        edt_txt = findViewById(R.id.edt_txt);
        edt_title = findViewById(R.id.edt_title);

        customDuaModel = new CustomDuaModel();

        try {
            customDuaModel = new Gson().fromJson(getIntent().getStringExtra("data"), CustomDuaModel.class);

            edt_title.setText(customDuaModel.getCustomDuaName());
            edt_txt.setText(customDuaModel.getCustomDuaText());

            //((Button)findViewById(R.id.btn_save)).setText(getResources().getString(R.string.update_dua));

            isUpdate = true;

        }catch (Exception e){
            customDuaModel = new CustomDuaModel();
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_save:
                if (isValid()) {
                    readyForCallApi();
                }
                break;

            case R.id.arrow_back:
                finish();
                break;
        }
    }

    private boolean isValid() {
        if (edt_title.getText().toString().length()<=0){
            GlobalTask.showToastMessage(this, getResources().getString(R.string.valid_dua_title));
            return false;
        }
        if (edt_txt.getText().toString().length()<=0){
            GlobalTask.showToastMessage(this, getResources().getString(R.string.valid_dua_desc));
            return false;
        }
        return true;
    }

    private void readyForCallApi() {
        customDuaModel.setCustomDuaName(edt_title.getText().toString());
        customDuaModel.setCustomDuaText(edt_txt.getText().toString());

        if (!isUpdate)
            callApi();
        else
            callUpdateApi();
    }

    private void callUpdateApi() {
        GlobalTask.showProgressDialog(context);

        Api.getClient().UpdateCustomDua(customDuaModel, "Bearer " + userProfile.getAccess_token()).enqueue(new Callback<ResultComman>() {
            @Override
            public void onResponse(Call<ResultComman> call, Response<ResultComman> response) {
                ResultComman resultObj = response.body();
                if (resultObj != null) {
                    if (resultObj.getContent() instanceof String) {
                        GlobalTask.showToastMessage(context, resultObj.getContent().toString());
                        finish();
                    }
                } else {
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

    private void callApi() {
        GlobalTask.showProgressDialog(context);

        Api.getClient().AddCustomDua(customDuaModel, "Bearer " + userProfile.getAccess_token()).enqueue(new Callback<ResultComman>() {
            @Override
            public void onResponse(Call<ResultComman> call, Response<ResultComman> response) {
                ResultComman resultObj = response.body();
                if (resultObj != null) {
                    if (resultObj.getContent() instanceof String) {
                        GlobalTask.showToastMessage(context, resultObj.getContent().toString());
                        finish();
                    }
                } else {
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
}
