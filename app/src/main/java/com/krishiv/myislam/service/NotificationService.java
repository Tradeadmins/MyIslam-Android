package com.krishiv.myislam.service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.app.Service;
import android.content.Intent;
import android.app.PendingIntent;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.view.View;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.krishiv.myislam.R;
import com.krishiv.myislam.activity.TheQuranDetailActivity;
import com.krishiv.myislam.dto.QuranDetailModel;

import java.util.ArrayList;

import androidx.annotation.RequiresApi;


public class NotificationService extends Service {
    Notification status;
    private final String LOG_TAG = "NotificationService";
    MediaPlayer mediaPlayer;
    private boolean playPause;
    private ProgressDialog progressDialog;
    private boolean initialStage = true;
    int currentUrlPos;
    int nextIndex;
    private double timeElapsed = 0, finalTime = 0;
    ArrayList<QuranDetailModel> list;
    Context context;
    private static int sTime = 0, eTime = 0;
    public static final String BROADCAST_ACTION = "displayevent";
    private final Handler handler = new Handler();
    Intent intent;
    boolean notification_play_pause = false;

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        intent = new Intent(BROADCAST_ACTION);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        boolean isNotification = intent.getBooleanExtra("notification", false);
        if (isNotification) {
            String play_or_pause = intent.getStringExtra("play_or_pause");
            if (play_or_pause.equals("play")) {
                notification_play_pause = true;
                handler.removeCallbacks(sendUpdatesToUI);
                list = intent.getParcelableArrayListExtra("list");
                currentUrlPos = intent.getIntExtra("current_pos", 0);
                if (mediaPlayer.isPlaying()) {
                    mediaPlayer.stop();
                    mediaPlayer.reset();
                }
                playNext(list.get(currentUrlPos).getAudio_url());
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    //                startMyOwnForeground();
                    startForeground();
                } else
                    startForeground(1, new Notification());

                handler.postDelayed(sendUpdatesToUI, 1000);
            } else if (play_or_pause.equals("next")) {
                notification_play_pause = true;
                initialStage = true;
                mediaPlayer.stop();
                mediaPlayer.reset();
                handler.removeCallbacks(sendUpdatesToUI);
                list = intent.getParcelableArrayListExtra("list");
                currentUrlPos = intent.getIntExtra("current_pos", 0);
                playNext(list.get(currentUrlPos).getAudio_url());
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    //                startMyOwnForeground();
                    startForeground();
                } else
                    startForeground(1, new Notification());
                handler.postDelayed(sendUpdatesToUI, 1000);
            } else if (play_or_pause.equals("pause")) {
                notification_play_pause = false;
                mediaPlayer.pause();
            } else if (play_or_pause.equals("start_again")) {
                notification_play_pause = true;
                mediaPlayer.start();
            } else {
                handler.removeCallbacks(sendUpdatesToUI);
                mediaPlayer.stop();
                mediaPlayer.release();
                stopForegroundService();
            }
        } else {
            if (intent.getAction().equals(Constants.ACTION.PREV_ACTION)) {
                notification_play_pause = true;
                initialStage = true;
                mediaPlayer.stop();
                mediaPlayer.reset();
                handler.removeCallbacks(sendUpdatesToUI);
//                list = intent.getParcelableArrayListExtra("list");
//                currentUrlPos = intent.getIntExtra("current_pos", 0);
                currentUrlPos--;
                playNext(list.get(currentUrlPos).getAudio_url());
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    startForeground();
                } else
                    startForeground(1, new Notification());
                handler.postDelayed(sendUpdatesToUI, 1000);

            } else if (intent.getAction().equals(Constants.ACTION.PLAY_ACTION)) {
                if (notification_play_pause) {
                    notification_play_pause = false;
                    mediaPlayer.pause();
                } else {
                    notification_play_pause = true;
                    mediaPlayer.start();
                }
            } else if (intent.getAction().equals(Constants.ACTION.NEXT_ACTION)) {
                notification_play_pause = true;
                initialStage = true;
                mediaPlayer.stop();
                mediaPlayer.reset();
                handler.removeCallbacks(sendUpdatesToUI);
//                list = intent.getParcelableArrayListExtra("list");
//                currentUrlPos = intent.getIntExtra("current_pos", 0);
                currentUrlPos++;
                playNext(list.get(currentUrlPos).getAudio_url());
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    startForeground();
                } else
                    startForeground(1, new Notification());
                handler.postDelayed(sendUpdatesToUI, 1000);
            } else if (intent.getAction().equals(
                    Constants.ACTION.STOPFOREGROUND_ACTION)) {
                handler.removeCallbacks(sendUpdatesToUI);
                mediaPlayer.stop();
                mediaPlayer.release();
                stopForegroundService();
            }
        }
        return super.onStartCommand(intent, flags, startId);
    }

    private void stopForegroundService() {
        stopForeground(true);
        stopSelf();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void startForeground() {
        RemoteViews views = new RemoteViews(getPackageName(),
                R.layout.status_bar);
        views.setViewVisibility(R.id.status_bar_icon, View.GONE);
        views.setViewVisibility(R.id.status_bar_album_art, View.GONE);
        Intent notificationIntent = new Intent(this, TheQuranDetailActivity.class);
        notificationIntent.setAction(Constants.ACTION.MAIN_ACTION);
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,
                notificationIntent, 0);
        Intent previousIntent = new Intent(this, NotificationService.class);
        previousIntent.setAction(Constants.ACTION.PREV_ACTION);
        PendingIntent ppreviousIntent = PendingIntent.getService(this, 0,
                previousIntent, 0);
        Intent playIntent = new Intent(this, NotificationService.class);
        playIntent.setAction(Constants.ACTION.PLAY_ACTION);
        PendingIntent pplayIntent = PendingIntent.getService(this, 0,
                playIntent, 0);
        Intent nextIntent = new Intent(this, NotificationService.class);
        nextIntent.setAction(Constants.ACTION.NEXT_ACTION);
        PendingIntent pnextIntent = PendingIntent.getService(this, 0,
                nextIntent, 0);
        Intent closeIntent = new Intent(this, NotificationService.class);
        closeIntent.setAction(Constants.ACTION.STOPFOREGROUND_ACTION);
        PendingIntent pcloseIntent = PendingIntent.getService(this, 0,
                closeIntent, 0);

        views.setOnClickPendingIntent(R.id.status_bar_play, pplayIntent);

        views.setOnClickPendingIntent(R.id.status_bar_next, pnextIntent);

        views.setOnClickPendingIntent(R.id.status_bar_prev, ppreviousIntent);

        views.setOnClickPendingIntent(R.id.status_bar_collapse, pcloseIntent);

        String NOTIFICATION_CHANNEL_ID = "com.krishiv.myislam";
        String channelName = "My Background Service";
        NotificationChannel chan = new NotificationChannel(NOTIFICATION_CHANNEL_ID, channelName, NotificationManager.IMPORTANCE_NONE);
        chan.setLightColor(Color.BLUE);
        chan.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        assert manager != null;
        manager.createNotificationChannel(chan);

        status = new Notification.Builder(this, NOTIFICATION_CHANNEL_ID).build();
        status.contentView = views;
        status.flags = Notification.FLAG_ONGOING_EVENT;
        status.icon = R.drawable.search_icon;
        status.contentIntent = pendingIntent;
        startForeground(Constants.NOTIFICATION_ID.FOREGROUND_SERVICE, status);
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
                        initialStage = true;
                        mediaPlayer.stop();
                        mediaPlayer.reset();
                        currentUrlPos++;
                        if (currentUrlPos < (list.size())) {
                            playNext(list.get(currentUrlPos).getAudio_url());
                        }
                    }
                });
                mediaPlayer.prepare();
                prepared = true;

            } catch (Exception e) {
                prepared = false;
            }
            return prepared;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            Intent intent = new Intent("ProgressUpdate");
            intent.putExtra("Status", "hide");
            if (mediaPlayer != null)
                finalTime = mediaPlayer.getDuration();
            intent.putExtra("finalTime", finalTime);
            if (mediaPlayer != null)
                eTime = mediaPlayer.getDuration();
            intent.putExtra("eTime", eTime);
            if (mediaPlayer != null)
                timeElapsed = mediaPlayer.getCurrentPosition();
            intent.putExtra("timeElapsed", timeElapsed);

            LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
            if (mediaPlayer != null)
                mediaPlayer.start();
            handler.postDelayed(sendUpdatesToUI, 1000);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Intent intent = new Intent("ProgressUpdate");
            intent.putExtra("Status", "show");
            intent.putExtra("currentPosition", currentUrlPos);
            LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
        }
    }

    private Runnable sendUpdatesToUI = new Runnable() {
        public void run() {
            DisplayLoggingInfo();
            handler.postDelayed(this, 1000); // 10 seconds
        }
    };

    private void DisplayLoggingInfo() {
        timeElapsed = mediaPlayer.getCurrentPosition();
        intent.putExtra("timeElapsed", mediaPlayer.getCurrentPosition());
        sendBroadcast(intent);
    }

    public void playNext(String url) {
        new Player().execute(url);
    }

}
