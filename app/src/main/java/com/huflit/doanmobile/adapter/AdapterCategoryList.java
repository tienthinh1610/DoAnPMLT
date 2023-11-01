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

public class AdapterCategoryList extends RecyclerView.Adapter<AdapterCategoryList.ViewHolder> {
    private ArrayList<Book> mbookcatagory;
    private Context mContext;
    private AdapterCategoryList.OnItemClickListener mListener;
    public AdapterCategoryList(Context context, ArrayList<Book> bookList) {
        mContext = context;
        mbookcatagory = bookList;
    }
    public void setOnItemClickListener(AdapterCategoryList.OnItemClickListener listener) {
        mListener = listener;
    }
    public interface OnItemClickListener {
        void onItemClick(Book book);
    }
    @Override
    public AdapterCategoryList.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(mContext);
        View view = layoutInflater.inflate(R.layout.layout_item_categorylist, parent, false);

        return new AdapterCategoryList.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(AdapterCategoryList.ViewHolder holder, int position) {
        Book book = mbookcatagory.get(position);
        holder.namebookcategory.setText(book.getName());
        holder.pricebookcategory.setText(String.format("%,d", book.getPrice()) + " VNĐ");

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
        public TextView namebookcategory;
        public TextView pricebookcategory;

        public ViewHolder(View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.imageViewcl);
            pricebookcategory = itemView.findViewById(R.id.tv_pricecl);
            namebookcategory = itemView.findViewById(R.id.tv_namecl);

        }

    }
}
