package com.ecom.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ecom.model.Users;
import com.ecom.repository.UserRepository; // Adjust import for UserRepository

@Service
public class AdminUserService {

    @Autowired
    private UserRepository userRepository; 

    public List<Users> getAllUsers() {
        return userRepository.findAll(); 
    }

    public Users getUserById(String id) { 
        return userRepository.findById(id).orElse(null); 
    }

    public void saveUser(Users user) {
        userRepository.save(user); 
    }

    public void deleteUser(String id) { 
        userRepository.deleteById(id); 
    }
}
