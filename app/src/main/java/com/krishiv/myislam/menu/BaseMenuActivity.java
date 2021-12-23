package com.krishiv.myislam.menu;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.krishiv.myislam.API.ApiInterface;
import com.krishiv.myislam.ItemClickListener;
import com.krishiv.myislam.R;
import com.krishiv.myislam.activity.ActivityDuas;
import com.krishiv.myislam.activity.ActivitySignUpp;
import com.krishiv.myislam.activity.ActivityThequran;
import com.krishiv.myislam.activity.Calendar;
import com.krishiv.myislam.activity.Community;
import com.krishiv.myislam.activity.Dashboard;
import com.krishiv.myislam.activity.Event;
import com.krishiv.myislam.activity.Faith;
import com.krishiv.myislam.activity.Hadiths;
import com.krishiv.myislam.activity.Instruction;
import com.krishiv.myislam.activity.Mosque;
import com.krishiv.myislam.activity.NameOfAllah;
import com.krishiv.myislam.activity.Pilgrim;
import com.krishiv.myislam.activity.PilgrimFind;
import com.krishiv.myislam.activity.PilgrimHajjUmrah;
import com.krishiv.myislam.activity.Prayer;
import com.krishiv.myislam.activity.ProphetMuhmmad;
import com.krishiv.myislam.activity.Qibla;
import com.krishiv.myislam.activity.Qurbani;
import com.krishiv.myislam.activity.RequestForPrayer;
import com.krishiv.myislam.activity.Setting;
import com.krishiv.myislam.activity.Taweed;
import com.krishiv.myislam.db.PrayerTimingDAO;
import com.krishiv.myislam.dto.LoginModel;
import com.krishiv.myislam.dto.NotiHandler;
import com.krishiv.myislam.dto.PrayerTimingModel;
import com.krishiv.myislam.dto.ResultComman;
import com.krishiv.myislam.utils.GPSTracker;
import com.krishiv.myislam.utils.GlobalTask;
import com.krishiv.myislam.utils.LocaleHelper;
import com.krishiv.myislam.utils.Shared;
import com.krishiv.myislam.utils.TimingUtils;
import com.krishiv.myislam.utils.bgservice.ConfigNextAlertData;
import com.krishiv.myislam.utils.bgservice.Util;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class BaseMenuActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    public static Object object;
    MenuAdapter menuAdapter;
    ExpandableListView expListView;
    List<String> listDataHeader;
    HashMap<String, List<String>> listDataChild;
    public FrameLayout container;
    ImageView img_menu, img_logo_menu;
    LayoutInflater inflater;
    public Context context;
    public Shared shared;
    public static LoginModel userProfile;
    public ImageView bottom_line;
    private TextView txt_calendar;
    public static PrayerTimingDAO db;

    protected void attachBaseContext(Context newBase) {
        String currentLanguage = LocaleHelper.getLanguage(newBase);
        if(currentLanguage==null)
        {
            currentLanguage = "en";
            super.attachBaseContext(LocaleHelper.setLocale(newBase,currentLanguage));
        }
        else
        {
            super.attachBaseContext(LocaleHelper.setLocale(newBase,currentLanguage));
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        object = this;
        context = this;
        shared = new Shared(context);
        userProfile = new Gson().fromJson(shared.getString(Shared.userProfile,""), LoginModel.class);
        db = new PrayerTimingDAO(context);
        inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        container = findViewById(R.id.container);
        img_menu = findViewById(R.id.img_menu);
        bottom_line = findViewById(R.id.bottom_line);
        txt_calendar = findViewById(R.id.txt_calendar);
        db.getByDate(TimingUtils.getToday(TimingUtils.SQLiteDateFormat));
        if(!db.isAvailable(TimingUtils.getToday(TimingUtils.SQLiteDateFormat))) {
            String mm = TimingUtils.getToday(TimingUtils.Month);
            String yyyy = TimingUtils.getToday(TimingUtils.Year);
            callApiForPrayerTimes(mm, yyyy, null);
        }
        else
            setTodayDate();

        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int width = metrics.widthPixels;

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View headerContainer = navigationView.getHeaderView(0);
        img_logo_menu = headerContainer.findViewById(R.id.img_logo_menu);
//        Menu Expandable

        expListView = (ExpandableListView) findViewById(R.id.lvExp);
        prepareListData();
        menuAdapter = new MenuAdapter(this, listDataHeader, listDataChild);
        expListView.setAdapter(menuAdapter);

        expListView.setChildIndicator(null);
        expListView.setDividerHeight(0);
        setItemclickListener();

        expListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {

            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {

                if (groupPosition == 0){
                    if (childPosition == 0){
                        startActivity(new Intent(context, NameOfAllah.class));
                    }else if (childPosition == 1){
                        startActivity(new Intent(context, ActivityThequran.class));
                    }else if (childPosition == 2){
                        startActivity(new Intent(context, Hadiths.class));
                    }else if (childPosition == 3){
                        startActivity(new Intent(context, Taweed.class));
                    }else if (childPosition == 4){
                        startActivity(new Intent(context, ProphetMuhmmad.class));
                    }
                }else if (groupPosition == 1){
                    if (childPosition == 0){
                        startActivity(new Intent(context, Qibla.class));
                    }else if (childPosition == 1){
                        startActivity(new Intent(context, Instruction.class));
                    }else if (childPosition == 2){
                        startActivity(new Intent(context, ActivityDuas.class));
                    }else if (childPosition == 3){
                        startActivity(new Intent(context, Hadiths.class));
                    }else if (childPosition == 4){
                        startActivity(new Intent(context, Mosque.class).putExtra("activity","mosque"));
                    }else if (childPosition == 5){
                        startActivity(new Intent(context, ActivityThequran.class));
                    }
                }else if (groupPosition == 4){
                    if (childPosition == 0){
                        startActivity(new Intent(context, PilgrimHajjUmrah.class).putExtra("actionFor", Pilgrim.ActionPilgrim.Hajj.ordinal()));
                    }else if (childPosition == 1){
                        startActivity(new Intent(context, PilgrimHajjUmrah.class).putExtra("actionFor", Pilgrim.ActionPilgrim.HajjGuide.ordinal()));
                    }else if (childPosition == 2){
                        startActivity(new Intent(context, PilgrimFind.class));
                    }
                }else if (groupPosition == 5){
                    if (childPosition == 0){
                        startActivity(new Intent(context, Mosque.class).putExtra("activity","mosque"));
                    }else if (childPosition == 1){
                        startActivity(new Intent(context, Mosque.class).putExtra("activity","restaurant"));
                    }else if (childPosition == 2){
                        startActivity(new Intent(context, RequestForPrayer.class));
                    }else if (childPosition == 3){
                        startActivity(new Intent(context, Qurbani.class));
                    }else if (childPosition == 4){
                        startActivity(new Intent(context, Event.class));
                    }
                }

                /*GlobalTask.showToastMessage(
                        getApplicationContext(),
                        listDataHeader.get(groupPosition)
                                + " : "
                                + listDataChild.get(
                                listDataHeader.get(groupPosition)).get(
                                childPosition));*/
                return false;
            }
        });

        /*expListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {

            @Override
            public void onGroupExpand(int groupPosition) {
                GlobalTask.showToastMessage(getApplicationContext(),
                        listDataHeader.get(groupPosition) + " Expanded");
            }
        });

        expListView.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {

            @Override
            public void onGroupCollapse(int groupPosition) {
                GlobalTask.showToastMessage(getApplicationContext(),
                        listDataHeader.get(groupPosition) + " Collapsed");

            }
        });*/

        expListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView expandableListView, View view, int i, long l) {
                return true;
            }
        });

        img_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(drawer.isDrawerOpen(Gravity.START)){
                    drawer.closeDrawer(Gravity.START);
                }else{
                    drawer.openDrawer(Gravity.START);
                }
            }
        });

        img_logo_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (object instanceof Dashboard){}
                else {
                    Intent intent = new Intent(context, Dashboard.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish();
                }
            }
        });

        txt_calendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (object instanceof Calendar){}
                else
                    startActivity(new Intent(context, Calendar.class));
            }
        });
    }

    private void setItemclickListener() {
        menuAdapter.setClickListener(new ItemClickListener() {
            @Override
            public void itemClicked(View View, int position, int clickedId) {
                if (clickedId == 0) {
                    if (position == 0) {
                        startActivity(new Intent(context, Faith.class));
                    }else if (position == 1) {
                        startActivity(new Intent(context, Prayer.class));
                    }else if (position == 2) {

                    }else if (position == 3) {

                    }else if (position == 4) {
                        startActivity(new Intent(context, Pilgrim.class));
                    }else if (position == 5) {
                        startActivity(new Intent(context, Community.class));
                    }else if (position == 6) {
                        startActivity(new Intent(context, Setting.class));
                    } else if (position == 7) {
                        shared.clearAll();
                        /*Intent serviceIntent = new Intent(context, CustomAlarmService.class);
                        stopService(serviceIntent);*/
                        Intent intent = new Intent(context, ActivitySignUpp.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        finish();
                    }
                }else{
                    if (expListView.isGroupExpanded(position))
                        expListView.collapseGroup(position);
                    else
                        expListView.expandGroup(position);
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        object = this;
    }

    public View setContentViev(int resource){
        View v = inflater.inflate(resource, null);
        container.addView(v);
        return v;
    }

    private void prepareListData() {
        listDataHeader = new ArrayList<String>();
        listDataChild = new HashMap<String, List<String>>();

        listDataHeader.add(getResourceString(R.string.faith));
        listDataHeader.add(getResourceString(R.string.prayer));
        listDataHeader.add(getResourceString(R.string.charity));
        listDataHeader.add(getResourceString(R.string.fasting));
        listDataHeader.add(getResourceString(R.string.pilgrimage));
        listDataHeader.add(getResourceString(R.string.community));
        listDataHeader.add(getResourceString(R.string.settings));
        listDataHeader.add("Logout");

        List<String> faith = new ArrayList<String>();
        faith.add(getResourceString(R.string.names_of_allah));
        faith.add(getResourceString(R.string.the_quran));
        faith.add(getResourceString(R.string.hadiths));
        faith.add(getResourceString(R.string.taweed));
        faith.add(getResourceString(R.string.prophet_muhammad));

        List<String> prayer = new ArrayList<String>();
        prayer.add(getResourceString(R.string.qibla_locator));
        prayer.add(getResourceString(R.string.menu_how_to_pray));
        prayer.add(getResourceString(R.string.duas_and_misbaha));
        prayer.add(getResourceString(R.string.hadiths));
        prayer.add(getResourceString(R.string.mosques));
        prayer.add(getResourceString(R.string.the_quran));

        List<String> fasting = new ArrayList<String>();
        fasting.add(getResourceString(R.string.ramadan_calendar));
        fasting.add(getResourceString(R.string.duas));

        List<String> charity = new ArrayList<String>();
        charity.add(getResourceString(R.string.zakat_calculator));
        charity.add(getResourceString(R.string.donate));
        charity.add(getResourceString(R.string.qurbani));
        charity.add(getResourceString(R.string.voting));


        List<String> pilgrimage = new ArrayList<String>();
        pilgrimage.add(getResourceString(R.string.start_preparation));
        pilgrimage.add(getResourceString(R.string.pilgrimage_guide));
        pilgrimage.add(getResourceString(R.string.find_pilgrim));

        List<String> community = new ArrayList<String>();
        community.add(getResourceString(R.string.mosques));
        community.add(getResourceString(R.string.halal_restaurant));
        community.add(getResourceString(R.string.prayer_request));
        community.add(getResourceString(R.string.qurbani));
        community.add(getResourceString(R.string.events));

        List<String> settings = new ArrayList<String>();
        List<String> logout = new ArrayList<String>();
        //settings.add("Allah");

        listDataChild.put(listDataHeader.get(0), faith);
        listDataChild.put(listDataHeader.get(1), prayer);
        listDataChild.put(listDataHeader.get(2), faith);
        listDataChild.put(listDataHeader.get(3), charity);
        listDataChild.put(listDataHeader.get(4), pilgrimage);
        listDataChild.put(listDataHeader.get(5), community);
        listDataChild.put(listDataHeader.get(6), settings);
        listDataChild.put(listDataHeader.get(7), logout);
    }

    public int GetPixelFromDips(float pixels) {
        // Get the screen's density scale
        final float scale = getResources().getDisplayMetrics().density;
        // Convert the dps to pixels, based on density scale
        return (int) (pixels * scale + 0.5f);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if(android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.JELLY_BEAN_MR2) {
//            expListView.setIndicatorBounds(expListView.getRight()-50, expListView.getLeft());
            expListView.setIndicatorBounds(expListView.getRight() - GetPixelFromDips(70), expListView.getLeft());
        } else {
//            expListView.setIndicatorBoundsRelative(expListView.getRight()-50, expListView.getLeft());
            expListView.setIndicatorBoundsRelative(expListView.getRight() - GetPixelFromDips(70), expListView.getLeft());
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    protected interface CallBackTask{
        void onTaskDone();
    }

    protected void callApiForPrayerTimes(String mm, String yyyy, final CallBackTask callBackTask) {
        if (!checkPermissions())
            return;

        final ProgressDialog progressDialog = GlobalTask.getProgressDialog(context);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://api.aladhan.com")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiInterface api = retrofit.create(ApiInterface.class);

        GPSTracker gpsTracker = new GPSTracker(context);

        api.GetPrayerTiming(gpsTracker.getLatitude()+"", gpsTracker.getLongitude()+"","1",mm, yyyy).enqueue(new Callback<ResultComman>() {
            @Override
            public void onResponse(Call<ResultComman> call, Response<ResultComman> response) {
                ResultComman resultObj = response.body();
                if (resultObj != null) {

                    ArrayList<PrayerTimingModel> data = GlobalTask.filterCalendarData(resultObj);

                    if (data.size()>0)
                        db.saveAll(data);

                    setTodayDate();

                    if (callBackTask != null)
                        callBackTask.onTaskDone();

                } else {
                    GlobalTask.showTostErrorResponse(context, response.errorBody());
                }

                progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<ResultComman> call, Throwable t) {
                progressDialog.dismiss();
            }
        });
    }

    private String getResourceString(int id){
        return getResources().getString(id);
    }

    private void setTodayDate() {
        PrayerTimingModel prayerTimingModel = db.getByDate(TimingUtils.getToday(TimingUtils.SQLiteDateFormat));

        if (prayerTimingModel != null)
            txt_calendar.setText(prayerTimingModel.getHijriModel().getDay() + " " +
                    prayerTimingModel.getHijriModel().getMonth_en() + " " +
                    prayerTimingModel.getHijriModel().getYear());

        String nextPrayer = "";

        Date currenTimeStamp = TimingUtils.getTimeStampOfTime(TimingUtils.getToday(TimingUtils.HourMinutesTimeFormat));
        if (currenTimeStamp.getTime() < getTimeStamp(prayerTimingModel.getFajr())){
            nextPrayer = getResourceString(R.string.fajr) + ": "  + prayerTimingModel.getFajr();
        }else if (currenTimeStamp.getTime() < getTimeStamp(prayerTimingModel.getDhuhr())){
            nextPrayer = getResourceString(R.string.dhudr) + ": "  + prayerTimingModel.getDhuhr();
        }else if (currenTimeStamp.getTime() < getTimeStamp(prayerTimingModel.getAsr())){
            nextPrayer = getResourceString(R.string.asar) + ": "  + prayerTimingModel.getAsr();
        }else if (currenTimeStamp.getTime() < getTimeStamp(prayerTimingModel.getMaghrib())){
            nextPrayer = getResourceString(R.string.magrib) + ": "  + prayerTimingModel.getMaghrib();
        }else if (currenTimeStamp.getTime() < getTimeStamp(prayerTimingModel.getIsha())){
            nextPrayer = getResourceString(R.string.isha) + ": "  + prayerTimingModel.getIsha();
        }else{
            prayerTimingModel = db.getByDate(TimingUtils.getNextDay(TimingUtils.SQLiteDateFormat));
            nextPrayer = getResourceString(R.string.fajr) + ": "  + prayerTimingModel.getFajr();
        }

        txt_calendar.setText(txt_calendar.getText().toString() +  " | " + nextPrayer);

        /*NotiHandler previouData = new Gson().fromJson(shared.getString(shared.upcomingAlarmData,""), NotiHandler.class);
        if (previouData == null) {
            Intent intent = new Intent(context, AlarmReceiver.class);
            sendBroadcast(intent);
        }*/
        /*if (!CustomAlarmService.isServiceRunning) {
            if (!shared.getString(Shared.userProfile, "").isEmpty()) {
                Intent serviceIntent = new Intent(this, CustomAlarmService.class);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    startForegroundService(serviceIntent);
                } else {
                    startService(serviceIntent);
                }
            }
        }*/
        if (!shared.getString(Shared.userProfile, "").isEmpty()) {
            NotiHandler previouData = new Gson().fromJson(shared.getString(shared.upcomingAlarmData,""), NotiHandler.class);

            if (previouData != null) {
                long current = TimingUtils.getStringToDateFormat(TimingUtils.AlarmTimeDateFormat, TimingUtils.getToday(TimingUtils.AlarmTimeDateFormat)).getTime();
                long upcoming = TimingUtils.getStringToDateFormat(TimingUtils.AlarmTimeDateFormat, previouData.date + " " + previouData.time).getTime();

                Log.e("Time", ":Current:" + TimingUtils.getDateToString(TimingUtils.AlarmTimeDateFormat, new Date(current)));
                Log.e("Time", ":Upcoming:" + TimingUtils.getDateToString(TimingUtils.AlarmTimeDateFormat, new Date(upcoming)));

                if (upcoming < current){
                    new ConfigNextAlertData(context);
                    Util.scheduleJob(context);
                }
            }else {
                new ConfigNextAlertData(context);
                Util.scheduleJob(context);
            }
        }
    }

    private Long getTimeStamp(String tine){
        return TimingUtils.getTimeStampOfTime(tine).getTime();
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
                    String mm = TimingUtils.getToday(TimingUtils.Month);
                    String yyyy = TimingUtils.getToday(TimingUtils.Year);
                    callApiForPrayerTimes(mm, yyyy, null);
                }
                return;
            }
        }
    }
}
