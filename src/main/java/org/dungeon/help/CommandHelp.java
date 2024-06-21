package main.java.org.dungeon.help;

import main.java.org.dungeon.utils.Constants;

public class CommandHelp {
	
	private final String name;
	
	private final String info;
	
	private final String[] aliases;
	
	private final String[] arguments;
	
	public CommandHelp(String name, String info, String[] aliases, String[] arguments) {
		this.name = name;
		this.info = info;
		this.aliases = aliases;
		this.arguments = arguments;
	}
	
	String[] getAliases() {
		String[] allAliases = new String[aliases.length + 1];
		allAliases[0] = name;
		System.arraycopy(aliases, 0, allAliases, 1, allAliases.length - 1);
		return allAliases;
	}
	
	boolean equalsIgnoreCase(String command) {
		if (name.equalsIgnoreCase(command)) {
			return true;
		}
		
		for (String alias : aliases) {
			if (alias.equalsIgnoreCase(command)) {
				return true;
			}
		}
		
		return false;
	}
	
	public String toString() {
		StringBuilder builder = new StringBuilder();
		
		builder.append(name);
		
		builder.append('\n').append('\n');
		
		if (info.isEmpty()) {
			builder.append(HelpConstants.NO_INFO);
		} else {
			builder.append("Info: ").append(info);
		}
		
		builder.append('\n').append('\n');
		
		if (aliases[0].isEmpty()) {
			builder.append(HelpConstants.NO_ALIASES);
		} else {
			builder.append("Aliases: ");
			for (int i = 0; i < aliases.length; i++) {
				if (i != 0) {
					builder.append(", ");
				}
				builder.append(aliases[i]);
			}
		}
		
		builder.append('\n').append('\n');
		builder.append("Usage: ");
		
		for (int i = 0; i < arguments.length; i++) {
			if (i != 0) {
				builder.append(", ");
			}
			builder.append(name);
			if (!arguments[i].isEmpty()) {
				builder.append(" [").append(arguments[i]).append("]");
			}
		}
		
		return builder.toString();
	}
	
	public String toOneLineString() {
		return toOneLineString(name);
	}
	
	public String toOneLineString(String alias) {
		if (name.equalsIgnoreCase(alias)) {
			return String.format(Constants.COMMAND_HELP_FORMAT, alias, info);
		}
		
		for (String registeredAlias : aliases) {
			if (alias.equalsIgnoreCase(registeredAlias)) {
				return String.format(Constants.COMMAND_HELP_FORMAT, alias, info);
			}
		}
		
		throw new IllegalArgumentException("alias is not a registered alias.");
	}

}
