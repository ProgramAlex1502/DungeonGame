package main.java.org.dungeon.io;

import java.io.File;
import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

import main.java.org.dungeon.utils.Utils;

public class DLogger {
	
	private static Logger logger;
	private static final String LOG_FILE_PATH = "logs/";
	private static final String LOG_FILE_NAME = "log.txt";
	
	private DLogger() {}
	
	public static void initialize() {
		if (logger == null) {
			try {
				logger = Logger.getLogger("main.java.org.dungeon");
				Handler handler = new FileHandler(getLogFilePath(), true);
				handler.setFormatter(new DFormatter());
				logger.setUseParentHandlers(false);
				logger.addHandler(handler);
				logger.setLevel(Level.ALL);
			} catch(IOException ignored) {
			}
		}
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
	
	public static String getLogFilePath() {
		File logFolder = new File(LOG_FILE_PATH);
		
		if (!logFolder.exists()) {
			if (!logFolder.mkdir()) {
				Utils.printFailedToCreateDirectoryMessage(LOG_FILE_PATH);
			}
		}
		
		return LOG_FILE_PATH + LOG_FILE_NAME;
	}

}
