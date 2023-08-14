package com.example.controller.Thread.t3;

import com.alibaba.fastjson.JSON;

import java.util.ArrayList;
import java.util.List;



public class Pear {


    int i =0;
    //声明一个全局变量，ArrayList线程不安全
    List<String> keyList = new ArrayList<>();




    public  String count()  {

        keyList.add("a");
        keyList.add("b");
        keyList.add("c");

        String str = JSON.toJSONString(keyList);
        System.out.println("i="+ i++);
        System.out.println();

        return str;
    }



}
