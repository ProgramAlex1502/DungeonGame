package org.dungeon.commands;

import org.dungeon.io.DLogger;

public class CommandDescription {
	
	private final String name;
	private final String info;
	
	public CommandDescription(String name, String info) {
		if (name == null) {
			throw new IllegalArgumentException("Cannot create CommandDescription with null name.");
		} else if (isNotLowercase(name)) {
			DLogger.warning("Passed a String that was not lowercase as a name for a CommandDescription!");
			name = name.toLowerCase();
		}
		this.name = name;
		this.info = info;
	}
	
	private static boolean isNotLowercase(String string) {
		for (char c : string.toCharArray()) {
			if (!Character.isLowerCase(c)) {
				return true;
			}
		}
		return false;
	}
	
	public String getName() {
		return name;
	}
	
	public String getInfo() {
		return info;
	}
	
	@Override
	public String toString() {
		return getName() + " : " + getInfo();
	}

}
