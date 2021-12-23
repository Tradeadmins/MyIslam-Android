package com.krishiv.myislam.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.krishiv.myislam.ItemClickListener;
import com.krishiv.myislam.R;
import com.krishiv.myislam.dto.RequestPrayerModel;

import java.util.ArrayList;

public class RequestPrayerAdapter extends RecyclerView.Adapter<RequestPrayerAdapter.MyViewHolder> {

    private ArrayList<RequestPrayerModel> list;
    private Context mcontext;
    private ItemClickListener clickListener;


    public RequestPrayerAdapter(Context context, ArrayList<RequestPrayerModel> list) {
        this.mcontext = context;
        this.list = list;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.fg_item_request_prayer, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        RequestPrayerModel m = (RequestPrayerModel) list.get(position);
        holder.txt.setText(m.getPrayerRequestText());
        holder.txt_duas.setText(m.getPrayerRequestTotalDuaCount() + " Duas");
        if (m.isPrayerRequestIsLiked()) {
            holder.lnr_isduamake.setBackground(mcontext.getResources().getDrawable(R.drawable.bg_btn_yellow));
        } else {
            holder.lnr_isduamake.setBackground(mcontext.getResources().getDrawable(R.drawable.bg_btn_border_black));
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void setClickListener(ItemClickListener itemClickListener) {
        this.clickListener = itemClickListener;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView txt, txt_duas;
        LinearLayout lnr_isduamake;

        public MyViewHolder(View view) {
            super(view);
            txt = (TextView) view.findViewById(R.id.txt);
            txt_duas = (TextView) view.findViewById(R.id.txt_duas);
            lnr_isduamake = view.findViewById(R.id.lnr_isduamake);
            //itemView.setOnClickListener(this);
            lnr_isduamake.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (clickListener != null) {
                clickListener.itemClicked(view, getAdapterPosition(),0);
            }
        }
    }
}
