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
import com.krishiv.myislam.dto.GoogleApiModel;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class MosqueListAdapter extends RecyclerView.Adapter<MosqueListAdapter.MyViewHolder> {

    layoutItemClicked layoutItemClicked;
    GoogleApiModel m;
    private ArrayList<GoogleApiModel> list;
    private Context mcontext;
    private ItemClickListener clickListener;

    public MosqueListAdapter(Context context, ArrayList<GoogleApiModel> list, layoutItemClicked layoutItemClicked) {
        this.mcontext = context;
        this.list = list;
        this.layoutItemClicked = layoutItemClicked;
    }

    @Override
    public MosqueListAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.fg_mosque_list_item, parent, false);
        return new MosqueListAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MosqueListAdapter.MyViewHolder holder, final int position) {
        m = (GoogleApiModel) list.get(position);
        holder.txt_name.setText(list.get(position).getName());
        holder.txt_address.setText(list.get(position).getFormatted_address());
        holder.txt_distance.setText(new DecimalFormat("##.##").format(m.getDistanceFar()) + " km");
        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("txt_address::::"+list.get(position).getFormatted_address());

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
    //new code
    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView txt_name, txt_address, txt_distance;
        LinearLayout layout;
        ImageView img;

        public MyViewHolder(View view) {
            super(view);
            txt_name = (TextView) view.findViewById(R.id.txt_name);
            txt_address = (TextView) view.findViewById(R.id.txt_address);
            txt_distance = (TextView) view.findViewById(R.id.txt_distance);
            layout = (LinearLayout) view.findViewById(R.id.layout);
//            img = view.findViewById(R.id.img);
            //itemView.setOnClickListener(this);
//            img.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (clickListener != null) {
                clickListener.itemClicked(view, getAdapterPosition(), 0);
            }
        }
    }

}