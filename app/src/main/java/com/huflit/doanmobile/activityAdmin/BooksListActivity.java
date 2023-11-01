package com.huflit.doanmobile.activityAdmin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.view.GravityCompat;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.Spinner;
import android.widget.Toast;

import com.huflit.doanmobile.R;
import com.huflit.doanmobile.SqlHelper.Mydatabase;
import com.huflit.doanmobile.activity.GioHangActivity;
import com.huflit.doanmobile.activity.InfoUserActivity;
import com.huflit.doanmobile.activity.InforOrder;
import com.huflit.doanmobile.activity.LoginActivity;
import com.huflit.doanmobile.activity.SearchActivity;
import com.huflit.doanmobile.activity.TrangchuActivity;
import com.huflit.doanmobile.adapter.AdapterBookList;
import com.huflit.doanmobile.adapter.AdapterCateList;
import com.huflit.doanmobile.classs.Book;
import com.huflit.doanmobile.classs.Category;
import com.huflit.doanmobile.classs.Loginstatus;

import java.util.ArrayList;
import java.util.List;

public class BooksListActivity extends AppCompatActivity {

    private ListView lvBook;
    private AdapterBookList adapterBookList;
    private AdapterCateList adapterCateList;
    private ArrayList<Book> bookList = new ArrayList<>();
    private ArrayList<Category> cateList = new ArrayList<>();
    private Mydatabase mydb;
    private Toolbar toolbar;
    ImageButton btn_add;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_books_list);
        Anhxa();
        ActionBar();
        bookList = mydb.getAllBooks();
        adapterBookList = new AdapterBookList(this, bookList);
        lvBook.setAdapter(adapterBookList);

        registerForContextMenu(btn_add);

        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popupMenu= new PopupMenu(BooksListActivity.this,view);
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        switch (menuItem.getItemId())
                        {
                            case R.id.item_addbook:
                                Intent intent = new Intent(BooksListActivity.this, AddBookActivity.class);
                                startActivity(intent);
                                break;
                            case R.id.item_addcate:
                                showAddCateDialog();
                                break;
                        }
                        return false;
                    }
                });
                popupMenu.inflate(R.menu.menu_add);
                popupMenu.show();
            }
        });
        btn_add.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                openContextMenu(v);
                return true;
            }
        });
    }

    private void Anhxa() {
        lvBook = findViewById(R.id.lvbookslist);
        mydb = new Mydatabase(this);
        toolbar = findViewById(R.id.toolbarbookslist);
        btn_add = findViewById(R.id.imgbt_add);
    }
    private void ActionBar(){
        setSupportActionBar(toolbar);
        toolbar.inflateMenu(R.menu.menu_add);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        getMenuInflater().inflate(R.menu.menu_showlist,menu);
        super.onCreateContextMenu(menu, v, menuInfo);
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.item_showlistbook:
                bookList.clear();
                cateList.clear();
                adapterBookList.notifyDataSetChanged();
                bookList = mydb.getAllBooks();
                adapterBookList = new AdapterBookList(this, bookList);
                lvBook.setAdapter(adapterBookList);
                break;
            case R.id.item_showlistcate:
                bookList.clear();
                cateList.clear();
                adapterBookList.notifyDataSetChanged();
                cateList = mydb.getAllCates();
                adapterCateList = new AdapterCateList(this, cateList);
                lvBook.setAdapter(adapterCateList);
                break;
        }
        return super.onContextItemSelected(item);
    }
    @Override
    protected void onResume() {
        super.onResume();
        bookList.clear();
        cateList.clear();
        adapterBookList.notifyDataSetChanged();
        bookList = mydb.getAllBooks();
        adapterBookList = new AdapterBookList(this, bookList);
        lvBook.setAdapter(adapterBookList);
    }
    private void showAddCateDialog(){
        ConstraintLayout constraintLayout = findViewById(R.id.AddCateConstraintLayout);
        View view = LayoutInflater.from(BooksListActivity.this).inflate(R.layout.dialog_addcate, constraintLayout);
        EditText edt_namecate = view.findViewById(R.id.edt_addcate);
        Button btn_add = view.findViewById(R.id.btn_add);

        AlertDialog.Builder builder = new AlertDialog.Builder(BooksListActivity.this);
        builder.setView(view);
        final AlertDialog alertDialog = builder.create();
        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String catename = edt_namecate.getText().toString().trim();
                if (catename.isEmpty()){
                    Toast.makeText(BooksListActivity.this, "Không được bỏ trống", Toast.LENGTH_SHORT).show();
                }else {
                    boolean result = mydb.addCate(catename);
                    if (result){
                        Toast.makeText(BooksListActivity.this, "Thêm thành công", Toast.LENGTH_SHORT).show();
                        bookList.clear();
                        cateList.clear();
                        adapterBookList.notifyDataSetChanged();
                        cateList = mydb.getAllCates();
                        adapterCateList = new AdapterCateList(BooksListActivity.this, cateList);
                        lvBook.setAdapter(adapterCateList);
                        alertDialog.dismiss();
                    }else{
                        Toast.makeText(BooksListActivity.this, "Thêm thất bại", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        if (alertDialog.getWindow() != null){
            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        }
        alertDialog.show();
    }
}