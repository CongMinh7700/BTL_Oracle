package com.test.library.repository;

import com.test.library.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepository extends JpaRepository<Customer,Long> {
    Customer findByUserName(String userName);


    @Modifying
    @Query(value = "DELETE FROM customers_roles WHERE customer_id = :customer_id", nativeQuery = true)
    void deleteCustomerRolesByCustomerId(@Param("customer_id") Long customer_id);

    void deleteCustomerById(Long id);
}
