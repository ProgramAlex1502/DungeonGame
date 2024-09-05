package org.dungeon.io;

import java.io.File;
import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.dungeon.util.Messenger;

public final class DLogger {
	
	private static final String LOG_FILE_PATH = "logs/";
	private static final String LOG_FILE_NAME = "log.txt";
	private static Logger logger;
	
	static {
		try {
			logger = Logger.getLogger("org.dungeon");
			Handler handler = new FileHandler(getLogFilePath(), true);
			handler.setFormatter(new DFormatter());
			logger.setUseParentHandlers(false);
			logger.addHandler(handler);
			logger.setLevel(Level.ALL);
		} catch (IOException ignored) {
		}
	}
	
	private DLogger() {
		throw new AssertionError();
	}
	
	public static void info(String message) {
		if (logger != null) {
			logger.info(message);
		}
	}
	
	public static void warning(String message) {
		if (logger != null) {
			logger.warning(message);
		}
	}
	
	public static void warning(String filename, int lineNumber, String messageEnd) {
		warning("Line " + lineNumber + " of " + filename + messageEnd);
	}
	
	private static String getLogFilePath() {
		File logFolder = new File(LOG_FILE_PATH);
		if (!logFolder.exists()) {
			if (!logFolder.mkdir()) {
				Messenger.printFailedToCreateDirectoryMessage(LOG_FILE_PATH);
			}
		}
		return LOG_FILE_PATH + LOG_FILE_NAME;
	}

}
