package com.example.controller.Thread.t2;

public class Number2 implements Runnable{




    private int i = 0;
    private Object object = new Object();
    String aa;

    @Override
    public void run() {

        while (true){
            synchronized (object) {
                notify();
                if(i < 100){
                    i++;
                    System.out.println(Thread.currentThread().getName()+"---"+i);
                }else{
                    break;
                }
                try {
                    wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }


}