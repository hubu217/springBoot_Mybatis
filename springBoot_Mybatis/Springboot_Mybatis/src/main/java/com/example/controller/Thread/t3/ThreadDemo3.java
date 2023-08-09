package com.example.controller.Thread.t3;

import java.util.ArrayList;
import java.util.List;

public class ThreadDemo3 {





    public static void main(String[] args) {


        // 启动100个线程
        Apple apple = new Apple();
        for( int i= 0;i< 200;i++) {
           //Apple apple = new Apple();
            Thread t = new Thread(apple);
            t.start();
        }

    }












}
