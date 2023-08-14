package com.example.controller.Thread.t3;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.util.StringUtil;

import java.util.ArrayList;
import java.util.List;


/**
 * 经典案例
 */
public class Apple  implements Runnable{



    //声明一个全局变量，ArrayList线程不安全
    List<String> keyList=new ArrayList<>();




    public synchronized void count2()  {

        keyList.add("a");
        keyList.add("b");
        keyList.add("c");
        keyList.add("d");
        System.out.println(Thread.currentThread().getName()+"-[keyList]: "+keyList);

        keyList.removeAll(keyList); //java.util.ConcurrentModificationException




    }


    @Override
    public  void run() {

        count2();

        /*keyList.add("a");
        keyList.add("b");
        keyList.add("c");
        keyList.add("d");
        System.out.println(Thread.currentThread().getName()+"-[keyList]: "+keyList);*/
    }
}
