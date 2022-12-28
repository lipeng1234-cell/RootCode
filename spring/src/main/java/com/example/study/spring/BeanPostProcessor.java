package com.example.study.spring;

public interface BeanPostProcessor {

    public Object postProcessBeforeInitialization(String beanName, Object bean);

    public Object postProcessAfterInitialization(String beanName, Object bean);

}
