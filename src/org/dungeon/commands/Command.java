package org.dungeon.commands;

public abstract class Command {
	
	private final CommandDescription description;
	
	public Command(String name) {
		this(name, null);
	}
	
	public Command(String name, String info) {
		description = new CommandDescription(name, info);
	}
	
	public CommandDescription getDescription() {
		return description;
	}
	
	public abstract CommandResult execute(IssuedCommand issuedCommand);
	
	@Override
	public String toString() {
		return description.toString();
	}

}
