package com.krishiv.myislam.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.krishiv.myislam.R;
import com.krishiv.myislam.dto.QuranTranslationModel;

import java.util.ArrayList;

public class QuranTranslationAdapter extends BaseAdapter {

    Context context;
    ArrayList<QuranTranslationModel> data;
    LayoutInflater inflater;

    public QuranTranslationAdapter(Context context, ArrayList<QuranTranslationModel> data) {
        this.context = context;
        this.data = data;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int i) {
        return i;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {

        Holder holder;

        if (view == null) {

            holder = new Holder();
            view = inflater.inflate(R.layout.item_quran_translation, null);

            holder.txt_first_lang = view.findViewById(R.id.txt_first_lang);
            holder.txt_second_lang = view.findViewById(R.id.txt_second_lang);
            holder.img_language = view.findViewById(R.id.img_language);

            view.setTag(holder);
        }

        holder = (Holder) view.getTag();

        holder.txt_first_lang.setText(data.get(position).getFirstLanguage());
        holder.txt_second_lang.setText(data.get(position).getSecondLanguage());

        return view;
    }

    class Holder {
        TextView txt_first_lang, txt_second_lang;
        ImageView img_language;
    }
}
