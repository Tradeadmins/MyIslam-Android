package com.krishiv.myislam.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.krishiv.myislam.R;
import com.krishiv.myislam.adapter.SuraAdapter;
import com.krishiv.myislam.dto.SuraModel;

import java.util.ArrayList;

public class FragmentJuz extends Fragment {

    RecyclerView the_quran_recyclerview;
    SuraAdapter theQuranPagerAdapter;
    Context context;
    ArrayList<SuraModel> suraArrayList = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_sura , container , false);


        return view;
    }
}
