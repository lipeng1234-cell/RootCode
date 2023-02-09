package com.huawei.it.controller;


import com.huawei.it.bytebuddy.MyInterceptor;
import lombok.extern.slf4j.Slf4j;
import net.bytebuddy.ByteBuddy;
import net.bytebuddy.dynamic.DynamicType;
import net.bytebuddy.implementation.MethodDelegation;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static net.bytebuddy.dynamic.loading.ClassLoadingStrategy.Default.INJECTION;
import static net.bytebuddy.matcher.ElementMatchers.named;

@RestController
@RequestMapping("/project")
@Slf4j
public class DailyProjectController {


    @RequestMapping("/queryProjectInfo")
    public String queryProjectInfo() throws InstantiationException, IllegalAccessException {
        log.info("queryProjectInfo 方法執行了");
        DynamicType.Unloaded<DailyProjectController> queryProjectInfo = new ByteBuddy()

                .subclass(DailyProjectController.class)

                .method(named("queryProjectInfo"))

                // 拦截DB.hello()方法，并委托给 Interceptor中的静态方法处理

                .intercept(MethodDelegation.to(MyInterceptor.class))

                .make();

        String helloWorld = queryProjectInfo

                .load(ClassLoader.getSystemClassLoader(), INJECTION)

                .getLoaded()

                .newInstance()

                .queryProjectInfo();


        return "genProjectService.queryProjectInfo()";
    }


}
