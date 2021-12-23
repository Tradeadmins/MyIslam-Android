package com.krishiv.myislam.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.Spinner;

import com.krishiv.myislam.R;
import com.krishiv.myislam.dto.ResultComman;
import com.krishiv.myislam.API.Api;
import com.krishiv.myislam.utils.GlobalTask;
import com.krishiv.myislam.utils.GlobalVariable;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActivityAlarmConfig1 extends AppCompatActivity{

    String[] strLanguage = {"Arabic", "English", "Turkey", "Malay"};
    Context context;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_config);

        context = this;

        final Spinner spnLanguage = findViewById(R.id.spn_language);

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, strLanguage);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnLanguage.setAdapter(dataAdapter);

        findViewById(R.id.arrow_next).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GlobalVariable.getRegistrationModel().setFajrNotification(((CheckBox)findViewById(R.id.tik_fajr_track)).isChecked());
                GlobalVariable.getRegistrationModel().setFajrAlarm(((CheckBox)findViewById(R.id.tik_fajr_alarm)).isChecked());
                GlobalVariable.getRegistrationModel().setDhudrNotification(((CheckBox)findViewById(R.id.tik_dhudr_track)).isChecked());
                GlobalVariable.getRegistrationModel().setDhudrAlarm(((CheckBox)findViewById(R.id.tik_dhudr_alarm)).isChecked());
                GlobalVariable.getRegistrationModel().setAsarNotification(((CheckBox)findViewById(R.id.tik_asar_track)).isChecked());
                GlobalVariable.getRegistrationModel().setAsarAlarm(((CheckBox)findViewById(R.id.tik_asar_alarm)).isChecked());
                GlobalVariable.getRegistrationModel().setMagribNotification(((CheckBox)findViewById(R.id.tik_magrib_track)).isChecked());
                GlobalVariable.getRegistrationModel().setMagribAlarm(((CheckBox)findViewById(R.id.tik_magrib_alarm)).isChecked());
                GlobalVariable.getRegistrationModel().setIshaNotification(((CheckBox)findViewById(R.id.tik_isha_track)).isChecked());
                GlobalVariable.getRegistrationModel().setIshaAlarm(((CheckBox)findViewById(R.id.tik_isha_alarm)).isChecked());
                GlobalVariable.getRegistrationModel().setLanguageCode(strLanguage[spnLanguage.getSelectedItemPosition()]);
                //GlobalVariable.getRegistrationModel().setSubscriptionType(strLanguage[spnLanguage.getSelectedItemPosition()]);
                callApi();
            }
        });
    }

    private void callApi() {
        GlobalTask.showProgressDialog(context);
        Api.getClient().Register(GlobalVariable.getRegistrationModel()).enqueue(new Callback<ResultComman>() {
            @Override
            public void onResponse(Call<ResultComman> call, Response<ResultComman> response) {
                ResultComman resultObj = response.body();
                if (resultObj != null){
//                    GlobalTask.showToastMessage(context, resultObj.getContent());
                    GlobalTask.showToastMessage(context, "Success to register");
//                    response.errorBody().string()
                }else {
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
