package com.test.library.service;

import com.test.library.model.Order;
import com.test.library.model.ShoppingCart;

import java.util.List;

public interface OrderService {
    Order save(ShoppingCart cart);
    List<Order> findAll(String username);

    List<Order> findALlOrders();

    Order acceptOrder(Long id);
    void cancelOrder(Long id);

}
