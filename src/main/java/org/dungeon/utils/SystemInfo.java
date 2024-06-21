package main.java.org.dungeon.utils;

import org.joda.time.DateTime;

import main.java.org.dungeon.io.IO;

public class SystemInfo {
	
	public static void printSystemInfo() {
		DateTime currentDate = new DateTime();
		IO.writeString("Time: " + Constants.TIME_FORMAT.print(currentDate));
		IO.writeString("Date: " + Constants.DATE_FORMAT.print(currentDate));
		IO.writeString("User: " + System.getProperty("user.name"));
		IO.writeString(getJavaVersionString());
		IO.writeString(getOSVersionString());
	}
	
	private static String getJavaVersionString() {
		return "Java version " + System.getProperty("java.version") + " by " + System.getProperty("java.vendor");
	}
	
	private static String getOSVersionString() {
		return String.format("%s (%s) %s", System.getProperty("os.name"), System.getProperty("os.arch"), System.getProperty("os.version"));
	}

}
