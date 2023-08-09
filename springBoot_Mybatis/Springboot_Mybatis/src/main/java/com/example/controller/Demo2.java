package com.example.controller;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.web.bind.annotation.*;


@Slf4j
@RestController
@RequestMapping("/demo2")
public class Demo2 {






    @RequestMapping(value = "/premain", method = RequestMethod.POST)
    @ResponseBody
    public  void premain(@RequestBody String args) {


        Demo2  demo2 = new Demo2();

        demo2.showA("A");
        demo2.showD("D");

        System.out.println("end!!!");
        System.out.println("end2!!!");
        System.out.println("end3!!!");


}



    public void showA(String a){

        System.out.println("====showA="+a);

        showB(a);

    }


    public void showB(String b){

        System.out.println("===showB="+b);

        showC(b);

    }

    public void showC(String c){

        System.out.println("===showC="+c);
        showD(c);

    }

    public void showD(String d){

        System.out.println("showD="+d);

    }

    public void showE(String e){

        System.out.println("showE="+e);

    }





}
