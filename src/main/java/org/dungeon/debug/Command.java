package main.java.org.dungeon.debug;

import main.java.org.dungeon.game.IssuedCommand;

public abstract class Command {
	
	private final String name;
	
	public Command(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
	
	abstract void execute(IssuedCommand issuedCommand);

}
