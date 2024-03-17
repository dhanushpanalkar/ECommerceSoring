package com.ecom.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ecom.model.CartItem;
import com.ecom.model.Orders;
import com.ecom.model.Product;
import com.ecom.model.Users;
import com.ecom.repository.CartRepository;
import com.ecom.repository.UserOrderRepository;

@Service
public class UserOrderService {

    @Autowired
    private UserOrderRepository userOrderRepository;
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private ProductService productService;
    
    @Autowired
    private CartRepository cartRepository;

    public String saveOrderForCurrentUser(String username, Orders order) {
        Users user = userService.getUserByUsername(username);
        List<CartItem> cartItems = cartRepository.findByUsername(username);
        
        double totalAmount = calculateTotalAmount(cartItems);
        
        order.setEmail(user.getEmail());
        order.setPhoneNumber(user.getPhoneNumber());
        order.setAddress(user.getAddress());
        order.setItems(cartItems);
        order.setOrderDate(new Date());
        order.setStatus("Waiting for Confirmation");
        order.setTotalAmount(totalAmount);
        
        for (CartItem cartItem : cartItems) {
            Product product = cartItem.getProduct();
            int quantityInCart = cartItem.getQuantity();
            int remainingQuantity = product.getQuantity() - quantityInCart;
            product.setQuantity(remainingQuantity);
            productService.updateProductQuantity(product);
        }
        
        
        Orders newOrder = userOrderRepository.save(order);
        if (newOrder.getId() != null) {
            cartRepository.deleteByUsername(username);
        }
        
        return newOrder.getId();
    }

    private double calculateTotalAmount(List<CartItem> cartItems) {
        double totalAmount = 0.0;
        for (CartItem cartItem : cartItems) {
            totalAmount += cartItem.getProduct().getPrice() * cartItem.getQuantity();
        }
        return totalAmount;
    }

    public List<Orders> getOrdersByUsername(String username) {
        return userOrderRepository.findByUsername(username);
    }

    // Method to retrieve cart items by username
    public List<CartItem> getCartItemsByUsername(String username) {
        return cartRepository.findByUsername(username);
    }
}
