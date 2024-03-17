package com.ecom.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.ecom.model.Orders;

@Repository
public interface UserOrderRepository extends MongoRepository<Orders, String> {
	List<Orders> findByUsername(String username);
}
