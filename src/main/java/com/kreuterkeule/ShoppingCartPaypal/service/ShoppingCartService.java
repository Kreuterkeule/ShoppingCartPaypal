package com.kreuterkeule.ShoppingCartPaypal.service;

import com.kreuterkeule.ShoppingCartPaypal.entity.ShoppingCart;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class ShoppingCartService {

    private Map<Integer, ShoppingCart> sessionCarts = new HashMap<>();

    public void addCart(Integer cartId, ShoppingCart cart) {
        sessionCarts.put(cartId, cart);
    }

    public ShoppingCart getCart(Integer cartId) {
        return sessionCarts.get(cartId);
    }

    public void removeCart(Integer cartId) {
        sessionCarts.remove(cartId);
    }

    public Double getValue(Integer cartId) {
        return sessionCarts.get(cartId).getShoppingCartValue();
    }
}
