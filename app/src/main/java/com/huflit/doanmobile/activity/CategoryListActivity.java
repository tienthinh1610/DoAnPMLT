package com.huflit.doanmobile.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.huflit.doanmobile.R;
import com.huflit.doanmobile.SqlHelper.Mydatabase;
import com.huflit.doanmobile.adapter.AdapterCategoryList;
import com.huflit.doanmobile.classs.Book;

import java.util.ArrayList;
import java.util.List;

public class CategoryListActivity extends AppCompatActivity {
    private  RecyclerView recyclerView;
    Toolbar toolbar;
    Mydatabase mydb;
    String musername;
    int mcategory;
    String mcategoryname;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_catrgory_list);
        mydb = new Mydatabase(this);

        toolbar = findViewById(R.id.toolbarcategorylist);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        Bundle p = getIntent().getExtras();
        if (p != null){
            musername = p.getString("username");
            mcategory = p.getInt("categoryID");
            mcategoryname = p.getString("categoryname");
        }
        toolbar.setTitle(mcategoryname);
        recyclerView = findViewById(R.id.rcvcategorylist);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        List<Book> categoryBooks = new ArrayList<>();
        categoryBooks = mydb.getCategory4Books(mcategory);
        AdapterCategoryList adapter = new AdapterCategoryList((Context) this, (ArrayList<Book>) categoryBooks);
        AdapterCategoryList.OnItemClickListener listener = new AdapterCategoryList.OnItemClickListener() {
            @Override
            public void onItemClick(Book book) {
                Intent intent = new Intent(CategoryListActivity.this, DetailBookActivity.class);
                Bundle b = new Bundle();
                b.putString("username", musername);
                b.putInt("bookid", book.getId());
                intent.putExtras(b);
                startActivity(intent);
            }
        };
        adapter.setOnItemClickListener(listener);
        recyclerView.setAdapter(adapter);
    }
}