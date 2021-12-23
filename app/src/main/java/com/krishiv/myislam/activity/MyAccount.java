package com.krishiv.myislam.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.krishiv.myislam.R;
import com.krishiv.myislam.menu.BaseMenuActivity;

public class MyAccount extends BaseMenuActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        View view = setContentViev(R.layout.fg_my_account);

        view.findViewById(R.id.btn_chg_pwd).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(context, ChangePass.class));
            }
        });

        view.findViewById(R.id.txt_logout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shared.clearAll();
//                shared.putString(Shared.userProfile, "");
//                shared.putString(shared.upcomingAlarmData,"");
                Intent intent = new Intent(context, Login.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }
        });

        view.findViewById(R.id.arrow_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}
