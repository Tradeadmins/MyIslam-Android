package com.krishiv.myislam.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.krishiv.myislam.R;
import com.krishiv.myislam.activity.TheJuzDetail;
import com.krishiv.myislam.activity.TheQuranDetailActivity;
import com.krishiv.myislam.dto.SuraModel;
import com.krishiv.myislam.utils.Shared;

import java.util.ArrayList;

public class SuraAdapter extends RecyclerView.Adapter<SuraAdapter.MyViewHolder> {

    private ArrayList<SuraModel> list;
    private Context mcontext;
    Shared shared;

    public SuraAdapter(Context mcontext, ArrayList<SuraModel> list) {
        this.mcontext = mcontext;
        this.list = list;
        shared = new Shared(mcontext);
    }

    @Override
    public SuraAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_sura2, parent, false);
        return new SuraAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(SuraAdapter.MyViewHolder holder, final int position) {

        holder.txt_count.setText(list.get(position).get_index());
        holder.txt_name.setText(list.get(position).get_tname());
        holder.txt_arabic.setText(list.get(position).get_name());
        holder.txt_name_ar.setText(list.get(position).get_ename());

        if (position == 0) {
            if (!shared.getString("Last", "").equals("")) {
                holder.txt_name_ar2.setText(shared.getString("Last", ""));
                holder.txt_arabic2.setText(shared.getString("LastArabic", ""));
                holder.lastknown_linear.setVisibility(View.VISIBLE);
            } else {
                holder.lastknown_linear.setVisibility(View.GONE);
            }
        } else {
            holder.lastknown_linear.setVisibility(View.GONE);
        }

        holder.lastknown_linear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String suraOrJuz = shared.getString("LastSuraOrJuz", "");
                if (suraOrJuz.equals("Sura")){
                    Intent in = new Intent(mcontext , TheQuranDetailActivity.class);
                    in.putExtra("sura_name", list.get(position).get_tname());
                    in.putExtra("isSongPlaying", "false");
                    in.putExtra("arabic_name", list.get(position).get_name());
                    in.putExtra("sura_id",shared.getString("LastSuraId", ""));
                    mcontext.startActivity(in);
                }else {
                    Intent in = new Intent(mcontext , TheJuzDetail.class);
                    in.putExtra("sura_name", list.get(position).get_tname());
                    in.putExtra("isSongPlaying", "false");
                    in.putExtra("aya_index",shared.getString("aya_ind", ""));
                    in.putExtra("arabic_name", list.get(position).get_name());
                    in.putExtra("sura_id",shared.getString("LastSuraId", ""));
                    mcontext.startActivity(in);
                }
            }
        });

        holder.main_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mcontext, TheQuranDetailActivity.class);
                intent.putExtra("sura_id", list.get(position).get_index());
                intent.putExtra("isSongPlaying", "false");
                intent.putExtra("sura_name", list.get(position).get_tname());
                intent.putExtra("arabic_name", list.get(position).get_name());
                intent.putParcelableArrayListExtra("suraList", list);
                if (Integer.parseInt(list.get(position).get_index()) == (list.size() - 1)) {
                    intent.putExtra("nextIndex", 0);
                    intent.putExtra("isLast", "YES");
                } else {
                    intent.putExtra("nextIndex", (position + 1));
                    intent.putExtra("isLast", "NO");
                }
                intent.putParcelableArrayListExtra("suraList", list);
                mcontext.startActivity(intent);

            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView txt_count, txt_name, txt_name_ar, txt_arabic, txt_name_ar2 , txt_arabic2;
        ImageView img_play_pause, img_like;
        LinearLayout main_layout, lastknown_linear;

        public MyViewHolder(View view) {
            super(view);
            txt_count = (TextView) view.findViewById(R.id.txt_count);
            txt_name = (TextView) view.findViewById(R.id.txt_name);
            txt_name_ar = view.findViewById(R.id.txt_name_ar);
            txt_arabic = view.findViewById(R.id.txt_arabic);
            txt_name_ar2 = view.findViewById(R.id.txt_name_ar2);
            lastknown_linear = view.findViewById(R.id.lastknown_linear);
            txt_arabic2 = view.findViewById(R.id.txt_arabic2);
            img_play_pause = view.findViewById(R.id.img_play_pause);
            main_layout = view.findViewById(R.id.main_layout);
            img_like = view.findViewById(R.id.img_like);
        }
    }


}