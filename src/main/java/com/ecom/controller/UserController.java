package com.ecom.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import com.ecom.model.Orders;
import com.ecom.model.Product;
import com.ecom.model.Users;
import com.ecom.service.AuthenticationService;
import com.ecom.service.ProductService;
import com.ecom.service.UserOrderService;
import com.ecom.service.UserService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserOrderService userOrderService;

    @Autowired
    private ProductService productService;
    
    @Autowired
    private AuthenticationService authenticationService;
    
   

    @PostMapping("/login")
    public String login(@RequestParam("username") String username,
                        @RequestParam("password") String password,
                        HttpSession session) {
        return authenticationService.authenticateUser(username, password, session);
    }
    
    @GetMapping("/index")
    public String redirectToIndex() {
        return "redirect:/user/products";
    }

    
    @GetMapping("/profile")
    public String showProfile(HttpServletRequest request, Model model) {
        String username = (String) request.getSession().getAttribute("username");
        Users user = userService.getUserByUsername(username);
        model.addAttribute("user", user);
        return "user/profile";
    }

    @PostMapping("/profile/update")
    public String updateProfile(@Validated @ModelAttribute("user") Users updatedUser, BindingResult bindingResult, Model model, HttpSession session) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("errorMessage", "Check For inputs");
            model.addAttribute("user", updatedUser);
            return "user/profile";
        }
        String username = updatedUser.getUsername();
        try {
            userService.updateUser(updatedUser);
            model.addAttribute("successMessage", "Profile updated successfully");
            return "user/profile";
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Failed to update profile");
            Users user = userService.getUserByUsername(username);
            model.addAttribute("user", user);
            return "user/profile";
        }
    }

    @GetMapping("/orders")
    public String viewOrders(Model model, HttpSession session) {
        String username = (String) session.getAttribute("username");
        if (username != null) {
            Users users = userService.getUserByUsername(username);
            List<Orders> orders = userOrderService.getOrdersByUsername(users.getUsername());
            model.addAttribute("orders", orders);
        }
        return "user/orderHistory";
    }


    @GetMapping("/login")
    public String showLoginForm() {
        return "public/login";
    }
    

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new Users());
        return "public/register";
    }


    @PostMapping("/register")
    public String registerUser(@ModelAttribute("user") @Valid Users users, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            return "public/register";
        }
        try {
            userService.saveUser(users);
            model.addAttribute("successMessage", "Registration successful! Please ");
            model.addAttribute("user", new Users());
            return "public/register"; 
        } catch (RuntimeException e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "public/register";
        }
    }


    @GetMapping({"/products"})
    public String getAllProducts(Model model) {
        List<Product> products = productService.getAllProducts();
        model.addAttribute("products", products);
        return "user/index";
    }
    
      
    @GetMapping("/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        return "redirect:/";
    }
}