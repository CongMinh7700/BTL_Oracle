package com.test.library.service.impl;

import com.test.library.dto.CustomerDto;
import com.test.library.dto.ProductDto;
import com.test.library.model.Customer;
import com.test.library.model.Product;
import com.test.library.repository.CustomerRepository;
import com.test.library.repository.RoleRepository;
import com.test.library.service.CustomerService;
import com.test.library.utils.ImageUpload;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService
{
@Autowired
private RoleRepository roleRepository;
@Autowired
private CustomerRepository customerRepository;
    @Autowired
    private ImageUpload imageUpload;
    @Override
    public Customer save(CustomerDto customerDto) {
        Customer customer = new Customer();
        customer.setFirstName(customerDto.getFirstName());
        customer.setLastName(customerDto.getLastName());
        customer.setPassword(customerDto.getPassword());
        customer.setUserName(customerDto.getUserName());
        customer.setRoles(Collections.singletonList(roleRepository.findByName("CUSTOMER")));
        return customerRepository.save(customer);
    }

    @Override
    @Transactional
    public Customer saveCus(MultipartFile imageCustomer, CustomerDto customerDto) {
        Customer customer = new Customer();
        try {

            if (imageCustomer == null) {
                customer.setImage(null);
            } else {
                imageUpload.uploadImage(imageCustomer);
                System.out.println("Upload successful");
                customer.setImage(Base64.getEncoder().encodeToString(imageCustomer.getBytes()));

            }
            customer.setUserName(customerDto.getUserName());
            customer.setFirstName(customerDto.getFirstName());
            customer.setLastName(customerDto.getLastName());
            customer.setPassword(customerDto.getPassword());
            customer.setPhoneNumber(customerDto.getPhoneNumber());
            customer.setAddress(customerDto.getAddress());

            customer.setRoles(Arrays.asList(roleRepository.findByName("CUSTOMER")));
            return customerRepository.save(customer);
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    @Override
    public Customer findByUserName(String userName) {
        return customerRepository.findByUserName(userName);
    }

    @Override
    public Customer update(CustomerDto dto) {
        Customer customer = customerRepository.findByUserName(dto.getUserName());
        customer.setAddress(dto.getAddress());
        customer.setCity(dto.getCity());
        customer.setCountry(dto.getCountry());
        customer.setPhoneNumber(dto.getPhoneNumber());
        return customerRepository.save(customer);
    }
    @Override
    public CustomerDto getCustomer(String username) {
        CustomerDto customerDto = new CustomerDto();
        Customer customer = customerRepository.findByUserName(username);
        customerDto.setFirstName(customer.getFirstName());
        customerDto.setLastName(customer.getLastName());
        customerDto.setUserName(customer.getUserName());
        customerDto.setPassword(customer.getPassword());
        customerDto.setAddress(customer.getAddress());
        customerDto.setPhoneNumber(customer.getPhoneNumber());
        customerDto.setCity(customer.getCity());
        customerDto.setCountry(customer.getCountry());
        return customerDto;
    }

    @Override
    public CustomerDto getById(Long id) {
        CustomerDto customerDto = new CustomerDto();
        Customer customer = customerRepository.getById(id);
        customerDto.setId(customer.getId());
        customerDto.setFirstName(customer.getFirstName());
        customerDto.setLastName(customer.getLastName());
        customerDto.setUserName(customer.getUserName());
        customerDto.setPassword(customer.getPassword());
        customerDto.setImage(customer.getImage());
        customerDto.setPhoneNumber(customer.getPhoneNumber());
        customerDto.setAddress(customer.getAddress());
        return customerDto;
    }

    @Override
    @Transactional
    public Customer updateCus(MultipartFile imageCustomer, CustomerDto customerDto) {
        try {
            Customer customerUpdate = customerRepository.findByUserName(customerDto.getUserName());
            if (imageCustomer != null && imageCustomer.getBytes().length > 0) {
                imageUpload.uploadImage(imageCustomer);
                customerUpdate.setImage(Base64.getEncoder().encodeToString(imageCustomer.getBytes()));
                if (imageUpload.checkExisted(imageCustomer)) {
                    customerUpdate.setImage(customerUpdate.getImage());
                } else  {
                    imageUpload.uploadImage(imageCustomer);
                    customerUpdate.setImage(Base64.getEncoder().encodeToString(imageCustomer.getBytes()));
                }
            }

            // Cập nhật các thuộc tính từ customerDto
            customerUpdate.setFirstName(customerDto.getFirstName());
            customerUpdate.setLastName(customerDto.getLastName());
            customerUpdate.setUserName(customerDto.getUserName());
            customerUpdate.setPassword(customerDto.getPassword());
            customerUpdate.setPhoneNumber(customerDto.getPhoneNumber());
            customerUpdate.setAddress(customerDto.getAddress());

            return customerRepository.save(customerUpdate);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    @Transactional
    public void deleteCus(Long id) {
        try{
            customerRepository.deleteCustomerRolesByCustomerId(id);
            customerRepository.deleteById(id);
        }catch (Exception e) {
            e.printStackTrace();
            System.out.println("This customer has an order ,Delete their order first");
        }

    }





    @Override
    public Customer changePass(CustomerDto customerDto) {
        Customer customer = customerRepository.findByUserName(customerDto.getUserName());
        customer.setPassword(customerDto.getPassword());
        return customerRepository.save(customer);
    }

    @Override
    public List<CustomerDto> allCustomer() {

            List<Customer> customers = customerRepository.findAll();
            List<CustomerDto> customerDtos = transferData(customers);
            return customerDtos;

    }
    private List<CustomerDto> transferData(List<Customer> customers) {
        List<CustomerDto> customerDtos = new ArrayList<>();
        for (Customer customer : customers) {
            CustomerDto customerDto = new CustomerDto();
            customerDto.setId(customer.getId());
            customerDto.setFirstName(customer.getFirstName());
            customerDto.setLastName(customer.getLastName());
            customerDto.setUserName(customer.getUserName());
            customerDto.setImage(customer.getImage());
            customerDto.setPhoneNumber(customer.getPhoneNumber());
            customerDto.setAddress(customer.getAddress());
            customerDto.setCity(customer.getCity());
            customerDto.setCountry(customer.getCountry());

            customerDtos.add(customerDto);
        }
        return customerDtos;
    }


}
