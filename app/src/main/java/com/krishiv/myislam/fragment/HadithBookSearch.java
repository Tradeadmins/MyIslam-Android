package com.krishiv.myislam.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.gson.Gson;
import com.krishiv.myislam.API.ApiInterface;
import com.krishiv.myislam.ItemClickListener;
import com.krishiv.myislam.R;
import com.krishiv.myislam.adapter.CustomSpinnerAdapter;
import com.krishiv.myislam.adapter.HadithBookAdapter;
import com.krishiv.myislam.dto.ChapterModel;
import com.krishiv.myislam.dto.HadithBookChapterModel;
import com.krishiv.myislam.dto.HadithCountModel;
import com.krishiv.myislam.dto.HadithFavouriteModel;
import com.krishiv.myislam.dto.HadithItemModel;
import com.krishiv.myislam.utils.GlobalTask;
import com.krishiv.myislam.utils.SharedHadithBook;
import com.krishiv.myislam.utils.SharedHadithFav;
import com.tmall.ultraviewpager.UltraViewPager;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class HadithBookSearch extends Fragment {
    private final String TAG = getClass().getSimpleName();
    SharedHadithFav pref;
    SharedHadithBook bookPref;
    ArrayList<HadithFavouriteModel> data, visibleData;
    Spinner chapterSpinner, hadithSpinner;
    Context context;
    TextView chapterid, hadithid, hadithNumber, txtarabic, txtEnglish;
    ArrayList<String> chapterList = new ArrayList<>();
    ArrayList<String> newchapterList = new ArrayList<>();
    ArrayList<String> hadithList = new ArrayList<>();
    ArrayAdapter<String> adapter;
    ArrayAdapter<String> adapterHadith;
    UltraViewPager ultraViewPager;
    HadithBookAdapter hadithBookAdapter;
    ImageView img_like;
    CustomSpinnerAdapter customSpinnerAdapter;
    Boolean isLike;
    ArrayList<HadithBookChapterModel> bookNameList = new ArrayList<>();
    int bookId, chapterId, hadithId;
    int chapterResponseId, hadithResponseId;
    RelativeLayout bookLayout, hadithLayout;
    private ItemClickListener clickListener;
    private ProgressDialog progressDialog;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.hadith_search, container, false);


        context = getActivity();
        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage(getString(R.string.please_wait));
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
        pref = new SharedHadithFav(context);
        bookPref = new SharedHadithBook(context);
        Bundle extras = getArguments();
        if (extras != null) {
            bookId = extras.getInt("bookId");
        }

        if (bookPref.getString(bookPref.hadithBookName, "").contentEquals("")) {
            String strData = loadJSONFromAsset();
            if (strData != null) {
                try {
                    JSONObject jsonObject = new JSONObject(strData);
                    bookPref.putString(bookPref.hadithBookName, jsonObject.getString("Hadith Book name"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
        bookNameList.addAll(bookPref.getDataArray());
        callApi();
        System.out.println("bookId  " + bookId);
        ultraViewPager = (UltraViewPager) view.findViewById(R.id.ultra_viewpager);
        data = new ArrayList<>();
        visibleData = new ArrayList<>();

        chapterSpinner = view.findViewById(R.id.chapterSpinner);
        hadithSpinner = view.findViewById(R.id.hadithSpinner);
        chapterid = view.findViewById(R.id.bookId);
        hadithid = view.findViewById(R.id.hadithId);
        hadithNumber = view.findViewById(R.id.hadith_number);
        txtarabic = view.findViewById(R.id.txt_arabic);
        txtEnglish = view.findViewById(R.id.txt_eng);
        img_like = view.findViewById(R.id.img_like);

        ArrayList<HadithFavouriteModel> arrayList = pref.getDataArray();
        if (arrayList != null) {
            for (int i = 0; i < arrayList.size(); i++) {
                visibleData.add(arrayList.get(i));
            }
        }

        data.addAll(visibleData);

        adapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_dropdown_item, newchapterList);
        chapterSpinner.setAdapter(adapter);
        chapterSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selected = chapterList.get(position);

                for (int i = 0; i < bookNameList.size(); i++) {

                    if (bookNameList.get(i).getId().equalsIgnoreCase(String.valueOf(bookId))) {
                        for (int j = 0; j < bookNameList.get(i).getBooks().size(); j++) {
                            if (selected.equalsIgnoreCase(bookNameList.get(i).getBooks().get(j).getId())) {
                                chapterid.setText(bookNameList.get(i).getBooks().get(j).getName());
                                chapterId = Integer.parseInt(bookNameList.get(i).getBooks().get(j).getId());
                            }
                            System.out.println("chapterId::::::::::::::: " + chapterId);
                        }
                    }
                }
                System.out.println("chapterid:::" + chapterid.getText().toString());
                callHadithApi();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        img_like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (data.size() != 0) {
                    isLike = false;
                    int position = 0;
                    for (int i = 0; i < data.size(); i++) {
                        if (data.get(i).getCollectionId() == bookId &&
                                data.get(i).getChapterId() == chapterId &&
                                data.get(i).getHadithId() == hadithId) {
                            position = i;
                            isLike = data.get(i).getLike();
                            break;
                        }
                    }

                    if (isLike) {
                        img_like.setImageDrawable(getResources().getDrawable(R.drawable.star));
                        data.remove(position);
                    } else {
                        img_like.setImageDrawable(getResources().getDrawable(R.drawable.rating_icon));
                        data.add(new HadithFavouriteModel(bookId, chapterResponseId, hadithResponseId, true, chapterid.getText().toString()));
                    }

                    String str = new Gson().toJson(data);
                    pref.putString(pref.hadithName, str);
                } else {
                    img_like.setImageDrawable(getResources().getDrawable(R.drawable.rating_icon));
                    data.add(new HadithFavouriteModel(bookId, chapterResponseId, hadithResponseId, true, chapterid.getText().toString()));
                    String str = new Gson().toJson(data);
                    pref.putString(pref.hadithName, str);
                }
            }
        });


        return view;
    }

    public String loadJSONFromAsset() {
        String json = null;
        try {
            InputStream is = getActivity().getAssets().open("bookjson.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }

    private void callApi() {
//        GlobalTask.showProgressDialog(context);

        progressDialog.show();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://muflihun.com")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiInterface api = retrofit.create(ApiInterface.class);

        api.getChapterByBookId(bookId)
                .enqueue(new Callback<List<ChapterModel>>() {
                    @Override
                    public void onResponse(Call<List<ChapterModel>> call, Response<List<ChapterModel>> response) {
                        List<ChapterModel> resultObj = response.body();
                        if (resultObj != null) {
                            try {
                                chapterList.clear();

                                for (int i = 0; i < resultObj.size(); i++) {
                                    chapterList.add(String.valueOf(resultObj.get(i).getBook()));
                                }
                                newchapterList.clear();
                                for (int m = 0; m < bookNameList.size(); m++) {
                                    if (bookNameList.get(m).getId().equalsIgnoreCase(String.valueOf(bookId))) {
                                        for (int j = 0; j < bookNameList.get(m).getBooks().size(); j++) {
                                            for (int i = 0; i < chapterList.size(); i++) {
                                                if (bookNameList.get(m).getBooks().get(j).getId().equalsIgnoreCase(chapterList.get(i))) {
                                                    newchapterList.add(bookNameList.get(m).getBooks().get(j).getName());
                                                }
                                            }
                                        }
                                    }

                                }
                                System.out.println("newchapterList::" + newchapterList);
                                adapter.notifyDataSetChanged();
                            } catch (Exception e) {
                                e.printStackTrace();
                                GlobalTask.hideProgressDialog();
                                progressDialog.dismiss();

                            }
                        } else {
                            progressDialog.dismiss();
                            GlobalTask.showTostErrorResponse(context, response.errorBody());
                        }
//                        GlobalTask.hideProgressDialog();
                    }

                    @Override
                    public void onFailure(Call<List<ChapterModel>> call, Throwable t) {
//                        GlobalTask.hideProgressDialog();
                        progressDialog.dismiss();

                    }
                });
    }

    private void callHadithApi() {
//        GlobalTask.showProgressDialog(context);


        Log.e(TAG, "callHadithApi");


        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://muflihun.com")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiInterface api = retrofit.create(ApiInterface.class);

        api.gethadithbyChapterid(bookId, chapterId)
                .enqueue(new Callback<List<HadithCountModel>>() {
                    @Override
                    public void onResponse(Call<List<HadithCountModel>> call, Response<List<HadithCountModel>> response) {
                        List<HadithCountModel> resultObj = response.body();

                        Log.e(TAG, "callHadithApi resp " + new Gson().toJson(resultObj));

                        if (resultObj != null) {
                            try {
                                hadithList.clear();

                                for (int i = 0; i < resultObj.size(); i++) {

                                    hadithList.add(String.valueOf(resultObj.get(i).getHadith()));

                                }
                                hadithid.setText(hadithList.get(0));
                                hadithId = Integer.parseInt(hadithList.get(0));

                                adapterHadith = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_dropdown_item, hadithList);
                                hadithSpinner.setAdapter(adapterHadith);
                                hadithSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                    @Override
                                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                        String selected = hadithList.get(position);
                                        hadithid.setText(selected);
                                        hadithId = Integer.parseInt(hadithid.getText().toString());
                                        System.out.println("hadithid:::" + hadithid.getText().toString());

                                        callHadithItemApi();
                                    }

                                    @Override
                                    public void onNothingSelected(AdapterView<?> parent) {

                                    }
                                });


                            } catch (Exception e) {
                                e.printStackTrace();
                                progressDialog.dismiss();
                            }
                        } else {
                            GlobalTask.showTostErrorResponse(context, response.errorBody());
                        }
                        progressDialog.dismiss();
                    }

                    @Override
                    public void onFailure(Call<List<HadithCountModel>> call, Throwable t) {

                        Log.e(TAG, "callHadithApi resp " + t.getMessage());

                        progressDialog.dismiss();
                    }
                });
    }

    private void callHadithItemApi() {


        Log.e(TAG, "callHadithItemApi");


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

                        Log.e(TAG, "response : " + new Gson().toJson(resultObj));

                        if (resultObj != null) {

                            Log.e(TAG, "response : " + new Gson().toJson(resultObj));

                            try {
                                img_like.setImageDrawable(getResources().getDrawable(R.drawable.star));
                                for (int i = 0; i < data.size(); i++) {
                                    if (data.get(i).getCollectionId() == bookId &&
                                            data.get(i).getChapterId() == chapterId &&
                                            data.get(i).getHadithId() == hadithId) {
                                        img_like.setImageDrawable(getResources().getDrawable(R.drawable.rating_icon));
                                        break;
                                    }
                                }
                                txtarabic.setText(Html.fromHtml(resultObj.getTextArabic()));
                                txtEnglish.setText(Html.fromHtml(
                                        resultObj.getText())
                                );
                                hadithNumber.setText("" + resultObj.getHadith());
                                chapterResponseId = resultObj.getBook();
                                hadithResponseId = resultObj.getHadith();
                                GlobalTask.hideProgressDialog();

                            } catch (Exception e) {
                                e.printStackTrace();
                                GlobalTask.hideProgressDialog();

                                progressDialog.dismiss();
                            }
                        } else {
                            GlobalTask.showTostErrorResponse(context, response.errorBody());
                        }
                        GlobalTask.hideProgressDialog();

                        progressDialog.dismiss();
                    }

                    @Override
                    public void onFailure(Call<HadithItemModel> call, Throwable t) {
                        GlobalTask.hideProgressDialog();

                        progressDialog.dismiss();
                    }
                });
    }

}
