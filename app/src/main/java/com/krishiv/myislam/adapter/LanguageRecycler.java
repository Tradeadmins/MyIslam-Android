package com.krishiv.myislam.adapter;

import android.content.Context;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.krishiv.myislam.R;
import com.krishiv.myislam.activity.ActivityAlarmConfig;
import com.krishiv.myislam.activity.TheQuranDetailActivity;
import com.krishiv.myislam.dto.LanguageTranslationModel;
import com.krishiv.myislam.dto.QuranDetailModel;
import com.krishiv.myislam.service.HttpHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class LanguageRecycler extends RecyclerView.Adapter<LanguageRecycler.MyViewHolder> {

    public ArrayList<LanguageTranslationModel> list;
    private Context mcontext;
    ActivityAlarmConfig.onLanguageSelectionListener onSelectListener;

    public LanguageRecycler(Context mcontext, ArrayList<LanguageTranslationModel> list, ActivityAlarmConfig.onLanguageSelectionListener onSelectListener) {
        this.mcontext = mcontext;
        this.list = list;
        this.onSelectListener = onSelectListener;
    }

    @Override
    public LanguageRecycler.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView;
        itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_quran_translation_recycle, parent, false);
        return new LanguageRecycler.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final LanguageRecycler.MyViewHolder holder, final int position) {

        holder.txt_language.setText(list.get(position).getQuranTranslateLanguage());
        holder.txt_language_by.setText(list.get(position).getQuranTranslateBy());

        holder.main_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSelectListener.onSelectLanguage(position);
            }
        });
        if (list.get(position).isLanguage_selection_status()) {
            holder.iv_check.setVisibility(View.VISIBLE);
        } else {
            holder.iv_check.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView txt_language, txt_language_by;
        ImageView iv_check;
        LinearLayout main_layout;

        public MyViewHolder(View view) {
            super(view);

            txt_language = (TextView) view.findViewById(R.id.txt_language);
            txt_language_by = view.findViewById(R.id.txt_language_by);
            iv_check = view.findViewById(R.id.iv_check);
            main_layout = view.findViewById(R.id.main_layout);

        }
    }

    private class GetContacts extends AsyncTask<Void, Void, Void> {

        String url;

        void GetContacts(String url) {
            this.url = url;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Toast.makeText(mcontext, "downloading", Toast.LENGTH_SHORT).show();
//            Toast.makeText(MainActivity.this,"Json Data is
//                    downloading",Toast.LENGTH_LONG).show();

        }

        @Override
        protected Void doInBackground(Void... arg0) {
            HttpHandler sh = new HttpHandler();
            // Making a request to url and getting response

            String jsonStr = sh.makeServiceCall(url);

            try {
                JSONObject jsonObj = new JSONObject(jsonStr);
                Log.e("", jsonObj.toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }

            Log.e("", "Response from url: " + jsonStr);
//            if (jsonStr != null) {
//                try {
//                    JSONObject jsonObj = new JSONObject(jsonStr);
//
//                    // Getting JSON Array node
//                    JSONArray contacts = jsonObj.getJSONArray("contacts");
//
//                    // looping through All Contacts
//                    for (int i = 0; i < contacts.length(); i++) {
//                        JSONObject c = contacts.getJSONObject(i);
//                        String id = c.getString("id");
//                        String name = c.getString("name");
//                        String email = c.getString("email");
//                        String address = c.getString("address");
//                        String gender = c.getString("gender");
//
//                        // Phone node is JSON Object
//                        JSONObject phone = c.getJSONObject("phone");
//                        String mobile = phone.getString("mobile");
//                        String home = phone.getString("home");
//                        String office = phone.getString("office");
//
//                        // tmp hash map for single contact
//
//                    }
//                } catch (final JSONException e) {
//                    Log.e("", "Json parsing error: " + e.getMessage());
//                    runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//                            Toast.makeText(getApplicationContext(),
//                                    "Json parsing error: " + e.getMessage(),
//                                    Toast.LENGTH_LONG).show();
//                        }
//                    });
//
//                }
//
//            } else {
//                Log.e("", "Couldn't get json from server.");
//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        Toast.makeText(getApplicationContext(),
//                                "Couldn't get json from server. Check LogCat for possible errors!",
//                                Toast.LENGTH_LONG).show();
//                    }
//                });
//            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
//            ListAdapter adapter = new SimpleAdapter(MainActivity.this, contactList,
//                    R.layout.list_item, new String[]{ "email","mobile"},
//                    new int[]{R.id.email, R.id.mobile});
//            lv.setAdapter(adapter);
        }

    }
}
