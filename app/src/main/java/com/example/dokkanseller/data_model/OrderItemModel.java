package com.example.dokkanseller.data_model;

import java.io.Serializable;
import java.util.List;

public class OrderItemModel implements Serializable {

    public viewAddressModel getAddress() {
        return address;
    }

    public void setAddress(viewAddressModel address) {
        this.address = address;
    }

    List <CartItem> cartItem ;
    String     key  , time;
    viewAddressModel address;

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public List<CartItem> getCartItem() {
        return cartItem;
    }

    public void setCartItem(List<CartItem> cartItem) {
        this.cartItem = cartItem;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }



    @Override
    public String toString() {
        return "OrderItemModel{" +
                "cartItem=" + cartItem +
                '}';
    }
}
