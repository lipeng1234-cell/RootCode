package com.example.scheduled;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class MyScheduled {

    //执行任务
    public MyScheduled() {
        ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(10);
        scheduledExecutorService.scheduleAtFixedRate(this::invokeTasK,1,2, TimeUnit.SECONDS);// 1s 后开始执行，每 2s 执行一次
    }
    private void invokeTasK(){
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss.SSS");
        String dateStr = sdf.format(new Date());
        System.out.println("ScheduledExecutorService执行定时任务：" + dateStr);
    }
}
