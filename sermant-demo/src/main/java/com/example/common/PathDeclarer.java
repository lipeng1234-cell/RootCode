package com.example.common;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.net.URL;
import java.security.ProtectionDomain;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.jar.JarFile;
import java.util.logging.ConsoleHandler;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

public class PathDeclarer {

    public static String getAgentPath() {
        return new File(PathDeclarer.class.getProtectionDomain().getCodeSource().getLocation().getPath()).getParent();
    }

    public static String getCorePath() {
        return getAgentPath()+ File.separatorChar + "core";
    }


    public static void main(String[] args) throws IOException {
        //Step-1
        ProtectionDomain protectionDomain = PathDeclarer.class.getProtectionDomain();
        System.out.println(protectionDomain);
        // (file:/D:/project/RootCode/sermant-demo/target/classes/ <no signer certificates>)
        System.out.println(protectionDomain.getCodeSource());
        // file:/D:/project/RootCode/sermant-demo/target/classes/
        System.out.println(protectionDomain.getCodeSource().getLocation());
        // /D:/project/RootCode/sermant-demo/target/classes/
        System.out.println(protectionDomain.getCodeSource().getLocation().getPath());
        // D:\project\RootCode\sermant-demo\target
        System.out.println(getAgentPath());
        // D:\project\RootCode\sermant-demo\target\core
        System.out.println(getCorePath());
        //Step-2
        File filePath = new File("D:\\project\\RootCode\\agent-demo\\target");
        String[] list = filePath.list(new MyFilenameFilter());
        Arrays.sort(list, String.CASE_INSENSITIVE_ORDER);
        for (String dirName : list) {
            System.out.println(dirName);
        }
        final File[] jars = filePath.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return name.endsWith(".jar");
            }
        });
        for (File jar : jars) {
            JarFile jarFile = new JarFile(jar);
            System.out.println(jarFile.getName());
        }
        //Step-3
        String lineSeparator = System.getProperty("line.separator");
        Logger logger = getLogger();
        logger.info("kk");
        //换行
        logger.info(lineSeparator+"ll");
        //Step-4
        List<URL> urlList = new ArrayList<>();
        for (File jar : jars) {
            urlList.add(jar.toURI().toURL());
        }
        System.out.println(urlList.toArray(new URL[0])[0].getPath());
    }

    private static Logger getLogger() {
        final Logger logger = Logger.getLogger("PathDeclarer");
        final ConsoleHandler handler = new ConsoleHandler();
        final String lineSeparator = System.getProperty("line.separator");
        handler.setFormatter(new Formatter() {
            @Override
            public String format(LogRecord record) {
                return "[" + record.getLevel() + "] " + record.getMessage() + lineSeparator;
            }
        });
        logger.addHandler(handler);
        logger.setUseParentHandlers(false);
        return logger;
    }
}
