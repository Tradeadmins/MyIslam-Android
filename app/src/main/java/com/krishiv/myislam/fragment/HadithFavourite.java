package com.krishiv.myislam.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.gson.Gson;
import com.krishiv.myislam.API.ApiInterface;
import com.krishiv.myislam.R;
import com.krishiv.myislam.dto.HadithFavouriteModel;
import com.krishiv.myislam.dto.HadithItemModel;
import com.krishiv.myislam.utils.GlobalTask;
import com.krishiv.myislam.utils.SharedHadithFav;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class HadithFavourite extends Fragment {

    SharedHadithFav pref;
    Spinner chapterSpinner, hadithSpinner;
    Context context;
    TextView chapterid, hadithid, hadithNumber, txtarabic, txtEnglish;
    ArrayList<String> chapterList = new ArrayList<>();
    ArrayList<String> hadithList = new ArrayList<>();
    ArrayAdapter<String> adapter;
    ArrayAdapter<String> adapterHadith;
    ArrayList<HadithFavouriteModel> visibleData;
    LinearLayout selectLayout;
    RelativeLayout mainLayout;
    ImageView img_like;
    RelativeLayout bookLayout, hadithLayout;
    int bookId, chapterId, hadithId;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.hadith_search, container, false);

        context = getActivity();
        pref = new SharedHadithFav(context);
        bookLayout = (RelativeLayout) view.findViewById(R.id.bookNameLayout);
        hadithLayout = (RelativeLayout) view.findViewById(R.id.hadithLayout);

        bookLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chapterSpinner.performClick();
            }
        });
        hadithLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hadithSpinner.performClick();
            }
        });
        mainLayout = view.findViewById(R.id.main_layout);
        selectLayout = view.findViewById(R.id.select_layout);
        Bundle extras = getArguments();
        img_like = view.findViewById(R.id.img_like);

        if (extras != null) {
            bookId = extras.getInt("bookId");
        }

        System.out.println("bookId     " + bookId);

        chapterSpinner = view.findViewById(R.id.chapterSpinner);
        hadithSpinner = view.findViewById(R.id.hadithSpinner);
        chapterid = view.findViewById(R.id.bookId);
        hadithid = view.findViewById(R.id.hadithId);
        hadithNumber = view.findViewById(R.id.hadith_number);
        txtarabic = view.findViewById(R.id.txt_arabic);
        txtEnglish = view.findViewById(R.id.txt_eng);
        img_like = view.findViewById(R.id.img_like);

        visibleData = pref.getDataArray();

        chapterList.clear();

        img_like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (visibleData.size() != 0) {
                    for (int i = 0; i < visibleData.size(); i++) {
                        if (visibleData.get(i).getCollectionId() == bookId &&
                                visibleData.get(i).getChapterId() == chapterId &&
                                visibleData.get(i).getHadithId() == hadithId) {

                            img_like.setImageDrawable(getResources().getDrawable(R.drawable.star));
                            visibleData.remove(i);
                            i--;
                            break;
                        }
                    }
                    String str = new Gson().toJson(visibleData);
                    pref.putString(pref.hadithName, str);

                    setListData();
                }
            }
        });

        setListData();
        return view;
    }

    private void setListData() {
        if (visibleData == null || visibleData.size()<=0){
            mainLayout.setVisibility(View.GONE);
            selectLayout.setVisibility(View.VISIBLE);
            return;
        }

        img_like.setImageDrawable(context.getResources().getDrawable(R.drawable.rating_icon));
        adapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_dropdown_item, chapterList);
        chapterSpinner.setAdapter(adapter);
        chapterSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                for (int i=0; i<visibleData.size(); i++){
                    if (visibleData.get(i).getCollectionId() == bookId && visibleData.get(i).getBookname() == chapterList.get(position)) {
                        chapterid.setText(visibleData.get(i).getBookname());
                        chapterId = visibleData.get(i).getChapterId();
                    }
                }
                getAllHadithFromChapter();
                adapterHadith = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_dropdown_item, hadithList);
                hadithSpinner.setAdapter(adapterHadith);
                hadithSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        String selected = hadithList.get(position);
                        hadithid.setText(selected);
                        hadithId = Integer.parseInt(selected);
                        callHadithItemApi();
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        chapterList.clear();
        hadithList.clear();

        if (visibleData ==null) {
            mainLayout.setVisibility(View.GONE);
            selectLayout.setVisibility(View.VISIBLE);
        } else {
            hadithList.clear();
            mainLayout.setVisibility(View.VISIBLE);
            selectLayout.setVisibility(View.GONE);

            HashSet<String> data = new HashSet<>();
            for (int i = 0; i < visibleData.size(); i++) {
                if (visibleData.get(i).getCollectionId() == bookId) {
                    data.add(String.valueOf(visibleData.get(i).getBookname()));
                }
            }
            chapterList.addAll(data);
            if (!chapterList.isEmpty()) {
                chapterid.setText(chapterList.get(0));
                if (adapter != null) {
                    adapter.notifyDataSetChanged();
                }
            }
        }
    }

    private void getAllHadithFromChapter(){
        hadithList.clear();

        HashSet<String> data = new HashSet<>();
        for (int i = 0; i < visibleData.size(); i++) {
            if (visibleData.get(i).getCollectionId() == bookId && visibleData.get(i).getChapterId() == chapterId) {
                data.add(String.valueOf(visibleData.get(i).getHadithId()));
            }
        }
        hadithList.addAll(data);


    }

    private void callHadithItemApi() {
        GlobalTask.showProgressDialog(context);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://muflihun.com")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiInterface api = retrofit.create(ApiInterface.class);

        api.getHadith(bookId, chapterId, hadithId)
                .enqueue(new Callback<HadithItemModel>() {

                    @Override
                    public void onResponse(Call<HadithItemModel> call, Response<HadithItemModel> response) {
                        HadithItemModel resultObj = response.body();
                        if (resultObj != null) {
                            try {

                                txtarabic.setText(Html.fromHtml(resultObj.getTextArabic()));
                                txtEnglish.setText(Html.fromHtml(
                                        resultObj.getText())
                                );
                                hadithNumber.setText("" + resultObj.getHadith());

                            } catch (Exception e) {
                                e.printStackTrace();
                                GlobalTask.hideProgressDialog();
                            }
                        } else {
                            GlobalTask.showTostErrorResponse(context, response.errorBody());
                        }
                        GlobalTask.hideProgressDialog();
                    }

                    @Override
                    public void onFailure(Call<HadithItemModel> call, Throwable t) {
                        GlobalTask.hideProgressDialog();
                    }
                });
    }
}
