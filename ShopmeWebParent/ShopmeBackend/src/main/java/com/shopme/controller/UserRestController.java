package com.shopme.controller;

import com.shopme.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserRestController {

    @Autowired
    private UserService userService;

//    @PostMapping("/users/check_email")
//    public String checkEmail(){
//        return
//    }
}
