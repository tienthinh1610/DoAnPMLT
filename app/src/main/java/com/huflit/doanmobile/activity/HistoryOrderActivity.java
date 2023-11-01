package com.huflit.doanmobile.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import com.huflit.doanmobile.R;
import com.huflit.doanmobile.SqlHelper.Mydatabase;
import com.huflit.doanmobile.adapter.AdapterHistoryOrder;
import com.huflit.doanmobile.classs.HistoryOrder;

import java.util.List;

public class HistoryOrderActivity extends AppCompatActivity {

    private ListView lvhistoryorder;
    private Mydatabase mydb;
    private List<HistoryOrder> orderList;
    private AdapterHistoryOrder adapter;
    String musername;
    int userID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_order);
        Anhxa();

        Bundle b = getIntent().getExtras();
        musername = b.getString("username");
        Toolbar toolbar =findViewById(R.id.toolbarhistoryorder);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        userID = mydb.getUserIdByUsername(musername);
        orderList = mydb.getAllOrders(userID);
        adapter = new AdapterHistoryOrder(this,R.layout.layout_item_historyorder,orderList);
        lvhistoryorder.setAdapter(adapter);
    }

    private void Anhxa() {
        lvhistoryorder = findViewById(R.id.lvhistoryorder);
        mydb = new Mydatabase(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        adapter.clear();
        orderList = mydb.getAllOrders(userID);
        adapter.notifyDataSetChanged();
        adapter.addAll(orderList);
    }
}