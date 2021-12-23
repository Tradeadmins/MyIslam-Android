package com.krishiv.myislam.activity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.internal.LinkedTreeMap;
import com.krishiv.myislam.API.Api;
import com.krishiv.myislam.API.ApiInterface;
import com.krishiv.myislam.R;
import com.krishiv.myislam.dto.RamdanResponseModel;
import com.krishiv.myislam.dto.ResultComman;
import com.krishiv.myislam.menu.BaseMenuActivity;
import com.krishiv.myislam.utils.GPSTracker;
import com.krishiv.myislam.utils.GlobalTask;
import com.krishiv.myislam.utils.Shared;
import com.krishiv.myislam.utils.TimingUtils;

import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class FastingActivity extends BaseMenuActivity implements View.OnClickListener {

    TextView daily_quote, fasting;
    GPSTracker gpsTracker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentViev(R.layout.activity_fasting);
        gpsTracker = new GPSTracker(this);
        isItPopupTime();
        callApiDailyQuote();
        callApi();
        daily_quote = findViewById(R.id.quote);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.arrow_back:
                finish();
                break;

            case R.id.duas:
                startActivity(new Intent(FastingActivity.this, ActivityDuas.class));
                break;
            case R.id.ramdan_calender:
                startActivity(new Intent(FastingActivity.this, RamdanCalenderActivity.class));
                break;
            case R.id.image_moon:
                break;

        }
    }

    private void isItPopupTime() {

        if (shared.getLong(Shared.lastDailyQuote, 0L) < Long.parseLong(TimingUtils.getToday(TimingUtils.DailyReminderDateFormat))) {
            if (java.util.Calendar.getInstance().get(java.util.Calendar.DAY_OF_WEEK) == 6) {
                callApiJumaQuote();
            }
            if (java.util.Calendar.getInstance().get(Calendar.DAY_OF_WEEK) == 1) {
                try {
                    List<String> dataCount = db.getTrackerWMYData();
                    openPopup(String.format(getResources().getString(R.string.popup_total_prayed), dataCount.get(0)));
                } catch (Exception e) {
                    Log.e("Exception", "Popup");
                }
            }
            callApiDailyQuote();
        }
    }

    private void callApiJumaQuote() {
        final ProgressDialog progressDialog = GlobalTask.getProgressDialog(context);
        progressDialog.show();
        Api.getClient().GetJumaQuoteByLang_Date("2", TimingUtils.getApiPassDate(), "Bearer " + userProfile.getAccess_token()).enqueue(new Callback<ResultComman>() {
            @Override
            public void onResponse(Call<ResultComman> call, Response<ResultComman> response) {
                ResultComman resultObj = response.body();
                shared.putLong(Shared.lastDailyQuote, Long.parseLong(TimingUtils.getToday(TimingUtils.DailyReminderDateFormat)));
                if (resultObj != null) {
                    if (resultObj.getContent() instanceof String) {
                        //GlobalTask.showToastMessage(context, resultObj.getContent().toString());
                    } else if (resultObj.getContent() instanceof LinkedTreeMap) {
                        JsonObject jsonObject = new Gson().toJsonTree(resultObj.getContent()).getAsJsonObject();
                        if (jsonObject.has("jumaQuoteText")) {
                            openPopup(jsonObject.get("jumaQuoteText").toString());
                        }
                    }
                } else {
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

    private void openPopup(String txt) {
        final Dialog dialog = new Dialog(context);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.setContentView(R.layout.popup_jummah);

        ((TextView) dialog.findViewById(R.id.txt_desc)).setText(txt);

        dialog.findViewById(R.id.btn_close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    private void callApiDailyQuote() {
        final ProgressDialog progressDialog = GlobalTask.getProgressDialog(context);
        progressDialog.show();

        Api.getClient().GetDailyQuoteByLang_Date("2", TimingUtils.getApiPassDate(), "Bearer " + userProfile.getAccess_token()).enqueue(new Callback<ResultComman>() {
            @Override
            public void onResponse(Call<ResultComman> call, Response<ResultComman> response) {
                ResultComman resultObj = response.body();
                shared.putLong(Shared.lastDailyQuote, Long.parseLong(TimingUtils.getToday(TimingUtils.DailyReminderDateFormat)));
                if (resultObj != null) {
                    if (resultObj.getContent() instanceof String) {
                    } else if (resultObj.getContent() instanceof LinkedTreeMap) {
                        JsonObject jsonObject = new Gson().toJsonTree(resultObj.getContent()).getAsJsonObject();
                        if (jsonObject.has("dailyQuoteText")) {
//                            openPopup(jsonObject.get("dailyQuoteText").toString());
                            daily_quote.setText(jsonObject.get("dailyQuoteText").toString());
                        } else {

                        }
                    }
                } else {
                }
                progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<ResultComman> call, Throwable t) {
                progressDialog.dismiss();
            }
        });
    }

    private void callApi() {
        GlobalTask.showProgressDialog(context);


        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://api.aladhan.com")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiInterface api = retrofit.create(ApiInterface.class);
        Calendar c = Calendar.getInstance();

        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        api.getRamdanData(gpsTracker.getLatitude(), gpsTracker.getLongitude(), 2, month, year)
                .enqueue(new Callback<RamdanResponseModel>() {
                    @Override
                    public void onResponse(Call<RamdanResponseModel> call, Response<RamdanResponseModel> response) {
                        RamdanResponseModel resultObj = response.body();
                        if (resultObj != null) {
                            try {
                                for (int i = 0; i < resultObj.getData().size(); i++) {
                                    resultObj.getData().get(i).getTimings().getAsr();
                                }


                            } catch (Exception e) {
                                e.printStackTrace();
                                GlobalTask.hideProgressDialog();

                            }
                        } else {
                            GlobalTask.showTostErrorResponse(context, response.errorBody());
                        }
                        GlobalTask.hideProgressDialog();
                    }

                    @Override
                    public void onFailure(Call<RamdanResponseModel> call, Throwable t) {
                        GlobalTask.hideProgressDialog();

                    }
                });
    }
}
