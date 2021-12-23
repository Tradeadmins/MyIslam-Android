package com.krishiv.myislam.fragment;

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
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.krishiv.myislam.R;
import com.krishiv.myislam.activity.TheJuzDetail;
import com.krishiv.myislam.activity.TheQuranDetailActivity;
import com.krishiv.myislam.adapter.TheQuranFavAdapter;
import com.krishiv.myislam.adapter.TheQuranSearchAdapter;
import com.krishiv.myislam.dto.QuranDetailModel;
import com.krishiv.myislam.dto.SuraModel;
import com.krishiv.myislam.service.NotificationService;
import com.krishiv.myislam.utils.Shared;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class FragmentFavourites extends Fragment {

    RecyclerView the_quran_recyclerview;
    TheQuranFavAdapter theQuranSearchAdapter;
    Context context;
    ArrayList<QuranDetailModel> list;
    Shared shared;
    private boolean playPause;
//    private MediaPlayer mediaPlayer;
    public ProgressDialog progressDialog;
    private boolean initialStage = true;
    Animation animation;
    int currentUrlPos = 0;
    ImageView img_play, img_next, imge_previous, img_close;
    RelativeLayout relative_song;
    int nextIndex;
    private SeekBar songPrgs;
    TextView startTime, songTime, txtVw1;
    private static int sTime = 0, eTime = 0 , showtime=0;
    private double timeElapsed = 0, finalTime = 0;
    private Handler durationHandler = new Handler();
    public boolean isPlaying = false;


    View view;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_fav, container, false);
        init(view);
        player(view);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(
                mMessageReceiver, new IntentFilter("ProgressUpdate"));
        getActivity().registerReceiver(broadcastReceiver, new IntentFilter(NotificationService.BROADCAST_ACTION));
    }



    //    @Override
//    protected void onResume() {
//        super.onResume();
//        LocalBroadcastManager.getInstance(context).registerReceiver(
//                mMessageReceiver, new IntentFilter("ProgressUpdate"));
//        context.registerReceiver(broadcastReceiver, new IntentFilter(NotificationService.BROADCAST_ACTION));
//        if (isSongPlaying.equals("true")) {
//            playPause = true;
//            relative_song.setAnimation(animation);
//            relative_song.setVisibility(View.VISIBLE);
//            currentUrlPos = 0;
//            img_play.setImageDrawable(getResources().getDrawable(R.drawable.pause));
//            startService();
//            relative_song.setAnimation(animation);
//            relative_song.setVisibility(View.VISIBLE);
//            for (int i = 0; i < mainArraylist.size(); i++) {
//                if (i == 0)
//                    mainArraylist.get(i).setPlaying(true);
//                else mainArraylist.get(i).setPlaying(false);
//            }
//            theQuranDetailAdapter.notifyDataSetChanged();
//        }
//    }
    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String message = intent.getStringExtra("Status");
            if (!isAppIsInBackground(getActivity())) {
                if (message.equals("show")) {
                    int pos = intent.getIntExtra("currentPosition", 0);
                    for (int i = 0; i < list.size(); i++) {
                        if (i == pos)
                            list.get(i).setPlaying(true);
                        else list.get(i).setPlaying(false);
                    }
//                    linearLayoutManager.scrollToPositionWithOffset(pos, 0);
                    theQuranSearchAdapter.notifyDataSetChanged();
                    progressDialog = new ProgressDialog(getActivity());
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

    public void player(View view) {
        img_play = (ImageView) view.findViewById(R.id.img_play);
        img_next = (ImageView) view.findViewById(R.id.img_next);
        imge_previous = (ImageView) view.findViewById(R.id.imge_previous);
        img_close = (ImageView) view.findViewById(R.id.img_close);
        relative_song = (RelativeLayout) view.findViewById(R.id.relative_song);
        startTime = (TextView) view.findViewById(R.id.txtStartTime);
        songTime = (TextView) view.findViewById(R.id.txtSongTime);
        songPrgs = view.findViewById(R.id.seekBar);
        txtVw1 = view.findViewById(R.id.txtVw1);
        songPrgs.setClickable(false);

//        mediaPlayer = new MediaPlayer();
//        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        progressDialog = new ProgressDialog(getActivity());

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
                    for (int i = 0; i < list.size(); i++) {
                        if (i == currentUrlPos)
                            list.get(i).setPlaying(true);
                        else list.get(i).setPlaying(false);
                    }
                    theQuranSearchAdapter.notifyDataSetChanged();
                } else {
                    durationHandler.removeCallbacks(updateSeekBarTime);
                    stopSong();
                    playPause = false;
                    initialStage = true;
                    relative_song.setVisibility(View.GONE);

                    for (int i = 0; i < list.size(); i++) {
                        list.get(i).setPlaying(false);
                    }
                    theQuranSearchAdapter.notifyDataSetChanged();
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
                for (int i = 0; i < list.size(); i++) {
                    list.get(i).setPlaying(false);
                }
                theQuranSearchAdapter.notifyDataSetChanged();
            }
        });


//        img_play.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                if (mediaPlayer.isPlaying()) {
////                    durationHandler.removeCallbacks(updateSeekBarTime);
//                    mediaPlayer.pause();
//                    img_play.setImageDrawable(getResources().getDrawable(R.drawable.play));
//                } else {
//                    mediaPlayer.start();
////                    playPause = false;
////                    initialStage = true;
//                    img_play.setImageDrawable(getResources().getDrawable(R.drawable.pause));
//                    songPrgs.setMax((int) finalTime);
//                    mediaPlayer.start();
//                    eTime = mediaPlayer.getDuration();
//                    finalTime = mediaPlayer.getDuration();
//                    timeElapsed = mediaPlayer.getCurrentPosition();
//                    songPrgs.setProgress((int) timeElapsed);
////                    durationHandler.removeCallbacks(updateSeekBarTime);
//                    durationHandler.postDelayed(updateSeekBarTime, 100);
//
//                    songTime.setText(String.format("%d : %02d", TimeUnit.MILLISECONDS.toMinutes(eTime),
//                            TimeUnit.MILLISECONDS.toSeconds(eTime) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(eTime))));
//
////                    playStopUrl(songList.get(currentUrlPos));
//                }
//
////                if (mediaPlayer.isPlaying()) {
////                    durationHandler.removeCallbacks(updateSeekBarTime);
////                    mediaPlayer.stop();
////                    img_play.setImageDrawable(getResources().getDrawable(R.drawable.play));
////                } else {
////                    mediaPlayer.reset();
////                    playPause = false;
////                    initialStage = true;
////                    img_play.setImageDrawable(getResources().getDrawable(R.drawable.pause));
////                    playStopUrl(list.get(currentUrlPos).getAudio_url());
////                }
//            }
//        });
//        img_next.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                durationHandler.removeCallbacks(updateSeekBarTime);
//                mediaPlayer.stop();
//                mediaPlayer.reset();
//                currentUrlPos++;
//                playPause = false;
//                initialStage = true;
//                if (currentUrlPos < (list.size())) {
//                    for (int i = 0; i < list.size(); i++) {
//                        if (i == currentUrlPos)
//                            list.get(i).setPlaying(true);
//                        else list.get(i).setPlaying(false);
//                    }
//                    theQuranSearchAdapter.notifyDataSetChanged();
//                    playStopUrl(list.get(currentUrlPos).getAudio_url());
//                } else {
//                    mediaPlayer.stop();
//                    for (int i = 0; i < list.size(); i++) {
//                        list.get(i).setPlaying(false);
//                    }
//                    relative_song.setVisibility(View.GONE);
//                }
//            }
//        });
//        imge_previous.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                durationHandler.removeCallbacks(updateSeekBarTime);
//                if (currentUrlPos == 0) {
//                    playPause = false;
//                    initialStage = true;
//                    relative_song.setVisibility(View.GONE);
//                    mediaPlayer.stop();
//                    mediaPlayer.reset();
//                    for (int i = 0; i < list.size(); i++) {
//                        list.get(i).setPlaying(false);
//                    }
//                    theQuranSearchAdapter.notifyDataSetChanged();
//                    return;
//                } else {
//                    mediaPlayer.stop();
//                    mediaPlayer.reset();
//                    playPause = false;
//                    initialStage = true;
//                    currentUrlPos--;
//                    for (int i = 0; i < list.size(); i++) {
//                        if (i == currentUrlPos)
//                            list.get(i).setPlaying(true);
//                        else list.get(i).setPlaying(false);
//                    }
//                    theQuranSearchAdapter.notifyDataSetChanged();
//                    playStopUrl(list.get(currentUrlPos).getAudio_url());
//                }
//            }
//        });
//
//        img_close.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                durationHandler.removeCallbacks(updateSeekBarTime);
//                playPause = false;
//                initialStage = true;
//                relative_song.setVisibility(View.GONE);
//                mediaPlayer.stop();
//                mediaPlayer.reset();
//                for (int i = 0; i < list.size(); i++) {
//
//                    list.get(i).setPlaying(false);
//                }
//                theQuranSearchAdapter.notifyDataSetChanged();
//            }
//        });
    }

//    private Runnable updateSeekBarTime = new Runnable() {
//        public void run() {
//            songPrgs.setProgress((int) timeElapsed);
////            double timeRemaining = finalTime - timeElapsed;
//            double timeRemaining = timeElapsed;
//            startTime.setText(String.format("%d: %02d", TimeUnit.MILLISECONDS.toMinutes((long) timeRemaining), TimeUnit.MILLISECONDS.toSeconds((long) timeRemaining) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long) timeRemaining))));
//            durationHandler.postDelayed(this, 100);
//        }
//    };
    private Runnable updateSeekBarTime = new Runnable() {
        public void run() {
//            timeElapsed = mediaPlayer.getCurrentPosition();
            songPrgs.setProgress((int) timeElapsed);
//            double timeRemaining = finalTime - timeElapsed;
            double timeRemaining = timeElapsed;
            startTime.setText(String.format("%d: %02d", TimeUnit.MILLISECONDS.toMinutes((long) timeRemaining), TimeUnit.MILLISECONDS.toSeconds((long) timeRemaining) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long) timeRemaining))));
            durationHandler.postDelayed(this, 100);
        }
    };
    @Override
    public void onPause() {
        super.onPause();
        getActivity().unregisterReceiver(broadcastReceiver);
    }

    public void startService() {
        Intent serviceIntent = new Intent(getActivity(), NotificationService.class);
        serviceIntent.setAction(com.krishiv.myislam.service.Constants.ACTION.STARTFOREGROUND_ACTION);
        serviceIntent.putParcelableArrayListExtra("list", list);
        serviceIntent.putExtra("notification", true);
        serviceIntent.putExtra("play_or_pause", "play");
        serviceIntent.putExtra("current_pos", currentUrlPos);
        isPlaying = true;
        getActivity().startService(serviceIntent);
    }

    public void pauseSong() {
        isPlaying = false;
        Intent serviceIntent = new Intent(getActivity(), NotificationService.class);
        serviceIntent.putExtra("play_or_pause", "pause");
        serviceIntent.putExtra("notification", true);
        getActivity().startService(serviceIntent);
        img_play.setImageDrawable(getResources().getDrawable(R.drawable.play));
    }

    public void startSongAgain() {
        isPlaying = true;
        Intent serviceIntent = new Intent(getActivity(), NotificationService.class);
        serviceIntent.putExtra("play_or_pause", "start_again");
        serviceIntent.putExtra("notification", true);
        getActivity().startService(serviceIntent);
        img_play.setImageDrawable(getResources().getDrawable(R.drawable.pause));
    }

    public void stopSong() {
        isPlaying = false;

        Intent serviceIntent = new Intent(getActivity(), NotificationService.class);
        serviceIntent.setAction(com.krishiv.myislam.service.Constants.ACTION.STARTFOREGROUND_ACTION);
        serviceIntent.putExtra("play_or_pause", "stop");
        serviceIntent.putExtra("notification",   true);
        getActivity().startService(serviceIntent);
        img_play.setImageDrawable(getResources().getDrawable(R.drawable.play));
    }

    public void nextSong() {
//        unregisterReceiver(broadcastReceiver);
        currentUrlPos++;
        if (currentUrlPos < (list.size() - 1)) {
            isPlaying = true;
            playPause = true;
            //registerReceiver(broadcastReceiver, new IntentFilter(NotificationService.BROADCAST_ACTION));
            Intent serviceIntent = new Intent(getActivity(), NotificationService.class);
            serviceIntent.setAction(com.krishiv.myislam.service.Constants.ACTION.STARTFOREGROUND_ACTION);
            serviceIntent.putParcelableArrayListExtra("list", list);
            serviceIntent.putExtra("notification", true);
            serviceIntent.putExtra("play_or_pause", "next");
            serviceIntent.putExtra("current_pos", currentUrlPos);
            getActivity().startService(serviceIntent);
            img_play.setImageDrawable(getResources().getDrawable(R.drawable.pause));
        }else {
            durationHandler.removeCallbacks(updateSeekBarTime);
            stopSong();
            playPause = false;
            initialStage = true;
            relative_song.setVisibility(View.GONE);

            for (int i = 0; i < list.size(); i++) {
                list.get(i).setPlaying(false);
            }
            theQuranSearchAdapter.notifyDataSetChanged();

//            startService();
//            durationHandler.removeCallbacks(updateSeekBarTime);
//            LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(mMessageReceiver);
//            playPause = false;
//            initialStage = true;
//            relative_song.setVisibility(View.GONE);
//            for (int i = 0; i < list.size(); i++) {
//                list.get(i).setPlaying(false);
//            }
//            theQuranSearchAdapter.notifyDataSetChanged();
        }

    }


    private void init(View view) {
        context = getActivity();
        shared = new Shared(context);
        animation = AnimationUtils.loadAnimation(context, R.anim.bottom_up);
        the_quran_recyclerview = view.findViewById(R.id.the_quran_recyclerview);
        the_quran_recyclerview.setLayoutManager(new LinearLayoutManager(getActivity()));
        list = shared.getFavoritesQuran(context);
        if (list != null) {
            theQuranSearchAdapter = new TheQuranFavAdapter(context, list, new onSelectFaav());
            the_quran_recyclerview.setAdapter(theQuranSearchAdapter);
        }
    }

    public class onSelectFaav {
        public void onSelect(int pos) {
            ArrayList<QuranDetailModel> quranDetailModels = shared.getFavoritesQuran(context);
            if (quranDetailModels != null) {
                for (int p = 0; p < quranDetailModels.size(); p++) {
                    if (quranDetailModels.get(p).getMain_index().equals(list.get(pos).getMain_index()) && quranDetailModels.get(p).getSura_index().equals(list.get(pos).getSura_index())) {
                        quranDetailModels.remove(quranDetailModels.get(p));
                    }
                }
                shared.saveFavoritesQuran(quranDetailModels, context);
            }
            list.clear();
            list.addAll(quranDetailModels);
            theQuranSearchAdapter.notifyDataSetChanged();
        }

        public void onSelectSong(int pos) {

            currentUrlPos = pos;
            img_play.setImageDrawable(getResources().getDrawable(R.drawable.pause));
            startService();
            nextIndex = pos + 1;
            relative_song.setAnimation(animation);
            relative_song.setVisibility(View.VISIBLE);
            for (int i = 0; i < list.size(); i++) {
                if (i == pos)
                    list.get(i).setPlaying(true);
                else list.get(i).setPlaying(false);
            }
            theQuranSearchAdapter.notifyDataSetChanged();

//            if (mediaPlayer!=null){
//                if (mediaPlayer.isPlaying())
//                mediaPlayer.stop();
//                mediaPlayer.reset();
//                currentUrlPos = pos;
//                nextIndex = pos + 1;
//                relative_song.setAnimation(animation);
//                relative_song.setVisibility(View.VISIBLE);
//                playPause = false;
//                initialStage = true;
//                playStopUrl(list.get(pos).getAudio_url());
//                for (int i = 0; i < list.size(); i++) {
//                    if (i == pos)
//                        list.get(i).setPlaying(true);
//                    else list.get(i).setPlaying(false);
//                }
//                theQuranSearchAdapter.notifyDataSetChanged();
//            }
        }
    }

//    public void playStopUrl(String url) {
//        if (!playPause) {
//            img_play.setImageDrawable(getResources().getDrawable(R.drawable.pause));
//            if (initialStage) {
//                new Player().execute(url);
//            } else {
//                if (!mediaPlayer.isPlaying()) {
//                    songPrgs.setMax((int) finalTime);
//                    mediaPlayer.start();
//                    eTime = mediaPlayer.getDuration();
//                    finalTime = mediaPlayer.getDuration();
//                    timeElapsed = mediaPlayer.getCurrentPosition();
//                    songPrgs.setProgress((int) timeElapsed);
//                    durationHandler.removeCallbacks(updateSeekBarTime);
//                    durationHandler.postDelayed(updateSeekBarTime, 100);
//                }
//                songTime.setText(String.format("%d : %02d", TimeUnit.MILLISECONDS.toMinutes(eTime),
//                        TimeUnit.MILLISECONDS.toSeconds(eTime) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(eTime))));
//            }
//            playPause = true;
//        } else {
//            img_play.setImageDrawable(getResources().getDrawable(R.drawable.play));
//
//            if (mediaPlayer.isPlaying()) {
//                mediaPlayer.pause();
//            }
//            playPause = false;
//        }
//    }



//    public void playNext(String url) {
//        new Player().execute(url);
//    }

//    class Player extends AsyncTask<String, Void, Boolean> {
//        @Override
//        protected Boolean doInBackground(String... strings) {
//            Boolean prepared = false;
//            try {
//                mediaPlayer.setDataSource(strings[0]);
//                mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
//                    @Override
//                    public void onCompletion(MediaPlayer mediaPlayer) {
//                        durationHandler.removeCallbacks(updateSeekBarTime);
//                        initialStage = true;
//                        playPause = false;
//                        mediaPlayer.stop();
//                        mediaPlayer.reset();
//                        currentUrlPos++;
//                        if (currentUrlPos < (list.size())) {
//                            playNext(list.get(currentUrlPos).getAudio_url());
//                            the_quran_recyclerview.getLayoutManager().scrollToPosition(currentUrlPos);
//                            for (int i = 0; i < list.size(); i++) {
//                                if (i == currentUrlPos)
//                                    list.get(i).setPlaying(true);
//                                else list.get(i).setPlaying(false);
//                            }
//                            theQuranSearchAdapter.notifyDataSetChanged();
//                        }else {
//                            for (int i = 0; i < list.size(); i++) {
//                                list.get(i).setPlaying(false);
//                            }
//                            theQuranSearchAdapter.notifyDataSetChanged();
//                            relative_song.setVisibility(View.GONE);
//                        }
//                    }
//                });
//
//                mediaPlayer.prepare();
//                prepared = true;
//
//            } catch (Exception e) {
//                Log.e("MyAudioStreamingApp", e.getMessage());
//                prepared = false;
//            }
//            return prepared;
//        }
//
//        @Override
//        protected void onPostExecute(Boolean aBoolean) {
//            super.onPostExecute(aBoolean);
//            if (progressDialog.isShowing()) {
//                progressDialog.cancel();
//            }
//            songPrgs.setMax((int) finalTime);
//            mediaPlayer.start();
//            initialStage = false;
//            finalTime = mediaPlayer.getDuration();
//            eTime = mediaPlayer.getDuration();
//            sTime = mediaPlayer.getCurrentPosition();
//            songPrgs.setMax((int) finalTime);
//            songTime.setText(String.format("%d : %02d", TimeUnit.MILLISECONDS.toMinutes(eTime),
//                    TimeUnit.MILLISECONDS.toSeconds(eTime) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(eTime))));
//            durationHandler.removeCallbacks(updateSeekBarTime);
//            durationHandler.postDelayed(updateSeekBarTime, 100);
//        }
//
//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
//            progressDialog.setMessage("Buffering...");
//            progressDialog.show();
//        }
//    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (list!=null){
            for (int i = 0; i < list.size(); i++) {
                list.get(i).setPlaying(false);
            }
        }

    }

//    @Override
//    public void onPause() {
//        super.onPause();
//        durationHandler.removeCallbacks(updateSeekBarTime);
//        mediaPlayer.stop();
//        mediaPlayer.release();
//        playPause = false;
//        initialStage = true;
//        relative_song.setVisibility(View.GONE);
//        for (int i = 0; i < list.size(); i++) {
//            list.get(i).setPlaying(false);
//        }
//        theQuranSearchAdapter.notifyDataSetChanged();
//    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(mMessageReceiver);
//        if (mediaPlayer!= null) mediaPlayer.release();
    }

    public boolean isAppIsInBackground(Context context) {
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
