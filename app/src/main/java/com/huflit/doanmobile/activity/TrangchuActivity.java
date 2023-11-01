package com.huflit.doanmobile.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.huflit.doanmobile.R;
import com.huflit.doanmobile.SqlHelper.Mydatabase;
import com.huflit.doanmobile.adapter.AdapterCateList;
import com.huflit.doanmobile.adapter.AdapterLvNav;
import com.huflit.doanmobile.adapter.AdapterRcvCategory;
import com.huflit.doanmobile.adapter.Adapterviewpager;
import com.huflit.doanmobile.classs.Book;
import com.huflit.doanmobile.classs.Category;
import com.huflit.doanmobile.classs.Loginstatus;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class TrangchuActivity extends AppCompatActivity {

    int currentPage = 0;
    ViewPager viewPager;
    DrawerLayout drawer_trangchu;
    Toolbar toolbar_trangchu;
    NavigationView nav_trangchu;
    AdapterLvNav adapterLvNav;
    ListView lv_nav;
    private ArrayList<Category> cateList = new ArrayList<>();
    private RecyclerView recyclerViewCategory1;
    private RecyclerView recyclerViewCategory2;
    private RecyclerView recyclerViewCategory3;
    private RecyclerView recyclerViewCategory4;
    private List<ArrayList<Book>> bookCategories = new ArrayList<>();
    private  String musername;
    private Mydatabase mydb;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trangchu);
        Anhxa();
        ActionBar();
        setAutoScrollViewScroll();

        setRCV();
        Bundle p = getIntent().getExtras();
        if (p != null){
            musername = p.getString("username");
        }
        cateList = mydb.getAllCates();
        adapterLvNav = new AdapterLvNav(this,cateList, musername);
        lv_nav.setAdapter(adapterLvNav);
    }

    private void Anhxa() {
        drawer_trangchu = (DrawerLayout) findViewById(R.id.drawer_trangchu);
        toolbar_trangchu = (Toolbar) findViewById(R.id.toolbar_trangchu);
        nav_trangchu = (NavigationView) findViewById(R.id.nav_trangchu);
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        mydb = new Mydatabase(this);
        lv_nav = findViewById(R.id.lv_nav);
        //Anh xa rcv
        recyclerViewCategory1 = findViewById(R.id.recyclerViewCategory1);
        recyclerViewCategory2 = findViewById(R.id.recyclerViewCategory2);
        recyclerViewCategory3 = findViewById(R.id.recyclerViewCategory3);
        recyclerViewCategory4 = findViewById(R.id.recyclerViewCategory4);

        Adapterviewpager adapterviewpager = new Adapterviewpager(this);
        viewPager.setAdapter(adapterviewpager);
        setAutoScrollViewScroll();
        viewPager.setClipToOutline(true);

    }
    private void setRCV(){


        //Thiết lập LayoutManager cho rcv
        LinearLayoutManager layoutManager1 = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        LinearLayoutManager layoutManager2 = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        LinearLayoutManager layoutManager3 = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        LinearLayoutManager layoutManager4 = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerViewCategory1.setLayoutManager(layoutManager1);
        recyclerViewCategory2.setLayoutManager(layoutManager2);
        recyclerViewCategory3.setLayoutManager(layoutManager3);
        recyclerViewCategory4.setLayoutManager(layoutManager4);
        //
        List<Book> category1Books = new ArrayList<>();
        List<Book> category2Books = new ArrayList<>();
        List<Book> category3Books = new ArrayList<>();
        List<Book> category4Books = new ArrayList<>();
        //
        category1Books = mydb.getCategory4Books(1); // Lấy sách theo category 1
        category2Books = mydb.getCategory4Books(2); // Lấy sách theo category 2
        category3Books = mydb.getCategory4Books(3); // Lấy sách theo category 3
        category4Books = mydb.getCategory4Books(4); // Lấy sách theo category 4
        //
        bookCategories.add((ArrayList<Book>) category1Books);
        bookCategories.add((ArrayList<Book>) category2Books);
        bookCategories.add((ArrayList<Book>) category3Books);
        bookCategories.add((ArrayList<Book>) category4Books);
        //
        AdapterRcvCategory adapter1 = new AdapterRcvCategory((Context) this, (ArrayList<Book>) category1Books);
        AdapterRcvCategory adapter2 = new AdapterRcvCategory((Context) this, (ArrayList<Book>) category2Books);
        AdapterRcvCategory adapter3 = new AdapterRcvCategory((Context) this, (ArrayList<Book>) category3Books);
        AdapterRcvCategory adapter4 = new AdapterRcvCategory((Context) this, (ArrayList<Book>) category4Books);
        //
        AdapterRcvCategory.OnItemClickListener listener = new AdapterRcvCategory.OnItemClickListener() {
            @Override
            public void onItemClick(Book book) {
                Intent intent = new Intent(TrangchuActivity.this, DetailBookActivity.class);
                Bundle b = new Bundle();
                b.putString("username", musername);
                b.putInt("bookid", book.getId());
                intent.putExtras(b);
                startActivity(intent);
            }
        };
        //
        adapter1.setOnItemClickListener(listener);
        adapter2.setOnItemClickListener(listener);
        adapter3.setOnItemClickListener(listener);
        adapter4.setOnItemClickListener(listener);
        //
        recyclerViewCategory1.setAdapter(adapter1);
        recyclerViewCategory2.setAdapter(adapter2);
        recyclerViewCategory3.setAdapter(adapter3);
        recyclerViewCategory4.setAdapter(adapter4);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_toolbar,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.search_item:
                Intent intent = new Intent(TrangchuActivity.this, SearchActivity.class);
                if ( musername != null){
                    Bundle b = new Bundle();
                    b.putString("username", musername);
                    intent.putExtras(b);
                }
                startActivity(intent);
                break;
            case R.id.accout_item:
                if (Loginstatus.getInstance().isLoggedIn() == true){
                    Intent i = new Intent(TrangchuActivity.this, InfoUserActivity.class);
                    if ( musername != null){
                        Bundle b = new Bundle();
                        b.putString("username", musername);
                        i.putExtras(b);
                    }
                    startActivity(i);
                }
                else {
                    Toast.makeText(this, "Bạn chưa đăng nhập", Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(TrangchuActivity.this, LoginActivity.class);
                    startActivity(i);
                }
                break;
            case R.id.shoppingcart_item:
                if (Loginstatus.getInstance().isLoggedIn() == true){
                    Intent i = new Intent(TrangchuActivity.this, GioHangActivity.class);
                    if ( musername != null){
                        Bundle b = new Bundle();
                        b.putString("username", musername);
                        i.putExtras(b);
                    }
                    startActivity(i);
                }
                else {
                    Toast.makeText(this, "Bạn chưa đăng nhập", Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(TrangchuActivity.this, LoginActivity.class);
                    startActivity(i);
                }
        }
        return super.onOptionsItemSelected(item);
    }

    private void ActionBar(){
        setSupportActionBar(toolbar_trangchu);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        setTitle("");
        toolbar_trangchu.setNavigationIcon(R.drawable.ic_widgets);
        toolbar_trangchu.inflateMenu(R.menu.menu_toolbar);
        toolbar_trangchu.setTitle(null);

        toolbar_trangchu.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawer_trangchu.openDrawer(GravityCompat.START);
            }
        });

    }
    private void setAutoScrollViewScroll() {
        Timer timer;
        final long DELAY_MS = 500;//delay in milliseconds before task is to be executed
        final long PERIOD_MS = 2000; // time in milliseconds between successive task executions.
        final Handler handler = new Handler();
        final Runnable Update = new Runnable() {
            public void run() {
                if (currentPage == 4) {
                    currentPage = 0;
                }
                viewPager.setCurrentItem(currentPage++, true);
            }
        };

        timer = new Timer(); // This will create a new Thread
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(Update);
            }
        },DELAY_MS, PERIOD_MS);
    }
}
