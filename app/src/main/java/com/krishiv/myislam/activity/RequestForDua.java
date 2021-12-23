package com.krishiv.myislam.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.krishiv.myislam.R;
import com.krishiv.myislam.dto.RequestPrayerModel;
import com.krishiv.myislam.dto.ResultComman;
import com.krishiv.myislam.menu.BaseMenuActivity;
import com.krishiv.myislam.API.Api;
import com.krishiv.myislam.utils.GlobalTask;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RequestForDua extends BaseMenuActivity implements View.OnClickListener{

    EditText edt_txt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentViev(R.layout.fg_request_for_dua);

        edt_txt = findViewById(R.id.edt_txt);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_save:
                callApi();
                break;

            case R.id.arrow_back:
                finish();
                break;
        }
    }

    private void callApi() {
        GlobalTask.showProgressDialog(context);

        RequestPrayerModel requestPrayerModel = new RequestPrayerModel();
        requestPrayerModel.setPrayerRequestText(edt_txt.getText().toString().trim());

        Api.getClient().AddPrayerRequest(requestPrayerModel, "Bearer " + userProfile.getAccess_token()).enqueue(new Callback<ResultComman>() {
            @Override
            public void onResponse(Call<ResultComman> call, Response<ResultComman> response) {
                ResultComman resultObj = response.body();
                if (resultObj != null) {
                    if (resultObj.getContent() instanceof String) {
                        //GlobalTask.showToastMessage(context, resultObj.getContent().toString());
                        finish();
                    }
                } else {
//                    GlobalTask.showTostErrorResponse(context, response.errorBody());
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
