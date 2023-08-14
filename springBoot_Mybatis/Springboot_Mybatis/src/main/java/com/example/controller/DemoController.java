package com.example.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.entity.User;
import com.example.service.IDemoService;
import com.example.service.IUserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.apache.poi.poifs.filesystem.DirectoryEntry;
import org.apache.poi.poifs.filesystem.DocumentEntry;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * @see 全局变量 和局部变量 的并发问题模拟
 * @Date: 2018/9/26 0026
 * @Time: 14:42
 */
@Slf4j
@RestController
@RequestMapping("/demoController")
public class DemoController {




    @Autowired
    private DemoController demoController;

    @Autowired
    private IDemoService demoService;


    int count = 0;

    List<String> overallList_controler = new ArrayList<String>();





    @RequestMapping("/testLoaclVar1")
    public String testLoaclVar1( ) {


            return  demoService.testLoaclVar1();
    }


    @RequestMapping("/testLoaclVar2")
    @ResponseBody
    public String testLoaclVar2( ) {

    // count++;
     //System.out.println("全局变量 count="+count);

      overallList_controler.add("a");
      overallList_controler.add("b");
      overallList_controler.add("c");


      String str = JSON.toJSONString(overallList_controler);
      System.out.println("[线程名]:"+Thread.currentThread().getName()+";  [此DemoController对象的全局变量]:"+demoController+"; [overallList_controler]:"+str);


        return  str;
    }


    @RequestMapping("/testLoaclVar3")
    @ResponseBody
    public String testLoaclVar3( ) {


        return   demoService.testLoaclVarShow();
    }














}
