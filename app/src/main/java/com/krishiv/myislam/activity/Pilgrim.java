package com.krishiv.myislam.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.krishiv.myislam.R;
import com.krishiv.myislam.menu.BaseMenuActivity;

public class Pilgrim extends BaseMenuActivity implements View.OnClickListener {

    public static enum ActionPilgrim{
        Hajj,
        Umrah,
        HajjGuide,
        UmrahGuide,
        HajjComplete,
        UmrahComplete,
        HajjGuideComplete,
        UmrahGuideComplete
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentViev(R.layout.activity_pilgrim);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){

            case R.id.txt_start_preparation:
                startActivity(new Intent(context, PilgrimHajjUmrah.class).putExtra("actionFor", ActionPilgrim.Hajj.ordinal()));
                break;

            case R.id.txt_pilgrimage_guide:
                startActivity(new Intent(context, PilgrimHajjUmrah.class).putExtra("actionFor", ActionPilgrim.HajjGuide.ordinal()));
                break;

            case R.id.txt_find_pilgrimage:
                startActivity(new Intent(context, PilgrimFind.class));
                break;

            case R.id.arrow_back:
                finish();
                break;
        }
    }
}
