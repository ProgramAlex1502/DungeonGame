package main.java.org.dungeon.stats;

import java.io.Serializable;

import main.java.org.dungeon.game.IssuedCommand;
import main.java.org.dungeon.io.IO;

public final class Statistics implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private final WorldStatistics worldStatistics = new WorldStatistics();
	private final CommandStatistics commandStats = new CommandStatistics();
	
	public WorldStatistics getWorldStatistics() {
		return worldStatistics;
	}
	
	public void addCommand(IssuedCommand issuedCommand) {
		commandStats.addCommand(issuedCommand);
	}
	
	public void printAllStatistics() {
		printCommandStatistics();
		printWorldStatistics();
	}
	
	private void printCommandStatistics() {
		int commandCount = commandStats.getCommandCount();
		int chars = commandStats.getChars();
		int words = commandStats.getWords();
		IO.writeKeyValueString("Commands issued", String.valueOf(commandCount));
		IO.writeKeyValueString("Characters entered", String.valueOf(chars));
		IO.writeKeyValueString("Average characters per command", String.format("%.2f", (double) chars / commandCount));
		IO.writeKeyValueString("Words entered", String.valueOf(words));
		IO.writeKeyValueString("Average words per command", String.format("%.2f", (double) words / commandCount));
	}
	
	private void printWorldStatistics() {
		IO.writeKeyValueString("Created Locations", String.valueOf(worldStatistics.getLocationCount()));
		IO.writeKeyValueString("Spawned Creatures", String.valueOf(worldStatistics.getCreatureCount()));
	}

}
