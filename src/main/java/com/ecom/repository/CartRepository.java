package com.ecom.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.ecom.model.CartItem;
import com.ecom.model.Product;

@Repository
public interface CartRepository extends MongoRepository<CartItem, String> {
          
    void deleteByUsername(String username);

    List<CartItem> findByUsername(String username);

    CartItem findByUsernameAndProduct(String username, Product product);
}
