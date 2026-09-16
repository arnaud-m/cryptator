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

	@Deprecated
	public static void configureLoggers(final Level level) {
		final LoggerContext context =
				(LoggerContext) LoggerFactory.getILoggerFactory();

		ch.qos.logback.classic.Logger cryptatorLogger = context.getLogger("cryptator");
		cryptatorLogger.setLevel(level);

		//    	for (ch.qos.logback.classic.Logger logger : context.getLoggerList()) {
		//    	    System.out.printf(
		//    	        "%s: level=%s, effective=%s%n",
		//    	        logger.getName(),
		//    	        logger.getLevel(),
		//    	        logger.getEffectiveLevel()
		//    	    );
		//    	}
	}

	public static void flushLogs() {
		LoggerContext context = (LoggerContext) LoggerFactory.getILoggerFactory();
		context.stop();
	}

	public static final ICryptaLogManager DEFAULT_LOG_MANAGER = new ICryptaLogManager() {};

	public static ICryptaLogManager getDefaultLogManager() {
		return DEFAULT_LOG_MANAGER;
	}

	public static final ICryptaLogManager CRYPTATOR_LOG_MANAGER = new ICryptaLogManager() {

		@Override
		public void setQuiet() {
			ICryptaLogManager.super.setQuiet();
			final LoggerContext context =
					(LoggerContext) LoggerFactory.getILoggerFactory();

			context.getLogger("cryptator.Cryptator").setLevel(Level.INFO);
			context.getLogger("cryptator.cmd.CryptaBiConsumer.primary").setLevel(Level.INFO);
			context.getLogger("cryptator.cmd.AbstractOptionsParser").setLevel(Level.INFO);
		}
		
		@Override
		public void setVeryVerbose() {
			ICryptaLogManager.super.setVeryVerbose();
			final LoggerContext context =
					(LoggerContext) LoggerFactory.getILoggerFactory();
			 context.getLogger("cryptator.choco.ChocoLogger.primary").setLevel(Level.DEBUG);
		}
	};
	
	public static ICryptaLogManager getCryptatorLogManager() {
		return CRYPTATOR_LOG_MANAGER;
	}

	public static final ICryptaLogManager CRYPTAGEN_LOG_MANAGER = new ICryptaLogManager() {

		@Override
		public void setQuiet() {
			ICryptaLogManager.super.setQuiet();
			final LoggerContext context =
					(LoggerContext) LoggerFactory.getILoggerFactory();

			context.getLogger("cryptator.Cryptagen").setLevel(Level.INFO);
	    	context.getLogger("cryptator.cmd.CryptaBiConsumer.primary").setLevel(Level.INFO);
	    	context.getLogger("cryptator.cmd.AbstractOptionsParser").setLevel(Level.INFO);
	    	context.getLogger("cryptator.choco.ChocoLogger.primary").setLevel(Level.INFO);
		}
		
		@Override
		public void setNormal() {
			ICryptaLogManager.super.setNormal();
			final LoggerContext context =
					(LoggerContext) LoggerFactory.getILoggerFactory();
			context.getLogger("cryptator.Cryptagen").setLevel(Level.DEBUG);
	    	context.getLogger("cryptator.cmd.AbstractOptionsParser").setLevel(Level.DEBUG);
	    	context.getLogger("cryptator.choco.ChocoLogger.primary").setLevel(Level.DEBUG);
		}
		
		@Override
		public void setVerbose() {
			ICryptaLogManager.super.setVerbose();
			final LoggerContext context =
					(LoggerContext) LoggerFactory.getILoggerFactory();
			  context.getLogger("cryptator.gen.CryptaListGenerator").setLevel(Level.TRACE);
		      context.getLogger("cryptator.choco.ChocoLogger.primary").setLevel(Level.TRACE);
		}
		
	};
	
	public static ICryptaLogManager getCryptagenLogManager() {
		return CRYPTAGEN_LOG_MANAGER;
	}



}
