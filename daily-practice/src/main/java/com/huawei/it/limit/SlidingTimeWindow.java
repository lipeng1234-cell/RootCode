package com.huawei.it.limit;

import org.springframework.scheduling.annotation.Scheduled;

import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.atomic.AtomicInteger;

public class SlidingTimeWindow {


    private int window_spec = 200;

    private int window_size = 5;

    private int limitValue = 500;

    private Long lastRequestTime;


    private final TreeMap<Long, AtomicInteger> buckets = new TreeMap<>();


    Boolean slidingWindowsTyrAcquire() {
        long currentTimeMillis = System.currentTimeMillis();
        long key = currentTimeMillis - (currentTimeMillis % window_spec);
        canPassCalculate(currentTimeMillis,key);
        return true;
    }

    private boolean canPassCalculate(long currentTimeMillis,long key) {
        Iterator<Map.Entry<Long, AtomicInteger>> iterator = buckets.entrySet().iterator();
        AtomicInteger counter = new AtomicInteger();
        while (iterator.hasNext()){
            Map.Entry<Long, AtomicInteger> next = iterator.next();
            AtomicInteger value = next.getValue();
            counter.addAndGet(value.get());
        }
        if (counter.get()<=limitValue){
            buckets.get(key).incrementAndGet();
            return true;
        }
            return false;
    }

    @Scheduled
    private void scheduleClearMethod(){
        Iterator<Map.Entry<Long, AtomicInteger>> iterator = buckets.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<Long, AtomicInteger> next = iterator.next();
            if (System.currentTimeMillis() - window_spec * (window_size) > next.getKey()) {
                iterator.remove();
            }
        }
    }

}
