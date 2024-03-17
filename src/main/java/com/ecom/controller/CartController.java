package com.ecom.controller;

import com.ecom.model.CartItem;
import com.ecom.model.Product;
import com.ecom.model.Users;
import com.ecom.service.CartService;
import com.ecom.service.ProductService;
import com.ecom.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.servlet.http.HttpSession;
import java.util.List;

@Controller
@RequestMapping("/cart")
public class CartController {

    @Autowired
    private CartService cartService;

    @Autowired
    private ProductService productService;
    
    @Autowired
    private UserService userService;

    @GetMapping("")
    public String viewCart(Model model, HttpSession session) {
        String username = (String) session.getAttribute("username");
        Users users = userService.getUserByUsername(username);
        String shippingAddress = users.getAddress();
        if (username != null) {
            List<CartItem> cartItems = cartService.getCartItemsByUsername(username);
            double totalAmount = cartService.calculateTotalAmount(cartItems);
            model.addAttribute("cartItems", cartItems);
            model.addAttribute("totalAmount", totalAmount);
            model.addAttribute("shippingAddress", shippingAddress);
            return "user/cart";
        } else {
            return "redirect:/user/login";
        }
    }


    @PostMapping("/add/{productId}")
    public String addToCart(@PathVariable String productId, @RequestParam int quantity, RedirectAttributes redirectAttributes, HttpSession session) {
        String username = (String) session.getAttribute("username");
        System.out.println(quantity);
        if (username != null) {
            Product product = productService.getProductById(productId);
            if (product != null) {
                cartService.addToCart(product, quantity, username);
                redirectAttributes.addFlashAttribute("successMessage", "Product added to cart successfully");
            } else {
                redirectAttributes.addFlashAttribute("errorMessage", "Product not found");
            }
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "You need to login first");
        }
        return "redirect:/cart";
    }

    @PostMapping("/update/{cartItemId}")
    public String updateCartItem(@PathVariable String cartItemId, @RequestParam int quantity, HttpSession session) {
        String username = (String) session.getAttribute("username");
        if (username != null) {
            cartService.updateCartItemQuantity(cartItemId, quantity);
        }
        return "redirect:/cart";
    }

    @PostMapping("/remove/{cartItemId}")
    public String removeCartItem(@PathVariable String cartItemId, HttpSession session) {
        String username = (String) session.getAttribute("username");
        if (username != null) {
            cartService.removeFromCart(cartItemId);
        }
        return "redirect:/cart";
    }
}
