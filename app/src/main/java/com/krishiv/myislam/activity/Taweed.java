package com.krishiv.myislam.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.krishiv.myislam.R;
import com.krishiv.myislam.menu.BaseMenuActivity;

public class Taweed extends BaseMenuActivity implements View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentViev(R.layout.activity_taweed);

        ((TextView)findViewById(R.id.txt_title)).setText(getResources().getString(R.string.taweed));
        ((TextView)findViewById(R.id.txt_desc)).setText(getResources().getString(R.string.taweed_desc));
        ((TextView)findViewById(R.id.txt_desc)).setPadding(20,20,20,20);
//        findViewById(R.id.txt_desc_title).setVisibility(View.GONE);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){

            case R.id.arrow_back:
                finish();
                break;
        }
    }
}
