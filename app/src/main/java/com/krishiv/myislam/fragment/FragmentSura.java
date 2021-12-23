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
import com.krishiv.myislam.adapter.SuraAdapter;
import com.krishiv.myislam.dto.SuraModel;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class FragmentSura extends Fragment {

    RecyclerView the_quran_recyclerview;
    SuraAdapter theQuranPagerAdapter;
    Context context;
    ArrayList<SuraModel>  suraArrayList = new ArrayList<>();
    String path;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sura, container, false);
        init(view);
        suraArrayList.clear();
        suraArrayList = getArguments().getParcelableArrayList("suralist");
        path = getArguments().getString("path");
        setAdapter(suraArrayList);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        theQuranPagerAdapter.notifyDataSetChanged();
    }

    private void init(View view) {
        context = getActivity();
        the_quran_recyclerview = view.findViewById(R.id.the_quran_recyclerview);
    }

    private void setAdapter(ArrayList<SuraModel> suraAist) {
        //parseData();
        theQuranPagerAdapter = new SuraAdapter(context, suraAist);
        the_quran_recyclerview.setLayoutManager(new LinearLayoutManager(getContext()));
        the_quran_recyclerview.setAdapter(theQuranPagerAdapter);
    }

    public String loadJSONFromAsset() {
        String json = null;
        try {
            InputStream is = context.getAssets().open("Quranjson.json");
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

    private void parseData(){
        try {
            JSONObject result = new JSONObject(loadJSONFromAsset());
            JSONObject quranObject = result.getJSONObject("quran");
            JSONObject surasObject = quranObject.getJSONObject("suras");
            JSONArray arraySura = surasObject.getJSONArray("sura");

            for (int i=0 ; i<arraySura.length() ; i++){
                JSONObject suraObject = arraySura.getJSONObject(i);
                SuraModel suraModel = new SuraModel();
                suraModel.set_ayas(suraObject.optString("_ayas"));
                suraModel.set_ename(suraObject.optString("_ename"));
                suraModel.set_index(suraObject.optString("_index"));
                suraModel.set_name(suraObject.optString("_name"));
                suraModel.set_order(suraObject.optString("_order"));
                suraModel.set_start(suraObject.optString("_start"));
                suraModel.set_tname(suraObject.optString("_tname"));
                suraModel.set_type(suraObject.optString("_type"));
                suraModel.set_rukus(suraObject.optString("_rukus"));

                suraArrayList.add(suraModel);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
