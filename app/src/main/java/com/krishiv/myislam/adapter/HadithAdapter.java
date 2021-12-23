package com.krishiv.myislam.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.krishiv.myislam.ItemClickListener;
import com.krishiv.myislam.R;
import com.krishiv.myislam.dto.BookListModel;
import com.krishiv.myislam.dto.HadithModel;

import java.util.ArrayList;

public class HadithAdapter extends RecyclerView.Adapter<HadithAdapter.MyViewHolder> {

    private ArrayList<HadithModel> list;
    private ArrayList<BookListModel> booklist;
    private Context mcontext;
    private ItemClickListener clickListener;
    layoutClicked layoutClicked;
    public HadithAdapter(Context context, ArrayList<BookListModel> list,layoutClicked layoutClicked) {
        this.mcontext = context;
        this.booklist = list;
        this.layoutClicked=layoutClicked;
    }


    @NonNull
    public void setCategoryList(ArrayList<BookListModel> planList) {
        this.booklist = planList;
        notifyDataSetChanged();
    }

    @Override
    public HadithAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.hadith_item_book, parent, false);
        return new HadithAdapter.MyViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(HadithAdapter.MyViewHolder holder, final int position) {
//        HadithModel m =  list.get(position);

        BookListModel obj=booklist.get(position);
        holder.txt_desc.setText(obj.getBookName());
        holder.arabic_name.setText(obj.getArabicName());
        holder.writerName.setText(obj.getWriterName());
        holder.noOfHadists.setText(obj.getHaditNumber());
        holder.mainLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 layoutClicked.onLayoutClicked(position,booklist.get(position).getBookName(),booklist.get(position).getBookId());
            }
        });


    }

    @Override
    public int getItemCount() {
        return booklist.size();
    }

    public void setClickListener(ItemClickListener itemClickListener) {
        this.clickListener = itemClickListener;
    }

    public interface layoutClicked {

        public void onLayoutClicked(int position, String bookname, int bookId);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView txt_desc,noOfHadists,writerName,arabic_name;

//        ImageView img_like;

        LinearLayout mainLayout;

        public MyViewHolder(View view) {
            super(view);
            txt_desc = (TextView) view.findViewById(R.id.txt_desc);
            arabic_name = (TextView) view.findViewById(R.id.txt_arabic);
            noOfHadists = (TextView) view.findViewById(R.id.no_of_hadidts);
            writerName = (TextView) view.findViewById(R.id.writer_name);
            mainLayout = (LinearLayout) view.findViewById(R.id.main_layout);


        }

        @Override
        public void onClick(View view) {
            if (clickListener != null) {
                clickListener.itemClicked(view, getAdapterPosition(), 0);
            }
        }
    }

}