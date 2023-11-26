package com.test.admin.controller;

import com.test.library.dto.AdminDto;
import com.test.library.model.Admin;
import com.test.library.model.Customer;
import com.test.library.service.impl.AdminServiceImpl;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;

@Controller
@RequiredArgsConstructor
public class LoginController {

    private final AdminServiceImpl adminService;

    private final BCryptPasswordEncoder passwordEncoder;

    @RequestMapping("/login")
    public String loginForm(Model model) {
        model.addAttribute("title","Login Page");
        return "login";
    }
    @RequestMapping("/index")
    public String home(Model model, HttpSession session, Principal principal) {
        Admin admin = adminService.findByUserName(principal.getName());
        model.addAttribute("title","Home Page");
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication == null ||authentication instanceof AnonymousAuthenticationToken){
            return "redirect:/login";
        }
        session.setAttribute("username", admin.getFirstName() + " " + admin.getLastName());
        return "index";
    }

    @GetMapping("/register")
    public String register(Model model) {
        model.addAttribute("title","Register");
        model.addAttribute("adminDto", new AdminDto());
        return "register";
    }

    @GetMapping("/forgot-password")
    public String forgotPassword(Model model) {
        model.addAttribute("title","Forgot Password");
        return "forgot-password";
    }

    @PostMapping("/register-new")
    public String addNewAdmin(@Valid @ModelAttribute("adminDto") AdminDto adminDto,
                              BindingResult result, Model model) {

        try {

            if (result.hasErrors()) {
                model.addAttribute("adminDto", adminDto);

                return "register";
            }
            String username = adminDto.getUserName();
            Admin admin = adminService.findByUserName(username);
            if (admin != null) {
                model.addAttribute("adminDto", adminDto);
                System.out.println("admin not null");
                model.addAttribute("emailError", "Your email has been registered");
                return "register";
            }
            if (adminDto.getPassword().equals(adminDto.getRepeatPassword()) && !adminDto.getPassword().isEmpty()) {
                adminDto.setPassword(passwordEncoder.encode(adminDto.getPassword()));
                adminService.save(adminDto);
                System.out.println("successfully");
                model.addAttribute("success", "Register successfully !");
                model.addAttribute("adminDto", adminDto);
            }  else {
                model.addAttribute("adminDto", adminDto);
                model.addAttribute("passwordError","Your password maybe wrong ! check again");
                System.out.println("Password not same");

            }

        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("errors", "The server has been wrong !");
        }
        return "register";


    }

}
