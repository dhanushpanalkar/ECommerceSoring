package com.ecom.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import com.ecom.model.Users;
import com.ecom.repository.UserRepository;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private MongoTemplate mongoTemplate;


    @Override
    public Users getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public void saveUser(Users user) {
    	 Users existingUser = userRepository.findByEmail(user.getEmail());
         if (existingUser != null) {
             throw new RuntimeException("User already exists");
         }
    	user.setRole("ADMIN");
        userRepository.save(user);
    }

    @Override
    public String getUserRoleByUsername(String username) {
        Users user = userRepository.findByUsername(username);
        if (user != null) {
            return user.getRole(); 
        } else {
            return null; 
        }
    }
    
    public void updateUser(Users updatedUser) {
        Query query = new Query(Criteria.where("username").is(updatedUser.getUsername()));
        Update update = new Update()
            .set("password", updatedUser.getPassword())
            .set("email", updatedUser.getEmail())
            .set("phoneNumber", updatedUser.getPhoneNumber())
            .set("address", updatedUser.getAddress());
      
        mongoTemplate.updateFirst(query, update, Users.class);
    }

}
