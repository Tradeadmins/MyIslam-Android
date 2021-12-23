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
import com.krishiv.myislam.dto.GoogleApiModel;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class QurbaniAdapter extends RecyclerView.Adapter<QurbaniAdapter.MyViewHolder> {

    layoutItemClicked layoutItemClicked;
    GoogleApiModel m;
    private ArrayList<GoogleApiModel> list;
    private Context mcontext;
    private ItemClickListener clickListener;

    //new code
    public QurbaniAdapter(Context context, ArrayList<GoogleApiModel> list, layoutItemClicked layoutItemClicked) {
        this.mcontext = context;
        this.layoutItemClicked = layoutItemClicked;
        this.list = list;
    }

    @Override
    public QurbaniAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.fg_item_charity_name, parent, false);
        return new QurbaniAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(QurbaniAdapter.MyViewHolder holder, final int position) {
        m = (GoogleApiModel) list.get(position);
        holder.txt_name.setText(list.get(position).getName());
        holder.txt_address.setText(list.get(position).getFormatted_address());
        holder.txt_distance.setText(new DecimalFormat("##.##").format(m.getDistanceFar()) + " km");
        if (m.getFormatted_address().contentEquals("00000")) {
            holder.main_layout.setBackground(mcontext.getResources().getDrawable(R.drawable.bg_btn_yellow));
//            holder.txt_name.setTextColor(mcontext.getResources().getColor(R.color.colorBlack));
        } else {
            holder.main_layout.setBackground(mcontext.getResources().getDrawable(R.drawable.bg_transparent_black));
        }
        holder.main_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layoutItemClicked.onItemClick(position, list.get(position).getLat(), list.get(position).getLng(), list.get(position).getName(), list.get(position).getFormatted_address());
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void setClickListener(ItemClickListener itemClickListener) {
        this.clickListener = itemClickListener;
    }

    public interface layoutItemClicked {
        public void onItemClick(int position, Double lat, Double longitude, String eventname, String address);

    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView txt_name, txt_address, txt_distance;
        LinearLayout main_layout;

        public MyViewHolder(View view) {
            super(view);
            main_layout = (LinearLayout) view.findViewById(R.id.main_layout);
            txt_name = (TextView) view.findViewById(R.id.txt_name);
            txt_address = (TextView) view.findViewById(R.id.txt_address);
            txt_distance = (TextView) view.findViewById(R.id.txt_distance);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (clickListener != null) {
                clickListener.itemClicked(view, getAdapterPosition(), 0);
            }
        }
    }
}