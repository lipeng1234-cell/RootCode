package com.example;

import com.example.common.BootArgsBuilder;
import com.example.common.MyFilenameFilter;
import com.example.common.PathDeclarer;

import java.io.File;
import java.io.IOException;
import java.lang.instrument.Instrumentation;
import java.util.Map;
import java.util.jar.JarFile;
import java.util.logging.ConsoleHandler;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

public class AgentPremain {
    static Logger logger = getLogger();

    public static void premain(String agentArgs, Instrumentation instrumentation) throws Exception {
        
        loadCoreLib(instrumentation);

        // 初始化启动参数
        logger.info("Building argument map... ");
        final Map<String, Object> argsMap = BootArgsBuilder.build(agentArgs);

        AgentCoreEntrance.run(argsMap, instrumentation);

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
}
