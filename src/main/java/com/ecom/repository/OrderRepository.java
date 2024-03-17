package com.ecom.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.ecom.model.Orders;

@Repository
public interface OrderRepository extends MongoRepository<Orders, String> {
}
