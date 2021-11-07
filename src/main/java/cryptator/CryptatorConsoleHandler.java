/**
 * This file is part of cryptator, https://github.com/arnaud-m/cryptator
 *
 * Copyright (c) 2021, Université Côte d'Azur. All rights reserved.
 *
 * Licensed under the BSD 3-clause license.
 * See LICENSE file in the project root for full license information.
 */
package cryptator;

import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.SimpleFormatter;
import java.util.logging.StreamHandler;

public class CryptatorConsoleHandler extends StreamHandler {

	    public CryptatorConsoleHandler() {
	        setOutputStream(System.out);
	        setLevel(Level.ALL); // Handlers should not filter, loggers should
	        setFormatter(new SimpleFormatter());
	    }

	    @Override
	    public synchronized void publish(LogRecord record) {
	        super.publish(record);
	        flush();
	    }

	    @Override
	    public synchronized void close() {
	        flush();
	    }
	}
