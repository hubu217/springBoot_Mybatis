package com.example.controller.Thread.t2;

import org.apache.poi.ss.formula.functions.T;

public class Number1 implements Runnable{




    private int i = 0;
    private String aa;

    @Override
    public void run() {

        while (true){
                        synchronized (this) {

                                    if(i < 100){
                                        i++;
                                        System.out.println(Thread.currentThread().getName()+"---"+i);
                                    }else{
                                        break;
                                    }
                                    try {
                                        notify();
                                        wait();
                                       // Thread.sleep(5000);

                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                        }
        }
    }


}