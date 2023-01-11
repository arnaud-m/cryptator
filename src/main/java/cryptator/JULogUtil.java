/*
 * This file is part of cryptator, https://github.com/arnaud-m/cryptator
 *
 * Copyright (c) 2023, Université Côte d'Azur. All rights reserved.
 *
 * Licensed under the BSD 3-clause license.
 * See LICENSE file in the project root for full license information.
 */
package cryptator;

import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

import cryptator.game.CryptaGameEngine;
import cryptator.solver.CryptaSolver;
import cryptator.specs.ICryptaLogManager;

public final class JULogUtil {

    private static final String PROPERTIES = "logging.properties";

    private JULogUtil() {
    }

    public static void readResourceConfigurationLoggers(final String resourcePath) {
        final InputStream stream = Cryptator.class.getClassLoader().getResourceAsStream(resourcePath);
        try {
            LogManager.getLogManager().readConfiguration(stream);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void configureDefaultLoggers() {
        readResourceConfigurationLoggers(PROPERTIES);
    }

    public static void configureTestLoggers() {
        readResourceConfigurationLoggers(PROPERTIES);
        configureLoggers(Level.WARNING);
    }

    public static void configureSilentLoggers() {
        readResourceConfigurationLoggers(PROPERTIES);
        configureLoggers(Level.OFF);
    }

    public static void configureLoggers(final Level level) {
        setLevel(level, Cryptator.LOGGER, Cryptamancer.LOGGER, Cryptagen.LOGGER, CryptaSolver.LOGGER,
                CryptaGameEngine.LOGGER);
    }

    public static void setLevel(final Level level, final Logger... loggers) {
        for (Logger logger : loggers) {
            logger.setLevel(level);
        }
    }

    public static void flushLogs(final Logger logger) {
        logger.log(Level.FINEST, "Flush logger {0}", logger.getName());
        for (Handler handler : logger.getHandlers()) {
            handler.flush();
        }
    }

    public static void flushLogs() {
        final LogManager manager = LogManager.getLogManager();
        final Enumeration<String> names = manager.getLoggerNames();
        final String pkg = JULogUtil.class.getPackage().getName();
        while (names.hasMoreElements()) {
            final String name = names.nextElement();
            if (name.startsWith(pkg)) {
                flushLogs(manager.getLogger(name));
            }
        }
    }

    public static final ICryptaLogManager DEFAULT_LOG_MANAGER = new DefaultLogManager();

    public static ICryptaLogManager getDefaultLogManager() {
        return DEFAULT_LOG_MANAGER;
    }

    private static class DefaultLogManager implements ICryptaLogManager {

        @Override
        public void setQuiet() {
            ICryptaLogManager.super.setQuiet();
            JULogUtil.setLevel(Level.INFO, Cryptagen.LOGGER, Cryptator.LOGGER);
        }

        @Override
        public void setNormal() {
            ICryptaLogManager.super.setNormal();
            JULogUtil.setLevel(Level.CONFIG, Cryptagen.LOGGER);
        }

        @Override
        public void setVerbose() {
            ICryptaLogManager.super.setVerbose();
            JULogUtil.setLevel(Level.FINE, Cryptagen.LOGGER);
        }

    }

}
