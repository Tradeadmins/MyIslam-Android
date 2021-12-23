package com.krishiv.myislam.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.krishiv.myislam.R;
import com.krishiv.myislam.menu.BaseMenuActivity;

public class PilgrimHajjUmrah extends BaseMenuActivity implements View.OnClickListener {

    Pilgrim.ActionPilgrim currenActionFor;
    RelativeLayout rlt_hajj, rlt_umrah;
    TextView txt_hajj;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentViev(R.layout.activity_pilgrim_hajj_umrah);
        currenActionFor = Pilgrim.ActionPilgrim.values()[getIntent().getIntExtra("actionFor", 0)];

        txt_hajj = findViewById(R.id.txt_hajj);
        rlt_hajj = findViewById(R.id.rlt_hajj);
        rlt_umrah = findViewById(R.id.rlt_umrah);

        if (currenActionFor == Pilgrim.ActionPilgrim.HajjGuide){
            txt_hajj.setText(getResources().getString(R.string.hajj)+"/"+getResources().getString(R.string.umrah));
            rlt_umrah.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){

            case R.id.rlt_hajj:
                startActivity(new Intent(context, PilgrimHajjUmrahTask.class).putExtra("actionFor", currenActionFor==Pilgrim.ActionPilgrim.Hajj?Pilgrim.ActionPilgrim.Hajj.ordinal():Pilgrim.ActionPilgrim.HajjGuide.ordinal()));
                break;

            case R.id.rlt_umrah:
                startActivity(new Intent(context, PilgrimHajjUmrahTask.class).putExtra("actionFor", currenActionFor==Pilgrim.ActionPilgrim.Hajj?Pilgrim.ActionPilgrim.Umrah.ordinal():Pilgrim.ActionPilgrim.UmrahGuide.ordinal()));
                break;

            case R.id.arrow_back:
                finish();
                break;
        }
    }
}
