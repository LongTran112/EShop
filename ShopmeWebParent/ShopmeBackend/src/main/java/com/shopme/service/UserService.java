package com.shopme.service;

import com.shopme.common.entity.Role;
import com.shopme.common.entity.User;
import com.shopme.repository.RoleRepository;
import com.shopme.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepo;
    @Autowired
    private RoleRepository roleRepo;

    public List<User> getAllUser(){
        return (List<User>) userRepo.findAll();
    }

    public List<Role> getAllRole(){
        return (List<Role>) roleRepo.findAll();
    }

    public void save(User user){
        user.setPassword(encodePassword(user.getPassword()));
        userRepo.save(user);
    }

    public String encodePassword(String password){
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        return encoder.encode(password);
    }
}
