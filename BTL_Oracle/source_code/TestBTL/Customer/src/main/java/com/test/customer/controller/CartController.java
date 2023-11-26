package com.test.customer.controller;

import com.test.library.dto.ProductDto;
import com.test.library.model.Customer;
import com.test.library.model.Product;
import com.test.library.model.ShoppingCart;
import com.test.library.service.CustomerService;
import com.test.library.service.ProductService;
import com.test.library.service.ShoppingCartService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@Controller
@RequiredArgsConstructor
public class CartController {

    private final CustomerService customerService;

    private final ProductService productService;

    private final ShoppingCartService cartService;

    @GetMapping("/cart")
    public String cart(Model model, Principal principal, HttpSession session) {
        if (principal == null) {
            return "redirect:/login";
        }
        Customer customer = customerService.findByUserName(principal.getName());
        ShoppingCart cart = customer.getShoppingCart();
        if (cart == null) {
            model.addAttribute("check");

        }
        if (cart != null) {
            model.addAttribute("grandTotal", cart.getTotalPrices());

            session.setAttribute("totalItems", cart.getTotalItems());
        }
        model.addAttribute("shoppingCart", cart);
        model.addAttribute("title", "Cart");
        //model.addAttribute("subTotal", cart.getTotalPrices());

        return "cart";


    }

    @PostMapping("/add-to-cart")
    public String addItemToCart(@RequestParam("id") Long productId,
                                @RequestParam(value ="quanity",required = false,defaultValue = "1") int quantity,
                                Principal principal,
                                Model model,
                                HttpServletRequest request,
                                HttpSession session){
ProductDto productDto = productService.getById(productId);
        if(principal == null){
            return "redirect:/login";
        }
        String userName = principal.getName();
        ShoppingCart shoppingCart = cartService.addItemToCart(productDto, quantity,userName);
        session.setAttribute("totalItems", shoppingCart.getTotalItems());
        model.addAttribute("shoppingCart", shoppingCart);
        return "redirect:" + request.getHeader("Referer");
    }

    @RequestMapping(value = "/update-cart", method = RequestMethod.POST, params = "action=update")
    public String updateCart(@RequestParam("id") Long id,
                             @RequestParam("quantity") int quantity,
                             Model model,
                             Principal principal,
                             HttpSession session) {
        if (principal == null) {
            return "redirect:/login";
        }
        ProductDto productDto = productService.getById(id);
        String username = principal.getName();
        ShoppingCart shoppingCart = cartService.updateCart(productDto, quantity, username);
        model.addAttribute("shoppingCart", shoppingCart);
        session.setAttribute("totalItems", shoppingCart.getTotalItems());
        return "redirect:/cart";

    }

    @RequestMapping(value = "/update-cart", method = RequestMethod.POST, params = "action=delete")
    public String deleteItem(@RequestParam("id") Long id,
                             Model model,
                             Principal principal,
                             HttpSession session
    ) {
        if (principal == null) {
            return "redirect:/login";
        } else {
            ProductDto productDto = productService.getById(id);
            String username = principal.getName();
            ShoppingCart shoppingCart = cartService.removeItemFromCart(productDto, username);
            model.addAttribute("shoppingCart", shoppingCart);
            session.setAttribute("totalItems", shoppingCart.getTotalItems());
            return "redirect:/cart";
        }
    }

}
