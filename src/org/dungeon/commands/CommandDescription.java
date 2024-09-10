package org.dungeon.commands;

public class CommandDescription {
	
	private final String name;
	private final String info;
	
	public CommandDescription(String name, String info) {
		if (name == null) {
			throw new IllegalArgumentException("Cannot create CommandDescription with null name.");
		}
		this.name = name;
		this.info = info;
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
