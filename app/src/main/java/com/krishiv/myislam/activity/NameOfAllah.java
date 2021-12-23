package com.krishiv.myislam.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.google.gson.Gson;
import com.krishiv.myislam.ItemClickListener;
import com.krishiv.myislam.R;
import com.krishiv.myislam.adapter.EventListAdapter;
import com.krishiv.myislam.adapter.NameOfAllahAdapter;
import com.krishiv.myislam.dto.NameOfAllahModel;
import com.krishiv.myislam.menu.BaseMenuActivity;
import com.krishiv.myislam.utils.GlobalTask;
import com.krishiv.myislam.utils.SharedAllahName;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class NameOfAllah extends BaseMenuActivity implements ItemClickListener, View.OnClickListener{
    SharedAllahName pref;
    ArrayList<NameOfAllahModel> data, visibleData;
    NameOfAllahAdapter adapter;
    RecyclerView recyclerView;
    TextView txt_desc_small, txt_desc_full;
    ImageView img_collapse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentViev(R.layout.activity_name_of_allah);

        pref = new SharedAllahName(context);
        recyclerView = findViewById(R.id.recyclerview);
        txt_desc_small = findViewById(R.id.txt_desc_small);
        txt_desc_full = findViewById(R.id.txt_desc_full);
        img_collapse = findViewById(R.id.img_collapse);

        data = new ArrayList<>();
        visibleData = new ArrayList<>();

        adapter = new NameOfAllahAdapter(context, visibleData);
        adapter.setClickListener(this);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);

        if (pref.getString(pref.allahName,"").contentEquals("")){
            String strData = loadJSONFromAsset();
            if (strData != null) {
                try {
                    JSONObject jsonObject = new JSONObject(strData);
                    pref.putString(pref.allahName, jsonObject.getString("Text Source Allah"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }

        data.addAll(pref.getDataArray());
        visibleData.addAll(data);

        if (data.size()>0){
            adapter.notifyDataSetChanged();
        }else{
            GlobalTask.showToastMessage(context,"Can not read AllahNames Data");
        }

        setTextSpanable();

        img_collapse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                txt_desc_small.setVisibility(View.VISIBLE);
                txt_desc_full.setVisibility(View.GONE);
                img_collapse.setVisibility(View.GONE);
            }
        });

        setTextSpanableTab();
        setTextSpanableTitle();
    }

    private void setTextSpanableTab() {
        String strText = ((RadioButton)findViewById(R.id.rd_list)).getText().toString();
        String clickOn = getResources().getString(R.string.allah);
        SpannableString spannableString = new SpannableString(strText);
        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(View view) {

            }

            public void updateDrawState(TextPaint ds) {
                ds.setUnderlineText(false);
            }
        };
        int startIndexOfLink = strText.indexOf(clickOn);
        spannableString.setSpan(clickableSpan, startIndexOfLink,
                startIndexOfLink + clickOn.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        spannableString.setSpan(new ForegroundColorSpan(ContextCompat.getColor(context, R.color.colorYellow)), startIndexOfLink,
                startIndexOfLink + clickOn.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        ((RadioButton)findViewById(R.id.rd_list)).setHighlightColor(Color.TRANSPARENT);
        ((RadioButton)findViewById(R.id.rd_list)).setMovementMethod(LinkMovementMethod.getInstance());
        ((RadioButton)findViewById(R.id.rd_list)).setText(spannableString, TextView.BufferType.SPANNABLE);
    }

    private void setTextSpanableTitle() {
        String strText = ((RadioButton)findViewById(R.id.rd_list)).getText().toString();
        String clickOn = getResources().getString(R.string.allah);
        SpannableString spannableString = new SpannableString(strText);
        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(View view) {

            }

            public void updateDrawState(TextPaint ds) {
                ds.setUnderlineText(false);
            }
        };
        int startIndexOfLink = strText.indexOf(clickOn);
        spannableString.setSpan(clickableSpan, startIndexOfLink,
                startIndexOfLink + clickOn.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        spannableString.setSpan(new ForegroundColorSpan(ContextCompat.getColor(context, R.color.colorYellow)), startIndexOfLink,
                startIndexOfLink + clickOn.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        ((TextView)findViewById(R.id.txt_title)).setHighlightColor(Color.TRANSPARENT);
        ((TextView)findViewById(R.id.txt_title)).setMovementMethod(LinkMovementMethod.getInstance());
        ((TextView)findViewById(R.id.txt_title)).setText(spannableString, TextView.BufferType.SPANNABLE);
    }

    private void setTextSpanable() {
        String strText = txt_desc_small.getText().toString();
        String clickOn = "read more";
//        String clickOn = getResources().getString(R.string.txt_read_more);
        SpannableString spannableString = new SpannableString(strText);
        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(View view) {
                txt_desc_small.setVisibility(View.GONE);
                txt_desc_full.setVisibility(View.VISIBLE);
                img_collapse.setVisibility(View.VISIBLE);
            }

            public void updateDrawState(TextPaint ds) {
                ds.setUnderlineText(false);
            }
        };
        int startIndexOfLink = strText.indexOf(clickOn);
        spannableString.setSpan(clickableSpan, startIndexOfLink,
                startIndexOfLink + clickOn.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        spannableString.setSpan(new ForegroundColorSpan(ContextCompat.getColor(context, R.color.colorYellow)), startIndexOfLink,
                startIndexOfLink + clickOn.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        txt_desc_small.setHighlightColor(Color.TRANSPARENT);
        txt_desc_small.setMovementMethod(LinkMovementMethod.getInstance());
        txt_desc_small.setText(spannableString, TextView.BufferType.SPANNABLE);
    }

    public String loadJSONFromAsset() {
        String json = null;
        try {
            InputStream is = context.getAssets().open("NameOfAllahDetail.json");
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

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rd_list:
                visibleData.clear();
                visibleData.addAll(data);
                adapter.notifyDataSetChanged();
                break;

            case R.id.rd_fav:
                visibleData.clear();
                for (int i=0; i<data.size(); i++) {
                    if (data.get(i).isLike())
                        visibleData.add(data.get(i));
                }
                if (visibleData.size()<=0)
                    GlobalTask.showToastMessage(context, "Favourite list is empty");

                adapter.notifyDataSetChanged();
                break;

            case R.id.arrow_back:
                finish();
                break;
        }
    }

    @Override
    public void itemClicked(View View, int position, int clickedId) {
        visibleData.get(position).setLike(!visibleData.get(position).isLike());
        pref.putString(pref.allahName, new Gson().toJson(data));

        if(((RadioButton)findViewById(R.id.rd_fav)).isChecked())
            visibleData.remove(position);

        adapter.notifyDataSetChanged();
    }
}