package com.example.controller.Thread.t1;

/*同一对象的多个线程thread0/1/2，对共享变量count的操作，需要将count的值声明为volatile
 * 并且因为多个线程操作的是同一个对象ticket，因此count是资源共享的
 *
 */

public class Ticket implements Runnable {


    volatile int count = 1000;   //总共的票数量
    Boolean flag = true;
    Object locObj = new Object();


    @Override
    public void run() {

        while (count > 0) {

            getTicket();


        }

        //线程结束后的处理程序
        System.out.println(Thread.currentThread().getName() + "执行结束");

    }

    //定义了一个同步方法  此方法同时只能被一个线程执行完毕后，另一个线程才能够执行

    public void getTicket() {

        if (count > 0) {
            System.out.println(Thread.currentThread().getName() + ":卖出一张票-票号=" + count);
            count--;

            // Thread.yield();

        }
    }

}