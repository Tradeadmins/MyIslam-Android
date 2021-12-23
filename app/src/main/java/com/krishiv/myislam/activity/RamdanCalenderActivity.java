package com.krishiv.myislam.activity;

import android.arch.lifecycle.ViewModelStoreOwner;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.krishiv.myislam.R;
import com.krishiv.myislam.adapter.CalenderRecyclerAdapter;
import com.krishiv.myislam.menu.BaseMenuActivity;

import java.util.ArrayList;

public class RamdanCalenderActivity extends BaseMenuActivity implements View.OnClickListener {

    RecyclerView recycler1,recycler2,recycler3;
    CalenderRecyclerAdapter adapter,adapter2,adapter3;
    ArrayList<Integer> dateList = new ArrayList<Integer>();
    ArrayList<Integer> dateList2 = new ArrayList<Integer>();
    ArrayList<Integer> dateList3 = new ArrayList<Integer>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentViev(R.layout.activity_ramdan_calender);

        dateList.add(1);dateList.add(2);dateList.add(3);dateList.add(4);dateList.add(5);dateList.add(6);dateList.add(7);dateList.add(8);dateList.add(9);dateList.add(10);
        dateList2.add(11);dateList2.add(12);dateList2.add(13);dateList2.add(14);dateList2.add(15);dateList2.add(16);dateList2.add(17);dateList2.add(18);dateList2.add(19);dateList2.add(20);
        dateList3.add(21);dateList3.add(22);dateList3.add(23);dateList3.add(24);dateList3.add(25);dateList3.add(26);dateList3.add(27);dateList3.add(28);dateList3.add(29);dateList3.add(30);

        recycler1=findViewById(R.id.recycler1);
        recycler2=findViewById(R.id.recycler2);
        recycler3=findViewById(R.id.recycler3);

        adapter = new CalenderRecyclerAdapter(context, dateList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
        recycler1.setLayoutManager(mLayoutManager);
        recycler1.setItemAnimator(new DefaultItemAnimator());
        recycler1.setAdapter(adapter);

        adapter2 = new CalenderRecyclerAdapter(context, dateList2);
        RecyclerView.LayoutManager mLayoutManager2 = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
        recycler2.setLayoutManager(mLayoutManager2);
        recycler2.setItemAnimator(new DefaultItemAnimator());
        recycler2.setAdapter(adapter2);

        adapter3 = new CalenderRecyclerAdapter(context, dateList3);
        RecyclerView.LayoutManager mLayoutManager3 = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
        recycler3.setLayoutManager(mLayoutManager3);
        recycler3.setItemAnimator(new DefaultItemAnimator());
        recycler3.setAdapter(adapter3);



    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.arrow_back:
                finish();
                break;


        }
    }
}
