package com.example.annotation.test;

import com.example.annotation.CherryAnnotation;
import com.example.annotation.Gender;
import com.example.annotation.Gender.GenderType;
import com.example.annotation.Name;
import com.example.annotation.Profile;

public class Dog {


    @Name(value = "姓名注解")
    private String var1;

    @Gender(gender = GenderType.Female)
    private String var2;

    @Profile(id = 1001, height = 180, nativePlace = "CN")
    private String var3;


    @CherryAnnotation(name = "CherryAnnotationTest", age = 23, score = {99, 66, 77})
    public String getVar1() {
        return var1;
    }

    public void setVar1(String var1) {
        this.var1 = var1;
    }

    public String getVar2() {
        return var2;
    }

    public void setVar2(String var2) {
        this.var2 = var2;
    }

    public String getVar3() {
        return var3;
    }

    public void setVar3(String var3) {
        this.var3 = var3;
    }


}