package com.huawei.it.bytebuddy;

import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import javax.servlet.http.HttpServletRequest;

public class MyInterceptor {

    public static String intercept() {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = (HttpServletRequest) requestAttributes.resolveReference(RequestAttributes.REFERENCE_REQUEST);
        String requestURI = request.getRequestURI();
        System.out.println(requestURI);
        return requestURI;
    }
}
