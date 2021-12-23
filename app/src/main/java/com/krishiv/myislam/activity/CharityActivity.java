package com.krishiv.myislam.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.krishiv.myislam.R;
import com.krishiv.myislam.menu.BaseMenuActivity;

public class CharityActivity extends BaseMenuActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentViev(R.layout.activity_charity);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.arrow_back:
                finish();
                break;
            case R.id.txt_zakat:
               startActivity(new Intent(CharityActivity.this,ZakatCalculator.class));
                break;
            case R.id.txt_qurbani:
                startActivity(new Intent(CharityActivity.this,Qurbani.class));
                break;

        }
    }
}
