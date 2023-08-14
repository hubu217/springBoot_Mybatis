package com.example.service.impl;

import com.alibaba.fastjson.JSON;
import com.example.controller.Thread.t3.Pear;
import com.example.service.IDemoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author:0xOO
 * @Date: 2018/9/26 0026
 * @Time: 15:23
 */


@Service
public class DemoServiceImpl implements IDemoService {




    @Autowired
    private DemoServiceImpl demoServiceImpl;

    List<String> overallList_service = new ArrayList<String >();



    @Override
    public String testLoaclVar1() {

        Pear pear = new Pear();
        String  str =  pear.count();

        System.out.println("[线程名]:"+Thread.currentThread().getName()+";  [此pear对象的全局变量]:"+pear+"; [overallList_pear]:"+str);

        return str;

    }


    @Override
    public String testLoaclVarShow() {

        overallList_service.add("a");
        overallList_service.add("b");
        overallList_service.add("c");
        String str = JSON.toJSONString(overallList_service);

        System.out.println("[线程名]:"+Thread.currentThread().getName()+";  [此DemoServiceImpl对象的全局变量]:"+demoServiceImpl+"; [overallList_service]:"+str);

        return str;

    }





}
