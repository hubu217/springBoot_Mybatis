package com.example;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.context.annotation.Bean;
import org.springframework.http.converter.HttpMessageConverter;

import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;

@MapperScan("com.example.mapper") //扫描的mapper
@SpringBootApplication
public class DemoApplication {


    public static void main(String[] args) {

        System.out.println("kkkkkkkkk");


        SpringApplication.run(DemoApplication.class, args);
    }
	
	
	/*@Bean
	public HttpMessageConverters fastJsonHttpMessageConverters() {
		
	        FastJsonHttpMessageConverter fastConverter = new FastJsonHttpMessageConverter();
	        FastJsonConfig fastJsonConfig = new FastJsonConfig();
	        fastJsonConfig.setSerializerFeatures(SerializerFeature.PrettyFormat);
	        fastConverter.setFastJsonConfig(fastJsonConfig);
	        HttpMessageConverter<?> converter = fastConverter;
	        
	        return new HttpMessageConverters(converter);
	}*/

}
