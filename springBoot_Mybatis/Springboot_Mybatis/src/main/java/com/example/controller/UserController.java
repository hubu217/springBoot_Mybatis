package com.example.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.example.entity.User;
import com.example.service.IUserService;

/**
 * @Author:0xOO
 * @Date: 2018/9/26 0026
 * @Time: 14:42
 */

@RestController
@RequestMapping("/testBoot")
public class UserController {
	
	
	
	

    @Autowired
    private IUserService userService;
    
    
    
    
    

    @RequestMapping("getUser/{id}")
    public String GetUser(@PathVariable int id){
    	
    	User user = userService.Sel(id);
    	
        return JSON.toJSONString(user);
    }
    
    
    
}
