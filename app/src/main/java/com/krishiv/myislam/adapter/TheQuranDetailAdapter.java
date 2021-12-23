package com.krishiv.myislam.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.krishiv.myislam.R;
import com.krishiv.myislam.activity.TheQuranDetailActivity;
import com.krishiv.myislam.dto.QuranDetailModel;
import java.util.ArrayList;

public class TheQuranDetailAdapter extends RecyclerView.Adapter<TheQuranDetailAdapter.MyViewHolder> {

    private ArrayList<QuranDetailModel> list;
    private Context mcontext;
    TheQuranDetailActivity.onSelectStar onSelectStar;
    public boolean isAbdullahAdded;

    public TheQuranDetailAdapter(Context mcontext, ArrayList<QuranDetailModel> list, TheQuranDetailActivity.onSelectStar onSelectStar, boolean isAbdullahAdded) {
        this.mcontext = mcontext;
        this.list = list;
        this.onSelectStar = onSelectStar;
        this.isAbdullahAdded = isAbdullahAdded;
    }

    @Override
    public TheQuranDetailAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView;
        itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_quran_detail, parent, false);
        return new TheQuranDetailAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final TheQuranDetailAdapter.MyViewHolder holder, final int position) {

        if (isAbdullahAdded) {
            if (position == 0) {
                holder.txt_ar.setText("بِسْمِ اللَّهِ الرَّحْمَنِ الرَّحِيمِ");
                holder.sedondary_layout.setVisibility(View.VISIBLE);
            } else {
                holder.sedondary_layout.setVisibility(View.GONE);
            }
        } else {
            holder.sedondary_layout.setVisibility(View.GONE);
        }

//        if (isAbdullahAdded) {
//            holder.txt_ar.setText(list.get(position).getAya_text());
//            holder.sedondary_layout.setVisibility(View.VISIBLE);
//            holder.main_layout.setVisibility(View.GONE);
//        } else {
//            holder.sedondary_layout.setVisibility(View.GONE);
//            holder.main_layout.setVisibility(View.VISIBLE);
//        }
        holder.txt_count.setText(list.get(position).getMain_index());
        holder.txt_arabic_name.setText(list.get(position).getAya_text());
        holder.txt_name_app.setText(list.get(position).getAppLanugageName());

        holder.img_like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (list.get(position).isFavourites()) {
                    onSelectStar.onSelect(position, false);
                } else {
                    onSelectStar.onSelect(position, true);
                }
            }
        });

        if (list.get(position).isFavourites()) {
            holder.img_like.setImageDrawable(mcontext.getResources().getDrawable(R.drawable.rate_star_button));
        } else {
            holder.img_like.setImageDrawable(mcontext.getResources().getDrawable(R.drawable.star));
        }

        Log.e("audio_url", "" + position + " " + list.get(position).getAudio_url());

        holder.img_play_pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSelectStar.onSongSelect(position, true);
            }
        });

        if (list.get(position).isPlaying()) {
            holder.relative_whole.setBackground(mcontext.getResources().getDrawable(R.color.semi_transparent));
        } else {
            holder.relative_whole.setBackground(mcontext.getResources().getDrawable(R.color.transparent));
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView txt_count, txt_arabic_name, txt_name_app;
        ImageView img_play_pause, img_like;
        TextView txt_ar;
        RelativeLayout sedondary_layout, relative_whole;

        public MyViewHolder(View view) {
            super(view);
            txt_ar = view.findViewById(R.id.txt_ar);
            txt_count = (TextView) view.findViewById(R.id.txt_count);
            txt_arabic_name = view.findViewById(R.id.txt_arabic_name);
            txt_name_app = view.findViewById(R.id.txt_name);
            img_play_pause = view.findViewById(R.id.img_play_pause);
            img_like = view.findViewById(R.id.img_like);
            sedondary_layout = view.findViewById(R.id.secondary_linear);
            relative_whole = view.findViewById(R.id.relative_whole);
        }
    }
}
