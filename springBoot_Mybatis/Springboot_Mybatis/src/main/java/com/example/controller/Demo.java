package com.example.controller;

import org.apache.commons.lang.StringUtils;

public class Demo {


    public static void main(String[] args) {

    String t = "aabbccdd";
       boolean  flag = StringUtils.equals("aa",t.substring(0, 40));
       System.out.println("flag="+flag);

       String temp = null;
       String tempSUb = StringUtils.substring(t, 0 , 500);
       System.out.println("tempSUb="+tempSUb);
    }
}
