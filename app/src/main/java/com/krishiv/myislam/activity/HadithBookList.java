package com.krishiv.myislam.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.FrameLayout;

import com.krishiv.myislam.ItemClickListener;
import com.krishiv.myislam.R;
import com.krishiv.myislam.adapter.HadithAdapter;
import com.krishiv.myislam.dto.BookListModel;
import com.krishiv.myislam.fragment.HadithBookSearch;
import com.krishiv.myislam.fragment.HadithFavourite;
import com.krishiv.myislam.menu.BaseMenuActivity;
import com.krishiv.myislam.utils.GPSTracker;
import com.krishiv.myislam.utils.GlobalTask;

import java.util.ArrayList;

public class HadithBookList extends BaseMenuActivity implements ItemClickListener, View.OnClickListener {
    HadithAdapter adapter;
    HadithBookSearch hadithBookSearch;
    HadithFavourite hadithFavourite ;
    Context context;
    FrameLayout frameLayout;
    String bookId;
     int currentFragment;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentViev(R.layout.new_hadith_collection);


        bookId = getIntent().getStringExtra("bookId");

        frameLayout = findViewById(R.id.content);
        hadithBookSearch = new HadithBookSearch();
        hadithFavourite = new HadithFavourite();
        if (savedInstanceState == null) {
            currentFragment=1;
            Bundle args = new Bundle();
            args.putInt("bookId", Integer.parseInt(bookId));
            hadithBookSearch.setArguments(args);
            FragmentManager manager = getSupportFragmentManager();
            FragmentTransaction ft = manager.beginTransaction();
            ft.replace(R.id.content, hadithBookSearch);
            ft.commit();

        }

    }

    @Override
    public void itemClicked(View View, int position, int option) {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.arrow_back:
                finish();
                break;
            case R.id.rd_list:
                if(currentFragment==1)
               break;
                currentFragment=1;
                Bundle args = new Bundle();
                args.putInt("bookId", Integer.parseInt(bookId));
                hadithBookSearch.setArguments(args);
                FragmentManager manager = getSupportFragmentManager();
                FragmentTransaction ft = manager.beginTransaction();
                ft.replace(R.id.content, hadithBookSearch);
                ft.commit();
                break;
            case R.id.rd_my:
                if(currentFragment==2)
                    break;
                currentFragment=2;
                Bundle args1 = new Bundle();
                args1.putInt("bookId", Integer.parseInt(bookId));
                hadithFavourite.setArguments(args1);
                FragmentManager manager1 = getSupportFragmentManager();
                FragmentTransaction ft1 = manager1.beginTransaction();
                ft1.replace(R.id.content, hadithFavourite);
                ft1.commit();
                break;

        }
    }



    @Override
    protected void onDestroy() {
        GlobalTask.hideProgressDialog();
        super.onDestroy();
    }
}
