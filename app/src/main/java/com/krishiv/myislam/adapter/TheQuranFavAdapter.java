package com.krishiv.myislam.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.krishiv.myislam.ItemClickListener;
import com.krishiv.myislam.R;
import com.krishiv.myislam.dto.QuranDetailModel;
import com.krishiv.myislam.fragment.FragmentFavourites;

import java.util.ArrayList;

public class TheQuranFavAdapter extends RecyclerView.Adapter<TheQuranFavAdapter.MyViewHolder> {

    private ArrayList<QuranDetailModel> list;
    private Context mcontext;
    FragmentFavourites.onSelectFaav onSelectFaav;

    public TheQuranFavAdapter(Context mcontext, ArrayList<QuranDetailModel> list, FragmentFavourites.onSelectFaav onSelectFaav) {
        this.mcontext = mcontext;
        this.list = list;
        this.onSelectFaav=onSelectFaav;
    }

    @Override
    public TheQuranFavAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_quran_detail_fav, parent, false);
        return new TheQuranFavAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(TheQuranFavAdapter.MyViewHolder holder, final int position) {

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
                onSelectFaav.onSelect(position);
            }
        });
        holder.img_play_pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSelectFaav.onSelectSong(position);
            }
        });
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

    public class MyViewHolder extends RecyclerView.ViewHolder  {
        TextView txt_count , txt_arabic_name , txt_name_app;
        ImageView img_play_pause, img_like;
        RelativeLayout sedondary_layout , relative_whole;
        public MyViewHolder(View view) {
            super(view);
            txt_count = (TextView) view.findViewById(R.id.txt_count);
            txt_arabic_name = view.findViewById(R.id.txt_arabic_name);
            txt_name_app = view.findViewById(R.id.txt_name);

            img_play_pause = view.findViewById(R.id.img_play_pause);
            img_like = view.findViewById(R.id.img_like);
            sedondary_layout = view.findViewById(R.id.secondary_linear);
            relative_whole= view.findViewById(R.id.relative_whole);
        }
    }

}
