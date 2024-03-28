package com.huflit.doanmobile.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.huflit.doanmobile.R;
import com.huflit.doanmobile.SqlHelper.Mydatabase;

public class RegisterActivity extends AppCompatActivity {
    //public  static  Mydatabase mydb;
    EditText medt_username,medt_password;
    AppCompatButton mbtn_regis;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
         medt_username = findViewById(R.id.edt_uesrname);
         medt_password = findViewById(R.id.edt_password);
         mbtn_regis = findViewById(R.id.btn_regis);
         Mydatabase mydb = Mydatabase.getInstance(this);

         mbtn_regis.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 String username = medt_username.getText().toString().trim();
                 String password = medt_password.getText().toString().trim();
                 if (TextUtils.isEmpty(username) || TextUtils.isEmpty(password))
                 {
                     Toast.makeText(RegisterActivity.this, "Register failed", Toast.LENGTH_SHORT).show();
                 }
                 else if (username.equals("admin") && password.equals("admin"))
                 {
                     Toast.makeText(RegisterActivity.this, "Register failed", Toast.LENGTH_SHORT).show();
                 }
                 else {
                     Boolean checkusername = mydb.checkUserName(username);
                     if (checkusername == false)
                     {
                         Boolean insert = mydb.addUserAccout(username, password);
                         if (insert = true)
                         {
                             Toast.makeText(RegisterActivity.this, "Register successful", Toast.LENGTH_SHORT).show();
                             onBackPressed();
                         }
                         else {
                             Toast.makeText(RegisterActivity.this, "Register failed", Toast.LENGTH_SHORT).show();
                         }
                     }else {
                         Toast.makeText(RegisterActivity.this, "User already exists", Toast.LENGTH_SHORT).show();
                     }
                 }
             }
         });
    }

    public void backlogin(View view) {
        onBackPressed();
    }
}