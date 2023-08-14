package com.example.controller.Thread.t3;

public class ThreadDemo4 {







    public static void main(String[] args) {

        // 定义线程实现接口
        Runnable runnable = new Runnable(){

                Pear  pear = new Pear();

                @Override
                public void run() {

                    pear.count();

                }
        };

        // 启动100个线程
        for( int i= 0;i< 100;i++) {
            new Thread(runnable).start();
        }
    }


}
