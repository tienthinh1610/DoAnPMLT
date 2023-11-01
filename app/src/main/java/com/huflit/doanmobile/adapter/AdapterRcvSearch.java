package com.huflit.doanmobile.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.huflit.doanmobile.R;
import com.huflit.doanmobile.classs.Book;

import java.util.ArrayList;
import java.util.List;

public class AdapterRcvSearch extends RecyclerView.Adapter<AdapterRcvSearch.ViewHolder> implements Filterable {
    private Context context;
    private List<Book> bookList;
    private OnItemClickListener listener;

    public AdapterRcvSearch(List<Book> bookList, Context context) {
        this.bookList = bookList;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_item_rcvsearch, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Book book = bookList.get(position);

        holder.tvName.setText(book.getName());
        holder.tvPrice.setText(String.format("%,dđ", book.getPrice()));
        Glide.with(context)
                .load("file:///android_asset/" + book.getImage1())
                .into(holder.imageView);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onItemClick(book);
                }
            }
        });
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String searchText = charSequence.toString().toLowerCase();
                List<Book> filteredList = new ArrayList<>();
                if (searchText.isEmpty()) {
                    filteredList.addAll(bookList);
                } else {
                    for (Book book : bookList) {
                        if (book.getName().toLowerCase().contains(searchText)) {
                            filteredList.add(book);
                        }
                    }
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = filteredList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                bookList.clear();
                bookList.addAll((List<Book>) filterResults.values);
                notifyDataSetChanged();
            }
        };
    }
    public void setData(List<Book> filteredList) {
        this.bookList = filteredList;
        notifyDataSetChanged();
    }
    @Override
    public int getItemCount() {
        return bookList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;
        TextView tvName;
        TextView tvPrice;

        public ViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.img_itembooksearch);
            tvName = itemView.findViewById(R.id.tv_namebooksearch);
            tvPrice = itemView.findViewById(R.id.tv_pricebooksearch);
        }
    }
    public interface OnItemClickListener {
        void onItemClick(Book book);
    }
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }
}
