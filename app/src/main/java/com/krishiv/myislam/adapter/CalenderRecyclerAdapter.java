package com.krishiv.myislam.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.krishiv.myislam.ItemClickListener;
import com.krishiv.myislam.R;
import com.krishiv.myislam.dto.CustomDuaModel;

import java.util.ArrayList;

public class CalenderRecyclerAdapter extends RecyclerView.Adapter<CalenderRecyclerAdapter.MyViewHolder> {

    private ArrayList<Integer> list;
    private Context mcontext;
    private ItemClickListener clickListener;

    public CalenderRecyclerAdapter(Context context, ArrayList<Integer> list) {
        this.mcontext = context;
        this.list = list;
    }

    @Override
    public CalenderRecyclerAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.fasting_calender_item, parent, false);
        return new CalenderRecyclerAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(CalenderRecyclerAdapter.MyViewHolder holder, int position) {
        int m=list.get(position);
        holder.date.setText(String.valueOf(m));

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void setClickListener(ItemClickListener itemClickListener) {
        this.clickListener = itemClickListener;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView date;

        public MyViewHolder(View view) {
            super(view);
            date = view.findViewById(R.id.date_calender);

        }

        @Override
        public void onClick(View view) {
            if (clickListener != null) {
                switch (view.getId()){
                    case R.id.date_calender:
                        break;

                }
            }
        }
    }
}