package com.ecom.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.servlet.ModelAndView;

import com.ecom.model.Orders;
import com.ecom.service.UserOrderService;

@Controller
public class OrderController {

    @Autowired
    private UserOrderService userOrderService;
    
    @PostMapping("/order/checkout")
    public ModelAndView checkout(@SessionAttribute("username") String username) {
        Orders order = new Orders();
        order.setUsername(username);
        
        String orderId = userOrderService.saveOrderForCurrentUser(username, order);      
        
        ModelAndView modelAndView = new ModelAndView("user/orderConfirmation");
        modelAndView.addObject("username", username);
        modelAndView.addObject("orderId", orderId);
        modelAndView.addObject("orderDate", order.getOrderDate());
        return modelAndView;
    }
}
