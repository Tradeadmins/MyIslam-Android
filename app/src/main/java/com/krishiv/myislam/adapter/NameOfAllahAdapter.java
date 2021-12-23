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
import com.krishiv.myislam.dto.NameOfAllahModel;

import java.util.ArrayList;

public class NameOfAllahAdapter extends RecyclerView.Adapter<NameOfAllahAdapter.MyViewHolder> {
    private ArrayList<NameOfAllahModel> list;
    private Context mcontext;
    private ItemClickListener clickListener;

    public NameOfAllahAdapter(Context context, ArrayList<NameOfAllahModel> list) {
        this.mcontext = context;
        this.list = list;
    }

    @Override
    public NameOfAllahAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_name_of_allah, parent, false);
        return new NameOfAllahAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(NameOfAllahAdapter.MyViewHolder holder, int position) {
        NameOfAllahModel m = (NameOfAllahModel) list.get(position);
        holder.txt_name_arabic.setText(m.getText());
        holder.txt_name_english.setText(m.getComment());
        holder.txt_name_desc.setText(m.getUndefined());
        holder.txt_count.setText(m.getSection());

        if (m.isLike())
            holder.img_like.setImageDrawable(mcontext.getResources().getDrawable(R.drawable.rating_icon));
        else
            holder.img_like.setImageDrawable(mcontext.getResources().getDrawable(R.drawable.star));

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void setClickListener(ItemClickListener itemClickListener) {
        this.clickListener = itemClickListener;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView txt_name_arabic, txt_name_english, txt_name_desc, txt_count;
        ImageView img_like;

        public MyViewHolder(View view) {
            super(view);
            txt_name_arabic = (TextView) view.findViewById(R.id.txt_name_arabic);
            txt_name_english = (TextView) view.findViewById(R.id.txt_name_english);
            txt_name_desc = (TextView) view.findViewById(R.id.txt_name_desc);
            txt_count = (TextView) view.findViewById(R.id.txt_count);
            img_like = view.findViewById(R.id.img_like);
//            itemView.setOnClickListener(this);
            img_like.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (clickListener != null) {
                clickListener.itemClicked(view, getAdapterPosition(),0);
            }
        }
    }
}