package com.example.annotation.test;

import java.lang.reflect.*;

import com.alibaba.fastjson.JSON;
import com.example.annotation.Gender;
import com.example.annotation.Name;
import com.example.annotation.Profile;

public class CustomUtils {
	
	
	
	
	
 
    public static void getInfo(Class<?> clazz) {
    	
	        
	        Field[] fields = clazz.getDeclaredFields();
	        
	        for (Field field : fields) {
	        	
			            if (field.isAnnotationPresent(Name.class)) {
				                Name arg0 = field.getAnnotation(Name.class);
				                String name =  arg0.value();  
				                System.out.println("Gmw  name=" + name);
			            }
			            
			            if (field.isAnnotationPresent(Gender.class)) {
				                Gender arg0 = field.getAnnotation(Gender.class);
				                String gender = JSON.toJSONString(arg0.gender());
				                System.out.println("Gmw  gender=" + gender);
			            }
			            
			            if (field.isAnnotationPresent(Profile.class)) {
				                Profile arg0 = field.getAnnotation(Profile.class);
				               
				                int id = arg0.id();
				                int height = arg0.height();
				                String nativePlace = arg0.nativePlace();
				                
				                System.out.println("Gmw   profile id="+id+"; height="+height+"; nativePlace="+nativePlace);
			            }
	        }
    }
 
}