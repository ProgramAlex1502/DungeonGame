package main.java.org.dungeon.help;

class CommandHelpBuilder {
	
	private String name;
	private String info;
	private String[] aliases;
	private String[] arguments;
	
	public void setName(String name) {
		this.name = name;
	}
	
	public void setInfo(String info) {
		this.info = info;
	}
	
	public void setAliases(String[] aliases) {
		this.aliases = aliases;
	}
	
	public void setArguments(String[] arguments) {
		this.arguments = arguments;
	}
	
	public CommandHelp createCommandHelp() {
		return new CommandHelp(name, info, aliases, arguments);
	}

}
