package com.huawei.it;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import java.io.IOException;

@WebFilter(filterName = "httpServletRequestWrapperFilter", urlPatterns = {"/*"})
public class HttpServletRequestWrapperFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        System.out.println("过滤器执行了");
        chain.doFilter(request, response);

    }

    @Override
    public void destroy() {

    }
}