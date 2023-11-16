package com.huflit.doanmobile.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.huflit.doanmobile.R;
import com.huflit.doanmobile.SqlHelper.Mydatabase;
import com.huflit.doanmobile.activity.GioHangActivity;
import com.huflit.doanmobile.classs.Book;
import com.huflit.doanmobile.classs.Cart;

import java.util.ArrayList;
import java.util.List;

public class AdapterCart extends RecyclerView.Adapter<AdapterCart.ViewHolder> {
    private List<Cart> cartList;
    private Context context;
    private Mydatabase mydatabase;

    public AdapterCart(List<Cart> cartList, Context context, Mydatabase mydatabase) {
        this.cartList = cartList;
        this.context = context;
        this.mydatabase = mydatabase;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView tvTitle, tvPrice, tvQuantity;
        ImageButton btnDelete, btn_Cong, btn_Tru;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.img_bookcart);
            tvTitle = itemView.findViewById(R.id.tv_namebookcart);
            tvPrice = itemView.findViewById(R.id.tv_pricebookcart);
            tvQuantity = itemView.findViewById(R.id.tv_quantity);
            btnDelete = itemView.findViewById(R.id.btn_deletebookcart);
            btn_Cong = itemView.findViewById(R.id.btnCong);
            btn_Tru = itemView.findViewById(R.id.btnTru);
        }
    }

    @NonNull
    @Override
    public AdapterCart.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_item_cart, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterCart.ViewHolder holder, int position) {
        final Cart cart = cartList.get(position);
        holder.tvTitle.setText(cart.getBook().getName());
        holder.tvPrice.setText(String.format("%,d", cart.getBook().getPrice())+ " VNĐ");
        holder.tvQuantity.setText(String.valueOf(cart.getQuantity()));
        Glide.with(context)
                .load("file:///android_asset/" + cart.getBook().getImage1())
                .into(holder.imageView);

        holder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setMessage("Bạn có chắc muốn xóa sản phẩm khỏi giỏ hàng");

                builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        boolean result = mydatabase.deleteCart(cart.getCartId());
                        if (result) {
                            cartList.remove(cart);
                            notifyDataSetChanged();
                            Toast.makeText(context, "Đã xóa sản phẩm khỏi giỏ hàng", Toast.LENGTH_SHORT).show();
                            ((GioHangActivity)context).setTotalPrice();
                        } else {
                            Toast.makeText(context, "Xóa sản phẩm khỏi giỏ hàng thất bại", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

        holder.btn_Cong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int quantity = cart.getQuantity() + 1;
                cart.setQuantity(quantity);
                boolean result = mydatabase.updateQuantityCart(cart.getCartId(), quantity);
                if (result) {
                    holder.tvQuantity.setText(String.valueOf(quantity));
                    ((GioHangActivity)context).setTotalPrice();
                } else {
                    Toast.makeText(context, "Cập nhật giỏ hàng thất bại", Toast.LENGTH_SHORT).show();
                }
            }
        });

        holder.btn_Tru.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int quantity = cart.getQuantity() - 1;
                if (quantity > 0){
                    cart.setQuantity(quantity);
                    boolean result = mydatabase.updateQuantityCart(cart.getCartId(), quantity);
                    if (result) {
                        holder.tvQuantity.setText(String.valueOf(quantity));
                        ((GioHangActivity)context).setTotalPrice();
                    } else {
                        Toast.makeText(context, "Cập nhật giỏ hàng thất bại", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return cartList.size();
    }
}
