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
import com.krishiv.myislam.activity.Pilgrim;
import com.krishiv.myislam.dto.HajjGuideTaskModel;
import com.krishiv.myislam.dto.HajjTaskModel;
import com.krishiv.myislam.dto.UmrahGuideTaskModel;
import com.krishiv.myislam.dto.UmrahTaskModel;

import java.util.ArrayList;

public class PilgrimHajjUmrahAdapter extends RecyclerView.Adapter<PilgrimHajjUmrahAdapter.MyViewHolder> {
    private ArrayList<Object> list;
    private Context mcontext;
    private ItemClickListener clickListener;
    private Pilgrim.ActionPilgrim actionFor;

    public PilgrimHajjUmrahAdapter(Context context, ArrayList<Object> list, Pilgrim.ActionPilgrim actionFor) {
        this.mcontext = context;
        this.list = list;
        this.actionFor = actionFor;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_pilgrim_task, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        if (actionFor == Pilgrim.ActionPilgrim.Hajj) {
            HajjTaskModel m = (HajjTaskModel) list.get(position);
            holder.txt.setText(m.getHajjTaskName());

            if (m.isRecitation()) {
                holder.img_like.setVisibility(View.GONE);
                holder.txt_recitation_click.setVisibility(View.VISIBLE);
            }
            else {
                holder.img_like.setVisibility(View.VISIBLE);
                holder.txt_recitation_click.setVisibility(View.GONE);
            }

            if (m.isHajjTaskIsCompleted())
                holder.img_like.setImageDrawable(mcontext.getResources().getDrawable(R.drawable.ic_round_check));
            else
                holder.img_like.setImageDrawable(mcontext.getResources().getDrawable(R.drawable.ic_round_uncheck));
        }else if (actionFor == Pilgrim.ActionPilgrim.Umrah) {
            UmrahTaskModel m = (UmrahTaskModel) list.get(position);
            holder.txt.setText(m.getUmrahTaskName());

            if (m.isRecitation()) {
                holder.img_like.setVisibility(View.GONE);
                holder.txt_recitation_click.setVisibility(View.VISIBLE);
            }
            else {
                holder.img_like.setVisibility(View.VISIBLE);
                holder.txt_recitation_click.setVisibility(View.GONE);
            }

            if (m.isUmrahTaskIsCompleted())
                holder.img_like.setImageDrawable(mcontext.getResources().getDrawable(R.drawable.ic_round_check));
            else
                holder.img_like.setImageDrawable(mcontext.getResources().getDrawable(R.drawable.ic_round_uncheck));
        }else if (actionFor == Pilgrim.ActionPilgrim.HajjGuide) {
            HajjGuideTaskModel m = (HajjGuideTaskModel) list.get(position);
            holder.txt.setText(m.getHajjGuideName());
            if (m.isRecitation()) {
                holder.img_like.setVisibility(View.GONE);
                holder.txt_recitation_click.setVisibility(View.VISIBLE);
            }
            else {
                holder.img_like.setVisibility(View.VISIBLE);
                holder.txt_recitation_click.setVisibility(View.GONE);
            }

            if (m.isHajjGuideIsCompleted())
                holder.img_like.setImageDrawable(mcontext.getResources().getDrawable(R.drawable.ic_round_check));
            else
                holder.img_like.setImageDrawable(mcontext.getResources().getDrawable(R.drawable.ic_round_uncheck));
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
        TextView txt, txt_recitation_click;
        ImageView img_like;

        public MyViewHolder(View view) {
            super(view);
            txt = (TextView) view.findViewById(R.id.txt);
            txt_recitation_click = (TextView) view.findViewById(R.id.txt_recitation_click);
            img_like = view.findViewById(R.id.img_like);
            img_like.setOnClickListener(this);
            txt_recitation_click.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (clickListener != null) {
                if (view.getId() == img_like.getId()) {
                    clickListener.itemClicked(view, getAdapterPosition(), view.getId() == img_like.getId() ? 0 : 1);
                }else{
                    clickListener.itemClicked(view, getAdapterPosition(), 2);
                }
            }
        }
    }
}
