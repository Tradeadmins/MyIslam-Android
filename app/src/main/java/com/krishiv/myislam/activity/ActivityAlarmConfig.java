package com.krishiv.myislam.activity;

import android.Manifest;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SwitchCompat;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.internal.LinkedTreeMap;
import com.google.gson.reflect.TypeToken;
import com.krishiv.myislam.R;
import com.krishiv.myislam.adapter.InstructionAdapter;
import com.krishiv.myislam.adapter.LanguageRecycler;
import com.krishiv.myislam.adapter.QuranTranslationAdapter;
import com.krishiv.myislam.db.PrayerTimingDAO;
import com.krishiv.myislam.dto.AppConfigModel;
import com.krishiv.myislam.dto.InstructionModel;
import com.krishiv.myislam.dto.LanguageTranslationModel;
import com.krishiv.myislam.dto.PrayerTimingModel;
import com.krishiv.myislam.dto.QuranTranslationModel;
import com.krishiv.myislam.dto.ResultComman;
import com.krishiv.myislam.API.Api;
import com.krishiv.myislam.API.ApiInterface;
import com.krishiv.myislam.dto.ResultLanguage;
import com.krishiv.myislam.menu.BaseMenuActivity;
import com.krishiv.myislam.service.Constants;
import com.krishiv.myislam.utils.GPSTracker;
import com.krishiv.myislam.utils.GlobalTask;
import com.krishiv.myislam.utils.GlobalVariable;
import com.krishiv.myislam.utils.LocaleHelper;
import com.krishiv.myislam.utils.Shared;
import com.krishiv.myislam.utils.TimingUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class ActivityAlarmConfig extends AppCompatActivity {

    String[] strLanguage;
    Spinner spnLanguage;
    Context context;
    PrayerTimingDAO db;
    TextView txt_setting_menu;
    ArrayList<LanguageTranslationModel> languageTranslationModelArrayList = new ArrayList<>();
    LinearLayout relative_quran_translation;
    RecyclerView language_recyclerview;
    LanguageRecycler languageRecycler;
    TextView qurantv;
    File fileWithinMyDir;
    Animation slideUpAnimation;
    TextView txt_select_language;
    int previous_position = -1;
    ImageView cancel;

    protected void attachBaseContext(Context newBase) {
        String currentLanguage = LocaleHelper.getLanguage(newBase);
        if (currentLanguage == null) {
            currentLanguage = "en";
            super.attachBaseContext(LocaleHelper.setLocale(newBase, currentLanguage));
        } else {
            super.attachBaseContext(LocaleHelper.setLocale(newBase, currentLanguage));
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_config);

        context = this;
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        strLanguage = getResources().getStringArray(R.array.languages);
        qurantv = findViewById(R.id.qurantv);
        spnLanguage = findViewById(R.id.spn_language);
        //spnTranslation = findViewById(R.id.spn_translation);
        txt_setting_menu = findViewById(R.id.txt_setting_menu);
        relative_quran_translation = findViewById(R.id.relative_quran_translation);
        txt_select_language = findViewById(R.id.txt_select_language);
        language_recyclerview = findViewById(R.id.language_recyclerview);
        cancel = findViewById(R.id.cancel);
        language_recyclerview.setLayoutManager(new LinearLayoutManager(this));
        languageRecycler = new LanguageRecycler(context, languageTranslationModelArrayList, new onLanguageSelectionListener());
        language_recyclerview.setAdapter(languageRecycler);

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, strLanguage);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnLanguage.setAdapter(dataAdapter);

        //spnTranslation.setAdapter(dataAdapter);
        //setSpinnerTranslation();

        findViewById(R.id.arrow_next).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GlobalVariable.getRegistrationModel().setFajrNotification(true);
                GlobalVariable.getRegistrationModel().setFajrAlarm(((SwitchCompat) findViewById(R.id.tik_fajr_alarm)).isChecked());
                GlobalVariable.getRegistrationModel().setDhudrNotification(true);
                GlobalVariable.getRegistrationModel().setDhudrAlarm(((SwitchCompat) findViewById(R.id.tik_dhudr_alarm)).isChecked());
                GlobalVariable.getRegistrationModel().setAsarNotification(true);
                GlobalVariable.getRegistrationModel().setAsarAlarm(((SwitchCompat) findViewById(R.id.tik_asar_alarm)).isChecked());
                GlobalVariable.getRegistrationModel().setMagribNotification(true);
                GlobalVariable.getRegistrationModel().setMagribAlarm(((SwitchCompat) findViewById(R.id.tik_magrib_alarm)).isChecked());
                GlobalVariable.getRegistrationModel().setIshaNotification(true);
                GlobalVariable.getRegistrationModel().setIshaAlarm(((SwitchCompat) findViewById(R.id.tik_isha_alarm)).isChecked());
                GlobalVariable.getRegistrationModel().setLanguageCode(strLanguage[spnLanguage.getSelectedItemPosition()]);
                //GlobalVariable.getRegistrationModel().setSubscriptionType(strLanguage[spnLanguage.getSelectedItemPosition()]);
                setAlarmConfig();

                LocaleHelper.setLocale(context, getResources().getStringArray(R.array.languages_ext)[spnLanguage.getSelectedItemPosition()]);

                //callApi();
                Intent intent = new Intent(context, Dashboard.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }
        });

        db = new PrayerTimingDAO(context);

        if (!db.isAvailable(TimingUtils.getToday(TimingUtils.SQLiteDateFormat)))
            callApiForPrayerTimes();

        setAlarmConfig();
        setTextSpanable();

        anim();
        txt_select_language.setText("" + strLanguage[0]);
        txt_select_language.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (languageTranslationModelArrayList.size() == 0) {
                    getAllQuranTranslationApiCall();
                } else {
                    relative_quran_translation.setVisibility(View.VISIBLE);
                    relative_quran_translation.startAnimation(slideUpAnimation);
                }


            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (relative_quran_translation.getVisibility() == View.VISIBLE) {
                    relative_quran_translation.setVisibility(View.GONE);
                }
            }
        });
    }

    private void anim() {
        slideUpAnimation = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.bottom_up);
    }


    private void setTextSpanable() {
        String strText = txt_setting_menu.getText().toString();
        String clickOn = getResources().getString(R.string.txt_setting_menu);
        SpannableString spannableString = new SpannableString(strText);
        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(View view) {

            }

            public void updateDrawState(TextPaint ds) {
                ds.setUnderlineText(false);
            }
        };
        int startIndexOfLink = strText.indexOf(clickOn);
        spannableString.setSpan(clickableSpan, startIndexOfLink,
                startIndexOfLink + clickOn.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        spannableString.setSpan(new ForegroundColorSpan(ContextCompat.getColor(context, R.color.colorYellow)), startIndexOfLink,
                startIndexOfLink + clickOn.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        txt_setting_menu.setHighlightColor(Color.TRANSPARENT);
        txt_setting_menu.setMovementMethod(LinkMovementMethod.getInstance());
        txt_setting_menu.setText(spannableString, TextView.BufferType.SPANNABLE);
    }

    @Override
    public void onBackPressed() {
        if (relative_quran_translation.getVisibility() == View.VISIBLE) {
            relative_quran_translation.setVisibility(View.GONE);
            return;
        }
        onBackPressed();
    }

    private void setAlarmConfig() {
        AppConfigModel appConfigModel = new AppConfigModel();
        appConfigModel.setFajrAlarm(GlobalVariable.getRegistrationModel().isFajrAlarm());
        appConfigModel.setDhudrAlarm(GlobalVariable.getRegistrationModel().isDhudrAlarm());
        appConfigModel.setAsarAlarm(GlobalVariable.getRegistrationModel().isAsarAlarm());
        appConfigModel.setMagribAlarm(GlobalVariable.getRegistrationModel().isMagribAlarm());
        appConfigModel.setIshaAlarm(GlobalVariable.getRegistrationModel().isIshaAlarm());

        appConfigModel.setFajrNotification(GlobalVariable.getRegistrationModel().isFajrNotification());
        appConfigModel.setDhudrNotification(GlobalVariable.getRegistrationModel().isDhudrNotification());
        appConfigModel.setAsarNotification(GlobalVariable.getRegistrationModel().isAsarNotification());
        appConfigModel.setMagribNotification(GlobalVariable.getRegistrationModel().isMagribNotification());
        appConfigModel.setIshaNotification(GlobalVariable.getRegistrationModel().isIshaNotification());

        Shared pref = new Shared(context);
        pref.putString(pref.appConfig, new Gson().toJson(appConfigModel));
    }

    private void callApiForPrayerTimes() {
        if (!checkPermissions())
            return;

        GlobalTask.showProgressDialog(context);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://api.aladhan.com")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiInterface api = retrofit.create(ApiInterface.class);

        GPSTracker gpsTracker = new GPSTracker(context);

        String mm = TimingUtils.getToday(TimingUtils.Month);
        String yyyy = TimingUtils.getToday(TimingUtils.Year);

        api.GetPrayerTiming(gpsTracker.getLatitude() + "", gpsTracker.getLongitude() + "", "1", mm, yyyy).enqueue(new Callback<ResultComman>() {
            @Override
            public void onResponse(Call<ResultComman> call, Response<ResultComman> response) {
                ResultComman resultObj = response.body();
                if (resultObj != null) {

                    ArrayList<PrayerTimingModel> data = GlobalTask.filterCalendarData(resultObj);
                    if (data.size() > 0) {
                        db.saveAll(data);
                        setTodayDate();
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


    private void getAllQuranTranslationApiCall() {
        GlobalTask.showProgressDialog(context);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://103.73.190.66:8080")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiInterface api = retrofit.create(ApiInterface.class);

        api.getAllQuranTranslation().enqueue(new Callback<ResultLanguage>() {
            @Override
            public void onResponse(Call<ResultLanguage> call, Response<ResultLanguage> response) {
                ResultLanguage resultObj = response.body();
                if (resultObj != null) {
                    if (response.body().getStatusCode() == 200) {

                        ArrayList<LanguageTranslationModel> list = response.body().getContent();

//                        if (languageTranslationModelArrayList.size() == 0)
//                             GlobalTask.showToastMessage(context, "Data not available");
//                        else {
                        languageTranslationModelArrayList.clear();
                        languageTranslationModelArrayList.addAll(list);
                        languageRecycler.notifyDataSetChanged();
                        relative_quran_translation.setVisibility(View.VISIBLE);
                        relative_quran_translation.startAnimation(slideUpAnimation);
//                            languageRecycler = new LanguageRecycler(context, languageTranslationModelArrayList, new onLanguageSelectionListener());
//                            language_recyclerview.setAdapter(languageRecycler);
//                        }

                    } else {
                        GlobalTask.showToastMessage(context, resultObj.getErrorMessage().toString());
                    }
                } else {
                    GlobalTask.showTostErrorResponse(context, response.errorBody());
                }
                GlobalTask.hideProgressDialog();
            }

            @Override
            public void onFailure(Call<ResultLanguage> call, Throwable t) {
                GlobalTask.hideProgressDialog();
            }
        });
    }

//    protected Dialog onCreateDialog(int id) {
//        switch (id) {
//            case progress_bar_type: // we set this to 0
//                pDialog = new ProgressDialog(this);
//                pDialog.setMessage("Downloading file. Please wait...");
//                pDialog.setIndeterminate(false);
//                pDialog.setMax(100);
//                pDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
//                pDialog.setCancelable(true);
//                pDialog.show();
//                return pDialog;
//            default:
//                return null;
//        }
//    }

    private class DownloadFile extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            GlobalTask.showProgressDialog(context);
        }

        /**
         * Downloading file in background thread
         */
        @Override
        protected String doInBackground(String... f_url) {

            int count;
            try {
                URL url = new URL(f_url[0]);
                URLConnection connection = url.openConnection();
                connection.connect();
                // getting file length
                int lengthOfFile = connection.getContentLength();

                InputStream input = new BufferedInputStream(url.openStream(), 8192);

                File mydir = context.getDir("myislam", Context.MODE_PRIVATE); //Creating an internal dir;
                fileWithinMyDir = new File(mydir, Constants.MY_LANGUAGE_FILE); //Getting a file within the dir.
                FileOutputStream output = new FileOutputStream(fileWithinMyDir); //Use the s

                byte data[] = new byte[1024];

//                long total = 0;
                while ((count = input.read(data)) != -1) {
//                    total += count;
                    // publishing the progress....
                    // After this onProgressUpdate will be called
//                    publishProgress("" + (int) ((total * 100) / lengthOfFile));
//                    Log.d(TAG, "Progress: " + (int) ((total * 100) / lengthOfFile));
                    // writing data to file
                    output.write(data, 0, count);
                }

                // flushing output
                output.flush();
                // closing streams
                output.close();
                input.close();
                return "Downloaded at: " + fileWithinMyDir;

            } catch (Exception e) {
                Log.e("Error: ", e.getMessage());
            }
            return "Something went wrong";
        }

        @Override
        protected void onPostExecute(String message) {
            GlobalTask.hideProgressDialog();
            new Shared(context).putString(Shared.LANGUAGE_PATH, "" + fileWithinMyDir);
            relative_quran_translation.setVisibility(View.GONE);
//            Intent in = new Intent(ActivityAlarmConfig.this , ActivityThequran.class);
//            in.putExtra("path",""+fileWithinMyDir);
//            startActivity(in);

//            Toast.makeText(getApplicationContext(),
//                    message, Toast.LENGTH_LONG).show();
        }
    }

    public class onLanguageSelectionListener {
        public void onSelectLanguage(int position) {
            if (previous_position != -1)
                languageTranslationModelArrayList.get(previous_position).setLanguage_selection_status(false);
            languageTranslationModelArrayList.get(position).setLanguage_selection_status(true);
            languageRecycler.notifyDataSetChanged();
            txt_select_language.setText(languageTranslationModelArrayList.get(position).getQuranTranslateLanguage());
            new DownloadFile().execute(languageTranslationModelArrayList.get(position).getQuranTranslateUrl());
            previous_position = position;
        }
    }

    private Long getTimeStamp(String tine) {
        return TimingUtils.getTimeStampOfTime(tine).getTime();
    }

    private void setTodayDate() {
        TextView txt_calendar = findViewById(R.id.txt_calendar);

        PrayerTimingModel prayerTimingModel = db.getByDate(TimingUtils.getToday(TimingUtils.SQLiteDateFormat));

        if (prayerTimingModel != null)
            txt_calendar.setText(prayerTimingModel.getHijriModel().getDay() + " " +
                    prayerTimingModel.getHijriModel().getMonth_en() + " " +
                    prayerTimingModel.getHijriModel().getYear());

        String nextPrayer = "";

        Date currenTimeStamp = TimingUtils.getTimeStampOfTime(TimingUtils.getToday(TimingUtils.HourMinutesTimeFormat));
        if (currenTimeStamp.getTime() < getTimeStamp(prayerTimingModel.getFajr())) {
            nextPrayer = "Fajr: " + prayerTimingModel.getFajr();
        } else if (currenTimeStamp.getTime() < getTimeStamp(prayerTimingModel.getDhuhr())) {
            nextPrayer = "Dhuhr: " + prayerTimingModel.getDhuhr();
        } else if (currenTimeStamp.getTime() < getTimeStamp(prayerTimingModel.getAsr())) {
            nextPrayer = "Asar: " + prayerTimingModel.getAsr();
        } else if (currenTimeStamp.getTime() < getTimeStamp(prayerTimingModel.getMaghrib())) {
            nextPrayer = "Maghrib: " + prayerTimingModel.getMaghrib();
        } else if (currenTimeStamp.getTime() < getTimeStamp(prayerTimingModel.getIsha())) {
            nextPrayer = "Isha: " + prayerTimingModel.getIsha();
        } else {
            prayerTimingModel = db.getByDate(TimingUtils.getNextDay(TimingUtils.SQLiteDateFormat));
            nextPrayer = "Fajr: " + prayerTimingModel.getFajr();
        }

        txt_calendar.setText(txt_calendar.getText().toString() + " | " + nextPrayer);
    }

    private void setSpinnerTranslation() {

        ArrayList<QuranTranslationModel> dataTranslation = new ArrayList<>();
        dataTranslation.add(new QuranTranslationModel(strLanguage[1], strLanguage[1]));
        dataTranslation.add(new QuranTranslationModel(strLanguage[2], strLanguage[2]));
        dataTranslation.add(new QuranTranslationModel(strLanguage[3], strLanguage[3]));

        QuranTranslationAdapter adapterTranslation = new QuranTranslationAdapter(context, dataTranslation);
        //spnTranslation.setAdapter(adapterTranslation);
    }

    private void callApi() {
        GlobalTask.showProgressDialog(context);

        Log.e("REQ_DATA", new Gson().toJson(GlobalVariable.getRegistrationModel()));

        if (GlobalVariable.getRegistrationModel().getProvider().trim().length() > 0) {
            Api.getClient().ExternalRegister(GlobalVariable.getRegistrationModel()).enqueue(new Callback<ResultComman>() {
                @Override
                public void onResponse(Call<ResultComman> call, Response<ResultComman> response) {
                    ResultComman resultObj = response.body();
                    if (resultObj != null) {
                        if (resultObj.getContent() instanceof String) {
                            GlobalTask.showToastMessage(context, resultObj.getContent().toString());
                        } else if (resultObj.getContent() instanceof LinkedTreeMap) {
                            JsonObject jsonObject = new Gson().toJsonTree(resultObj.getContent()).getAsJsonObject();
                            new Shared(context).putString(Shared.userProfile, jsonObject.toString());
                            Intent intent = new Intent(context, Dashboard.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
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
        } else {
            Api.getClient().Register(GlobalVariable.getRegistrationModel()).enqueue(new Callback<ResultComman>() {
                @Override
                public void onResponse(Call<ResultComman> call, Response<ResultComman> response) {
                    ResultComman resultObj = response.body();
                    if (resultObj != null) {
                        if (resultObj.getContent() instanceof String) {
                            GlobalTask.showToastMessage(context, resultObj.getContent().toString());
                            Intent intent = new Intent(context, Login.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                            finish();
                        } else if (resultObj.getContent() instanceof LinkedTreeMap) {
                            JsonObject jsonObject = new Gson().toJsonTree(resultObj.getContent()).getAsJsonObject();
                            new Shared(context).putString(Shared.userProfile, jsonObject.toString());
                            Intent intent = new Intent(context, Dashboard.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                            finish();
                        }
                    } else {
                        try {
                            String data = response.errorBody().string();
                            JSONObject jsonObject = new JSONObject(data);
                            if (jsonObject.has("errorMessage"))
                                GlobalTask.showToastMessage(context, jsonObject.getString("errorMessage"));

                            /*if (jsonObject.has("statusCode")) {
                                if (jsonObject.getInt("statusCode") == 400) {
                                    Intent intent = new Intent(context, Welcome.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    startActivity(intent);
                                    finish();
                                }
                            }*/
                        } catch (JSONException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
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

    /*Location*/
    final int PERMISSION_ALL = 1;
    String[] PERMISSIONS = {
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION
    };

    private boolean checkPermissions() {
        if (!hasPermissions(context, PERMISSIONS)) {
            ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_ALL);
            return false;
        }
        return true;
    }

    public boolean hasPermissions(Context context, String... permissions) {
        if (context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissionsList[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_ALL: {
                if (grantResults.length > 0) {
                    String permissionsDenied = "";
                    for (String per : permissionsList) {
                        if (grantResults[0] == PackageManager.PERMISSION_DENIED) {
                            permissionsDenied += "\n" + per;
                        }
                    }
                    callApiForPrayerTimes();
                }
                return;
            }
        }
    }
}
