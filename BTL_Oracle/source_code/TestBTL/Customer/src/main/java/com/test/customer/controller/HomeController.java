package com.test.customer.controller;

import com.test.library.dto.ProductDto;
import com.test.library.model.Category;
import com.test.library.model.Customer;
import com.test.library.model.Product;
import com.test.library.model.ShoppingCart;
import com.test.library.service.CategoryService;
import com.test.library.service.CustomerService;
import com.test.library.service.ProductService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.security.Principal;
import java.util.List;

@Controller
@RequiredArgsConstructor

public class HomeController {

    private final CustomerService customerService;
    @RequestMapping(value ={"/index","/"},method= RequestMethod.GET)
    public String home(Model model, Principal principal, HttpSession session){
        model.addAttribute("title", "Home");
        model.addAttribute("page", "Home");
        if (principal != null) {
            Customer customer = customerService.findByUserName(principal.getName());
            session.setAttribute("username", customer.getFirstName() + " " + customer.getLastName());
            ShoppingCart shoppingCart = customer.getShoppingCart();
            if (shoppingCart != null) {
                session.setAttribute("totalItems", shoppingCart.getTotalItems());
            }
        }
        return "home";
    }
    @GetMapping("/contact")
    public String contact(Model model) {
        model.addAttribute("title", "Contact");
        model.addAttribute("page", "Contact");
        return "contact-us";
    }

}
