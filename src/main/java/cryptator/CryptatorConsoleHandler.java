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
	    public void publish(LogRecord record) {
	        super.publish(record);
	        flush();
	    }

	    @Override
	    public void close() {
	        flush();
	    }
	}
