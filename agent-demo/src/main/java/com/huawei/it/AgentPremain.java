package com.huawei.it;

import com.huawei.it.common.MyFilenameFilter;
import com.huawei.it.common.PathDeclarer;

import java.io.File;
import java.io.IOException;
import java.lang.instrument.Instrumentation;
import java.util.jar.JarFile;
import java.util.logging.ConsoleHandler;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

public class AgentPremain {


//    public static void premain(String agentOps, Instrumentation inst) {
//        System.out.println("=========premain方法执行========");
//        System.out.println(agentOps);
//        // 添加Transformer
//        inst.addTransformer(new MyTransFromAgent());
//    }

    private static Logger logger = getLogger();
    private static Logger getLogger() {
        final Logger logger = Logger.getLogger("AgentPremain");
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

    public static void premain(String agentOps, Instrumentation inst) {
        //加载核心类库
        loadCoreLib(inst);
    }

    private static void loadCoreLib(Instrumentation instrumentation) {
        final File coreDir = new File(PathDeclarer.getCorePath());
        if (!coreDir.exists() || !coreDir.isDirectory()) {
            throw new RuntimeException("core directory is not exist or is not directory.");
        }

        final File[] jars = coreDir.listFiles(new MyFilenameFilter());
        if (jars == null || jars.length <= 0) {
            throw new RuntimeException("core directory is empty");
        }

        for (File jar : jars) {
            JarFile jarFile = null;
            try {
                jarFile = new JarFile(jar);
                instrumentation.appendToSystemClassLoaderSearch(jarFile);
            } catch (IOException e) {
                throw new RuntimeException(e);
            } finally {
                if (jarFile != null) {
                    try {
                        jarFile.close();
                    } catch (IOException ignored) {
                        logger.severe(ignored.getMessage());
                    }
                }
            }
        }
    }
}
