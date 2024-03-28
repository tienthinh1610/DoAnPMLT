package com.huflit.doanmobile.classs;



public class Cart implements CartPrototype{
    private int cartId;
    private int userId;
    private Book book;
    private int quantity;

    public Cart(int cartId, int userId, Book book, int quantity) {
        this.cartId = cartId;
        this.userId = userId;
        this.book = book;
        this.quantity = quantity;
    }

    public int getCartId() {
        return cartId;
    }

    public void setCartId(int cartId) {
        this.cartId = cartId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    //Prototype
    @Override
    public Cart clone(int quantity) {
        // Trả về một đối tượng Cart mới với quantity mới
        return new Cart(this.cartId, this.userId, this.book, quantity);
    }
}
