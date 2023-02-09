package com.huawei.it.limit;

import java.util.concurrent.atomic.AtomicInteger;

public class FixedWindowsTryAcquire {
    private AtomicInteger counter = new AtomicInteger();
    private Long lastRequestTime;

    private Long windowUntie = 1L;

    private Long limitValue = 1000L;

    Boolean fixedWindowsTryAcquire() {
        long currentTimeMillis = System.currentTimeMillis();
        if (currentTimeMillis - lastRequestTime > windowUntie) {
            counter.set(0);
            lastRequestTime = currentTimeMillis;
        }
        if (counter.get() < limitValue) {
            counter.incrementAndGet();
            return true;
        }
        return false;
    }
}
