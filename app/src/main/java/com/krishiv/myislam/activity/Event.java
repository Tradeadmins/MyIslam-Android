package com.krishiv.myislam.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.FrameLayout;

import com.krishiv.myislam.R;
import com.krishiv.myislam.fragment.EventList;
import com.krishiv.myislam.fragment.EventMap;
import com.krishiv.myislam.fragment.EventMy;
import com.krishiv.myislam.menu.BaseMenuActivity;

public class Event extends BaseMenuActivity implements View.OnClickListener{

    FrameLayout sub_container;
    EventList eventList;
    EventMap eventMap;
    EventMy eventMy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentViev(R.layout.activity_event);

        sub_container = findViewById(R.id.sub_container);

        eventList = new EventList();
        eventMap = new EventMap();
        eventMy = new EventMy();

        if (savedInstanceState == null) {
            setFragment(eventList);
        }
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rd_list:
                setFragment(eventList);
                break;

            case R.id.rd_map:
                setFragment(eventMap);
                break;

            case R.id.rd_myevent:
                setFragment(eventMy);
                break;

            case R.id.fab_create_event:
                startActivity(new Intent(context, EventCreate.class));
                break;

            case R.id.arrow_back:
                finish();
                break;
        }
    }

    public void setFragment(Fragment fragment){

        if (fragment instanceof EventList)
            findViewById(R.id.fab_create_event).setVisibility(View.GONE);
        else if (fragment instanceof EventMap)
            findViewById(R.id.fab_create_event).setVisibility(View.GONE);
        else
            findViewById(R.id.fab_create_event).setVisibility(View.VISIBLE);

        getSupportFragmentManager()
                .beginTransaction()
                .replace(sub_container.getId(), fragment, "")
//                .addToBackStack(backStackStateName)
                .commit();
    }
}
