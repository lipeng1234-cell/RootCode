package com.example.config;


import com.example.scheduled.MyScheduled;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;

public class RootScheduledConfig {
    @ConditionalOnMissingBean(MyScheduled.class)
    @Bean
    private MyScheduled createScheduled() {
        return new MyScheduled();
    }
}
