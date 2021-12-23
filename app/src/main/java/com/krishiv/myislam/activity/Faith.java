package com.krishiv.myislam.activity;

import android.content.Intent;
import android.graphics.Point;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.krishiv.myislam.R;
import com.krishiv.myislam.menu.BaseMenuActivity;

public class Faith extends BaseMenuActivity implements View.OnClickListener {

    ImageView img_play;
    MediaPlayer m;
    TextView allahName,quran,hadith,tawheed,profet;
    LinearLayout linearLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentViev(R.layout.activity_faith);

        img_play = findViewById(R.id.img_play);
        allahName = findViewById(R.id.txt_allah_name);
        quran = findViewById(R.id.txt_quran);
        hadith = findViewById(R.id.txt_hadith);
        tawheed = findViewById(R.id.txt_taweed);
        profet = findViewById(R.id.txt_prophet_muhammad);
        linearLayout = findViewById(R.id.linearLayout);

        Display display = getWindowManager().getDefaultDisplay();
        DisplayMetrics displayMetrics = Faith.this.getResources().getDisplayMetrics();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;

        profet.setWidth(width/2);
        img_play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (m!=null){
                    if (m.isPlaying()) {
                        stopMediaPlayer();
                        img_play.setImageDrawable(getResources().getDrawable(R.drawable.play_btn));
                        return;
                    }
                }

                m = new MediaPlayer();
                playAudio();
                m.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    public void onCompletion(MediaPlayer mp) {
                        stopMediaPlayer();
                        img_play.setImageDrawable(getResources().getDrawable(R.drawable.play_btn));
                    }
                });
            }
        });


    }

    public void playAudio() {
        try {
            if (m.isPlaying()) {
                m.stop();
                m.release();
                m = new MediaPlayer();
                img_play.setImageDrawable(getResources().getDrawable(R.drawable.play_btn));
                return;
            }
            img_play.setImageDrawable(getResources().getDrawable(R.drawable.pause_icon));
            m = MediaPlayer.create(getApplicationContext(), R.raw.kalimashahada);
            m.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (m!=null && m.isPlaying()) {
            m.stop();
            m.release();
            m = new MediaPlayer();
            img_play.setImageDrawable(getResources().getDrawable(R.drawable.play_btn));
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.txt_allah_name:
                startActivity(new Intent(context, NameOfAllah.class));
                break;

            case R.id.txt_quran:
                startActivity(new Intent(context, ActivityThequran.class));
                break;

            case R.id.txt_hadith:
                startActivity(new Intent(context, Hadiths.class));
                break;

            case R.id.txt_taweed:
                startActivity(new Intent(context, Taweed.class));
                break;

            case R.id.txt_prophet_muhammad:
                startActivity(new Intent(context, ProphetMuhmmad.class));
                break;

            case R.id.arrow_back:
                finish();
                break;
        }
    }

    @Override
    public void onDestroy() {
        stopMediaPlayer();
        super.onDestroy();
    }

    public void stopMediaPlayer() {
        if (m != null) {
            m.release();
            m = null;
        }

    }
}
