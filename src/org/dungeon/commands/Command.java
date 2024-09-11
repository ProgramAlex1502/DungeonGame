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
	
	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		Command command = (Command) o;
		return description.getName().equals(command.getDescription().getName());
	}
	
	@Override
	public int hashCode() {
		return description.getName().hashCode();
	}
	
	public abstract CommandResult execute(IssuedCommand issuedCommand);
	
	@Override
	public String toString() {
		return description.toString();
	}

}
