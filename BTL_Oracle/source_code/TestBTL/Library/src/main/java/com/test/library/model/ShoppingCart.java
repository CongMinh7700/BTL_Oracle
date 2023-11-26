package com.test.library.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@Entity
@Table(name = "shopping_cart")
public class ShoppingCart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="shopping_cart_id")
    private Long id;
    @Column(name="total_item")
    private int totalItems;
    @Column(name="total_price")
    private double totalPrices;
    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "customer_id", referencedColumnName = "customer_id")
    private Customer customer;

    @OneToMany(cascade = CascadeType.ALL,mappedBy ="cart")
    private Set<CartItem> cartItem;

    public ShoppingCart() {
        this.cartItem = new HashSet<>();
        this.totalItems = 0;
        this.totalPrices = 0.0;
    }

    @Override
    public String toString() {
        return "ShoppingCart{" +
                "id=" + id +
                ", customer=" + customer.getUserName() +
                ", totalPrice=" + totalPrices +
                ", totalItems=" + totalItems +
                ", cartItems=" + cartItem.size() +
                '}';
    }
}
