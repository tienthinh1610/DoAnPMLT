package com.huflit.doanmobile.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.huflit.doanmobile.R;
import com.huflit.doanmobile.classs.Book;

import java.util.ArrayList;

public class AdapterRcvCategory extends RecyclerView.Adapter<AdapterRcvCategory.ViewHolder> {
    private ArrayList<Book> mbookcatagory;
    private Context mContext;
    private AdapterRcvCategory.OnItemClickListener mListener;
    public AdapterRcvCategory(Context context, ArrayList<Book> bookList) {
        mContext = context;
        mbookcatagory = bookList;
    }
    public void setOnItemClickListener(AdapterRcvCategory.OnItemClickListener listener) {
        mListener = listener;
    }
    public interface OnItemClickListener {
        void onItemClick(Book book);
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(mContext);
        View view = layoutInflater.inflate(R.layout.layout_item_listbook, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Book book = mbookcatagory.get(position);
        holder.namebook.setText(book.getName());
        holder.pricebook.setText(String.format("%,d", book.getPrice()) + " VNĐ");

        // Load image from assets folder using Glide library
        Glide.with(mContext)
                .load("file:///android_asset/" + book.getImage1())
                .into(holder.imageView);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mListener != null) {
                    mListener.onItemClick(book);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mbookcatagory.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public ImageView imageView;
        public TextView namebook;
        public TextView pricebook;

        public ViewHolder(View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.img_book);
            namebook = itemView.findViewById(R.id.tv_namebook);
            pricebook = itemView.findViewById(R.id.tv_pricebook);

        }

    }
}
