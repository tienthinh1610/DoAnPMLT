package com.huflit.doanmobile.adapter;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import com.bumptech.glide.Glide;
import com.huflit.doanmobile.R;
import com.huflit.doanmobile.SqlHelper.DatabaseHelper;
import com.huflit.doanmobile.SqlHelper.Mydatabase;
import com.huflit.doanmobile.activityAdmin.AddBookActivity;
import com.huflit.doanmobile.activityAdmin.BooksListActivity;
import com.huflit.doanmobile.classs.Book;

import java.util.ArrayList;
import java.util.List;

public class AdapterBookList extends BaseAdapter {
    private Context context;
    private ArrayList<Book> bookList;
    Mydatabase mydb;
    public AdapterBookList(Context context, ArrayList<Book> bookList) {
        this.context = context;
        this.bookList = bookList;
    }

    @Override
    public int getCount() {
        return bookList.size();
    }

    @Override
    public Object getItem(int position) {
        return bookList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return bookList.get(position).getId();
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.layout_item_listbooks, null);

        ImageView imgBook = view.findViewById(R.id.imageView);
        TextView tvName = view.findViewById(R.id.tv_nameb);
        TextView tvPrice = view.findViewById(R.id.tv_priceb);
        Button btnDelete = view.findViewById(R.id.btn_xoasach);
        Button btnEdit = view.findViewById(R.id.btn_suasach);

        final Book book = bookList.get(position);

        tvName.setText(book.getName());
        tvPrice.setText(String.format("%,d", book.getPrice())+ " VNĐ");

        Glide.with(context)
                .load("file:///android_asset/" + book.getImage1())
                .into(imgBook);

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDeleteDialog(position);
            }
        });

        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showEditDialog(position);
            }
        });

        return view;
    }

    private void showDeleteDialog(final int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Delete book");
        builder.setMessage("Bạn có chắc muốn xóa sách?");

        builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Mydatabase mydb = new Mydatabase(context);
                mydb.deleteBook(bookList.get(position).getId());
                //composite
                mydb.removeBookToCategory(bookList.get(position).getCategoryId(),bookList.get(position).getName());
                bookList.remove(position);
                notifyDataSetChanged();
                Toast.makeText(context, "Xóa sách thành công!", Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void showEditDialog(final int position) {
        mydb = new Mydatabase(context);
        final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.layout_dialog_edit_book);
        dialog.setTitle("Edit book");

        final EditText edtName = dialog.findViewById(R.id.edt_book_name);
        final EditText edtPrice = dialog.findViewById(R.id.edt_book_price);
        final EditText edtAuthor = dialog.findViewById(R.id.edt_book_author);
        final EditText edtDescription = dialog.findViewById(R.id.edt_book_description);
        final EditText imgBook1 = dialog.findViewById(R.id.imgBook1);
        final EditText imgBook2 = dialog.findViewById(R.id.imgBook2);
        final EditText imgBook3 = dialog.findViewById(R.id.imgBook3);
        final Spinner spinner = dialog.findViewById(R.id.spinner_category);
        ArrayList<String> dscategory = new ArrayList<String>();
        dscategory.add("Sách Thiếu Nhi");
        dscategory.add("Sách Văn Học");
        dscategory.add("Sách Kinh Tế");
        dscategory.add("Sách Tâm Lý");
        ArrayAdapter adapterspinner = new ArrayAdapter(context, android.R.layout.simple_spinner_item, dscategory);
        spinner.setAdapter(adapterspinner);

        final Book book = bookList.get(position);
        int bookid = book.getId();
        edtName.setText(book.getName());
        edtPrice.setText(String.valueOf(book.getPrice()));
        edtAuthor.setText(book.getAuthor());
        edtDescription.setText(book.getDescription());
        imgBook1.setText(book.getImage1());
        imgBook2.setText(book.getImage2());
        imgBook3.setText(book.getImage3());
        spinner.setSelection(book.getCategoryId() - 1);

        Button btnSave = dialog.findViewById(R.id.btnsave);
        Button btnCancel = dialog.findViewById(R.id.btncancel);

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = edtName.getText().toString().trim();
                String author = edtAuthor.getText().toString().trim();
                String priceStr = edtPrice.getText().toString().trim();
                String description = edtDescription.getText().toString().trim();
                String image1 = imgBook1.getText().toString().trim();
                String image2 = imgBook2.getText().toString().trim();
                String image3 = imgBook3.getText().toString().trim();
                int categoryId = (int) spinner.getSelectedItemId() + 1;
                if (name.isEmpty() || author.isEmpty() || priceStr.isEmpty()) {
                    Toast.makeText(context, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                    return;
                }
                else {
                    int price = Integer.parseInt(priceStr);
                    boolean updatebook = mydb.updateBook(bookid, categoryId, name, price, author, description, image1, image2, image3);
                    if (updatebook = true)
                    {
                        Toast.makeText(context, "Cập nhật sách thành công", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                        notifyDataSetChanged();
                    }
                    else {
                        Toast.makeText(context, "Cập nhật sách thất bại", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }
}
