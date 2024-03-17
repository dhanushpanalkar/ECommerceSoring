package com.ecom.service;

import com.ecom.model.CartItem;
import com.ecom.model.Product;
import com.ecom.model.Users;
import com.ecom.repository.CartRepository;
import com.ecom.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CartService {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private UserRepository userRepository;

    public List<CartItem> getCartItemsByUsername(String username) {
        return cartRepository.findByUsername(username);
    }

    public void addToCart(Product product, int quantity, String username) {
        Users user = userRepository.findByUsername(username);
        if (user != null) {
            CartItem existingItem = cartRepository.findByUsernameAndProduct(username, product);
            if (existingItem != null) {
                existingItem.setQuantity(existingItem.getQuantity() + quantity);
            } else {
                CartItem cartItem = new CartItem();
                cartItem.setUsername(username);
                cartItem.setProduct(product);
                cartItem.setQuantity(quantity);
                cartRepository.save(cartItem);
            }
        }
    }

    public void removeFromCart(String cartItemId) {
        cartRepository.deleteById(cartItemId);
    }

    public double calculateTotalAmount(List<CartItem> cartItems) {
        double totalAmount = 0.0;
        for (CartItem cartItem : cartItems) {
            totalAmount += cartItem.getProduct().getPrice() * cartItem.getQuantity();
            cartItem.setTotal(totalAmount);
            cartRepository.save(cartItem);
        }
        return totalAmount;
    }

    public void updateCartItemQuantity(String cartItemId, int quantity) {
        CartItem cartItem = cartRepository.findById(cartItemId).orElse(null);
        if (cartItem != null) {
            cartItem.setQuantity(quantity);
            cartRepository.save(cartItem);
        }
    }
}
