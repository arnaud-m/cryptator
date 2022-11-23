/**
 * This file is part of cryptator, https://github.com/arnaud-m/cryptator
 *
 * Copyright (c) 2022, Université Côte d'Azur. All rights reserved.
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

public final class JULogUtil {

    private static final String PROPERTIES = "logging.properties";

    private JULogUtil() {
    }

    public static void readResourceConfigurationLoggers(String resourcePath) {
        final InputStream stream = Cryptator.class.getClassLoader().getResourceAsStream(resourcePath);
        try {
            LogManager.getLogManager().readConfiguration(stream);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void configureLoggers() {
        readResourceConfigurationLoggers(PROPERTIES);
    }

    public static void configureTestLoggers() {
        readResourceConfigurationLoggers(PROPERTIES);
        setLevel(Level.WARNING, Cryptator.LOGGER, Cryptamancer.LOGGER, Cryptagen.LOGGER, CryptaSolver.LOGGER,
                CryptaGameEngine.LOGGER);
    }

    public static void configureJsonLoggers() {
        readResourceConfigurationLoggers(PROPERTIES);
        setLevel(Level.INFO, Cryptator.LOGGER, Cryptamancer.LOGGER, Cryptagen.LOGGER, CryptaSolver.LOGGER,
                CryptaGameEngine.LOGGER);
    }

    public static final void setLevel(Level level, Logger... loggers) {
        for (Logger logger : loggers) {
            logger.setLevel(level);
        }
    }

    public static void flushLogs(Logger logger) {
        logger.log(Level.FINE, "Flush logger {0}", logger.getName());
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

}
