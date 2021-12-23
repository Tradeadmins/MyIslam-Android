package com.krishiv.myislam.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.krishiv.myislam.R;
import com.krishiv.myislam.menu.BaseMenuActivity;

public class Setting extends BaseMenuActivity {

    LinearLayout lnr_my_account, lnr_prayer_time, lnr_app_language;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        bottom_line.setVisibility(View.GONE);
        View view = setContentViev(R.layout.activity_setting);

        lnr_my_account = view.findViewById(R.id.lnr_my_account);
        lnr_prayer_time = view.findViewById(R.id.lnr_prayer_time);
        lnr_app_language = view.findViewById(R.id.lnr_app_language);

        lnr_my_account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(context, MyAccount.class));
            }
        });

        lnr_prayer_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(context, PrayerTimeNotification.class));
            }
        });

        lnr_app_language.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(context, AppLanguage.class));
            }
        });
    }
}
