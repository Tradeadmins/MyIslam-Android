package com.krishiv.myislam.adapter;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
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
import com.krishiv.myislam.dto.EventModel;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class EventListAdapter extends RecyclerView.Adapter<EventListAdapter.MyViewHolder> {

    String eventCategories[];
    private ArrayList<EventModel> list;
    private Context mcontext;
    private ItemClickListener clickListener;
    layoutItemClicked layoutItemClicked;
    EventModel m;

    public EventListAdapter(Context context, ArrayList<EventModel> list,layoutItemClicked layoutItemClicked) {
        this.mcontext = context;
        this.list = list;
        this.layoutItemClicked=layoutItemClicked;
        eventCategories = mcontext.getResources().getStringArray(R.array.event_category);
    }

    @Override
    public EventListAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.fg_event_list_item, parent, false);
        return new EventListAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(EventListAdapter.MyViewHolder holder, final int position) {
         m = (EventModel) list.get(position);
        holder.txt_event_name.setText(m.getMyEventName());
        holder.txt_event_category.setText(getCategory(list.get(position).getMyEventCategory()));

        holder.txt_event_address.setText(list.get(position).getAddress());
        holder.txt_event_distance.setText(new DecimalFormat("##.##").format(Double.parseDouble(m.getDistance())) + " km");

//        holder.img_event.setImageDrawable(mcontext.getResources());
        holder.img_event.setImageDrawable(mcontext.getResources().getDrawable(mcontext.getResources().getIdentifier("img_event_" + m.getMyEventCategory(), "drawable", mcontext.getPackageName())));
        holder.img_event.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            layoutItemClicked.onItemClick(position,list.get(position).getMyEventLatitude(),list.get(position).getMyEventLongitude(),list.get(position).getMyEventName(),list.get(position).getAddress());
            }
        });

    }

    private String getCategory(int categoryId) {
        return eventCategories[categoryId - 1];
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
    //new code
    public void setClickListener(ItemClickListener itemClickListener) {
        this.clickListener = itemClickListener;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView txt_event_category, txt_event_address, txt_event_distance, txt_event_name;
        ImageView img_event;
        LinearLayout layout;

        public MyViewHolder(View view) {
            super(view);
            txt_event_name = (TextView) view.findViewById(R.id.txt_event_name);
            txt_event_category = (TextView) view.findViewById(R.id.txt_event_category);
            txt_event_address = (TextView) view.findViewById(R.id.txt_event_address);
            txt_event_distance = (TextView) view.findViewById(R.id.txt_event_distance);
            layout = (LinearLayout) view.findViewById(R.id.layout);
            img_event = view.findViewById(R.id.img_event);
            img_event.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (clickListener != null) {
                clickListener.itemClicked(view, getAdapterPosition(), 0);
            }
        }
    }

    public  interface  layoutItemClicked{

        public void onItemClick(int position,String lat,String longitude,String eventname, String address);

    }


}