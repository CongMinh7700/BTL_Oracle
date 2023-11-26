package com.test.library.service;

import com.test.library.dto.CustomerDto;
import com.test.library.dto.ProductDto;
import com.test.library.model.Customer;
import com.test.library.model.Product;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface CustomerService {
    Customer save(CustomerDto customerDto);
    Customer saveCus(MultipartFile imageCustomer, CustomerDto customerDto);
    Customer findByUserName(String userName);
    Customer update(CustomerDto customerDto);
    Customer changePass(CustomerDto customerDto);
    public List<CustomerDto> allCustomer() ;
    CustomerDto getCustomer(String username);
    CustomerDto getById(Long id);
    Customer updateCus(MultipartFile imageCustomer,CustomerDto customerDto);
    void deleteCus(Long id);
}
