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
import com.krishiv.myislam.dto.EventModel;
import com.krishiv.myislam.utils.TimingUtils;

import java.util.ArrayList;

public class EventMyAdapter extends RecyclerView.Adapter<EventMyAdapter.MyViewHolder> {

    private ArrayList<EventModel> list;
    private Context mcontext;
    private ItemClickListener clickListener;
    String eventCategories[];

    public EventMyAdapter(Context context, ArrayList<EventModel> list) {
        this.mcontext = context;
        this.list = list;
        eventCategories = mcontext.getResources().getStringArray(R.array.event_category);
    }

    @Override
    public EventMyAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.fg_event_mylist_item, parent, false);
        return new EventMyAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(EventMyAdapter.MyViewHolder holder, int position) {
        EventModel m = (EventModel) list.get(position);
        holder.txt.setText(
                mcontext.getString(R.string.edt_hint_event_name) + ": "+ m.getMyEventName() +"\n"+
                mcontext.getString(R.string.edt_hint_event_type) + ": "+ getCategory(m.getMyEventCategory()) +"\n"+
                mcontext.getString(R.string.edt_hint_address) + ": "+ m.getAddress() +"\n"+
                mcontext.getString(R.string.txt_event_start_time) + ": "+ getTime(m.getMyEventStartDate()) +"\n"+
                mcontext.getString(R.string.txt_event_end_time) + ": "+ getTime(m.getMyEventEndDate()) +"\n"+
                mcontext.getString(R.string.edt_hint_description) + ": "+ m.getDescription() +"\n"+
                mcontext.getString(R.string.txt_event_date) + ": "+ getDate(m.getMyEventEndDate())
        );
    }

    private String getDate(String myEventDate) {
       return TimingUtils.getStringTimeBasedOnFormat(TimingUtils.ServerTimeFormat, TimingUtils.DisplayDateSlashFormat, myEventDate);
    }

    private String getTime(String myEventDate) {
        return TimingUtils.getStringTimeBasedOnFormat(TimingUtils.ServerTimeFormat, TimingUtils.HourMinutesAMPMTimeFormat, myEventDate);
    }

    private String getCategory(int categoryId) {
        return eventCategories[categoryId-1];
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void setClickListener(ItemClickListener itemClickListener) {
        this.clickListener = itemClickListener;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView txt, txt_edit, txt_delete;

        public MyViewHolder(View view) {
            super(view);
            txt = (TextView) view.findViewById(R.id.txt);
            txt_edit = (TextView) view.findViewById(R.id.txt_edit);
            txt_delete = (TextView) view.findViewById(R.id.txt_delete);
            //itemView.setOnClickListener(this);
            txt_edit.setOnClickListener(this);
            txt_delete.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (clickListener != null) {
                if (view.getId() == R.id.txt_edit)
                    clickListener.itemClicked(view, getAdapterPosition(),1);
                else if (view.getId() == R.id.txt_delete)
                    clickListener.itemClicked(view, getAdapterPosition(),2);
            }
        }
    }
}