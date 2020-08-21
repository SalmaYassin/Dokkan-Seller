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
    String status   , key ;
    viewAddressModel address;

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


    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
