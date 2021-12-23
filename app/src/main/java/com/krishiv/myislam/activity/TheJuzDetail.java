package com.krishiv.myislam.activity;

import android.app.ActivityManager;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.krishiv.myislam.R;
import com.krishiv.myislam.adapter.JazDetailAdapter;
import com.krishiv.myislam.dto.QuranDetailModel;
import com.krishiv.myislam.dto.SuraModel;
import com.krishiv.myislam.menu.BaseMenuActivity;
import com.krishiv.myislam.service.NotificationService;
import com.krishiv.myislam.utils.Constants;
import com.krishiv.myislam.utils.Shared;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class TheJuzDetail extends BaseMenuActivity {

    RecyclerView detail_recycler;
    JazDetailAdapter theQuranDetailAdapter;
    public String sura_id = "";
    public String aya_index = "";
    ArrayList<QuranDetailModel> list = new ArrayList<>();
    ArrayList<QuranDetailModel> mainArraylist = new ArrayList<>();
    Context context;
    Shared shared;
    ImageView back;
    String path;

    ArrayList<String> songList = new ArrayList<>();
    private boolean playPause;
    private MediaPlayer mediaPlayer;
    private ProgressDialog progressDialog;
    private boolean initialStage = true;
    Animation animation;
    int currentUrlPos = 0;
    ImageView img_play, img_next, imge_previous, img_close;
    RelativeLayout relative_song;
    ArrayList<SuraModel> suraList;
    String isSongPlaying;
    int nextIndex;
    boolean isBismillahAdded = false;
    private SeekBar songPrgs;
    TextView startTime, songTime, txtVw1;
    private static int sTime = 0, eTime = 0 ,  showtime = 0;;
    private double timeElapsed = 0, finalTime = 0;
    private Handler durationHandler = new Handler();
    public String isLastPosition = "";
    LinearLayoutManager linearLayoutManager;
    public boolean isPlaying = true;
    String sura_name , arabic_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentViev(R.layout.activity_quran_detail);
        sura_id = getIntent().getStringExtra("sura_id");
        aya_index = getIntent().getStringExtra("aya_index");
//        path = getIntent().getStringExtra("path");
        shared = new Shared(TheJuzDetail.this);
        path = shared.getString(Shared.LANGUAGE_PATH, "");
        isLastPosition = getIntent().getStringExtra("isLast");
        suraList = this.getIntent().getParcelableArrayListExtra("suraList");
        isSongPlaying = getIntent().getStringExtra("isSongPlaying");
        sura_name = getIntent().getStringExtra("sura_name");
        arabic_name = getIntent().getStringExtra("arabic_name");
        nextIndex = getIntent().getIntExtra("nextIndex", 0);
        if (isSongPlaying!=null){
            if (isSongPlaying.equals("true")) {
                sura_id = suraList.get((nextIndex + 1)).get_index();
                sura_name = suraList.get((nextIndex + 1)).get_ename();
                arabic_name= suraList.get((nextIndex + 1)).get_name();
            }
        }

        init();
        player();
    }

    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String message = intent.getStringExtra("Status");
            if (!isAppIsInBackground(TheJuzDetail.this)) {
                if (message.equals("show")) {
                    int pos = intent.getIntExtra("currentPosition", 0);
                    for (int i = 0; i < mainArraylist.size(); i++) {
                        if (i == pos)
                            mainArraylist.get(i).setPlaying(true);
                        else mainArraylist.get(i).setPlaying(false);
                    }
                    linearLayoutManager.scrollToPositionWithOffset(pos, 0);
                    theQuranDetailAdapter.notifyDataSetChanged();
                    progressDialog = new ProgressDialog(TheJuzDetail.this);
                    progressDialog.setMessage("Buffering..");
                    progressDialog.show();
                } else {
                    progressDialog.cancel();
                    eTime = intent.getIntExtra("eTime", 0);
                    finalTime = intent.getDoubleExtra("finalTime", 0.0);
                    songPrgs.setMax((int) finalTime);
                    timeElapsed = intent.getDoubleExtra("timeElapsed", 0.0);
                    startTime.setText(String.format("%d: %02d", TimeUnit.MILLISECONDS.toMinutes((long) timeElapsed), TimeUnit.MILLISECONDS.toSeconds((long) timeElapsed) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long) timeElapsed))));
                    songTime.setText(String.format("%d : %02d", TimeUnit.MILLISECONDS.toMinutes(eTime),
                            TimeUnit.MILLISECONDS.toSeconds(eTime) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(eTime))));
                }
            }
        }
    };

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
//            timeElapsed = intent.getDoubleExtra("timeElapsed", 0.0);
            showtime = intent.getIntExtra("timeElapsed", 0);
            songPrgs.setProgress(showtime);
            double timeRemaining = (showtime);
            startTime.setText(String.format("%d: %02d", TimeUnit.MILLISECONDS.toMinutes((long) timeRemaining), TimeUnit.MILLISECONDS.toSeconds((long) timeRemaining) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long) timeRemaining))));
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(TheJuzDetail.this).registerReceiver(
                mMessageReceiver, new IntentFilter("ProgressUpdate"));
        registerReceiver(broadcastReceiver, new IntentFilter(NotificationService.BROADCAST_ACTION));
        if (isSongPlaying.equals("true")) {
            playPause = true;
            relative_song.setAnimation(animation);
            relative_song.setVisibility(View.VISIBLE);
            currentUrlPos = 0;
            img_play.setImageDrawable(getResources().getDrawable(R.drawable.pause));
            startService();
            relative_song.setAnimation(animation);
            relative_song.setVisibility(View.VISIBLE);
            for (int i = 0; i < mainArraylist.size(); i++) {
                if (i == 0)
                    mainArraylist.get(i).setPlaying(true);
                else mainArraylist.get(i).setPlaying(false);
            }
            theQuranDetailAdapter.notifyDataSetChanged();
        }
    }


    public void player() {
        img_play = (ImageView) findViewById(R.id.img_play);
        img_next = (ImageView) findViewById(R.id.img_next);
        imge_previous = (ImageView) findViewById(R.id.imge_previous);
        img_close = (ImageView) findViewById(R.id.img_close);
        relative_song = (RelativeLayout) findViewById(R.id.relative_song);
        startTime = (TextView) findViewById(R.id.txtStartTime);
        songTime = (TextView) findViewById(R.id.txtSongTime);
        songPrgs = findViewById(R.id.seekBar);
        txtVw1 = findViewById(R.id.txtVw1);
        songPrgs.setClickable(false);

        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        img_play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isPlaying) {
                    pauseSong();
                } else {
                    startSongAgain();
                }
            }
        });
        img_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nextSong();
            }
        });
        imge_previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                durationHandler.removeCallbacks(updateSeekBarTime);
                img_play.setImageDrawable(getResources().getDrawable(R.drawable.pause));
                nextIndex--;
                if (currentUrlPos != 0) {
                    currentUrlPos--;
                    startService();
                    relative_song.setAnimation(animation);
                    relative_song.setVisibility(View.VISIBLE);
                    for (int i = 0; i < mainArraylist.size(); i++) {
                        if (i == currentUrlPos)
                            mainArraylist.get(i).setPlaying(true);
                        else mainArraylist.get(i).setPlaying(false);
                    }
                    theQuranDetailAdapter.notifyDataSetChanged();
                } else {
                    durationHandler.removeCallbacks(updateSeekBarTime);
                    stopSong();
                    playPause = false;
                    initialStage = true;
                    relative_song.setVisibility(View.GONE);

                    for (int i = 0; i < mainArraylist.size(); i++) {
                        mainArraylist.get(i).setPlaying(false);
                    }
                    theQuranDetailAdapter.notifyDataSetChanged();
                }
//                else {
//                    stopSong();
//                    durationHandler.removeCallbacks(updateSeekBarTime);
//                    LocalBroadcastManager.getInstance(TheQuranDetailActivity.this).unregisterReceiver(mMessageReceiver);
//                    playPause = false;
//                    initialStage = true;
//                    relative_song.setVisibility(View.GONE);
//                    for (int i = 0; i < mainArraylist.size(); i++) {
//                        mainArraylist.get(i).setPlaying(false);
//                    }
//                    theQuranDetailAdapter.notifyDataSetChanged();
//
//                    progressDialog.setMessage("Please wait..");
//                    progressDialog.show();
//                    ;
//                    progressDialog.setCancelable(false);
//                    new Handler().postDelayed(new Runnable() {
//                        @Override
//                        public void run() {
//                            if (progressDialog != null)
//                                progressDialog.cancel();
//                            finish();
//                            Intent intent = new Intent(TheQuranDetailActivity.this, TheQuranDetailActivity.class);
//                            intent.putExtra("isSongPlaying", "true");
//                            intent.putParcelableArrayListExtra("suraList", suraList);
//                            if (nextIndex == songList.size()) {
//                                intent.putExtra("nextIndex", 0);
//                                intent.putExtra("isLast", "YES");
//                            } else {
//                                intent.putExtra("nextIndex", nextIndex);
//                                intent.putExtra("isLast", "NO");
//                            }
//                            startActivity(intent);
//                            finish();
//                        }
//                    }, 1000);
//                }
            }
        });

        img_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                durationHandler.removeCallbacks(updateSeekBarTime);
                stopSong();
                playPause = false;
                initialStage = true;
                relative_song.setVisibility(View.GONE);
                for (int i = 0; i < mainArraylist.size(); i++) {
                    mainArraylist.get(i).setPlaying(false);
                }
                theQuranDetailAdapter.notifyDataSetChanged();
            }
        });
    }

    private Runnable updateSeekBarTime = new Runnable() {
        public void run() {
            songPrgs.setProgress((int) timeElapsed);
//            double timeRemaining = finalTime - timeElapsed;
            double timeRemaining = timeElapsed;
            startTime.setText(String.format("%d: %02d", TimeUnit.MILLISECONDS.toMinutes((long) timeRemaining), TimeUnit.MILLISECONDS.toSeconds((long) timeRemaining) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long) timeRemaining))));
            durationHandler.postDelayed(this, 100);
        }
    };

    @Override
    protected void onPause() {
        super.onPause();
        int i = linearLayoutManager.findFirstVisibleItemPosition();
        shared.putInt("LastPosition",i);
        i++;
        shared.putString("LastSuraOrJuz","Juz");
        shared.putString("LastSuraId",sura_id);
        shared.putString("Last",""+sura_name+" ("+""+i+")");
        shared.putString("LastArabic",""+arabic_name);

        shared.putString("aya_ind",aya_index);
        unregisterReceiver(broadcastReceiver);
    }

    public void startService() {
        Intent serviceIntent = new Intent(TheJuzDetail.this, NotificationService.class);
        serviceIntent.setAction(com.krishiv.myislam.service.Constants.ACTION.STARTFOREGROUND_ACTION);
        serviceIntent.putParcelableArrayListExtra("list", mainArraylist);
        serviceIntent.putExtra("notification", true);
        serviceIntent.putExtra("play_or_pause", "play");
        serviceIntent.putExtra("current_pos", currentUrlPos);
        isPlaying = true;
        startService(serviceIntent);
    }

    public void pauseSong() {
        isPlaying = false;
        Intent serviceIntent = new Intent(TheJuzDetail.this, NotificationService.class);
        serviceIntent.putExtra("play_or_pause", "pause");
        serviceIntent.putExtra("notification", true);
        startService(serviceIntent);
        img_play.setImageDrawable(getResources().getDrawable(R.drawable.play));
    }

    public void startSongAgain() {
        isPlaying = true;
        Intent serviceIntent = new Intent(TheJuzDetail.this, NotificationService.class);
        serviceIntent.putExtra("play_or_pause", "start_again");
        serviceIntent.putExtra("notification", true);
        startService(serviceIntent);
        img_play.setImageDrawable(getResources().getDrawable(R.drawable.pause));
    }

    public void stopSong() {
        isPlaying = false;

        Intent serviceIntent = new Intent(TheJuzDetail.this, NotificationService.class);
        serviceIntent.setAction(com.krishiv.myislam.service.Constants.ACTION.STARTFOREGROUND_ACTION);
        serviceIntent.putExtra("play_or_pause", "stop");
        serviceIntent.putExtra("notification", true);
        startService(serviceIntent);
        img_play.setImageDrawable(getResources().getDrawable(R.drawable.play));
    }

    public void nextSong() {
//        unregisterReceiver(broadcastReceiver);
        currentUrlPos++;
        if (currentUrlPos < (songList.size() - 1)) {
            isPlaying = true;
            //registerReceiver(broadcastReceiver, new IntentFilter(NotificationService.BROADCAST_ACTION));
            Intent serviceIntent = new Intent(TheJuzDetail.this, NotificationService.class);
            serviceIntent.setAction(com.krishiv.myislam.service.Constants.ACTION.STARTFOREGROUND_ACTION);
            serviceIntent.putParcelableArrayListExtra("list", mainArraylist);
            serviceIntent.putExtra("notification", true);
            serviceIntent.putExtra("play_or_pause", "next");
            serviceIntent.putExtra("current_pos", currentUrlPos);
            startService(serviceIntent);
            img_play.setImageDrawable(getResources().getDrawable(R.drawable.pause));
        } else {
//            Intent myService = new Intent(TheQuranDetailActivity.this, NotificationService.class);
//            stopService(myService);
            stopSong();
            durationHandler.removeCallbacks(updateSeekBarTime);
            LocalBroadcastManager.getInstance(this).unregisterReceiver(mMessageReceiver);
            playPause = false;
            initialStage = true;
            relative_song.setVisibility(View.GONE);
            for (int i = 0; i < mainArraylist.size(); i++) {
                mainArraylist.get(i).setPlaying(false);
            }
            theQuranDetailAdapter.notifyDataSetChanged();

            progressDialog.setMessage("Please wait..");
            progressDialog.show();
            progressDialog.setCancelable(false);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    isPlaying = true;
                    if (progressDialog != null)
                        progressDialog.cancel();
                    finish();
                    Intent intent = new Intent(TheJuzDetail.this, TheQuranDetailActivity.class);
                    intent.putExtra("isSongPlaying", "true");
                    intent.putParcelableArrayListExtra("suraList", suraList);
                    if (nextIndex == songList.size()) {
                        intent.putExtra("nextIndex", 0);
                        intent.putExtra("isLast", "YES");
                    } else {
                        intent.putExtra("nextIndex", nextIndex);
                        intent.putExtra("isLast", "NO");
                    }
                    startActivity(intent);
                    finish();
                }
            }, 1000);
        }
        playPause = true;
    }


    private void init() {
        context = this;
        shared = new Shared(context);
        detail_recycler = findViewById(R.id.detail_recycler);
        back = findViewById(R.id.arrow_back);
//        detail_recycler.setLayoutManager(new LinearLayoutManager(this));
        mainArraylist = parseDetails(sura_id);
        ArrayList<QuranDetailModel> AppLanguageArraylist = parseDetailsAppLanguage(sura_id);

//        for (int i = 0; i < mainArraylist.size(); i++) {
//            mainArraylist.get(i).setAppLanugageName(AppLanguageArraylist.get(i).getAya_text());
//        }


        for (int i = 0; i < mainArraylist.size(); i++) {
            String audio_url = "";
            if (mainArraylist.get(i).getSura_index().length() == 1) {
                audio_url = Constants.QURAN_AUDIO_BASE_URL + "00" + mainArraylist.get(i).getSura_index();
            } else if (mainArraylist.get(i).getSura_index().length() == 2) {
                audio_url = Constants.QURAN_AUDIO_BASE_URL + "0" + mainArraylist.get(i).getSura_index();
            } else {
                audio_url = Constants.QURAN_AUDIO_BASE_URL + mainArraylist.get(i).getSura_index();
            }
            if (mainArraylist.get(i).getMain_index().length() == 1) {
                audio_url = audio_url + "00" + mainArraylist.get(i).getMain_index();
            } else if (mainArraylist.get(i).getMain_index().length() == 2) {
                audio_url = audio_url + "0" + mainArraylist.get(i).getMain_index();
            } else {
                audio_url = audio_url + mainArraylist.get(i).getMain_index();
            }
            audio_url = audio_url + ".mp3";
            mainArraylist.get(i).setAudio_url(audio_url);
            songList.add(audio_url);
            mainArraylist.get(i).setAppLanugageName(AppLanguageArraylist.get(i).getAya_text());
        }

        ArrayList<QuranDetailModel> saveArrayList = shared.getFavoritesQuran(context);

        if (saveArrayList != null) {
            for (int pp = 0; pp < mainArraylist.size(); pp++) {
                for (int p = 0; p < saveArrayList.size(); p++) {
                    if (mainArraylist.get(pp).getSura_index().equals(saveArrayList.get(p).getSura_index()) && mainArraylist.get(pp).getMain_index().equals(saveArrayList.get(p).getMain_index())) {
                        mainArraylist.get(pp).setFavourites(true);
                    }
                }
            }
        }
        linearLayoutManager = new LinearLayoutManager(context);
        detail_recycler.setLayoutManager(linearLayoutManager);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        theQuranDetailAdapter = new JazDetailAdapter(context, mainArraylist, new onSelectStar(), isBismillahAdded);

        if (shared.getString("Last", "").equals("")) {
            final int desired_index = (Integer.parseInt(aya_index) - 1);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    detail_recycler.setAdapter(theQuranDetailAdapter);
                    detail_recycler.getLayoutManager().scrollToPosition(desired_index);
                }
            }, 200);
        }else {
            final int desired_index = shared.getInt("LastPosition");
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    detail_recycler.setAdapter(theQuranDetailAdapter);
                    detail_recycler.getLayoutManager().scrollToPosition(desired_index);
                }
            }, 200);
        }

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    public class onSelectStar {
        public void onSelect(int pos, boolean status) {
            if (status) {
                mainArraylist.get(pos).setFavourites(true);
                theQuranDetailAdapter.notifyItemChanged(pos);

                ArrayList<QuranDetailModel> quranDetailModels = shared.getFavoritesQuran(context);
                if (quranDetailModels != null) {
                    quranDetailModels.add(mainArraylist.get(pos));
                    shared.saveFavoritesQuran(quranDetailModels, context);
                } else {
                    ArrayList<QuranDetailModel> newList = new ArrayList<>();
                    newList.add(mainArraylist.get(pos));
                    shared.saveFavoritesQuran(newList, context);
                }
            } else {
                mainArraylist.get(pos).setFavourites(false);
                theQuranDetailAdapter.notifyItemChanged(pos);
                ArrayList<QuranDetailModel> quranDetailModels = shared.getFavoritesQuran(context);
                if (quranDetailModels != null) {
                    for (int p = 0; p < quranDetailModels.size(); p++) {
                        if (quranDetailModels.get(p).getMain_index().equals(mainArraylist.get(pos).getMain_index()) && quranDetailModels.get(p).getSura_index().equals(mainArraylist.get(pos).getSura_index())) {
                            quranDetailModels.remove(quranDetailModels.get(p));
                        }
                    }
                    shared.saveFavoritesQuran(quranDetailModels, context);
                }
            }
        }

        public void onSongSelect(int pos, boolean status) {

            currentUrlPos = pos;
            img_play.setImageDrawable(getResources().getDrawable(R.drawable.pause));
            startService();
            nextIndex = pos + 1;
            relative_song.setAnimation(animation);
            relative_song.setVisibility(View.VISIBLE);
            for (int i = 0; i < mainArraylist.size(); i++) {
                if (i == pos)
                    mainArraylist.get(i).setPlaying(true);
                else mainArraylist.get(i).setPlaying(false);
            }
            theQuranDetailAdapter.notifyDataSetChanged();

//            if (mediaPlayer != null) {
//                if (mediaPlayer.isPlaying())
//                mediaPlayer.stop();
//                mediaPlayer.reset();
//                currentUrlPos = pos;
//                nextIndex = pos + 1;
//                relative_song.setAnimation(animation);
//                relative_song.setVisibility(View.VISIBLE);
//                playPause = false;
//                initialStage = true;
//                playStopUrl(songList.get(pos));
//                for (int i = 0; i < mainArraylist.size(); i++) {
//                    if (i == pos)
//                        mainArraylist.get(i).setPlaying(true);
//                    else mainArraylist.get(i).setPlaying(false);
//                }
//                theQuranDetailAdapter.notifyDataSetChanged();
//            }
        }
    }

    public String loadJSONFromAsset(String file) {
        String json = null;
        try {
            InputStream is = this.getAssets().open(file);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }


    public ArrayList<QuranDetailModel> parseDetails(String suraid) {
        try {
            JSONObject Arabic = new JSONObject(loadJSONFromAsset("Arabic.json"));
            JSONObject ARABIC_JSON = Arabic.getJSONObject("quran");
            JSONObject APP_LANGUAGE_JSON = new JSONObject(loadJSONFromAsset("codebeautifyEN.json"));
            ArrayList<QuranDetailModel> quranDetailModelArrayList = new ArrayList<>();
            JSONArray jsonArray = ARABIC_JSON.getJSONArray("sura");
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject suraObject = jsonArray.getJSONObject(i);
                String suraindex = suraObject.optString("_index");

                if (suraindex.equals(suraid)) {
                    JSONArray ayaArray = suraObject.getJSONArray("aya");
                    if (ayaArray.getJSONObject(0).has("_bismillah")) {
                        isBismillahAdded = true;
                    } else {
                        isBismillahAdded = false;
                    }
                    for (int p = 0; p < ayaArray.length(); p++) {
                        QuranDetailModel quranDetailModel = new QuranDetailModel();
                        quranDetailModel.setSura_index(suraObject.optString("_index"));
                        quranDetailModel.setSura_name(suraObject.optString("_name"));
                        JSONObject ayaObject = ayaArray.getJSONObject(p);
                        quranDetailModel.setMain_index(ayaObject.getString("_index"));
                        quranDetailModel.setAya_text(ayaObject.getString("_text"));
                        quranDetailModelArrayList.add(quranDetailModel);
                    }
                    return quranDetailModelArrayList;
                }
            }
            return quranDetailModelArrayList;
        } catch (Exception e) {
            Log.e("f", "" + e.toString());
        }
        return new ArrayList<QuranDetailModel>();
    }


    public ArrayList<QuranDetailModel> parseDetailsAppLanguage(String suraid) {
        try {

            JSONObject Arabic;
            if (!path.equals("")) {
                Arabic = new JSONObject(readFromFile());
            } else {
                Arabic = new JSONObject(loadJSONFromAsset("codebeautifyEN.json"));
            }
            JSONObject APP_LANGUAGE_JSON = Arabic.getJSONObject("quran");
            ArrayList<QuranDetailModel> quranDetailModelArrayList = new ArrayList<>();
            JSONArray jsonArray = APP_LANGUAGE_JSON.getJSONArray("sura");
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject suraObject = jsonArray.getJSONObject(i);
                String suraindex = suraObject.optString("_index");

                if (suraindex.equals(suraid)) {
                    JSONArray ayaArray = suraObject.getJSONArray("aya");
                    for (int p = 0; p < ayaArray.length(); p++) {
                        QuranDetailModel quranDetailModel = new QuranDetailModel();
                        quranDetailModel.setSura_index(suraObject.optString("_index"));
                        quranDetailModel.setSura_name(suraObject.optString("_name"));
                        JSONObject ayaObject = ayaArray.getJSONObject(p);
                        quranDetailModel.setMain_index(ayaObject.getString("_index"));
                        quranDetailModel.setAya_text(ayaObject.getString("_text"));
                        quranDetailModelArrayList.add(quranDetailModel);
                    }
                    return quranDetailModelArrayList;
                }
            }
            return quranDetailModelArrayList;
        } catch (Exception e) {
            Log.e("f", "" + e.toString());
        }
        return new ArrayList<QuranDetailModel>();
    }


    private String readFromFile() {

        String ret = "";
        InputStream inputStream = null;
        try {
            inputStream = new FileInputStream(path);

            if (inputStream != null) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String receiveString = "";
                StringBuilder stringBuilder = new StringBuilder();

                while ((receiveString = bufferedReader.readLine()) != null) {
                    stringBuilder.append(receiveString);
                }

                ret = stringBuilder.toString();
            }
        } catch (FileNotFoundException e) {
            Log.e("login activity", "File not found: " + e.toString());
        } catch (IOException e) {
            Log.e("login activity", "Can not read file: " + e.toString());
        } catch (Exception e) {
            Log.e("login activity", "Can not read file: " + e.toString());
        } finally {
            try {
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return ret;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        if (mediaPlayer != null) mediaPlayer.release();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mMessageReceiver);

    }

    public class MyBroadRequestReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            durationHandler.postDelayed(updateSeekBarTime, 100);
        }
    }

    private boolean isAppIsInBackground(Context context) {
        boolean isInBackground = true;
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT_WATCH) {
            List<ActivityManager.RunningAppProcessInfo> runningProcesses = am.getRunningAppProcesses();
            for (ActivityManager.RunningAppProcessInfo processInfo : runningProcesses) {
                if (processInfo.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                    for (String activeProcess : processInfo.pkgList) {
                        if (activeProcess.equals(context.getPackageName())) {
                            isInBackground = false;
                        }
                    }
                }
            }
        } else {
            List<ActivityManager.RunningTaskInfo> taskInfo = am.getRunningTasks(1);
            ComponentName componentInfo = taskInfo.get(0).topActivity;
            if (componentInfo.getPackageName().equals(context.getPackageName())) {
                isInBackground = false;
            }
        }

        return isInBackground;
    }
}
