package com.krishiv.myislam.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.krishiv.myislam.R;
import com.krishiv.myislam.activity.TheJuzDetail;
import com.krishiv.myislam.activity.TheQuranDetailActivity;
import com.krishiv.myislam.dto.SuraModel;
import java.util.ArrayList;

public class JuzAdapter extends RecyclerView.Adapter<JuzAdapter.MyViewHolder> {

    private ArrayList<SuraModel> list;
    private Context mcontext;
    String path;

    public JuzAdapter(Context context, ArrayList<SuraModel> list,String path) {
        this.mcontext = context;
        this.list = list;
        this.path=path;
    }

    @Override
    public JuzAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_sura, parent, false);
        return new JuzAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(JuzAdapter.MyViewHolder holder, final int position) {

        holder.txt_count.setText(list.get(position).get_index());
        holder.txt_name.setText(list.get(position).get_tname());
        holder.txt_arabic.setText(list.get(position).get_name());
        holder.txt_name_ar.setText(list.get(position).get_ename());
        holder.main_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(mcontext, TheJuzDetail.class);
                in.putExtra("sura_id",list.get(position).get_ayas());
                in.putExtra("aya_index",list.get(position).getAya_index());
                in.putExtra("path",path);
                in.putExtra("isSongPlaying","false");
                in.putParcelableArrayListExtra("suraList",list);
                String suraname = list.get(position).get_ename().replaceAll("[0-9]", "");
                String suraname2 = suraname.replace("(","").replace(")","");
                in.putExtra("sura_name",suraname2);
                in.putExtra("arabic_name", list.get(position).get_name());
                if (Integer.parseInt(list.get(position).get_index())==(list.size()-1)){
                    in.putExtra("nextIndex",0);
                    in.putExtra("isLast","YES");
                    // to do
                }else {
                    in.putExtra("nextIndex",(position+1));
                    in.putExtra("isLast","NO");
                }
                in.putParcelableArrayListExtra("suraList",list);
                mcontext.startActivity(in);


//                        Intent intent = new Intent(mcontext, TheQuranDetailActivity.class);
//                        intent.putExtra("sura_id",list.get(position).get_index());
//                        intent.putExtra("isSongPlaying","false");
//                        intent.putParcelableArrayListExtra("suraList",list);
//                        if (Integer.parseInt(list.get(position).get_index())==(list.size()-1)){
//                            intent.putExtra("nextIndex",0);
//                            intent.putExtra("isLast","YES");
//                            // to do
//                        }else {
//                            intent.putExtra("nextIndex",(position+1));
//                            intent.putExtra("isLast","NO");
//                        }
//                        intent.putParcelableArrayListExtra("suraList",list);
//                        mcontext.startActivity(intent);



            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder  {
        TextView txt_count, txt_name , txt_name_ar , txt_arabic;
        ImageView img_play_pause, img_like;
        LinearLayout main_layout;

        public MyViewHolder(View view) {
            super(view);
            txt_count = (TextView) view.findViewById(R.id.txt_count);
            txt_name = (TextView) view.findViewById(R.id.txt_name);
            txt_name_ar = view.findViewById(R.id.txt_name_ar);
            txt_arabic = view.findViewById(R.id.txt_arabic);
            main_layout = view.findViewById(R.id.main_layout);
        }
    }
}