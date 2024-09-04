package org.dungeon.io;

import java.text.SimpleDateFormat;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;

class DFormatter extends Formatter {
	
	private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("[yyyy-MM-dd HH:mm:ss]");

	@Override
	public String format(LogRecord record) {
		return DATE_FORMAT.format(record.getMillis()) + " (" + record.getLevel() + ") : " + record.getMessage() + "\n";
	}

}
