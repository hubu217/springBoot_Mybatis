package com.example;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

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
