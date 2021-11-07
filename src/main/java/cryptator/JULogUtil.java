/**
 * This file is part of cryptator, https://github.com/arnaud-m/cryptator
 *
 * Copyright (c) 2021, Université Côte d'Azur. All rights reserved.
 *
 * Licensed under the BSD 3-clause license.
 * See LICENSE file in the project root for full license information.
 */
package cryptator;

import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

import cryptator.game.CryptaGameEngine;
import cryptator.solver.CryptaSolver;

public final class JULogUtil {
	
	private static final String PROPERTIES = "logging.properties";

	private JULogUtil() {}

	public static final void readResourceConfigurationLoggers(String resourcePath) {
		final InputStream stream = Cryptator.class.getClassLoader().
				getResourceAsStream(resourcePath);
		try {
			LogManager.getLogManager().readConfiguration(stream);
	
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static final void configureLoggers() {
		readResourceConfigurationLoggers(PROPERTIES);
	}

	public static final void configureTestLoggers() {
		readResourceConfigurationLoggers(PROPERTIES);
		setLevel(Level.WARNING, 
				Cryptator.LOGGER, Cryptamancer.LOGGER, Cryptagen.LOGGER, 
				CryptaSolver.LOGGER, CryptaGameEngine.LOGGER
				);
	}
	
	public static final void setLevel(Level level, Logger...loggers) {
		for (Logger logger : loggers) {
			logger.setLevel(level);
		}
	}
	
	// TODO public static final flushLogs() {
	

}
