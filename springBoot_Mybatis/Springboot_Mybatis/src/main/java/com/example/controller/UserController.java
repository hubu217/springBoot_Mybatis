package com.example.controller;

import java.util.ArrayList;
import java.util.List;

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
    
    int count=20; 
    
    
    
    
    

    @RequestMapping("getUser/{id}")
    public String GetUser(@PathVariable int id){
    	
    	User user = userService.Sel(id);
    	
        return JSON.toJSONString(user);
    }
    
    
    
    
    @RequestMapping("sellTicket")
    public synchronized String sellTicket( ){
    	
    	
        
    	String msg ="";
    	List<Integer> lst = new ArrayList<Integer>();
    	if(count>0) {
    		  msg = "票号="+count+"; 已卖出";
    		  lst.add(count);
    		  System.out.println(msg);
    		  count--;
    		  System.out.println("=================还剩下数量="+count);
    	}
    	
    	
    	//System.out.println("总共卖出了="+lst.size());
    	
		return msg;
    	
    }
    
    
    
}
