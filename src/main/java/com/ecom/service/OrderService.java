package com.ecom.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import com.ecom.model.Orders;
import com.ecom.repository.OrderRepository;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;
    
    @Autowired
    private MongoTemplate mongoTemplate;

    public List<Orders> getAllOrders() {
        return orderRepository.findAll();
    }

    public Orders getOrderById(String id) {
        return orderRepository.findById(id).orElse(null);
    }
    
    public void saveOrder(Orders orders) {
        orderRepository.save(orders);
    }
    
    public void updateOrderStatus(String orderId, String status) {
        Query query = new Query(Criteria.where("id").is(orderId));
        Update update = new Update();
        update.set("status", status);
        mongoTemplate.updateFirst(query, update, Orders.class);
    }
    

}