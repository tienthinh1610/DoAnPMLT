package com.huflit.doanmobile.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.View;

import com.huflit.doanmobile.R;
import com.huflit.doanmobile.SqlHelper.Mydatabase;
import com.huflit.doanmobile.adapter.AdapterRcvSearch;
import com.huflit.doanmobile.classs.Book;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends AppCompatActivity {

    private AdapterRcvSearch adapter;
    private List<Book> bookList;
    Mydatabase mydb = Mydatabase.getInstance(this);
    private SearchView searchView;
    private RecyclerView rcv_searchbooks;
    Toolbar toolbar;
    String musername;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        Anhxa();

        mydb = new Mydatabase(this);
        bookList = mydb.getAllBooks();
        adapter = new AdapterRcvSearch(bookList, this);
        rcv_searchbooks.setAdapter(adapter);

        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        searchView.requestFocus();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                List<Book> filteredList = filter(bookList, newText);
                adapter.setData(filteredList);
                adapter.notifyDataSetChanged();

                return true;
            }
        });
        Bundle p = getIntent().getExtras();
        if (p != null){
            musername = p.getString("username");
        }
        adapter.setOnItemClickListener(new AdapterRcvSearch.OnItemClickListener() {
            @Override
            public void onItemClick(Book book) {
                Intent intent = new Intent(SearchActivity.this, DetailBookActivity.class);
                Bundle b = new Bundle();
                b.putString("username", musername);
                b.putInt("bookid", book.getId());
                intent.putExtras(b);
                startActivity(intent);
            }
        });
    }

    private void Anhxa() {
        searchView =findViewById(R.id.sv_books);
        rcv_searchbooks = findViewById(R.id.rcv_searchbook);
        toolbar = findViewById(R.id.toolbarsearch);
        rcv_searchbooks.setLayoutManager(new LinearLayoutManager(this));
    }
    private List<Book> filter(List<Book> books, String query) {
        query = query.toLowerCase().trim();

        final List<Book> filteredList = new ArrayList<>();
        for (Book book : books) {
            if (book.getName().toLowerCase().contains(query)) {
                filteredList.add(book);
            }
        }
        return filteredList;
    }
}