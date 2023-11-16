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
import com.huflit.doanmobile.SqlHelper.Mydatabase;
import com.huflit.doanmobile.classs.Book;
import com.huflit.doanmobile.classs.OrderDetail;

import java.util.List;

public class AdapterOrderDetail extends RecyclerView.Adapter<AdapterOrderDetail.ViewHolder> {
    private Context context;
    private List<OrderDetail> orderDetailList;
    private Mydatabase mydb;

    public AdapterOrderDetail(Context context, List<OrderDetail> orderDetailList, Mydatabase mydb) {
        this.context = context;
        this.orderDetailList = orderDetailList;
        this.mydb = mydb;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_item_rcvdetailorder, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        OrderDetail orderDetail = orderDetailList.get(position);
        Book book = mydb.getBookById(orderDetail.getBookId());

        holder.tvOrderDetailName.setText(book.getName());
        holder.tvOrderDetailQuantity.setText("x" + orderDetail.getQuantity());
        holder.tvOrderDetailPrice.setText(String.format("%,d", book.getPrice())+ " VNĐ");

        Glide.with(context)
                .load("file:///android_asset/" + book.getImage1())
                .into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return orderDetailList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;
        TextView tvOrderDetailName;
        TextView tvOrderDetailQuantity;
        TextView tvOrderDetailPrice;

        public ViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.img_itemdetailorder);
            tvOrderDetailName = itemView.findViewById(R.id.tv_namebook);
            tvOrderDetailQuantity = itemView.findViewById(R.id.tv_quantity);
            tvOrderDetailPrice = itemView.findViewById(R.id.tv_pricebook);
        }
    }
}
