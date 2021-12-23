package com.krishiv.myislam.fragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.krishiv.myislam.ItemClickListener;
import com.krishiv.myislam.R;
import com.krishiv.myislam.activity.Mosque;
import com.krishiv.myislam.adapter.MosqueListAdapter;

public class MosqueList extends Fragment implements ItemClickListener,MosqueListAdapter.layoutItemClicked {

    MosqueListAdapter adapter;
    RecyclerView recyclerView;
    Context context;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fg_event_list, container, false);

        context = getActivity();
        recyclerView = view.findViewById(R.id.recyclerview);
        if (Mosque.data.size() <= 0) {
            Mosque.getDataFromBase = new Mosque.GetDataFromBase() {
                @Override
                public void onDataGet() {
                    setAdapterData();
                }
            };
        }else{
           setAdapterData();
        }

        return view;
    }

    void setAdapterData(){
        adapter = new MosqueListAdapter(context, Mosque.data,this);
        adapter.setClickListener(this);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void itemClicked(View View, int position, int option) {

    }

//new code

    @Override
    public void onItemClick(int position, Double lat, Double longitude, String eventname, String address) {
        String geoUri = "http://maps.google.co.in/maps?q=" + address;
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(geoUri));
        context.startActivity(intent);
    }
}
