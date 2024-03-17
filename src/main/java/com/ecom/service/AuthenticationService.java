package com.ecom.service;

import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class AuthenticationService {

    @Autowired
    private UserService userService;

    public String authenticateUser(String username, String password, HttpSession session) {
        String role = userService.getUserRoleByUsername(username);
        if (role != null) {
            if (role.equalsIgnoreCase("admin")) {
                session.setAttribute("username", username);
                return "redirect:/admin/dashboard";
            } else if (role.equalsIgnoreCase("user")) {
                session.setAttribute("username", username);
                return "redirect:/user/index";
            }
        }
        return "redirect:/?error";
    }
}
