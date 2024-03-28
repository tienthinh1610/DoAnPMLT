package com.huflit.doanmobile.SqlHelper;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.nfc.Tag;
import android.util.Log;

import com.huflit.doanmobile.activityAdmin.UsersListActivity;
import com.huflit.doanmobile.classs.Book;
import com.huflit.doanmobile.classs.Cart;
import com.huflit.doanmobile.classs.Category;
import com.huflit.doanmobile.classs.HistoryOrder;
import com.huflit.doanmobile.classs.OrderDetail;
import com.huflit.doanmobile.classs.Users;

import java.util.ArrayList;
import java.util.List;


public class Mydatabase {
    private static Mydatabase instance;
    SQLiteDatabase db;
    DatabaseHelper DBhelper;
    public  Mydatabase(Context context){
        DBhelper = new DatabaseHelper(context);
        db = DBhelper.getWritableDatabase();
    }
    public static synchronized Mydatabase getInstance(Context context) {
        if (instance == null) {
            instance = new Mydatabase(context.getApplicationContext());
        }
        return instance;
    }
    //Thêm tài khoản (Đăng Ký)
    public boolean addUserAccout(String username, String password) {
        ContentValues values = new ContentValues();

        values.put(DBhelper.COLUMN_USER_USERNAME, username);
        values.put(DBhelper.COLUMN_USER_PASSWORD, password);
        long result = db.insert(DBhelper.TABLE_USERS, null, values);
        if (result == -1)
            return false;
        else
            return true;
    }
    //Kiểm tra tài khoản (Đăng Nhập)
    public boolean checkUserAccout(String username, String password) {
        String query = "SELECT * FROM " + DBhelper.TABLE_USERS + " WHERE " +
                DBhelper.COLUMN_USER_USERNAME + "=? AND " + DBhelper.COLUMN_USER_PASSWORD + "=?";
        Cursor cursor = db.rawQuery(query, new String[]{username, password});
        if (cursor.getCount() > 0)
            return true;
        else
            return false;
    }
    //Kiểm tra tài khoản đã tồn tại hay chưa
    public boolean checkUserName(String username) {
        String query = "SELECT * FROM " + DBhelper.TABLE_USERS + " WHERE " +
                DBhelper.COLUMN_USER_USERNAME + "=?";
        Cursor cursor = db.rawQuery(query, new String[]{username});
        if (cursor.getCount() > 0)
            return true;
        else
            return false;
    }
    //Xóa người dùng
    public boolean deleteuser(long userId ){
        int result = db.delete(DBhelper.TABLE_USERS, DBhelper.COLUMN_USER_ID + "=?", new String[]{String.valueOf(userId)});
        if (result > 0) {
            // xóa thành công
            return true;
        } else {
            // xóa không thành công
            return false;
        }
    }
    //Xóa sách
    public boolean deleteBook(int bookId) {
        int result = db.delete(DBhelper.TABLE_BOOKS, DBhelper.COLUMN_BOOK_ID + " = ?",
                new String[]{String.valueOf(bookId)});

        return result > 0;
    }
    //Sửa sách
    public boolean updateBook(int bookid, int categoryId, String bookName, int bookPrice, String bookAuthor, String description, String image1, String image2, String image3) {
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_BOOK_CATEGORY_ID, categoryId);
        values.put(DatabaseHelper.COLUMN_BOOK_NAME, bookName);
        values.put(DatabaseHelper.COLUMN_BOOK_AUTHOR, bookAuthor);
        values.put(DatabaseHelper.COLUMN_BOOK_DESCRIPTION, description);
        values.put(DatabaseHelper.COLUMN_BOOK_PRICE, bookPrice);
        values.put(DatabaseHelper.COLUMN_BOOK_IMAGE_1, image1);
        values.put(DatabaseHelper.COLUMN_BOOK_IMAGE_2, image2);
        values.put(DatabaseHelper.COLUMN_BOOK_IMAGE_3, image3);
        int result = db.update(DBhelper.TABLE_BOOKS, values, DBhelper.COLUMN_BOOK_ID + " = ?",
                new String[]{String.valueOf(bookid)});
        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }
    public List<Book> getCategory4Books(int categoryId) {
        List<Book> bookList = new ArrayList<>();
        String query = "SELECT * FROM " + DBhelper.TABLE_BOOKS + " WHERE " + DBhelper.COLUMN_BOOK_CATEGORY_ID + " = ?";
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(categoryId)});
        if (cursor.moveToFirst()) {
            do {
                int bookId = cursor.getInt(0);
                int bookCategoryId = cursor.getInt(1);
                String bookName = cursor.getString(2);
                String bookAuthor = cursor.getString(3);
                String bookDescription = cursor.getString(4);
                int bookPrice = cursor.getInt(5);
                String bookImage1 = cursor.getString(6);
                String bookImage2 = cursor.getString(7);
                String bookImage3 = cursor.getString(8);

                Book book = new Book(bookId, bookCategoryId, bookName, bookAuthor, bookDescription, bookPrice, bookImage1, bookImage2, bookImage3);
                bookList.add(book);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return bookList;
    }
    public ArrayList<Book> getAllBooks() {
        ArrayList<Book> books = new ArrayList<Book>();

        Cursor cursor = db.query(DBhelper.TABLE_BOOKS, null, null, null, null, null, null);

        if (cursor.moveToFirst()) {
            do {
                int book_id = cursor.getInt(0);
                int category_id = cursor.getInt(1);
                String book_name = cursor.getString(2);
                String book_author = cursor.getString(3);
                String book_description = cursor.getString(4);
                int book_price = cursor.getInt(5);
                String book_image_1 = cursor.getString(6);
                String book_image_2 = cursor.getString(7);
                String book_image_3 = cursor.getString(8);

                Book book = new Book(book_id, category_id, book_name, book_author, book_description, book_price, book_image_1, book_image_2, book_image_3);
                books.add(book);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return books;
    }
    //composite
    public boolean removeBookToCategory(int categoryId, String bookName) {
        // Tìm danh mục theo categoryId
        Category category = getCateById(categoryId);

        if (category != null) {
            // Tạo một đối tượng Book mới
            Book book = getBookByName(bookName);

            // Thêm sách vào danh sách sách của Category
            category.remove(book);
            return true;
        } else {
            return false; // Hoặc xử lý tùy ý khi không tìm thấy Category
        }
    }
    public boolean addBook(int categoryId, String bookName, int bookPrice, String bookAuthor, String description, String image1, String image2, String image3) {
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_BOOK_CATEGORY_ID, categoryId);
        values.put(DatabaseHelper.COLUMN_BOOK_NAME, bookName);
        values.put(DatabaseHelper.COLUMN_BOOK_AUTHOR, bookAuthor);
        values.put(DatabaseHelper.COLUMN_BOOK_DESCRIPTION, description);
        values.put(DatabaseHelper.COLUMN_BOOK_PRICE, bookPrice);
        values.put(DatabaseHelper.COLUMN_BOOK_IMAGE_1, image1);
        values.put(DatabaseHelper.COLUMN_BOOK_IMAGE_2, image2);
        values.put(DatabaseHelper.COLUMN_BOOK_IMAGE_3, image3);
        long result = db.insert(DatabaseHelper.TABLE_BOOKS, null, values);
        if (result == -1) {
            return false;
        } else {
            //return true;
            // Tạo một đối tượng Book mới
            Book book = new Book(/* pass parameters */);

            // Thêm sách vào danh sách sách của Category
            Category category = getCateById(categoryId);
            if (category != null) {
                category.add(book);
            }

            return true;
        }
    }
    public Users getInforAccount (String musername){
        Users user = null;
        Cursor cursor = db.query(DBhelper.TABLE_USERS, new String[]{DBhelper.COLUMN_USER_ID, DBhelper.COLUMN_USER_USERNAME,
                        DBhelper.COLUMN_USER_PASSWORD, DBhelper.COLUMN_USER_NAME, DBhelper.COLUMN_USER_EMAIL, DBhelper.COLUMN_USER_PHONE},
                DBhelper.COLUMN_USER_USERNAME + "=?", new String[]{musername}, null, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
            user = new Users(
                    cursor.getInt(0),
                    cursor.getString(1),
                    cursor.getString(2),
                    cursor.getString(3),
                    cursor.getString(4),
                    cursor.getString(5)
            );
        }
        cursor.close();
        return user;
    }
    public boolean updateUser(String username, String name, String email, String phone) {
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_USER_NAME, name);
        values.put(DatabaseHelper.COLUMN_USER_EMAIL, email);
        values.put(DatabaseHelper.COLUMN_USER_PHONE, phone);

        int rowsAffected = db.update(DatabaseHelper.TABLE_USERS, values,
                DatabaseHelper.COLUMN_USER_USERNAME + " = ?", new String[]{username});

        return rowsAffected > 0;
    }
    public Book getBookByName(String bookName) {
        String[] columns = {DatabaseHelper.COLUMN_BOOK_ID, DatabaseHelper.COLUMN_BOOK_CATEGORY_ID,
                DatabaseHelper.COLUMN_BOOK_NAME, DatabaseHelper.COLUMN_BOOK_AUTHOR,
                DatabaseHelper.COLUMN_BOOK_DESCRIPTION, DatabaseHelper.COLUMN_BOOK_PRICE,
                DatabaseHelper.COLUMN_BOOK_IMAGE_1, DatabaseHelper.COLUMN_BOOK_IMAGE_2,
                DatabaseHelper.COLUMN_BOOK_IMAGE_3};

        String selection = DatabaseHelper.COLUMN_BOOK_NAME + " = ?";
        String[] selectionArgs = {bookName};

        Cursor cursor = db.query(DatabaseHelper.TABLE_BOOKS, columns, selection, selectionArgs, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            int id = cursor.getInt(0);
            int categoryId = cursor.getInt(1);
            String name = cursor.getString(2);
            String author = cursor.getString(3);
            String description = cursor.getString(4);
            int price = cursor.getInt(5);
            String image1 = cursor.getString(6);
            String image2 = cursor.getString(7);
            String image3 = cursor.getString(8);
            cursor.close();
            return new Book(id, categoryId, name, author, description, price, image1, image2, image3);
        }

        return null; // Trả về null nếu không tìm thấy sách với tên tương ứng
    }
    public Book getBookById(int bookId) {

        String[] columns = {DBhelper.COLUMN_BOOK_ID, DBhelper.COLUMN_BOOK_CATEGORY_ID, DBhelper.COLUMN_BOOK_NAME,
                DBhelper.COLUMN_BOOK_AUTHOR, DBhelper.COLUMN_BOOK_DESCRIPTION, DBhelper.COLUMN_BOOK_PRICE,
                DBhelper.COLUMN_BOOK_IMAGE_1, DBhelper.COLUMN_BOOK_IMAGE_2, DBhelper.COLUMN_BOOK_IMAGE_3};
        String selection = DBhelper.COLUMN_BOOK_ID + " = ?";
        String[] selectionArgs = {String.valueOf(bookId)};

        Cursor cursor = db.query(DBhelper.TABLE_BOOKS, columns, selection, selectionArgs, null, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            int id = cursor.getInt(0);
            int categoryId = cursor.getInt(1);
            String name = cursor.getString(2);
            String author = cursor.getString(3);
            String description = cursor.getString(4);
            int price = cursor.getInt(5);
            String image1 = cursor.getString(6);
            String image2 = cursor.getString(7);
            String image3 = cursor.getString(8);
            Book book = new Book(id, categoryId, name, author, description, price, image1, image2, image3);
            cursor.close();
            return book;
        }
        return null;
    }
    public int getUserIdByUsername(String username) {
        int userId = -1;
        String[] columns = {DatabaseHelper.COLUMN_USER_ID};
        String selection = DatabaseHelper.COLUMN_USER_USERNAME + " = ?";
        String[] selectionArgs = {username};
        Cursor cursor = db.query(DatabaseHelper.TABLE_USERS, columns, selection, selectionArgs, null, null, null);
        if (cursor.moveToFirst()) {
            userId = cursor.getInt(0);
        }
        cursor.close();
        return userId;
    }
    public boolean addToCart(String username, int bookId, int quantity) {
        //Prototype Pattern
        int userId = getUserIdByUsername(username);
        // Lấy thông tin sách từ bookId
        Book book = getBookById(bookId);
        // Tạo một đối tượng Cart từ cơ sở dữ liệu hoặc từ một nguồn khác
        Cart cartPrototype = new Cart(0, userId, book, quantity);
        // Tạo một bản sao của đối tượng Cart với quantity mới
        Cart newCart = cartPrototype.clone(quantity);
        // Thêm đối tượng Cart mới vào cơ sở dữ liệu
        ContentValues values = new ContentValues();
        //values.put(DatabaseHelper.COLUMN_CART_USER_ID, getUserIdByUsername(username));
        //values.put(DatabaseHelper.COLUMN_CART_BOOK_ID, bookId);
        //values.put(DatabaseHelper.COLUMN_CART_QUANTITY, quantity);
        values.put(DatabaseHelper.COLUMN_CART_USER_ID, newCart.getUserId());
        values.put(DatabaseHelper.COLUMN_CART_BOOK_ID, newCart.getBook().getId());
        values.put(DatabaseHelper.COLUMN_CART_QUANTITY, newCart.getQuantity());

        long result = db.insert(DatabaseHelper.TABLE_CART, null, values);
        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }
    public ArrayList<Cart> getCartByUserID(int userID) {
        ArrayList<Cart> cartList = new ArrayList<>();

        String query = "SELECT * FROM " + DBhelper.TABLE_CART + " WHERE " + DBhelper.COLUMN_CART_USER_ID + " = ?";
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(userID)});

        if (cursor.moveToFirst()) {
            do {
                int cartId = cursor.getInt(0);
                int userId = cursor.getInt(1);
                int bookId = cursor.getInt(2);
                int quantity = cursor.getInt(3);

                Book book = getBookById(bookId);
                if (book != null) {
                    Cart cart = new Cart(cartId, userId, book, quantity);
                    cartList.add(cart);
                }

            } while (cursor.moveToNext());
        }

        cursor.close();

        return cartList;
    }
    public boolean deleteCart(int cartId) {
        int result = db.delete(DBhelper.TABLE_CART, DBhelper.COLUMN_CART_ID + " = ?", new String[]{String.valueOf(cartId)});
        return result > 0;
    }
    public boolean updateQuantityCart(int cartId, int quantity) {
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_CART_QUANTITY, quantity);
        int result = db.update(DatabaseHelper.TABLE_CART, values,
                DatabaseHelper.COLUMN_CART_ID + " = ?", new String[]{String.valueOf(cartId)});
        if (result == -1) {
            return false;
        }
        return true;
    }
    public boolean checkBookInCart(int bookId, String username) {
        int userId = getUserIdByUsername(username);
        List<Cart> cartList = getCartByUserID(userId);
        for (Cart cart : cartList) {
            if (cart.getBook().getId() == bookId) {
                return true;
            }
        }
        return false;
    }
    public int getCartIdByBookId(int bookId, String usename) {
        int userId = getUserIdByUsername(usename);
        String query = "SELECT " + DatabaseHelper.COLUMN_CART_ID + " FROM " +
                DatabaseHelper.TABLE_CART + " WHERE " +
                DatabaseHelper.COLUMN_CART_USER_ID + " = ?" + " AND " +
                DatabaseHelper.COLUMN_CART_BOOK_ID + " = ?";
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(userId), String.valueOf(bookId)});
        if (cursor.moveToFirst()) {
            int cartId = cursor.getInt(0);
            cursor.close();
            return cartId;
        } else {
            cursor.close();
            return -1;
        }
    }
    public int getQuantityByCartId(int cartId) {
        String query = "SELECT " + DatabaseHelper.COLUMN_CART_QUANTITY + " FROM " + DatabaseHelper.TABLE_CART +
                " WHERE " + DatabaseHelper.COLUMN_CART_ID + "=?";
        Cursor cursor = db.rawQuery(query, new String[] {String.valueOf(cartId)});
        int quantity = -1;
        if (cursor.moveToFirst()) {
            quantity = cursor.getInt(0);
        }
        cursor.close();
        return quantity;
    }
    public List<HistoryOrder> getAllOrders(int userid) {
        List<HistoryOrder> ordersList = new ArrayList<>();
        Cursor cursor = db.rawQuery("SELECT * FROM " + DBhelper.TABLE_HISTORY_ORDER + " WHERE " +
                DBhelper.COLUMN_ORDER_USER_ID + "=?" , new String[]{String.valueOf(userid)});

        if (cursor.moveToFirst()) {
            do {
                int orderId = cursor.getInt(0);
                int userId = cursor.getInt(1);
                int totalPrice = cursor.getInt(2);
                String orderDate = cursor.getString(3);
                String receiverName = cursor.getString(4);
                String receiverPhone = cursor.getString(5);
                String receiverAddress = cursor.getString(6);
                String paymentMethod = cursor.getString(7);
                HistoryOrder order = new HistoryOrder(orderId, userId, totalPrice, orderDate, receiverName, receiverPhone, receiverAddress, paymentMethod);
                ordersList.add(order);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return ordersList;
    }
    public long insertHistoryOrder(int userId, int totalPrice, String orderDate, String receiverName,
                                   String receiverPhone, String receiverAddress, String paymentMethod) {
        ContentValues values = new ContentValues();
        values.put(DBhelper.COLUMN_ORDER_USER_ID, userId);
        values.put(DBhelper.COLUMN_ORDER_TOTAL_PRICE, totalPrice);
        values.put(DBhelper.COLUMN_ORDER_DATE, orderDate);
        values.put(DBhelper.COLUMN_HISTORY_RECEIVER_NAME, receiverName);
        values.put(DBhelper.COLUMN_HISTORY_RECEIVER_PHONE, receiverPhone);
        values.put(DBhelper.COLUMN_HISTORY_RECEIVER_ADDRESS, receiverAddress);
        values.put(DBhelper.COLUMN_HISTORY_PAYMENT_METHOD, paymentMethod);

        long id = db.insert(DBhelper.TABLE_HISTORY_ORDER, null, values);
        return id;
    }
    public void insertOrderDetail(long orderId, int bookId, int quantity) {
        ContentValues values = new ContentValues();
        values.put(DBhelper.COLUMN_ORDER_DETAIL_ORDER_ID, orderId);
        values.put(DBhelper.COLUMN_ORDER_DETAIL_BOOK_ID, bookId);
        values.put(DBhelper.COLUMN_ORDER_DETAIL_QUANTITY, quantity);
        db.insert(DBhelper.TABLE_ORDER_DETAIL, null, values);
    }
    public HistoryOrder getOrderById(int id) {
        Cursor cursor = db.rawQuery("SELECT * FROM " + DBhelper.TABLE_HISTORY_ORDER
                + " WHERE " + DBhelper.COLUMN_ORDER_ID + "=?", new String[]{String.valueOf(id)});
        if (cursor != null && cursor.moveToFirst()) {
            int Orderid = cursor.getInt(0);
            int UserId = cursor.getInt(1);
            int TotalPrice = cursor.getInt(2);
            String OrderDate = cursor.getString(3);
            String ReceiverName = cursor.getString(4);
            String ReceiverPhone = cursor.getString(5);
            String ReceiverAddress = cursor.getString(6);
            String PaymentMethod = cursor.getString(7);
            HistoryOrder order = new HistoryOrder(Orderid,UserId,TotalPrice,OrderDate,
                                                  ReceiverName,ReceiverPhone,ReceiverAddress,PaymentMethod);
            cursor.close();
            return order;
        }
        return null;
    }
    public List<OrderDetail> getOrderDetailByOrderId(int orderId) {
        List<OrderDetail> orderDetailList = new ArrayList<>();

        String selectQuery = "SELECT * FROM " + DBhelper.TABLE_ORDER_DETAIL
                + " WHERE " + DBhelper.COLUMN_ORDER_DETAIL_ORDER_ID + " = " + orderId;

        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                OrderDetail orderDetail = new OrderDetail(
                        cursor.getInt(0),
                        cursor.getInt(1),
                        cursor.getInt(2),
                        cursor.getInt(3)
                );
                orderDetailList.add(orderDetail);
            } while (cursor.moveToNext());
        }

        cursor.close();

        return orderDetailList;
    }
    public boolean deleteOrder(int orderId) {
        db.delete(DBhelper.TABLE_ORDER_DETAIL, DBhelper.COLUMN_ORDER_DETAIL_ORDER_ID
                + " = ?", new String[]{String.valueOf(orderId)});

        int result = db.delete(DBhelper.TABLE_HISTORY_ORDER, DBhelper.COLUMN_ORDER_ID
                + " = ?", new String[]{String.valueOf(orderId)});
        if (result == -1) {
            return false;
        }
        return true;
    }
    public ArrayList<Users> getAlluser() {
        ArrayList<Users> usersList = new ArrayList<>();

        Cursor cursor = db.query(DatabaseHelper.TABLE_USERS, null, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                Users user = new Users();
                user.setUser_id(cursor.getInt(0));
                user.setUsername(cursor.getString(1));
                user.setPassword(cursor.getString(2));
                user.setName(cursor.getString(3));
                user.setEmail(cursor.getString(4));
                user.setPhone(cursor.getString(5));
                usersList.add(user);
            } while (cursor.moveToNext());
        }
        cursor.close();

        return usersList;
    }
    public boolean addCate(String catename) {
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_CATEGORY_NAME, catename);

        long result = db.insert(DatabaseHelper.TABLE_CATEGORIES, null, values);
        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }
    public boolean deletecate(long cateId ){
        int result = db.delete(DBhelper.TABLE_CATEGORIES, DBhelper.COLUMN_CATEGORY_ID + "=?", new String[]{String.valueOf(cateId)});
        if (result > 0) {
            return true;
        } else {
            return false;
        }
    }
    public ArrayList<Category> getAllCates() {
        ArrayList<Category> cates = new ArrayList<Category>();

        Cursor cursor = db.query(DBhelper.TABLE_CATEGORIES, null, null, null, null, null, null);

        if (cursor.moveToFirst()) {
            do {
                int category_id = cursor.getInt(0);
                String cate_name = cursor.getString(1);

                Category cate = new Category(category_id, cate_name);
                cates.add(cate);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return cates;
    }
    public ArrayList<String> getAllCatename() {
        ArrayList<String> cates = new ArrayList<>();

        Cursor cursor = db.query(DBhelper.TABLE_CATEGORIES, null, null, null, null, null, null);

        if (cursor.moveToFirst()) {
            do {
                String cate_name = cursor.getString(1);
                cates.add(cate_name);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return cates;
    }
    public Category getCateById(long cateId) {
        String[] columns = {DatabaseHelper.COLUMN_CATEGORY_ID, DatabaseHelper.COLUMN_CATEGORY_NAME};
        String selection = DatabaseHelper.COLUMN_CATEGORY_ID + " = ?";
        String[] selectionArgs = {String.valueOf(cateId)};

        Cursor cursor = db.query(DatabaseHelper.TABLE_CATEGORIES, columns, selection, selectionArgs, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            int cateIds = cursor.getInt(0);
            String catename = cursor.getString(1);
            cursor.close();
            return new Category(cateIds, catename);
        }

        return null; // Trả về null nếu không tìm thấy danh mục với cateId tương ứng
    }
}
