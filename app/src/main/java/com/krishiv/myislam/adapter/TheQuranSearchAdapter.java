package com.krishiv.myislam.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.krishiv.myislam.ItemClickListener;
import com.krishiv.myislam.R;
import com.krishiv.myislam.activity.ActivityThequran;
import com.krishiv.myislam.dto.QuranDetailModel;

import java.util.ArrayList;

public class TheQuranSearchAdapter extends RecyclerView.Adapter<TheQuranSearchAdapter.MyViewHolder> {

    private ArrayList<QuranDetailModel> list;
    private Context mcontext;
    ActivityThequran.onSelectFavOnSearchAdapter onSelectFavOnSearchAdapter;

    public TheQuranSearchAdapter(Context mcontext, ArrayList<QuranDetailModel> list, ActivityThequran.onSelectFavOnSearchAdapter onSelectFavOnSearchAdapter) {
        this.mcontext = mcontext;
        this.list = list;
        this.onSelectFavOnSearchAdapter=onSelectFavOnSearchAdapter;
    }

    @Override
    public TheQuranSearchAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_quran_detail, parent, false);
        return new TheQuranSearchAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(TheQuranSearchAdapter.MyViewHolder holder, final int position) {

        holder.txt_count.setText(list.get(position).getMain_index());
        holder.txt_arabic_name.setText(list.get(position).getAya_text());
        holder.txt_name_app.setText(list.get(position).getAppLanugageName());

        if (list.get(position).isFavourites()){
            holder.img_like.setImageDrawable(mcontext.getResources().getDrawable(R.drawable.rate_star_button));
        }else {
            holder.img_like.setImageDrawable(mcontext.getResources().getDrawable(R.drawable.unselect_fav));
        }

        holder.img_like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (list.get(position).isFavourites()){
                    onSelectFavOnSearchAdapter.onSelect(position,false);
                }else {
                    onSelectFavOnSearchAdapter.onSelect(position,true);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView txt_count , txt_arabic_name , txt_name_app;
        ImageView img_play_pause, img_like;

        public MyViewHolder(View view) {
            super(view);
            txt_count = (TextView) view.findViewById(R.id.txt_count);
            txt_arabic_name = view.findViewById(R.id.txt_arabic_name);
            txt_name_app = view.findViewById(R.id.txt_name);

            img_play_pause = view.findViewById(R.id.img_play_pause);
            img_like = view.findViewById(R.id.img_like);

        }


    }

    public void filterList(ArrayList<QuranDetailModel> filterdNames) {
        this.list = filterdNames;
        notifyDataSetChanged();
    }
}
