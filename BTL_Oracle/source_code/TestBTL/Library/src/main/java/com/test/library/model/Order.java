package com.test.library.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="order_id")
    private Long id;
    private Date orderDate;
    private Date deliveryDate;
    private double shippingFee;
    @Column(name="status")
    private String orderStatus;
    @Column(name="quantity")
    private int quantity;
    @Column(name="tax_fee")
    private double taxFee;
    private String notes;
    private double totalPrice;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="customer_id",referencedColumnName = "customer_id")
    private Customer customer;
    private String paymentMethod;
    @Column(name="is_accept")
    private boolean isAccept;
    @OneToMany(cascade = CascadeType.ALL,mappedBy = "order")
    private List<OrderDetail> orderDetailList;


    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", orderDate=" + orderDate +
                ", deliveryDate=" + deliveryDate +
                ", totalPrice=" + totalPrice +
                ", tax='" + taxFee + '\'' +
                ", paymentMethod='" + paymentMethod + '\'' +
                ", customer=" + customer.getUserName() +
                ", orderDetailList=" + orderDetailList.size() +
                '}';
    }
}
