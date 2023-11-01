package com.huflit.doanmobile.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import com.huflit.doanmobile.R;
import com.huflit.doanmobile.SqlHelper.Mydatabase;
import com.huflit.doanmobile.classs.Category;
import com.huflit.doanmobile.classs.Users;

import java.util.List;

public class AdapterCateList extends BaseAdapter {
    private Context mContext;
    private List<Category> mCateList;

    public AdapterCateList(Context context, List<Category> cateList) {
        mContext = context;
        mCateList = cateList;
    }

    @Override
    public int getCount() {
        return mCateList.size();
    }

    @Override
    public Object getItem(int position) {
        return mCateList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        AdapterCateList.ViewHolder holder;

        if (view == null) {
            LayoutInflater inflater = LayoutInflater.from(mContext);
            view = inflater.inflate(R.layout.layout_listusers, null);
            holder = new AdapterCateList.ViewHolder();
            holder.txtUsername = (TextView) view.findViewById(R.id.userNameTextView);
            holder.btnDelete = (ImageButton) view.findViewById(R.id.btn_xoauser);
            view.setTag(holder);
        } else {
            holder = (AdapterCateList.ViewHolder) view.getTag();
        }

        final Category cate = mCateList.get(position);
        holder.txtUsername.setText(cate.getCatename());
        holder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDeleteDialog(position);
            }
        });
        return view;
    }
    private void showDeleteDialog(final int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle("Xóa thể loại");
        builder.setMessage("Có chắc chắn muốn xóa thể loại này?");
        final Category cate = mCateList.get(position);

        builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Mydatabase mydb = new Mydatabase(mContext);
                mCateList.remove(cate);
                mydb.deletecate(cate.getCateId());
                notifyDataSetChanged();
                Toast.makeText(mContext, "Xóa thể loại thành công!", Toast.LENGTH_SHORT).show();
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
    static class ViewHolder {
        TextView txtUsername;
        ImageButton btnDelete;
    }
}
