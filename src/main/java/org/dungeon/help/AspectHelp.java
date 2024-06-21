package main.java.org.dungeon.help;

public class AspectHelp {
	
	private final String name;
	
	private final String info;
	
	private final String[] aliases;
	
	public AspectHelp(String name, String info, String[] aliases) {
		this.name = name;
		this.info = info;
		this.aliases = aliases;
	}
	
	boolean equalsIgnoreCase(String word) {
		if (name.equalsIgnoreCase(word)) {
			return true;
		}
		
		for (String alias : aliases) {
			if (alias.equalsIgnoreCase(word)) {
				return true;
			}
		}
		
		return false;
	}
	
	public String toString() {
		StringBuilder builder = new StringBuilder();
		
		builder.append(name);
		
		builder.append('\n');
		
		if (info.isEmpty()) {
			builder.append(HelpConstants.NO_INFO);
		} else {
			builder.append("Info: ").append(info);
		}
		
		builder.append('\n');
		if (!aliases[0].isEmpty()) {
			builder.append("Also referred to as: ");
			for (int i = 0; i < aliases.length; i++) {
				if (i != 0) {
					builder.append(", ");
				}
				builder.append(aliases[i]);
			}
		}
		
		return builder.toString();
	}

}
