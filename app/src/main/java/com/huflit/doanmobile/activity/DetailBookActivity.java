package com.huflit.doanmobile.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.huflit.doanmobile.R;
import com.huflit.doanmobile.SqlHelper.Mydatabase;
import com.huflit.doanmobile.adapter.AdapterVpgDetailBook;
import com.huflit.doanmobile.classs.Book;
import com.huflit.doanmobile.classs.Loginstatus;

import java.util.ArrayList;

public class DetailBookActivity extends AppCompatActivity {
    Toolbar toolbar;
    private  String musername;
    private int mbookid;
    Mydatabase mydb = Mydatabase.getInstance(this);
    private  TextView namebook, pricebook, authorbook, describebook;
    ViewPager viewPager;
    Button btn_addtocart;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_book);
        Anhxa();

        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        Bundle p = getIntent().getExtras();
        if (p != null){
            musername = p.getString("username");
            mbookid = p.getInt("bookid");
        }
        Book book = mydb.getBookById(mbookid);
        if (book != null) {
            namebook.setText(book.getName());
            pricebook.setText(String.format("%,d", book.getPrice())+ " VNĐ");
            authorbook.setText("Tác giả: " + book.getAuthor());
            describebook.setText(book.getDescription());
        }

        ArrayList<String> imageUrls = new ArrayList<>();
        imageUrls.add(book.getImage1());
        imageUrls.add(book.getImage2());
        imageUrls.add(book.getImage3());
        AdapterVpgDetailBook adapter = new AdapterVpgDetailBook(this, imageUrls);
        viewPager.setAdapter(adapter);

        btn_addtocart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Loginstatus.getInstance().isLoggedIn() == true){
                    boolean checkbookincart = mydb.checkBookInCart(mbookid,musername);
                    if (checkbookincart)
                    {
                        int cartid = mydb.getCartIdByBookId(mbookid,musername);
                        int quantity = mydb.getQuantityByCartId(cartid);
                        int newquantity = quantity + 1;
                        boolean result = mydb.updateQuantityCart(cartid, newquantity);
                        if (result) {
                            Toast.makeText(DetailBookActivity.this, "Thêm vào giỏ hàng thành công!", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(DetailBookActivity.this, "Thêm vào giỏ hàng thất bại!", Toast.LENGTH_SHORT).show();
                        }
                    }else {
                        boolean result = mydb.addToCart(musername, mbookid, 1);
                        if (result) {
                            Toast.makeText(DetailBookActivity.this, "Thêm vào giỏ hàng thành công!", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(DetailBookActivity.this, "Thêm vào giỏ hàng thất bại!", Toast.LENGTH_SHORT).show();
                        }
                    }

                }
                else {
                    Toast.makeText(DetailBookActivity.this, "Bạn chưa đăng nhập", Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(DetailBookActivity.this, LoginActivity.class);
                    startActivity(i);
                }

            }
        });
    }

    private void Anhxa() {
        toolbar = findViewById(R.id.toolbardetailbook);
        namebook = findViewById(R.id.tv_namebook);
        pricebook = findViewById(R.id.tv_pricebook);
        authorbook = findViewById(R.id.tv_authorbook);
        describebook = findViewById(R.id.tv_describebook);
        viewPager = findViewById(R.id.vpg_imagebook);
        btn_addtocart = findViewById(R.id.btn_addtocart);
        mydb = new Mydatabase(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_datailbook,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.accout_item:
                if (Loginstatus.getInstance().isLoggedIn() == true){
                    Intent i = new Intent(DetailBookActivity.this, InfoUserActivity.class);
                    if ( musername != null){
                        Bundle b = new Bundle();
                        b.putString("username", musername);
                        i.putExtras(b);
                    }
                    startActivity(i);
                }
                else {
                    Toast.makeText(this, "Bạn chưa đăng nhập", Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(DetailBookActivity.this, LoginActivity.class);
                    startActivity(i);
                }
                break;
            case R.id.shoppingcart_item:
                if (Loginstatus.getInstance().isLoggedIn() == true){
                    Intent i = new Intent(DetailBookActivity.this, GioHangActivity.class);
                    if ( musername != null){
                        Bundle b = new Bundle();
                        b.putString("username", musername);
                        i.putExtras(b);
                    }
                    startActivity(i);
                }
                else {
                    Toast.makeText(this, "Bạn chưa đăng nhập", Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(DetailBookActivity.this, LoginActivity.class);
                    startActivity(i);
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}