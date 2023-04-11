package com.example;

import com.example.loader.ClassLoaderManager;

import java.lang.instrument.Instrumentation;
import java.util.Map;

public class AgentCoreEntrance {

    public static void run(Map<String, Object> argsMap, Instrumentation instrumentation) throws Exception {
        // 初始化框架类加载器
        ClassLoaderManager.init(argsMap);


    }
}
