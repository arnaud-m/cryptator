/*
 * This file is part of cryptator, https://github.com/arnaud-m/cryptator
 *
 * Copyright (c) 2021-2026, Université Côte d'Azur. All rights reserved.
 *
 * Licensed under the BSD 3-clause license.
 * See LICENSE file in the project root for full license information.
 */
package cryptator;


import org.slf4j.LoggerFactory;

import ch.qos.logback.classic.Level;
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
		configureLoggers(Level.WARN);
	}

	public static void configureSilentLoggers() {
		configureLoggers(Level.OFF);
	}

	public static void configureLoggers(final Level level) {
		getLoggerContext().getLogger("cryptator").setLevel(level);
	}


	public static void printLoggers() {
		for (ch.qos.logback.classic.Logger logger : getLoggerContext().getLoggerList()) {
			System.out.printf(
					"%s: level=%s, effective=%s%n",
					logger.getName(),
					logger.getLevel(),
					logger.getEffectiveLevel()
					);
		}
	}
	
	private static LoggerContext getLoggerContext() {
		return (LoggerContext) LoggerFactory.getILoggerFactory();
	}

	public static void flushLogs() {
		getLoggerContext().stop();
	}

	private static class DefaultLogManager implements ICryptaLogManager {
		
		@Override
		public void setSilent() {
			configureLoggers(Level.OFF);
		}

		@Override
		public void setQuiet() {
			configureLoggers(Level.WARN);
		}

		@Override
		public void setNormal() {
			configureLoggers(Level.INFO);
		}

		@Override
		public void setVerbose() {
			configureLoggers(Level.DEBUG);
		}

		@Override
		public void setDebug() {
			configureLoggers(Level.TRACE);
		}
	}
	

	private static final ICryptaLogManager DEFAULT_LOG_MANAGER = new DefaultLogManager();
	
	public static ICryptaLogManager getDefaultLogManager() {
		return DEFAULT_LOG_MANAGER;
	}

	private static class CryptatorLogManager extends DefaultLogManager {
		
		@Override
		public void setQuiet() {
			super.setQuiet();
			final LoggerContext context = getLoggerContext();
			context.getLogger("cryptator.Cryptator").setLevel(Level.INFO);
			context.getLogger("cryptator.cmd.CryptaBiConsumer.primary").setLevel(Level.INFO);
			context.getLogger("cryptator.cmd.AbstractOptionsParser").setLevel(Level.INFO);
		}
	}
	
	private static final ICryptaLogManager CRYPTATOR_LOG_MANAGER = new CryptatorLogManager();
	
	public static ICryptaLogManager getCryptatorLogManager() {
		return CRYPTATOR_LOG_MANAGER;
	}

	private static class CryptagenLogManager extends DefaultLogManager {
		
		@Override
		public void setQuiet() {
			super.setQuiet();
			final LoggerContext context = getLoggerContext();
			context.getLogger("cryptator.Cryptagen").setLevel(Level.INFO);
			context.getLogger("cryptator.cmd.CryptaBiConsumer.primary").setLevel(Level.INFO);
			context.getLogger("cryptator.cmd.AbstractOptionsParser").setLevel(Level.INFO);
			context.getLogger("cryptator.choco.ChocoLogger.primary").setLevel(Level.INFO);
		}

		@Override
		public void setNormal() {
			super.setNormal();
			final LoggerContext context = getLoggerContext();
			context.getLogger("cryptator.Cryptagen").setLevel(Level.DEBUG);
			context.getLogger("cryptator.cmd.AbstractOptionsParser").setLevel(Level.DEBUG);
			context.getLogger("cryptator.choco.ChocoLogger.primary").setLevel(Level.DEBUG);
		}

		@Override
		public void setVerbose() {
			super.setVerbose();
			final LoggerContext context = getLoggerContext();
			context.getLogger("cryptator.gen.CryptaListGenerator").setLevel(Level.TRACE);
			context.getLogger("cryptator.choco.ChocoLogger.primary").setLevel(Level.TRACE);
		}
	}

	private static final ICryptaLogManager CRYPTAGEN_LOG_MANAGER = new CryptagenLogManager();

	public static ICryptaLogManager getCryptagenLogManager() {
		return CRYPTAGEN_LOG_MANAGER;
	}



}
