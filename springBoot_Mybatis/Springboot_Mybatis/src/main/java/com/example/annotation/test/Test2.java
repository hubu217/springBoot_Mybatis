package com.example.annotation.test;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

import com.alibaba.fastjson.JSON;
import com.example.annotation.CherryAnnotation;


public class Test2 {


    public static void main(String[] args) {

        try {
            //获取Student的Class对象
            Class stuClass = Class.forName("com.example.annotation.test.Dog");
            Annotation[] annotationArr = stuClass.getAnnotations();
            for (Annotation an : annotationArr) {
                Class cls = an.getClass();
                System.out.println("class=" + cls + ";  className=" + cls.getName());
            }

            //说明一下，这里形参不能写成Integer.class，应写为int.class
            Method stuMethod = stuClass.getMethod("getVar1", null);

            if (stuMethod.isAnnotationPresent(CherryAnnotation.class)) {
                System.out.println("Dog类上配置了CherryAnnotation注解！");
                //获取该元素上指定类型的注解
                CherryAnnotation cherryAnnotation = stuMethod.getAnnotation(CherryAnnotation.class);
                String name = cherryAnnotation.name();
                int age = cherryAnnotation.age();
                int[] score = cherryAnnotation.score();

                System.out.println("name=" + name + ", age=" + age + ", score= " + JSON.toJSONString(score));

            } else {
                System.out.println("Dog类上没有配置CherryAnnotation注解！");
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }


}
