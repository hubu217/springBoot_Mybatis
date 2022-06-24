package com.example.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.example.service.IUnionPayService;

/**
 * @Author:0xOO
 * @Date: 2018/9/26 0026
 * @Time: 14:42
 */

@RestController
@RequestMapping("/unionPayController")
public class UnionPayController {
	
	
	
	

    @Autowired
    private IUnionPayService unionPayService;

    
    
    
    
    
    
    
    @RequestMapping(value="/getUnionPayCode",method=RequestMethod.POST)
    public String getUnionPayCode(@RequestBody Map<String,String> map){
    	
    	System.out.println("getUnionPayCode() 入参 map="+JSON.toJSONString(map));
    	
    	Map<String, String> targetMap = unionPayService.getUnionPayCode(map);
    	System.out.println("targetMap="+JSON.toJSONString(targetMap));
    	
    	String targetStr = JSON.toJSONString(targetMap);
    	
    	return targetStr;
    }
    
    
    
    
    
    
}
