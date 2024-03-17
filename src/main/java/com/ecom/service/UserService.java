package com.ecom.service;

import com.ecom.model.Users;

public interface UserService {
    Users getUserByUsername(String username);
    void saveUser(Users user);
    String getUserRoleByUsername(String username);
	void updateUser(Users updatedUser);
	  
}