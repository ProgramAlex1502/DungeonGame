package org.dungeon.game;

public abstract class Command {
	
	public final String name;
	public final String info;
	
	public Command(String name) {
		this.name = name;
		this.info = null;
	}
	
	public Command(String name, String info) {
		this.name = name;
		this.info = info;
	}
	
	abstract public void execute(IssuedCommand issuedCommand);

}
