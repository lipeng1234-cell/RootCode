package com.example.loader;

import com.example.common.CommonConstant;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ClassLoaderManager {
    public static void init(Map<String, Object> argsMap) throws MalformedURLException {

        initCommonClassLoader(argsMap.get(CommonConstant.COMMA).toString());


    }

    private static void initCommonClassLoader(String path) throws MalformedURLException {
        URL[] commonLibUrls = listCommonLibUrls(path);
    }

    private static URL[] listCommonLibUrls(String commonLibPath) throws MalformedURLException {
        File commonLibDir = new File(commonLibPath);
        File[] jars = commonLibDir.listFiles((file, name) -> name.endsWith(".jar"));
        List<URL> urlList = new ArrayList<>();
        for (File jar : jars) {
            urlList.add(jar.toURI().toURL());
        }
        return urlList.toArray(new URL[0]);
    }



}
