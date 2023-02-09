package com.huawei.it.limit;

public class LeakBucketLimitTryAcquire {

    private long leakRate;

    private long currentWater;

    private long requireTime;

    private long capacity;



//    boolean leakBucketLimitTryAcquire(){
//        long currentTimeMillis = System.currentTimeMillis();
//        long outWater =(currentTimeMillis-requireTime)/1000*leakRate;
//        long currentWater = Math.max(0, currentWater - outWater);
//        requireTime=currentTimeMillis;
//        if (currentWater<capacity){
//            currentWater++;
//            return true;
//        }
//        return true;
//    }


}
