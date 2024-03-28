package com.huflit.doanmobile.activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.huflit.doanmobile.R;
import com.huflit.doanmobile.SqlHelper.Mydatabase;
import com.huflit.doanmobile.classs.Cart;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class InforOrder extends AppCompatActivity {
    Toolbar toolbar;
    private EditText edtHoTen, edtSDT, edtDiaChi;
    private RadioGroup rgPaymentMethor;
    Mydatabase mydb = Mydatabase.getInstance(this);
    String musername;
    int mtotalprice;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_infor_order);
        Anhxa();
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        Bundle p = getIntent().getExtras();
        if (p != null){
            musername = p.getString("username");
            mtotalprice = p.getInt("totalprice");
        }
    }

    private void Anhxa() {
        toolbar = findViewById(R.id.toolbar);
        edtHoTen = findViewById(R.id.edt_hoten);
        edtSDT = findViewById(R.id.edt_sdt);
        edtDiaChi = findViewById(R.id.edt_diachi);
        rgPaymentMethor = findViewById(R.id.rg_ppthanhtoan);
        mydb = new Mydatabase(this);
    }

    public void btn_xacnhan(View view) {
        String hoTen = edtHoTen.getText().toString();
        String sdt = edtSDT.getText().toString();
        String diaChi = edtDiaChi.getText().toString();
        int paymentMethodId = rgPaymentMethor.getCheckedRadioButtonId();
        String paymentMethod = "";
        switch (paymentMethodId) {
            case R.id.rb_cod:
                paymentMethod = "COD";
                break;
            case R.id.rb_atm:
                paymentMethod = "ATM";
                break;
            case R.id.rb_momo:
                paymentMethod = "MoMo";
                break;
        }

        if (hoTen.isEmpty() && sdt.isEmpty() && diaChi.isEmpty() && paymentMethod.equals("")){
            Toast.makeText(this, "Vui lòng điền đầy đủ thông tin", Toast.LENGTH_SHORT).show();
        }else {
            int userId = mydb.getUserIdByUsername(musername);
            int totalPrice = mtotalprice;
            String currentDate = new SimpleDateFormat("dd-MM-YYYY", Locale.getDefault()).format(new Date());
            long orderId = mydb.insertHistoryOrder(userId, totalPrice, currentDate, hoTen, sdt, diaChi, paymentMethod);

            List<Cart> cartList = mydb.getCartByUserID(userId);
            for (Cart cart : cartList) {
                mydb.insertOrderDetail(orderId, cart.getBook().getId(), cart.getQuantity());
                mydb.deleteCart(cart.getCartId());
            }
            showSuccessDialog();
        }
    }
    private void showSuccessDialog(){
        ConstraintLayout constraintLayout = findViewById(R.id.successConstraintLayout);
        View view = LayoutInflater.from(InforOrder.this).inflate(R.layout.dialog_success, constraintLayout);
        Button btn_done = view.findViewById(R.id.btn_success);

        AlertDialog.Builder builder = new AlertDialog.Builder(InforOrder.this);
        builder.setView(view);
        final AlertDialog alertDialog = builder.create();
        btn_done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
                Intent intent = new Intent(InforOrder.this, TrangchuActivity.class);
                Bundle b = new Bundle();
                b.putString("username", musername);
                intent.putExtras(b);
                startActivity(intent);
                finish();
            }
        });
        if (alertDialog.getWindow() != null){
            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        }
        alertDialog.show();
    }
}