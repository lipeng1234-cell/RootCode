package com.example.study.service;

import com.example.study.spring.LiPenApplicationContext;

public class Test {

    public static void main(String[] args) {
        LiPenApplicationContext myContext = new LiPenApplicationContext(AppConfig.class);
        IUserService userService = (IUserService) myContext.getBean("userService");
        userService.test();
    }
}
