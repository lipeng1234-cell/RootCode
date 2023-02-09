package com.filter.config;



import com.filter.all.HttpServletRequestWrapperFilter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.core.Ordered;

public class RootFilterConfig {
    @ConditionalOnMissingBean(HttpServletRequestWrapperFilter.class)
    @Bean
    private FilterRegistrationBean<HttpServletRequestWrapperFilter> createFilterChain() {
        FilterRegistrationBean<HttpServletRequestWrapperFilter> bean = new FilterRegistrationBean<>(new HttpServletRequestWrapperFilter());
        bean.setOrder(Ordered.HIGHEST_PRECEDENCE);
        return bean;
    }
}
