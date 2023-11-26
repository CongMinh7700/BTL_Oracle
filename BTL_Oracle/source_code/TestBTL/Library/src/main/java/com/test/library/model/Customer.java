package com.test.library.model;

import jakarta.persistence.*;
import jakarta.persistence.metamodel.IdentifiableType;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.IdentityHashMap;
import java.util.List;


@Getter
@Setter
@EqualsAndHashCode
@AllArgsConstructor
@Entity
@Table(name="customers",uniqueConstraints = @UniqueConstraint(columnNames = "userName"))
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="customer_id")
    private Long id;
    @Size(min =3,max=15,message = "First name should have 3- 15 characters")
    private String firstName;
    @Size(min =3,max=15,message = "Last name should have 3- 15 characters")
    private String lastName;
    private String userName;
    private String country;
    @Column(name="phone_number")
    private String phoneNumber;
    private String address;
    @Lob
    @Column(name="image",columnDefinition = "CLOB")
    private String image;
    private String password;
    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
    @JoinColumn(name="city_id",referencedColumnName = "city_id")
    private City city;
    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(name ="customers_roles",joinColumns = @JoinColumn(name="customer_id", referencedColumnName = "customer_id"),
            inverseJoinColumns = @JoinColumn(name="role_id", referencedColumnName = "role_id"))
    private Collection<Role> roles;
    @OneToOne(mappedBy = "customer", cascade = CascadeType.ALL)
    private ShoppingCart shoppingCart;
    @OneToMany(mappedBy = "customer", cascade = CascadeType.PERSIST)
    private List<Order> orders;

    //
    public Customer() {

        this.country = "VN";
        this.shoppingCart = new ShoppingCart();
        this.orders = new ArrayList<>();
    }

    @Override
    public String toString() {
        return "Customer{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", username='" + userName + '\'' +
                ", password='" + password + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", address='" + address + '\'' +
                ", city=" + city.getName() +
                ", country='" + country + '\'' +
                ", roles=" + roles +
                ", cart=" + shoppingCart.getId() +
                ", orders=" + orders.size() +
                '}';
    }



}
