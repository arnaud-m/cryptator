/*
 * This file is part of cryptator, https://github.com/arnaud-m/cryptator
 *
 * Copyright (c) 2021-2026, Université Côte d'Azur. All rights reserved.
 *
 * Licensed under the BSD 3-clause license.
 * See LICENSE file in the project root for full license information.
 */
package cryptator;

import java.util.logging.Level;

import org.slf4j.LoggerFactory;

import ch.qos.logback.classic.LoggerContext;
import cryptator.specs.ICryptaLogManager;

public final class JULogUtil {

    private JULogUtil() {
    }
  
    public enum LoggerType {
        PRIMARY,
        SECONDARY;
        
    	public org.slf4j.Logger getLogger(Class<?> type) {
        	return LoggerFactory.getLogger(type.getName() +"." +  name().toLowerCase());
        }
    }
        

    public static void configureDefaultLoggers() {
    	
    }

    public static void configureTestLoggers() {
    	configureLoggers(Level.WARNING);
    }

    public static void configureSilentLoggers() {
    	configureLoggers(Level.OFF);
    }

    @Deprecated
    public static void configureLoggers(final Level level) {
        //setLevel(level, Cryptagen.JUL_LOGGER, AbstractCryptaSolver.JUL_LOGGER);
    }

    public static void flushLogs() {
    	LoggerContext context = (LoggerContext) LoggerFactory.getILoggerFactory();
    	context.stop();
    }

    public static final ICryptaLogManager DEFAULT_LOG_MANAGER = new ICryptaLogManager() {};

    public static ICryptaLogManager getDefaultLogManager() {
        return DEFAULT_LOG_MANAGER;
    }

}
