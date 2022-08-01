package com.example.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.serializer.SimplePropertyPreFilter;
import com.example.entity.User;
import com.example.service.IUserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import ch.qos.logback.core.recovery.ResilientSyslogOutputStream;

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
    	
    	User user2 = userService.Sel(id);
    	
    	User user = new User();
    	user.setId(1);
    	user.setPassWord("password");
    	user.setRealName("realName");
    	user.setUserName("userName");
    	
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
    
    
    public static void main(String[] args) throws JsonProcessingException {
		
    	User user = new User();
    	user.setId(1);
    	user.setPassWord("password");
    	user.setRealName("realName");
    	user.setUserName("userName");
    	
		/*SimplePropertyPreFilter filter = new SimplePropertyPreFilter();
		filter.getExcludes().add("passWord");
		
		
		System.out.println(JSON.toJSONString(user,filter, SerializerFeature.WriteMapNullValue));*/
    	
    	String jsonStr = new ObjectMapper().writeValueAsString(user);
    	
    	
    	System.out.println(JSON.toJSON(jsonStr));
    	
    			
    	
	}
    
    
}
