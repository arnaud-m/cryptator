/**
 * This file is part of cryptator, https://github.com/arnaud-m/cryptator
 *
 * Copyright (c) 2021, Université Côte d'Azur. All rights reserved.
 *
 * Licensed under the BSD 3-clause license.
 * See LICENSE file in the project root for full license information.
 */
package cryptator.cmd;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.OptionHandlerFilter;

import cryptator.config.CryptaConfig;

public abstract class AbstractOptionsParser<E extends CryptaConfig> {

	protected final Class<?> mainClass;

	protected final E config;

	protected AbstractOptionsParser(Class<?> mainClass, E config) {
		super();
		this.mainClass = mainClass;
		this.config = config;
	}

	public final Logger getLogger() {
		return Logger.getLogger(mainClass.getName());
	}

	protected final String getCommandName() {
		return mainClass.getName();
	}

	protected abstract String getArgumentName();

	protected abstract void configureLoggers();

	protected boolean checkConfiguration() {
		if(config.getArithmeticBase() < 2) {
			getLogger().severe("The Arithmetic base must be greater than 1.");
			return false;
		} 
		if(config.getRelaxMinDigitOccurence() < 0 || config.getRelaxMaxDigitOccurence() < 0) {
			getLogger().severe("Min/Max digit occurence is ignored because it cannot be negative.");
		}
		return true;
	}

	protected boolean checkArguments() {
		return ! config.getArguments().isEmpty();
	}

	
	public final E getConfig() {
		return config;
	}

	public final boolean parseOptions(String[] args) {
		final CmdLineParser parser = new CmdLineParser(config);
		try {
			// parse the arguments.
			parser.parseArgument(args);
			configureLoggers();
			if(checkConfiguration()) {
				if(checkArguments()) {
					getLogger().log(Level.CONFIG, "Parse options [OK]\n{0}", config);
					return true;
				} else {
					getLogger().log(Level.SEVERE, "Parse arguments [FAIL]\n{0}", config.getArguments());
				}
			} 
		} catch( CmdLineException e ) {
			getLogger().log(Level.SEVERE, "Parse arguments [FAIL]", e);
		}

		// if there's a problem in the command line,
		// you'll get this exception. this will report
		// an error message.
		System.err.println("java " + getCommandName() + " [options...] "+ getArgumentName());
		// print the list of available options
		parser.printUsage(System.err);
		// print option sample. This is useful some time
		System.err.println("\n  Example: java "+ getCommandName() +  " " + parser.printExample(OptionHandlerFilter.ALL) + " " + getArgumentName());
		getLogger().severe("Parse options [FAIL]");
		return false;

	}	
}