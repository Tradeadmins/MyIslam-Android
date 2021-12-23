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

public class CustomDuasAdapter extends RecyclerView.Adapter<CustomDuasAdapter.MyViewHolder> {

    private ArrayList<CustomDuaModel> list;
    private Context mcontext;
    private ItemClickListener clickListener;

    public CustomDuasAdapter(Context context, ArrayList<CustomDuaModel> list) {
        this.mcontext = context;
        this.list = list;
    }

    @Override
    public CustomDuasAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_custom_dua, parent, false);
        return new CustomDuasAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(CustomDuasAdapter.MyViewHolder holder, int position) {
        CustomDuaModel m = (CustomDuaModel) list.get(position);
        holder.txt_title.setText(m.getCustomDuaName());
        holder.txt_desc.setText(m.getCustomDuaText());

        if (m.isLike())
            holder.img_like.setImageDrawable(mcontext.getResources().getDrawable(R.drawable.rating_icon));
        else
            holder.img_like.setImageDrawable(mcontext.getResources().getDrawable(R.drawable.star));

        if (m.isExpanded()) {
            holder.txt_desc.setVisibility(View.VISIBLE);
            holder.img_expand.setRotation(0);
        }
        else{
            holder.txt_desc.setVisibility(View.GONE);
            holder.img_expand.setRotation(180);
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
        TextView txt_title, txt_desc;
        ImageView img_like, img_edt, img_delete, img_expand;

        public MyViewHolder(View view) {
            super(view);
            txt_title = (TextView) view.findViewById(R.id.txt_title);
            txt_desc = (TextView) view.findViewById(R.id.txt_desc);
            img_like = view.findViewById(R.id.img_like);
            img_edt = view.findViewById(R.id.img_edt);
            img_delete = view.findViewById(R.id.img_delete);
            img_expand = view.findViewById(R.id.img_expand);
//            itemView.setOnClickListener(this);
            img_like.setOnClickListener(this);
            img_like.setVisibility(View.GONE);

            img_edt.setOnClickListener(this);
            img_delete.setOnClickListener(this);
            img_expand.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (clickListener != null) {
                switch (view.getId()){
                    case R.id.img_like:
                        break;
                    case R.id.img_edt:
                        clickListener.itemClicked(view, getAdapterPosition(), 1);
                        break;
                    case R.id.img_delete:
                        clickListener.itemClicked(view, getAdapterPosition(), 2);
                        break;
                    case R.id.img_expand:
                        clickListener.itemClicked(view, getAdapterPosition(), 3);
                        break;
                }
            }
        }
    }
}