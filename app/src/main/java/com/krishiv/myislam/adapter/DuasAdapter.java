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
import com.krishiv.myislam.dto.DuasModel;
import com.krishiv.myislam.dto.NameOfAllahModel;

import java.util.ArrayList;

public class DuasAdapter extends RecyclerView.Adapter<DuasAdapter.MyViewHolder> {

    private ArrayList<DuasModel> list;
    private Context mcontext;
    private ItemClickListener clickListener;

    public DuasAdapter(Context context, ArrayList<DuasModel> list) {
        this.mcontext = context;
        this.list = list;
    }

    @Override
    public DuasAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_dua, parent, false);
        return new DuasAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(DuasAdapter.MyViewHolder holder, int position) {
        DuasModel m = (DuasModel) list.get(position);
        holder.txt_title.setText(m.getDuaName());
        holder.txt_desc.setText(m.getDuaEnglishText() + "\n\n" + m.getDuaPronunciationText()); //lng_depended

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
        for (int i = 0; i <list.size() ; i++) {
            if(list.get(i).isExpanded())
            {

            }

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
        ImageView img_like, img_expand;

        public MyViewHolder(View view) {
            super(view);
            txt_title = (TextView) view.findViewById(R.id.txt_title);
            txt_desc = (TextView) view.findViewById(R.id.txt_desc);
            img_like = view.findViewById(R.id.img_like);
            img_expand = view.findViewById(R.id.img_expand);
//            itemView.setOnClickListener(this);
            img_like.setOnClickListener(this);
            img_expand.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (clickListener != null) {
                clickListener.itemClicked(view, getAdapterPosition(),view.getId()==img_like.getId()?0:1);
            }
        }
    }
}