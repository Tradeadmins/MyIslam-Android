package com.krishiv.myislam.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;

import com.krishiv.myislam.R;
import com.krishiv.myislam.adapter.TheQuranSearchAdapter;
import com.krishiv.myislam.dto.QuranDetailModel;
import com.krishiv.myislam.dto.SuraModel;
import com.krishiv.myislam.fragment.FragmentFavourites;
import com.krishiv.myislam.fragment.FragmentJuzz;
import com.krishiv.myislam.fragment.FragmentSura;
import com.krishiv.myislam.menu.BaseMenuActivity;
import com.krishiv.myislam.utils.Shared;
import com.krishiv.myislam.utils.Utility;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class ActivityThequran extends BaseMenuActivity implements View.OnClickListener {

    FrameLayout sub_container;
    FragmentSura fragmentSura;
    FragmentJuzz fragmentJuz;
    FragmentFavourites fragmentFavourites;
    ArrayList<SuraModel> suraArrayList = new ArrayList<>();
    ArrayList<SuraModel> juzArrayList = new ArrayList<>();
    RecyclerView searRecyclerView;
    RelativeLayout searched_list_relative, thequran_relative, frame;
    EditText et_search;
    ImageView iv_search, iv_cancel;
    RadioGroup rdg_tab;
    LinearLayout search_relative;
    Animation slideUpAnimation;
    RelativeLayout main;
    TheQuranSearchAdapter theQuranPagerAdapter;
    ArrayList<QuranDetailModel> mainsearchedlist = new ArrayList<>();
    ArrayList<QuranDetailModel> filterdList = new ArrayList<>();
    String path;
    Shared shared;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        Bundle bundle = getIntent().getExtras();
        shared = new Shared(ActivityThequran.this);
        path = shared.getString(Shared.LANGUAGE_PATH, "");
//        path = bundle.getString("path");
        setContentViev(R.layout.activity_thequran);
        init();
        anim();
        listeners();
        parseData();
        setAdapter(); // searched adapter
        showFragments();
        if (savedInstanceState == null) {
            setFragment(fragmentSura);
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
    }

    private void showFragments() {
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList("suralist", suraArrayList);
        bundle.putString("path", path);
        fragmentSura = new FragmentSura();
        fragmentSura.setArguments(bundle);
//        Bundle bundle2 = new Bundle();
//        bundle2.putParcelableArrayList("juzlist", MAIN_JUZ_LIST2);
//        fragmentJuz = new FragmentJuzz();
//        fragmentJuz.setArguments(bundle2);
        fragmentFavourites = new FragmentFavourites();
    }

    private void listeners() {
        iv_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setAdapter();
                Utility.showKeyboard(ActivityThequran.this);
                searRecyclerView.setVisibility(View.GONE);
                search_relative.setVisibility(View.VISIBLE);
                thequran_relative.setVisibility(View.GONE);
                searched_list_relative.setVisibility(View.VISIBLE);
                searched_list_relative.startAnimation(slideUpAnimation);

                rdg_tab.setVisibility(View.GONE);
                frame.setVisibility(View.GONE);
            }
        });

        iv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
                main.clearFocus();
                et_search.clearFocus();
                //hideKeyboard(ActivityThequran.this);
                rdg_tab.setVisibility(View.VISIBLE);
                //Utility.hideKeyboard(ActivityThequran.this);
                et_search.setText("");
                thequran_relative.setVisibility(View.VISIBLE);
                search_relative.setVisibility(View.GONE);
                searched_list_relative.setVisibility(View.GONE);
                frame.setVisibility(View.VISIBLE);
            }
        });

        et_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (!et_search.getText().toString().equals("") && et_search.getText().toString().length() >= 2) {
                    filter(editable.toString());

                    if (searRecyclerView.getVisibility() == View.INVISIBLE || searRecyclerView.getVisibility() == View.GONE)
                        searRecyclerView.setVisibility(View.VISIBLE);
                    return;
                }
                if (et_search.getText().toString().equals("")) {
                    searRecyclerView.setVisibility(View.INVISIBLE);
                }
            }
        });
    }

    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }


    private void anim() {
        slideUpAnimation = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.bottom_up);
    }

    private void init() {
        main = findViewById(R.id.main);
        sub_container = findViewById(R.id.sub_container);
        searRecyclerView = findViewById(R.id.search_recylcer);
        searRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        searched_list_relative = findViewById(R.id.searched_list_relative);
        thequran_relative = findViewById(R.id.thequran_relative);
        search_relative = findViewById(R.id.search_relative);
        et_search = findViewById(R.id.et_search);
        iv_search = findViewById(R.id.iv_search);
        iv_cancel = findViewById(R.id.iv_cancel);
        rdg_tab = findViewById(R.id.rdg_tab);
        frame = findViewById(R.id.frame);
    }

    private void setAdapter() {
        mainsearchedlist = parseDetails();
        ArrayList<QuranDetailModel> applist = parseDetailsAppLanguage();
        for (int ii = 0; ii < mainsearchedlist.size(); ii++) {
            mainsearchedlist.get(ii).setAppLanugageName(applist.get(ii).getAya_text());
        }
        ArrayList<QuranDetailModel> saveArrayList = shared.getFavoritesQuran(context);

        if (saveArrayList != null) {
            for (int pp = 0; pp < mainsearchedlist.size(); pp++) {
                for (int p = 0; p < saveArrayList.size(); p++) {
                    if (mainsearchedlist.get(pp).getSura_index().equals(saveArrayList.get(p).getSura_index()) && mainsearchedlist.get(pp).getMain_index().equals(saveArrayList.get(p).getMain_index())) {
                        mainsearchedlist.get(pp).setFavourites(true);
                    }
                }
            }
        }
        theQuranPagerAdapter = new TheQuranSearchAdapter(context, mainsearchedlist, new onSelectFavOnSearchAdapter());
        searRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        searRecyclerView.setAdapter(theQuranPagerAdapter);
    }


    public class onSelectFavOnSearchAdapter {
        public void onSelect(final int pos, final boolean status) {
            if (status) {
                filterdList.get(pos).setFavourites(true);
                theQuranPagerAdapter.notifyItemChanged(pos);

                ArrayList<QuranDetailModel> quranDetailModels = shared.getFavoritesQuran(context);
                if (quranDetailModels != null) {
                    quranDetailModels.add(filterdList.get(pos));
                    shared.saveFavoritesQuran(quranDetailModels, context);
                } else {
                    ArrayList<QuranDetailModel> newList = new ArrayList<>();
                    newList.add(filterdList.get(pos));
                    shared.saveFavoritesQuran(newList, context);
                }
            } else {
                theQuranPagerAdapter.notifyItemChanged(pos);
                filterdList.get(pos).setFavourites(false);
                ArrayList<QuranDetailModel> quranDetailModels = shared.getFavoritesQuran(context);
                if (quranDetailModels != null) {
                    for (int p = 0; p < quranDetailModels.size(); p++) {
                        if (quranDetailModels.get(p).getMain_index().equals(filterdList.get(pos).getMain_index()) && quranDetailModels.get(p).getSura_index().equals(filterdList.get(pos).getSura_index())) {
                            quranDetailModels.remove(quranDetailModels.get(p));
                        }
                    }
                    shared.saveFavoritesQuran(quranDetailModels, context);
                }
            }
        }
    }

    public String loadJSONFromAsset(String file) {
        String json = null;
        try {
            InputStream is = this.getAssets().open(file);
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

    private String readFromFile() {

        String ret = "";
        InputStream inputStream = null;
        try {
            inputStream = new FileInputStream(path);

            if (inputStream != null) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String receiveString = "";
                StringBuilder stringBuilder = new StringBuilder();

                while ((receiveString = bufferedReader.readLine()) != null) {
                    stringBuilder.append(receiveString);
                }

                ret = stringBuilder.toString();
            }
        } catch (FileNotFoundException e) {
            Log.e("login activity", "File not found: " + e.toString());
        } catch (IOException e) {
            Log.e("login activity", "Can not read file: " + e.toString());
        } catch (Exception e) {
            Log.e("login activity", "Can not read file: " + e.toString());
        } finally {
            try {
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return ret;
    }


    public ArrayList<QuranDetailModel> parseDetails() {
        try {
            JSONObject Arabic = new JSONObject(loadJSONFromAsset("Arabic.json"));
            JSONObject ARABIC_JSON = Arabic.getJSONObject("quran");
            JSONObject APP_LANGUAGE_JSON = new JSONObject(loadJSONFromAsset("codebeautifyEN.json"));
            ArrayList<QuranDetailModel> quranDetailModelArrayList = new ArrayList<>();
            JSONArray jsonArray = ARABIC_JSON.getJSONArray("sura");
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject suraObject = jsonArray.getJSONObject(i);
                JSONArray ayaArray = suraObject.getJSONArray("aya");
                for (int p = 0; p < ayaArray.length(); p++) {
                    QuranDetailModel quranDetailModel = new QuranDetailModel();
                    quranDetailModel.setSura_index(suraObject.optString("_index"));
                    quranDetailModel.setSura_name(suraObject.optString("_name"));
                    JSONObject ayaObject = ayaArray.getJSONObject(p);
                    quranDetailModel.setMain_index(ayaObject.getString("_index"));
                    quranDetailModel.setAya_text(ayaObject.getString("_text"));
                    quranDetailModelArrayList.add(quranDetailModel);
                }
            }
            return quranDetailModelArrayList;
        } catch (Exception e) {
            Log.e("f", "" + e.toString());
        }
        return new ArrayList<QuranDetailModel>();
    }

    public ArrayList<QuranDetailModel> parseDetailsAppLanguage() {
        try {

//            String yourFilePath = path + "/" + "myfile.json";
//            File yourFile = new File( yourFilePath );
//            String jsongString = readFromFile();
//            JSONArray jarray = new JSONArray(str);
            JSONObject Arabic;
            if (!path.equals("")) {
                Arabic = new JSONObject(readFromFile());
            } else {
                Arabic = new JSONObject(loadJSONFromAsset("codebeautifyEN.json"));
            }
            JSONObject APP_LANGUAGE_JSON = Arabic.getJSONObject("quran");
            ArrayList<QuranDetailModel> quranDetailModelArrayList = new ArrayList<>();
            JSONArray jsonArray = APP_LANGUAGE_JSON.getJSONArray("sura");
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject suraObject = jsonArray.getJSONObject(i);
                String suraindex = suraObject.optString("_index");

                JSONArray ayaArray = suraObject.getJSONArray("aya");
                for (int p = 0; p < ayaArray.length(); p++) {
                    QuranDetailModel quranDetailModel = new QuranDetailModel();
                    quranDetailModel.setSura_index(suraObject.optString("_index"));
                    quranDetailModel.setSura_name(suraObject.optString("_name"));
                    JSONObject ayaObject = ayaArray.getJSONObject(p);
                    quranDetailModel.setMain_index(ayaObject.getString("_index"));
                    quranDetailModel.setAya_text(ayaObject.getString("_text"));
                    quranDetailModelArrayList.add(quranDetailModel);
                }
            }
            return quranDetailModelArrayList;
        } catch (Exception e) {
            Log.e("f", "" + e.toString());
        }
        return new ArrayList<QuranDetailModel>();
    }

    public void parseData() {
        try {
            JSONObject result = new JSONObject(loadJSONFromAsset("Quranjson.json"));
            JSONObject quranObject = result.getJSONObject("quran");
            JSONObject surasObject = quranObject.getJSONObject("suras");
            JSONArray arraySura = surasObject.getJSONArray("sura");
            suraArrayList.clear();
            for (int i = 0; i < arraySura.length(); i++) {
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

            JSONObject juzsObject = quranObject.getJSONObject("juzs");
            JSONArray juzArray = juzsObject.getJSONArray("juz");
            juzArrayList.clear();
            for (int p = 0; p < juzArray.length(); p++) {
                JSONObject juzObject = juzArray.getJSONObject(p);
                SuraModel suraModel2 = new SuraModel();
                suraModel2.set_index(juzObject.optString("_index"));
                suraModel2.set_tname("Juz " + "" + juzObject.optString("_index"));
                for (int l = 0; l < suraArrayList.size(); l++) {
                    if (suraArrayList.get(l).get_index().equals(juzObject.optString("_sura"))) {
                        suraModel2.set_ename(suraArrayList.get(l).get_tname() + " ( " + "" + juzObject.optString("_aya") + " )");
                        suraModel2.set_name(suraArrayList.get(l).get_name());
                        suraModel2.setAya_index(juzObject.optString("_aya"));
                        suraModel2.set_ayas(juzObject.optString("_sura"));
                        break;
                    }
                }
                juzArrayList.add(suraModel2);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rd_list:
                Bundle bundle = new Bundle();
                bundle.putParcelableArrayList("suralist", suraArrayList);
                bundle.putString("path", path);
                fragmentSura = new FragmentSura();
                fragmentSura.setArguments(bundle);
                setFragment(fragmentSura);
                break;

            case R.id.rd_map:
                Bundle bundle2 = new Bundle();
                bundle2.putParcelableArrayList("juzlist", juzArrayList);
                bundle2.putString("path", path);
                fragmentJuz = new FragmentJuzz();
                fragmentJuz.setArguments(bundle2);
                setFragment(fragmentJuz);
                break;

            case R.id.rd_myevent:
                setFragment(fragmentFavourites);
                break;

            case R.id.arrow_back:
                finish();
                break;
        }
    }

    private void filter(String text) {
        //new array list that will hold the filtered data
        filterdList.clear();
        if (text != null) {
            //looping through existing elements
            for (QuranDetailModel s : mainsearchedlist) {
                //if the existing elements contains the search input
                if (s.getAppLanugageName().toLowerCase().contains(text.toLowerCase())) {
                    //adding the element to filtered list
                    filterdList.add(s);
                }
            }
            //calling a method of the adapter class and passing the filtered list
            theQuranPagerAdapter.filterList(filterdList);
        }
    }

    private void setFragment(Fragment fragment) {

//        if (fragment instanceof EventList)
//            findViewById(R.id.fab_create_event).setVisibility(View.GONE);
//        else if (fragment instanceof EventMap)
//            findViewById(R.id.fab_create_event).setVisibility(View.GONE);
//        else
//            findViewById(R.id.fab_create_event).setVisibility(View.VISIBLE);

        getSupportFragmentManager()
                .beginTransaction()
                .replace(sub_container.getId(), fragment, "")
//                .addToBackStack(backStackStateName)
                .commit();
    }
}