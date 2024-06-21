package main.java.org.dungeon.io;

import java.text.SimpleDateFormat;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;

public class DFormatter extends Formatter {

	private static final SimpleDateFormat dateFormat = new SimpleDateFormat("[yyyy-MM-dd HH:mm:ss]");
	
	@Override
	public String format(LogRecord record) {
		return dateFormat.format(record.getMillis()) + " (" + record.getLevel() + ") : " + record.getMessage() + "\n";
	}

}
