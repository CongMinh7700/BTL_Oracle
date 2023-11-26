package com.test.admin.controller;

import com.test.library.dto.CustomerDto;
import com.test.library.dto.ProductDto;
import com.test.library.model.Category;
import com.test.library.service.CategoryService;
import com.test.library.service.CustomerService;
import com.test.library.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.List;
@Controller
@RequiredArgsConstructor
public class CustomerController {
    private final CustomerService customerService;
    private final BCryptPasswordEncoder passwordEncoder;
    @GetMapping("/customers")
    public String products(Model model, Principal principal) {
        if(principal == null){
            return "redirect:/login";
        }
        List<CustomerDto> customerDtoList =customerService.allCustomer();
        model.addAttribute("title","Manage Customers");
        model.addAttribute("customers", customerDtoList);
        model.addAttribute("size",customerDtoList.size());
        return "customers";
    }

    @GetMapping("/add-customer")
    public String addCustomerForm(Model model,Principal principal){
        if(principal == null){
            return "redirect:/login";
        }
        model.addAttribute("title", "Add New Customer");
        model.addAttribute("customerDto",new CustomerDto());
        return "add-customer";
    }
    @PostMapping("/save-customer")
    public String saveCustomer(@ModelAttribute("customerDto") CustomerDto customerDto,
                              @RequestParam("imageCustomer") MultipartFile imageCustomer,
                              RedirectAttributes attributes, Principal principal){
        try{
            if(principal == null){
                return "redirect:/login";
            }
            customerDto.setPassword(passwordEncoder.encode(customerDto.getPassword()));
            customerService.saveCus(imageCustomer,customerDto);
            attributes.addFlashAttribute("success","Add customer success");
        }catch(Exception e){
            e.printStackTrace();
            attributes.addFlashAttribute("error","Failed to add");
        }
        return "redirect:/customers";
    }
    @GetMapping("/update-customer/{id}")
    public String updateCustomerForm(@PathVariable("id") Long id, Model model, Principal principal){
        if(principal == null){
            return "redirect:/customers";
        }
        model.addAttribute("title","Update Customer");
        CustomerDto customerDto = customerService.getById(id);
        model.addAttribute("customerDto",customerDto);
        return "update-customer";

    }
    @PostMapping("/update-customer/{id}")
    public String updateCustomer(@ModelAttribute("customerDto") CustomerDto customerDto,
                                @RequestParam("imageCustomer") MultipartFile imageCustomer,
                                RedirectAttributes attributes,Principal principal){
        try{
            if(principal == null){
                return "redirect:/login";
            }
            customerDto.setPassword(passwordEncoder.encode(customerDto.getPassword()));
            customerService.updateCus(imageCustomer,customerDto);
            attributes.addFlashAttribute("success","Update successfully");
            System.out.println("Updating customer with ID: " + customerDto.getId());
            System.out.println("Received customer data: " + customerDto);

        }catch (Exception e){
            e.printStackTrace();
            attributes.addFlashAttribute("error","Error server, please try again!");
            System.out.println("ERORR customer data: " + customerDto);
        }
        return "redirect:/customers";
    }
    @RequestMapping(value = "/delete-customer", method = {RequestMethod.PUT, RequestMethod.GET})
    public String deleteCustomer(Long id, Principal principal,RedirectAttributes attributes) {
        try{
            if (principal == null) {
                return "redirect:/login";
            } else {
                customerService.deleteCus(id);
                attributes.addFlashAttribute("success","Delete successfully");

            }
        }catch (Exception e){
            e.printStackTrace();
            attributes.addFlashAttribute("error","This customer has an order ,Delete their order first");
        }
        return "redirect:/customers";
    }

}
