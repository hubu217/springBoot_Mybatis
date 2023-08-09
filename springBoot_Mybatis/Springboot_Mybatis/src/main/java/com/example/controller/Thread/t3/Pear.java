package com.example.controller.Thread.t3;

import java.util.ArrayList;
import java.util.List;

public class Pear {



    //声明一个全局变量，ArrayList线程不安全
    List<String> keyList=new ArrayList<>();




    public  void count2()  {

        keyList.add("a");
        keyList.add("b");
        keyList.add("c");
        keyList.add("d");
        System.out.println(Thread.currentThread().getName()+"-[keyList]: "+keyList);

    }



}
