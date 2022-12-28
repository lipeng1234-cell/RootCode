package com.example.agentdemo.agent;

import net.bytebuddy.implementation.bind.annotation.Origin;
import net.bytebuddy.implementation.bind.annotation.SuperCall;

import java.lang.reflect.Method;
import java.util.concurrent.Callable;

public class MyInterceptor {

    public static Object intercept(@Origin Method method, @SuperCall Callable<?> callable) throws Exception {
        long currentTimeMillis = System.currentTimeMillis();
        try {
            return callable.call();
        } finally {
            System.out.println("方法执行时间"+(System.currentTimeMillis()-currentTimeMillis));
        }

    }
}
