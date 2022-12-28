package com.example.study.service;

import com.example.study.spring.*;

@Component(value = "userService")
@Scope(value = "singleton")
public class UserService implements BeanNameAired, InitializingBean, IUserService {
    @Autowired
    private OrderService orderService;

    private String beanName;

    public void test() {
        System.out.println("kkakkak" + orderService);
    }

    @PostConstruct
    public void init(){
        System.out.println("初始化方法赋值");
    }

    @Override
    public void setBeanName(String beanName) {
        this.beanName = beanName;
        System.out.println("UserService setBeanName ");
    }

    @Override
    public void afterPropertiesSet() {
        System.out.println("UserService afterPropertiesSet");
    }
}
