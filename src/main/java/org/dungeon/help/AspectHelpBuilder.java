package main.java.org.dungeon.help;

class AspectHelpBuilder {
	
	private String name;
	private String info;
	private String[] aliases;
	
	public void setName(String name) {
		this.name = name;
	}
	
	public void setInfo(String info) {
		this.info = info;
	}
	
	public void setAliases(String[] aliases) {
		this.aliases = aliases;
	}
	
	public AspectHelp createAspectHelp() {
		return new AspectHelp(name, info, aliases);
	}

}
