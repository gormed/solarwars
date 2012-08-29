package com.solarwars.log;

import java.io.IOException;
import java.util.logging.LogManager;

/**
 * The Class is needed because the java.util.logging API
 * provides no way to configure a handler different for each logger.
 * With configure meant is the configuration over the java.util.logging.config.file.
 * 
 * @author fxdapokalypse
 *
 */
public class ExtendedServerFileHandler extends ExtendedFileHandler {

	public ExtendedServerFileHandler() throws IOException, SecurityException {
		this(LogManager.getLogManager().getProperty("com.solarwars.log.ExtendedServerFileHandler.pattern"));
	}

	public ExtendedServerFileHandler(String pattern, boolean append)
			throws IOException, SecurityException {
		super(pattern, append);
	}

	public ExtendedServerFileHandler(String pattern, int limit, int count,
			boolean append) throws IOException, SecurityException {
		super(pattern, limit, count, append);
	}

	public ExtendedServerFileHandler(String pattern, int limit, int count)
			throws IOException, SecurityException {
		super(pattern, limit, count);
	}

	public ExtendedServerFileHandler(String pattern) throws IOException,
			SecurityException {
		super(pattern);
	}

}
