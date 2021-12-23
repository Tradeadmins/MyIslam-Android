package com.krishiv.myislam.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.krishiv.myislam.R;
import com.krishiv.myislam.dto.InstructionModel;

import java.util.ArrayList;

public class InstructionAdapter extends PagerAdapter{

    Context context;
    ArrayList<InstructionModel> data;

    public InstructionAdapter(Context context, ArrayList<InstructionModel> data) {
        this.context = context;
        this.data = data;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
        return view == o;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        LinearLayout linearLayout = (LinearLayout) LayoutInflater.from(container.getContext()).inflate(R.layout.item_instructions, null);
        TextView textView = (TextView) linearLayout.findViewById(R.id.txt_desc);
        textView.setText(data.get(position).getInstructionDescription());
        container.addView(linearLayout);
        return linearLayout;
    }


    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        LinearLayout view = (LinearLayout) object;
        container.removeView(view);
    }

    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }
}
