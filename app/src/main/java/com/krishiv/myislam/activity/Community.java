package com.krishiv.myislam.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.krishiv.myislam.R;
import com.krishiv.myislam.menu.BaseMenuActivity;

public class Community extends BaseMenuActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentViev(R.layout.activity_community);

        findViewById(R.id.btn_prayer_req).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(context, RequestForPrayer.class));
            }
        });

        findViewById(R.id.btn_event).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(context, Event.class));
            }
        });

        findViewById(R.id.btn_mosque).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(context, Mosque.class).putExtra("activity","mosque"));
            }
        });

        findViewById(R.id.btn_restaurant).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(context, Mosque.class).putExtra("activity","restaurant"));
            }
        });

        findViewById(R.id.btn_qurbani).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(context, Qurbani.class));
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.arrow_back:
                finish();
                break;
        }
    }
}
