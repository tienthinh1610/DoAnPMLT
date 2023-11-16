package com.huflit.doanmobile.activityAdmin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.huflit.doanmobile.R;
import com.huflit.doanmobile.activity.LoginActivity;

public class AdminActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
    }

    public void dssp(View view) {
        Intent i = new Intent(AdminActivity.this,BooksListActivity.class);
        startActivity(i);
    }

    public void dsnd(View view) {
        Intent i = new Intent(AdminActivity.this, UsersListActivity.class);
        startActivity(i);
    }

    public void logout(View view) {
        Intent i = new Intent(AdminActivity.this, LoginActivity.class);
        startActivity(i);
        finish();
    }
}