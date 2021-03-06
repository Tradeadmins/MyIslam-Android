package com.krishiv.myislam.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
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
import java.util.concurrent.TimeUnit;

public class TheJuzDetail2 extends BaseMenuActivity {

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
    private static int sTime = 0, eTime = 0;
    private double timeElapsed = 0, finalTime = 0;
    private Handler durationHandler = new Handler();
    public String isLastPosition = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentViev(R.layout.activity_quran_detail);
        sura_id = getIntent().getStringExtra("sura_id");
        aya_index = getIntent().getStringExtra("aya_index");
//        path = getIntent().getStringExtra("path");
        shared = new Shared(TheJuzDetail2.this);
        path = shared.getString(Shared.LANGUAGE_PATH, "");
        isLastPosition = getIntent().getStringExtra("isLast");
        suraList = this.getIntent().getParcelableArrayListExtra("suraList");
        isSongPlaying = getIntent().getStringExtra("isSongPlaying");
        nextIndex = getIntent().getIntExtra("nextIndex", 0);
        if (isSongPlaying.equals("true")) {
            sura_id = suraList.get((nextIndex + 1)).get_index();
        }
        init();
    }

    @Override
    protected void onResume() {
        super.onResume();
        player();
        if (isSongPlaying.equals("true")) {
            mediaPlayer.reset();
            currentUrlPos = 0;
            playPause = false;
            initialStage = true;
            playStopUrl(songList.get(currentUrlPos));
            relative_song.setAnimation(animation);
            relative_song.setVisibility(View.VISIBLE);
            for (int i = 0; i < mainArraylist.size(); i++) {
                if (i == currentUrlPos)
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

        mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        progressDialog = new ProgressDialog(this);
        img_play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mediaPlayer.isPlaying()) {
//                    durationHandler.removeCallbacks(updateSeekBarTime);
                    mediaPlayer.pause();
                    img_play.setImageDrawable(getResources().getDrawable(R.drawable.play));
                } else {
                    mediaPlayer.start();
//                    playPause = false;
//                    initialStage = true;
                    img_play.setImageDrawable(getResources().getDrawable(R.drawable.pause));
                    songPrgs.setMax((int) finalTime);
                    mediaPlayer.start();
                    eTime = mediaPlayer.getDuration();
                    finalTime = mediaPlayer.getDuration();
                    timeElapsed = mediaPlayer.getCurrentPosition();
                    songPrgs.setProgress((int) timeElapsed);
//                    durationHandler.removeCallbacks(updateSeekBarTime);
                    durationHandler.postDelayed(updateSeekBarTime, 100);

                    songTime.setText(String.format("%d : %02d", TimeUnit.MILLISECONDS.toMinutes(eTime),
                            TimeUnit.MILLISECONDS.toSeconds(eTime) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(eTime))));


//                    playStopUrl(songList.get(currentUrlPos));
                }

//                if (mediaPlayer.isPlaying()) {
//                    durationHandler.removeCallbacks(updateSeekBarTime);
//                    mediaPlayer.stop();
//                    img_play.setImageDrawable(getResources().getDrawable(R.drawable.play));
//                } else {
//                    mediaPlayer.reset();
//                    playPause = false;
//                    initialStage = true;
//                    img_play.setImageDrawable(getResources().getDrawable(R.drawable.pause));
//                    playStopUrl(songList.get(currentUrlPos));
//                }
            }
        });
        img_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                durationHandler.removeCallbacks(updateSeekBarTime);
                mediaPlayer.stop();
                mediaPlayer.reset();
                currentUrlPos++;
                playPause = false;
                initialStage = true;
                if (currentUrlPos < (songList.size() - 1)) {
                    for (int i = 0; i < mainArraylist.size(); i++) {
                        if (i == currentUrlPos)
                            mainArraylist.get(i).setPlaying(true);
                        else mainArraylist.get(i).setPlaying(false);
                    }
                    theQuranDetailAdapter.notifyDataSetChanged();
                    playStopUrl(songList.get(currentUrlPos));
                } else {
                    mediaPlayer.stop();
                    finish();
                    Intent intent = new Intent(TheJuzDetail2.this, TheJuzDetail2.class);
//                    intent.putExtra("sura_id",list.get(position).get_index());
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
            }
        });
        imge_previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                durationHandler.removeCallbacks(updateSeekBarTime);
                if (currentUrlPos == 0) {
                    playPause = false;
                    initialStage = true;
                    relative_song.setVisibility(View.GONE);
                    mediaPlayer.stop();
                    mediaPlayer.reset();
                    for (int i = 0; i < mainArraylist.size(); i++) {

                        mainArraylist.get(i).setPlaying(false);
                    }
                    theQuranDetailAdapter.notifyDataSetChanged();
                    return;
                } else {
                    mediaPlayer.stop();
                    mediaPlayer.reset();
                    playPause = false;
                    initialStage = true;
                    currentUrlPos--;
                    for (int i = 0; i < mainArraylist.size(); i++) {
                        if (i == currentUrlPos)
                            mainArraylist.get(i).setPlaying(true);
                        else mainArraylist.get(i).setPlaying(false);
                    }
                    theQuranDetailAdapter.notifyDataSetChanged();
                    playStopUrl(songList.get(currentUrlPos));
                }

            }
        });

        img_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                durationHandler.removeCallbacks(updateSeekBarTime);
                playPause = false;
                initialStage = true;
                relative_song.setVisibility(View.GONE);
                mediaPlayer.stop();
                mediaPlayer.reset();
                for (int i = 0; i < mainArraylist.size(); i++) {

                    mainArraylist.get(i).setPlaying(false);
                }
                theQuranDetailAdapter.notifyDataSetChanged();
            }
        });
    }

    public void playStopUrl(String url) {
        if (!playPause) {
            img_play.setImageDrawable(getResources().getDrawable(R.drawable.pause));
            if (initialStage) {
                new TheJuzDetail2.Player().execute(url);
            } else {
                if (!mediaPlayer.isPlaying()) {
                    songPrgs.setMax((int) finalTime);
                    mediaPlayer.start();
                    eTime = mediaPlayer.getDuration();
                    finalTime = mediaPlayer.getDuration();
                    timeElapsed = mediaPlayer.getCurrentPosition();
                    songPrgs.setProgress((int) timeElapsed);
                    durationHandler.removeCallbacks(updateSeekBarTime);
                    durationHandler.postDelayed(updateSeekBarTime, 100);
                }
                songTime.setText(String.format("%d : %02d", TimeUnit.MILLISECONDS.toMinutes(eTime),
                        TimeUnit.MILLISECONDS.toSeconds(eTime) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(eTime))));
            }
            playPause = true;
        } else {
            img_play.setImageDrawable(getResources().getDrawable(R.drawable.play));

            if (mediaPlayer.isPlaying()) {
                mediaPlayer.pause();
            }
            playPause = false;
        }
    }

    private Runnable updateSeekBarTime = new Runnable() {
        public void run() {
            timeElapsed = mediaPlayer.getCurrentPosition();
            songPrgs.setProgress((int) timeElapsed);
//            double timeRemaining = finalTime - timeElapsed;
            double timeRemaining = timeElapsed;
            startTime.setText(String.format("%d: %02d", TimeUnit.MILLISECONDS.toMinutes((long) timeRemaining), TimeUnit.MILLISECONDS.toSeconds((long) timeRemaining) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long) timeRemaining))));
            durationHandler.postDelayed(this, 100);
        }
    };

    public void playNext(String url) {
        new TheJuzDetail2.Player().execute(url);
    }

    @Override
    protected void onPause() {
        super.onPause();
        durationHandler.removeCallbacks(updateSeekBarTime);
        mediaPlayer.stop();
        mediaPlayer.release();
        playPause = false;
        initialStage = true;
        relative_song.setVisibility(View.GONE);
        for (int i = 0; i < mainArraylist.size(); i++) {
            mainArraylist.get(i).setPlaying(false);
        }
        theQuranDetailAdapter.notifyDataSetChanged();
    }

    class Player extends AsyncTask<String, Void, Boolean> {
        @Override
        protected Boolean doInBackground(String... strings) {
            Boolean prepared = false;
            try {
                mediaPlayer.setDataSource(strings[0]);
                mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mediaPlayer) {
                        durationHandler.removeCallbacks(updateSeekBarTime);
                        initialStage = true;
                        playPause = false;
                        mediaPlayer.stop();
                        mediaPlayer.reset();
                        currentUrlPos++;
                        if (currentUrlPos < (songList.size())) {
                            playNext(songList.get(currentUrlPos));
                            detail_recycler.getLayoutManager().scrollToPosition(currentUrlPos);
                            for (int i = 0; i < mainArraylist.size(); i++) {
                                if (i == currentUrlPos)
                                    mainArraylist.get(i).setPlaying(true);
                                else mainArraylist.get(i).setPlaying(false);
                            }
                            theQuranDetailAdapter.notifyDataSetChanged();
                        } else {
                            finish();
                            Intent intent = new Intent(TheJuzDetail2.this, TheJuzDetail2.class);
//                    intent.putExtra("sura_id",list.get(position).get_index());
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
                    }
                });

                mediaPlayer.prepare();

                prepared = true;

            } catch (Exception e) {
                Log.e("MyAudioStreamingApp", e.getMessage());
                prepared = false;
            }
            return prepared;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            if (progressDialog.isShowing()) {
                progressDialog.cancel();
            }
            songPrgs.setMax((int) finalTime);
            mediaPlayer.start();
            initialStage = false;
            finalTime = mediaPlayer.getDuration();
            eTime = mediaPlayer.getDuration();
            sTime = mediaPlayer.getCurrentPosition();
            songPrgs.setMax((int) finalTime);
            songTime.setText(String.format("%d : %02d", TimeUnit.MILLISECONDS.toMinutes(eTime),
                    TimeUnit.MILLISECONDS.toSeconds(eTime) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(eTime))));
            durationHandler.removeCallbacks(updateSeekBarTime);
            durationHandler.postDelayed(updateSeekBarTime, 100);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.setMessage("Buffering...");
            progressDialog.show();
        }
    }

    private void init() {
        context = this;
        shared = new Shared(context);
        detail_recycler = findViewById(R.id.detail_recycler);
        back = findViewById(R.id.arrow_back);
        detail_recycler.setLayoutManager(new LinearLayoutManager(this));
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
//        theQuranDetailAdapter = new JazDetailAdapter(context, mainArraylist, new onSelectStar(), isBismillahAdded);

        final int desired_index = (Integer.parseInt(aya_index) - 1);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                detail_recycler.setAdapter(theQuranDetailAdapter);
                detail_recycler.getLayoutManager().scrollToPosition(desired_index);
            }
        }, 200);

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
            if (mediaPlayer != null) {
                if (mediaPlayer.isPlaying())
                mediaPlayer.stop();
                mediaPlayer.reset();
                currentUrlPos = pos;
                nextIndex = pos + 1;
                relative_song.setAnimation(animation);
                relative_song.setVisibility(View.VISIBLE);
                playPause = false;
                initialStage = true;
                playStopUrl(songList.get(pos));
                for (int i = 0; i < mainArraylist.size(); i++) {
                    if (i == pos)
                        mainArraylist.get(i).setPlaying(true);
                    else mainArraylist.get(i).setPlaying(false);
                }
                theQuranDetailAdapter.notifyDataSetChanged();
            }
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
        if (mediaPlayer != null) mediaPlayer.release();
    }
}
