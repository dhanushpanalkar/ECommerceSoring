package com.ecom.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.ecom.model.Product;

@Repository
public interface ProductRepository extends MongoRepository<Product, String> {

}
