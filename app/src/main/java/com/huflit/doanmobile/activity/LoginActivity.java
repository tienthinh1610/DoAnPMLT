package com.huflit.doanmobile.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.huflit.doanmobile.R;
import com.huflit.doanmobile.SqlHelper.Mydatabase;
import com.huflit.doanmobile.activityAdmin.AdminActivity;
import com.huflit.doanmobile.classs.Loginstatus;

public class LoginActivity extends AppCompatActivity {
    EditText medt_username,medt_password;
    Button btn_login;

    public  static  Mydatabase mydb;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Anhxa();
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = medt_username.getText().toString();
                String pass = medt_password.getText().toString();
                if (TextUtils.isEmpty(username) || TextUtils.isEmpty(pass))
                {
                    Toast.makeText(LoginActivity.this, "Login failed", Toast.LENGTH_SHORT).show();
                }
                else if (username.equals("admin") && pass.equals("admin"))
                {
                    Intent i = new Intent(getApplicationContext(), AdminActivity.class);
                    startActivity(i);
                }
                else
                {
                    Boolean checkuseracount = mydb.checkUserAccout(username,pass);
                    if (checkuseracount == true)
                    {
                        Loginstatus.getInstance().setLoggedIn(true);
                        Toast.makeText(LoginActivity.this, "Login successful", Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(getApplicationContext(),TrangchuActivity.class);
                        Bundle b = new Bundle();
                        b.putString("username", username);
                        i.putExtras(b);
                        startActivity(i);
                    }
                    else {
                        Toast.makeText(LoginActivity.this, "Login failed ", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

    }

    private void Anhxa() {
        btn_login = findViewById(R.id.btn_regis);
        medt_username = findViewById(R.id.edt_uesrname);
        medt_password = findViewById(R.id.edt_password);
        mydb = new Mydatabase(this);
    }

    public void dangky(View view) {
        Intent i = new Intent(LoginActivity.this, RegisterActivity.class);
        startActivity(i);
    }

    public void home(View view) {
        Intent intent = new Intent(LoginActivity.this, TrangchuActivity.class);
        startActivity(intent);
    }
}