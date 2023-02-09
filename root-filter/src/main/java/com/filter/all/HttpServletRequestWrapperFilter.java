package com.filter.all;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

//@WebFilter(filterName = "httpServletRequestWrapperFilter", urlPatterns = {"/*"})
public class HttpServletRequestWrapperFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        System.out.println("HttpServletRequestWrapperFilter 过滤器执行了");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        StringBuffer requestURL = httpRequest.getRequestURL();
        System.out.println("Root filter 过滤器执行了" + requestURL);
        chain.doFilter(request, response);

    }

    @Override
    public void destroy() {

    }
}