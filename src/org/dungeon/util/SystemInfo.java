package org.dungeon.util;

import java.util.Date;

import org.dungeon.io.IO;

public final class SystemInfo {
	
	private SystemInfo() {
		throw new AssertionError();
	}
	
	public static void printSystemInfo() {
		Date currentDate = new Date();
		IO.writeString("Time: " + Constants.TIME_FORMAT.format(currentDate));
		IO.writeString("Date: " + Constants.DATE_FORMAT.format(currentDate));
		IO.writeString("User: " + System.getProperty("user.name"));
		IO.writeString(getJavaVersionString());
		IO.writeString(getOSVersionString());
	}
	
	private static String getJavaVersionString() {
		return "Java version " + System.getProperty("java.version") + " by " + System.getProperty("java.vendor");
	}
	
	private static String getOSVersionString() {
		String name = System.getProperty("os.name");
		String arch = System.getProperty("os.arch");
		String version = System.getProperty("os.version");
		return String.format("%s (%s) %s", name, arch, version);
		
	}

}
