package com.huflit.doanmobile.activityAdmin;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.huflit.doanmobile.R;
import com.huflit.doanmobile.SqlHelper.DatabaseHelper;
import com.huflit.doanmobile.SqlHelper.Mydatabase;
import com.huflit.doanmobile.adapter.AdapterUsersList;
import com.huflit.doanmobile.classs.Users;

import java.util.ArrayList;
import java.util.List;

public class UsersListActivity extends AppCompatActivity {
    private ListView listView;
    private ArrayList<Users> mUserList;
    private AdapterUsersList adapter;
    private Mydatabase mydb;
    Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users_list);

        toolbar = findViewById(R.id.toolbaruserslist);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        mydb = new Mydatabase(this);
        listView = findViewById(R.id.lvuserslist);

        mUserList = mydb.getAlluser();
        adapter = new AdapterUsersList(this, mUserList);

        listView.setAdapter(adapter);
    }
}