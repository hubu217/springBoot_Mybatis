package com.example.controller.Thread.t1;

public class TicketSaler {


    public static void main(String[] args) {


        System.out.println("=========主线程开始！ 开始售票=========");

        int n = 10;
        Ticket ticket = new Ticket();
        Thread[] threads = new Thread[n];

        for (int i = 0; i < n; i++) {
            threads[i] = new Thread(ticket);
            Thread t = threads[i];
            t.setName("售票窗口:" + i);
            t.start();
        }


        //让新开的新城先执行，控制线程结束
        //thread[0]在thread[1]前执行完毕，thread[1]在thread[2]前执行完毕,thread[2]在主线程之前执行完毕
		
		/*for (int i = n - 1; i > 0; i--) {
			try {
				threads[i].join();
			} catch (InterruptedException e) { // TODO Auto-generated catch block
				e.printStackTrace();
			}
		}*/


        System.out.println("=========主线程结束! 结束售票=========");


    }

}