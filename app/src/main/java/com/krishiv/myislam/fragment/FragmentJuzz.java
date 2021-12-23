package com.krishiv.myislam.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.krishiv.myislam.R;
import com.krishiv.myislam.adapter.JuzAdapter;
import com.krishiv.myislam.adapter.SuraAdapter;
import com.krishiv.myislam.dto.SuraModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class FragmentJuzz extends Fragment {

    RecyclerView the_quran_recyclerview;
    JuzAdapter theQuranPagerAdapter;
    Context context;
    ArrayList<SuraModel>  suraArrayList = new ArrayList<>();
    String path;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sura, container, false);
        init(view);
        suraArrayList.clear();
        suraArrayList = getArguments().getParcelableArrayList("juzlist");
        path = getArguments().getString("path");
        setAdapter(suraArrayList);
        return view;
    }
    private void init(View view) {
        context = getActivity();
        the_quran_recyclerview = view.findViewById(R.id.the_quran_recyclerview);
    }

    private void setAdapter(ArrayList<SuraModel> suraAist) {
        theQuranPagerAdapter = new JuzAdapter(context, suraAist,path);
        the_quran_recyclerview.setLayoutManager(new LinearLayoutManager(getContext()));
        the_quran_recyclerview.setAdapter(theQuranPagerAdapter);
    }

}
