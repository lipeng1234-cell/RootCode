package com.huawei.it.common;

import java.io.File;

public class PathDeclarer {

    public static String getAgentPath() {
        return new File(PathDeclarer.class.getProtectionDomain().getCodeSource().getLocation().getPath()).getParent();
    }

    public static String getCorePath() {
        return "D:\\project\\RootCode\\root-scheduled\\target";
    }





}
