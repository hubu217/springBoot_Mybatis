package com.example.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.serializer.SimplePropertyPreFilter;
import com.example.entity.Tuser;
import com.example.entity.User;
import com.example.service.IUserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import ch.qos.logback.core.recovery.ResilientSyslogOutputStream;
import lombok.extern.slf4j.Slf4j;

/**
 * @Author:0xOO
 * @Date: 2018/9/26 0026
 * @Time: 14:42
 */
@Slf4j
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
    	
		/*	SimplePropertyPreFilter filter = new SimplePropertyPreFilter();
			filter.getExcludes().add("passWord");
			filter.getExcludes().add("id");
			String fastJsonStr = JSONObject.toJSONString(user ,  filter);*/
    	
    	String jackSonStr = new ObjectMapper().writeValueAsString(user);
    	System.out.println("jackSonStr [userName-realName]="+jackSonStr);
    	
    	
    	
    	
    	
    	String fastJsonStr = JSON.toJSONString(user);
		System.out.println("fastJsonStr [passWord-id]="+fastJsonStr );
		
		
		JSONObject jsonObj = JSON.parseObject(fastJsonStr, JSONObject.class);
		
		System.out.println("拼接前 jsonObj="+JSON.toJSONString(jsonObj));
		
		Tuser  tuser = new Tuser();
		tuser.setCreatedatetime(new Date());
		tuser.setName("tuser");
		
		Set<String> troles = new HashSet<String>();
		troles.add("set1");
		troles.add("set2");
		troles.add("set3");
		tuser.setTroles(troles);
		
		jsonObj.put("tname", tuser.getName());
		jsonObj.put("tcreattime", tuser.getCreatedatetime());
		jsonObj.put("troles", tuser.getTroles());
    	
    			
		System.out.println("拼接后 jsonObj="+JSON.toJSONString(jsonObj));
	}
    
    
}
