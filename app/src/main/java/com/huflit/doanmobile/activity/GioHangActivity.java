package com.huflit.doanmobile.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.huflit.doanmobile.R;
import com.huflit.doanmobile.SqlHelper.Mydatabase;
import com.huflit.doanmobile.adapter.AdapterCart;
import com.huflit.doanmobile.classs.Cart;
import com.huflit.doanmobile.classs.Loginstatus;

import java.util.ArrayList;
import java.util.List;

public class GioHangActivity extends AppCompatActivity {
    Toolbar toolbar;
    private RecyclerView recyclerView;
    private AdapterCart adapter;
    private List<Cart> cartList;
    private TextView totalTextView;
    Mydatabase mydb = Mydatabase.getInstance(this);
    private String username;
    int userid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_giohang);
        Anhxa();

        toolbar = findViewById(R.id.toolbargiohang);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        Bundle p = getIntent().getExtras();
        if (p != null){
            username = p.getString("username");
        }

        userid = mydb.getUserIdByUsername(username);
        cartList = mydb.getCartByUserID(userid);
        adapter = new AdapterCart(cartList, this, mydb);
        recyclerView.setAdapter(adapter);

        setTotalPrice();
    }
    private void Anhxa() {
        recyclerView = findViewById(R.id.rcv_cart);
        totalTextView = findViewById(R.id.tv_totalprice);
        toolbar = findViewById(R.id.toolbargiohang);
        mydb = Mydatabase.getInstance(this);
        cartList = new ArrayList<>();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }
    public void setTotalPrice() {
        int totalPrice = 0;
        for (int i = 0; i < cartList.size(); i++) {
            Cart cart = cartList.get(i);
            int quantity = cart.getQuantity();
            int price = cart.getBook().getPrice();
            totalPrice += quantity * price;
        }
        totalTextView.setText(String.format("%,d", totalPrice) + " VND");
    }
    public void btn_thanhtoan(View view) {
        if (cartList.size() == 0){
            Toast.makeText(this, "Không có sản phẩm nào trong giỏ hàng", Toast.LENGTH_SHORT).show();
        }else {
            String totalStr = totalTextView.getText().toString();
            totalStr = totalStr.replace(",", "").replace(" VND", "");
            int totalprice = Integer.parseInt(totalStr);
            Intent intent = new Intent(GioHangActivity.this,InforOrder.class);
            Bundle b = new Bundle();
            b.putString("username", username);
            b.putInt("totalprice", totalprice);
            intent.putExtras(b);
            startActivity(intent);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        cartList.clear();
        adapter.notifyDataSetChanged();
        cartList = mydb.getCartByUserID(userid);
        adapter = new AdapterCart(cartList, this, mydb);
        recyclerView.setAdapter(adapter);
    }
}
