package com.krishiv.myislam.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RadioButton;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.reflect.TypeToken;
import com.krishiv.myislam.API.Api;
import com.krishiv.myislam.ItemClickListener;
import com.krishiv.myislam.R;
import com.krishiv.myislam.adapter.HadithAdapter;
import com.krishiv.myislam.dto.BookListModel;
import com.krishiv.myislam.dto.HadithModel;
import com.krishiv.myislam.dto.ResultComman;
import com.krishiv.myislam.menu.BaseMenuActivity;
import com.krishiv.myislam.utils.GlobalTask;
import com.krishiv.myislam.utils.SharedFavourite;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Hadiths extends BaseMenuActivity implements ItemClickListener, View.OnClickListener,HadithAdapter.layoutClicked {

    public RadioButton collection, fav;
    SharedFavourite pref;
    ArrayList<HadithModel> data, visibleData;
    HadithAdapter adapter;
    RecyclerView recyclerView;
    FrameLayout frameLayout;
    HadithBookList hadithBookList;
    int Position;

    ArrayList<BookListModel> bookList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentViev(R.layout.activity_hadiths);
        recyclerView = findViewById(R.id.recyclerview);

        BookListModel weekDays1 = new BookListModel("Sahih Bukhari", "Imam Muhammad al-Bukhari", "7069Hadits",1,"صحيح البخاري");
        BookListModel weekDays2 = new BookListModel("Sahih Muslim", "Imam Muslim ibn al-Hajjaj", "7190Hadits",2,"صحيح مسلم");
        BookListModel weekDays3 = new BookListModel("Sunan Abi Dawood", "Imam Abu Dawad Sulayman", "5236Hadits",3,"سنن أبي داود");
        BookListModel weekDays4 = new BookListModel("Malik Imam Muwatta", "Imam Malik b.Anas b.malik", "1973Hadits",4,"مالك الإمام مؤتة");
        BookListModel weekDays5 = new BookListModel("Jami at-Tirmidhi", "Imam abu Isa at-Trimidhi", "4031Hadits",6,"جامي الترمذي");
        BookListModel weekDays6 = new BookListModel("Sunan Ibn Majah", "Imam Muhammad Ibn Majah", "4340Hadits",7,"سنن بن ماجه");
//        BookListModel weekDays7 = new BookListModel("Sunan an-Nasai", "Imam Ahmad an-Nasai", "5683Hadits",9);
        BookListModel weekDays8 = new BookListModel("Riyaad us-saliheen", " Imam Yahya al-Nawawi", "1895Hadits",10,"رياض الصالحين");
        bookList.add(weekDays1);
        bookList.add(weekDays2);
        bookList.add(weekDays3);
        bookList.add(weekDays4);
        bookList.add(weekDays5);
        bookList.add(weekDays6);
        bookList.add(weekDays8);


        adapter = new HadithAdapter(context, bookList, this);
        adapter.setClickListener(this);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);


        pref = new SharedFavourite(context);
        data = new ArrayList<>();
        collection = (RadioButton) findViewById(R.id.rd_list);
        fav = (RadioButton) findViewById(R.id.rd_my);

        hadithBookList = new HadithBookList();
        visibleData = new ArrayList<>();
        frameLayout = findViewById(R.id.content);




    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.arrow_back:
                finish();
                break;


        }
    }

    @Override
    public void itemClicked(View View, int position, int clickedId) {
    }


    private void callApi() {
        GlobalTask.showProgressDialog(context);
        Api.getClient().GetAllHadith("Bearer " + BaseMenuActivity.userProfile.getAccess_token()).enqueue(new Callback<ResultComman>() {
            @Override
            public void onResponse(Call<ResultComman> call, Response<ResultComman> response) {
                ResultComman resultObj = response.body();
                if (resultObj != null) {
                    if (resultObj.getContent() instanceof String) {
                        GlobalTask.showToastMessage(context, resultObj.getContent().toString());
                    } else if (resultObj.getContent() instanceof ArrayList) {
                        try {
                            JsonArray jsonObject = new Gson().toJsonTree(resultObj.getContent()).getAsJsonArray();
                            data.clear();
                            visibleData.clear();

                            Type listType = new TypeToken<List<HadithModel>>() {
                            }.getType();
                            data.addAll((ArrayList<HadithModel>) new Gson().fromJson(jsonObject.toString(), listType));
                            visibleData.addAll(data);
                            if (data.size() == 0)
                                GlobalTask.showToastMessage(context, "Data not available");
                        } catch (Exception e) {
                        }
                        adapter.notifyDataSetChanged();
                    }
                } else {
                    adapter.notifyDataSetChanged();
                    GlobalTask.showTostErrorResponse(context, response.errorBody());
                }
                GlobalTask.hideProgressDialog();
            }

            @Override
            public void onFailure(Call<ResultComman> call, Throwable t) {
                GlobalTask.hideProgressDialog();
            }
        });
    }




    @Override
    public void onLayoutClicked(int position, String bookname, int bookId) {

        Intent intent =new Intent(this,HadithBookList.class);
        intent.putExtra("bookId",""+bookId);
        startActivity(intent);
    }
}