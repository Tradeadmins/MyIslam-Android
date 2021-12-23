package com.krishiv.myislam.adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.media.MediaPlayer;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.krishiv.myislam.R;
import com.krishiv.myislam.activity.TheJuzDetail;
import com.krishiv.myislam.activity.TheQuranDetailActivity;
import com.krishiv.myislam.dto.QuranDetailModel;

import java.util.ArrayList;

public class JazDetailAdapter extends RecyclerView.Adapter<JazDetailAdapter.MyViewHolder> {

    private ArrayList<QuranDetailModel> list;
    private Context mcontext;
    TheJuzDetail.onSelectStar onSelectStar;
    boolean isBismillahAdded;

    public JazDetailAdapter(Context mcontext, ArrayList<QuranDetailModel> list, TheJuzDetail.onSelectStar onSelectStar, boolean isBismillahAdded) {
        this.mcontext = mcontext;
        this.list = list;
        this.onSelectStar = onSelectStar;
        this.isBismillahAdded = isBismillahAdded;
    }

    @Override
    public JazDetailAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_quran_detail, parent, false);
        return new JazDetailAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final JazDetailAdapter.MyViewHolder holder, final int position) {
        if (isBismillahAdded){
            if (position==0){
                holder.txt_ar.setText("بِسْمِ اللَّهِ الرَّحْمَنِ الرَّحِيمِ");
                holder.sedondary_layout.setVisibility(View.VISIBLE);
            }else {
                holder.sedondary_layout.setVisibility(View.GONE);
            }
        }else {
            holder.sedondary_layout.setVisibility(View.GONE);
        }
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
        holder.img_play_pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSelectStar.onSongSelect(position,true);
            }
        });

        final MediaPlayer mp = new MediaPlayer();

        if (list.get(position).isFavourites()) {
            holder.img_like.setImageDrawable(mcontext.getResources().getDrawable(R.drawable.rate_star_button));
        } else {
            holder.img_like.setImageDrawable(mcontext.getResources().getDrawable(R.drawable.unselect_fav));
        }
        if (list.get(position).isPlaying()){
            holder.relative_whole.setBackground(mcontext.getResources().getDrawable(R.color.semi_transparent));
        }else {
            holder.relative_whole.setBackground(mcontext.getResources().getDrawable(R.color.transparent));
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView txt_count, txt_arabic_name, txt_name_app , txt_ar;
        ImageView img_play_pause, img_like;
        RelativeLayout sedondary_layout , relative_whole;

        public MyViewHolder(View view) {
            super(view);
            txt_count = (TextView) view.findViewById(R.id.txt_count);
            txt_arabic_name = view.findViewById(R.id.txt_arabic_name);
            txt_name_app = view.findViewById(R.id.txt_name);
            txt_ar = view.findViewById(R.id.txt_ar);
            img_play_pause = view.findViewById(R.id.img_play_pause);
            img_like = view.findViewById(R.id.img_like);
            sedondary_layout = view.findViewById(R.id.secondary_linear);
            relative_whole= view.findViewById(R.id.relative_whole);

        }

    }

}
